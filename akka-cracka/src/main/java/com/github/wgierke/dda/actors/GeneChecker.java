package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.messages.GeneMessage;

public class GeneChecker extends AbstractLoggingActor {
    private void checkGenes(GeneMessage geneMessage) {

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GeneMessage.class, this::checkGenes)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }

    public static Props props() {
        return Props.create(GeneChecker.class);
    }
}
