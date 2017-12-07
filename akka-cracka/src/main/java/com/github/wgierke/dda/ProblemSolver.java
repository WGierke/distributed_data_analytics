package com.github.wgierke.dda;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.wgierke.dda.actors.Master;
import com.github.wgierke.dda.actors.Reader;
import com.github.wgierke.dda.messages.CrackMessage;
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
        System.out.println("Running Password CrackMessage :)");
        final ActorSystem actorSystem = ActorSystem.create("Cracker");
        // TODO change this to real listener
        ActorRef listener = actorSystem.actorOf(Reader.props());
        ActorRef master = actorSystem.actorOf(Master.props(listener));

        master.tell(new CrackMessage(), ActorRef.noSender());
        master.tell(new ShutdownMessage(), ActorRef.noSender());
    }

    private static void runGeneChecker() {
        System.out.println("Running Gene Check :)");
    }

    private static void runAll() {
        System.out.println("Running all :)");
    }
}
