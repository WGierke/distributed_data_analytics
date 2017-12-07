package com.github.wgierke.dda.actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.messages.ShutdownMessage;
import com.github.wgierke.dda.messages.WriteMessage;

import java.io.OutputStream;

public class Writer extends AbstractLoggingActor {
    private final OutputStream out;

    private Writer(OutputStream out) {
        this.out = out;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WriteMessage.class, this::write)
                .match(ShutdownMessage.class, this::shutdown)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }

    private void write(WriteMessage writeMessage) {
        try {
            out.write(writeMessage.getMessage().getBytes());
            out.flush();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static Props props(OutputStream out) {
        return Props.create(Writer.class, () -> new Writer(out));
    }

    private void shutdown(ShutdownMessage shutdownMessage) {
        this.log().info("Writer shutting down...");
    }
}
