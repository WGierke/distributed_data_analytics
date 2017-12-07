package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.github.wgierke.dda.messages.CheckGeneMessage;
import com.github.wgierke.dda.messages.CrackMessage;
import com.github.wgierke.dda.messages.ShutdownMessage;

public class Master extends AbstractLoggingActor {
    final private ActorRef listener;
    final private ActorRef reader;

    private Master(ActorRef listener) {
        this.listener = listener;
        this.reader = this.getContext().actorOf(Reader.props());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CrackMessage.class, this::crack)
                .match(CheckGeneMessage.class, this::checkGene)
                .match(ShutdownMessage.class, this::shutdown)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
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


    private void crack(CrackMessage crackMessage) {
        this.log().info("Received CrackMessage message");
    }

    private void checkGene(CheckGeneMessage checkGeneMessage) {

    }

    public static Props props(ActorRef listener) {
        return Props.create(Master.class, () -> new Master(listener));
    }
}
