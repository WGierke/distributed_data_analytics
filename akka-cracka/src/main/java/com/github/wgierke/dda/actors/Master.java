package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.*;

import java.util.List;
import java.util.stream.Collectors;

public class Master extends AbstractLoggingActor {
    final private ActorRef listener;
    final private ActorRef reader;
    final private ActorRef cracker;
    final private ActorRef geneChecker;

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
                .match(ShutdownMessage.class, this::shutdown)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }


    private void analyse(AnalyseStudentsMessage analyseStudentsMessage) {
        this.log().info("Received AnalyseStudentsMessage message");
        this.reader.tell(new CSVFileMessage("./students.csv"), this.getSelf());
    }

    private void passOnCSVResult(CSVResultMessage csvResultMessage) {
        this.log().info("Received CSVResultMessage");
        List<Student> students = csvResultMessage.getStudents();
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
