package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Master extends AbstractLoggingActor {
    final private ActorRef listener;
    final private ActorRef reader;
    final private ActorRef cracker;
    final private ActorRef geneChecker;

    private Set<Student> genesMatched = new HashSet<>();
    private Set<Student> passwordCracked = new HashSet<>();

    private int numStudents = -1;

    private Master(ActorRef listener) {
        this.listener = listener;
        this.reader = this.getContext().actorOf(Reader.props());
        this.cracker = this.getContext().actorOf(Cracker.props());
        this.geneChecker = this.getContext().actorOf(GeneChecker.props());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AnalyseStudentsMessage.class, this::analyse)
                .match(CSVResultMessage.class, this::passOnCSVResult)
                .match(GenesMatchedMessage.class, this::updateGenesMatched)
                .match(PasswordCrackedMessage.class, this::updatePasswordCracked)
                .match(ShutdownMessage.class, this::shutdown)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }

    private void updateGenesMatched(GenesMatchedMessage genesMatchedMessage) {
        genesMatched.add(genesMatchedMessage.getStudent());
        this.listener.tell(genesMatchedMessage, this.getSelf());
        this.check_is_finished();
    }

    private void updatePasswordCracked(PasswordCrackedMessage passwordCrackedMessage) {
        passwordCracked.add(passwordCrackedMessage.getStudent());
        this.listener.tell(passwordCrackedMessage, this.getSelf());
        this.check_is_finished();
    }

    private void check_is_finished() {
        if (this.genesMatched.size() == this.numStudents && this.passwordCracked.size() == this.numStudents) {
            this.log().info("Master is finished");
            try {
                writeResultsToCsv();
            } catch (IOException e) {
                System.out.println("An error occurred while trying to persist the solution:");
                e.printStackTrace();
            }
            this.getSelf().tell(new ShutdownMessage(), ActorRef.noSender());
        }
    }

    private void writeResultsToCsv() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./solution.csv"))) {
            String[] header = {"ID", "Student", "Password", "Gene_Partner", "Longest_Gene_Match"};
            writer.write(String.join(",", header));
            writer.newLine();
            for (Student student : this.passwordCracked) {
                ArrayList<String> values = new ArrayList<>();
                values.add(String.valueOf(student.getId()));
                values.add(student.getName());
                values.add(student.getPassword());
                values.add(String.valueOf(student.getGenePartner()));

                values.add(student.getLongestGeneMatch());
                writer.write(String.join(",", values));
                writer.newLine();
            }
        }
    }

    private void analyse(AnalyseStudentsMessage analyseStudentsMessage) {
        this.log().debug("Received AnalyseStudentsMessage message");
        this.reader.tell(new CSVFileMessage("./students.csv"), this.getSelf());
    }

    private void passOnCSVResult(CSVResultMessage csvResultMessage) {
        this.log().debug("Received CSVResultMessage");
        List<Student> students = csvResultMessage.getStudents();
        this.numStudents = students.size();

        String[] genes = students.stream().map(Student::getGenes).collect(Collectors.toList()).toArray(new String[0]);

        for (Student student : students) {
            this.cracker.tell(new CrackerMessage(student), this.getSelf());
            this.geneChecker.tell(new GeneCheckForStudentMessage(student, genes), this.getSelf());
        }
    }

    public static Props props(ActorRef listener) {
        return Props.create(Master.class, () -> new Master(listener));
    }

    private void shutdown(ShutdownMessage shutdownMessage) {
        this.log().info("Master shutting down...");
        this.reader.tell(new ShutdownMessage(), this.getSelf());
        this.listener.tell(new ShutdownMessage(), this.getSelf());
        this.getContext().stop(getSelf());

        // Wait for system to handle shutdown
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.getContext().system().terminate();
    }
}
