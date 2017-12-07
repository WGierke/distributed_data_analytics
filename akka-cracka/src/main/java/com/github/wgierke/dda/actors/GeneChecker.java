package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.GeneCheckForStudentMessage;
import com.github.wgierke.dda.messages.GenesMatchedMessage;

public class GeneChecker extends AbstractLoggingActor {
    private void checkGenes(GeneCheckForStudentMessage geneMessage) {
        Student student = geneMessage.getStudent();
        this.log().debug("Received gene challenge for student " + student.getName() + " (ID: " + student.getId() + ")");

        this.getSender().tell(new GenesMatchedMessage(student, 0), this.getSelf());
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
