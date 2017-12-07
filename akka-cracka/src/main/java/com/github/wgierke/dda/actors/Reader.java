package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.messages.CSVFileMessage;

public class Reader extends AbstractLoggingActor {
    private void parseCSV(CSVFileMessage csvMessage) {
        // TODO read file
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CSVFileMessage.class, this::parseCSV)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }

    public static Props props() {
        return Props.create(Reader.class);
    }
}
