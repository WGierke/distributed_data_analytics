package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.CrackerMessage;
import com.github.wgierke.dda.messages.PasswordCrackedMessage;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Cracker extends AbstractLoggingActor {
    private void crack(CrackerMessage crackerMessage) {
        Student student = crackerMessage.getStudent();
        this.log().debug("Received crack challenge for student " + student.getName() + " (ID: " + student.getId() + ")");

        for(int i = 1000000; i <= 9999999; i++) {
            if(Objects.equals(student.getHash(), hash(String.valueOf(i)))) {
                student.setPassword(String.valueOf(i));
                break;
            }
        }

        this.getSender().tell(new PasswordCrackedMessage(student), this.getSelf());
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

    private String hash(String line) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("SHA-256 is not supported :-(");
        }
        byte[] hashedBytes = new byte[0];
        try {
            hashedBytes = digest.digest(line.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("SHA-256 encoding is not supported :-(");
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < hashedBytes.length; i++)
            stringBuffer.append(Integer.toString((hashedBytes[i] & 0xff) + 0x100, 16).substring(1));
        return stringBuffer.toString();
    }
}
