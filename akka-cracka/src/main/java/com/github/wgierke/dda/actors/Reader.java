package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.CSVFileMessage;
import com.github.wgierke.dda.messages.CSVResultMessage;
import com.github.wgierke.dda.messages.ShutdownMessage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Reader extends AbstractLoggingActor {
    private void parseCSV(CSVFileMessage csvMessage) {
        this.log().info("Working dir: " + System.getProperty("user.dir"));
        this.log().info("Parsing CSV file " + csvMessage.getFilePath());
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(csvMessage.getFilePath()));
        } catch (FileNotFoundException e) {
            this.log().error("Cannot open file " + csvMessage.getFilePath());
            return;
        }

        List<Student> students = new ArrayList<>();
        try {
            String line = reader.readLine();
            do {
                if (line.equals("")) {
                    break;
                }
                String[] cols = line.split(",");
                if (cols.length != 4) {
                    this.log().error("Malformed CSV! Got " + cols.length + " columns, but expected 4.");
                    this.log().error("Line: " + line);
                    reader.close();
                    return;
                }

                int id = Integer.parseInt(cols[0]);
                String name = cols[1];
                String password = cols[2];
                String genes = cols[3].trim();
                students.add(new Student(id, name, password, genes));
                this.log().info("Parsed student: " + id);

                line = reader.readLine();
            } while (line != null);

            reader.close();
        } catch (IOException e) {
            this.log().error("Error while parsing the CSV file " + csvMessage.getFilePath() + ".\n\t" + e.getMessage());
            return;
        }

        this.getSender().tell(new CSVResultMessage(students), this.getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CSVFileMessage.class, this::parseCSV)
                .match(ShutdownMessage.class, this::shutdown)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }

    public static Props props() {
        return Props.create(Reader.class);
    }

    private void shutdown(ShutdownMessage shutdownMessage) {
        this.log().info("Reader shutting down...");
    }
}
