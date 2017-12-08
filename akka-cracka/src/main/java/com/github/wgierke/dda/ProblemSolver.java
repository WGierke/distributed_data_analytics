package com.github.wgierke.dda;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.wgierke.dda.actors.Master;
import com.github.wgierke.dda.actors.Writer;
import com.github.wgierke.dda.messages.AnalyseStudentsMessage;

public class ProblemSolver {
    public static void main(String[] args) {
        String path = args[0];
        runAll(path);
    }

    private static void runAll(String studentsFilePath) {
        //TODO: add support for path argument
        System.out.println("Running PasswordCracker and GeneChecker :)");
        final ActorSystem actorSystem = ActorSystem.create("Cracker");
        ActorRef listener = actorSystem.actorOf(Writer.props(System.out));
        ActorRef master = actorSystem.actorOf(Master.props(listener));
        master.tell(new AnalyseStudentsMessage(), ActorRef.noSender());
    }
}
