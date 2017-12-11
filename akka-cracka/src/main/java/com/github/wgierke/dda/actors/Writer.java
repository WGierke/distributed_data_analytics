package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.GenesMatchedMessage;
import com.github.wgierke.dda.messages.PasswordCrackedMessage;
import com.github.wgierke.dda.messages.ShutdownMessage;
import com.github.wgierke.dda.messages.WriteMessage;

import java.io.OutputStream;

public class Writer extends AbstractLoggingActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WriteMessage.class, this::write)
                .match(GenesMatchedMessage.class, this::writeGenesMatched)
                .match(PasswordCrackedMessage.class, this::writePasswordCracked)
                .match(ShutdownMessage.class, this::shutdown)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }

    private void write(String message) {
        this.log().info(message);
    }

    private void write(WriteMessage writeMessage) {
        this.write(writeMessage.getMessage());
    }

    private void writePasswordCracked(PasswordCrackedMessage passwordCrackedMessage) {
        Student student = passwordCrackedMessage.getStudent();
        String message = "Password cracked for student " + student.getName() + " (ID: " + student.getId() + ")\n\t" +
                "Hash: " + student.getHash() + " --> Password: " + student.getPassword() + "\n";
        this.write(message);
    }

    private void writeGenesMatched(GenesMatchedMessage genesMatchedMessage) {
        Student student = genesMatchedMessage.getStudent();
        String message = "Genes matched for student " + student.getName() + " (ID: " + student.getId() + ")\n\t" +
                "Gene partner ID: " + student.getGenePartner() + " - Longest gene match: " + student.getLongestGeneMatch() + "\n";
        this.write(message);
    }

    public static Props props() {
        return Props.create(Writer.class);
    }

    private void shutdown(ShutdownMessage shutdownMessage) {
        this.log().info("Writer shutting down...");
    }
}
