package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.GeneCheckForStudentMessage;

public class GeneChecker extends AbstractLoggingActor {
    private void checkGenes(GeneCheckForStudentMessage geneMessage) {
        Student student = geneMessage.getStudent();
        this.log().info("Received gene challenge for student " + student.getName() + " (ID: " + student.getId() + ")");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GeneCheckForStudentMessage.class, this::checkGenes)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }

    public static Props props() {
        return Props.create(GeneChecker.class);
    }
}
