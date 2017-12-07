package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.CrackerMessage;

public class Cracker extends AbstractLoggingActor {
    private void crack(CrackerMessage crackerMessage) {
        Student student = crackerMessage.getStudent();
        this.log().info("Received crack challenge for student " + student.getName() + " (ID: " + student.getId() + ")");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CrackerMessage.class, this::crack)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }

    public static Props props() {
        return Props.create(Cracker.class);
    }
}
