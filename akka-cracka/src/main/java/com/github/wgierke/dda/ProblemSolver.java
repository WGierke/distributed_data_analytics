package com.github.wgierke.dda;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.wgierke.dda.actors.Master;
import com.github.wgierke.dda.actors.Writer;
import com.github.wgierke.dda.messages.AnalyseStudentsMessage;
import com.github.wgierke.dda.messages.ShutdownMessage;

public class ProblemSolver {
    public static void main(String[] args) {
        String command = (args.length == 1) ? args[0] : "all";

        switch (command) {
            case "pw":
                runCracker();
                break;
            case "gene":
                runGeneChecker();
                break;
            case "all":
                runAll();
                break;
            default:
                System.out.println("Bad command! Need to provide 'pw', 'gene', 'all', or no command.");
                System.exit(1);
        }
    }

    private static void runCracker() {
        System.out.println("Running Password AnalyseStudentsMessage :)");
        final ActorSystem actorSystem = ActorSystem.create("Cracker");
        ActorRef listener = actorSystem.actorOf(Writer.props(System.out));
        ActorRef master = actorSystem.actorOf(Master.props(listener));

        master.tell(new AnalyseStudentsMessage(), ActorRef.noSender());

//        try {
//            Thread.sleep(10 * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        master.tell(new ShutdownMessage(), ActorRef.noSender());
    }

    private static void runGeneChecker() {
        System.out.println("Running Gene Check :)");
        final ActorSystem actorSystem = ActorSystem.create("GeneChecker");
        ActorRef listener = actorSystem.actorOf(Writer.props(System.out));
        ActorRef master = actorSystem.actorOf(Master.props(listener));

        master.tell(new AnalyseStudentsMessage(), ActorRef.noSender());
    }

    private static void runAll() {
        System.out.println("Running all :)");
    }
}
