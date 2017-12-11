package com.github.wgierke.dda;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.wgierke.dda.actors.Master;
import com.github.wgierke.dda.actors.Writer;
import com.github.wgierke.dda.messages.AnalyseStudentsMessage;

import java.util.Objects;

public class ProblemSolver {
    public static void main(String[] args) {
        String path;
        if(args.length < 2 || !Objects.equals(args[0], "--path")) {
            System.out.println("The program should be invoked using 'java -jar akka-cracka.jar --path students.csv'.");
            System.out.println("Since no CSV path was correctly given we're assuming the input file is 'students.csv'. Continuing ...");
            path = "students.csv";
        }
        else{
            path = args[1];
        }
        runAll(path);
    }

    private static void runAll(String studentsFilePath) {
        System.out.println("Running PasswordCracker and GeneChecker :)");
        final ActorSystem actorSystem = ActorSystem.create("Cracker");
        ActorRef listener = actorSystem.actorOf(Writer.props());
        ActorRef master = actorSystem.actorOf(Master.props(listener, studentsFilePath));
        master.tell(new AnalyseStudentsMessage(), ActorRef.noSender());
    }
}
