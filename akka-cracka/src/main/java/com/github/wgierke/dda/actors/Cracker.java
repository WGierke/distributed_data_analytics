package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.CrackerMessage;
import com.github.wgierke.dda.messages.PasswordCrackedMessage;

public class Cracker extends AbstractLoggingActor {
    private void crack(CrackerMessage crackerMessage) {
        Student student = crackerMessage.getStudent();
        this.log().debug("Received crack challenge for student " + student.getName() + " (ID: " + student.getId() + ")");

        this.getSender().tell(new PasswordCrackedMessage(student, "0000000"), this.getSelf());
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
