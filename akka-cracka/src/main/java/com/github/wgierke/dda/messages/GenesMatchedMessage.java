package com.github.wgierke.dda.messages;

import com.github.wgierke.dda.Student;

import java.io.Serializable;

public class GenesMatchedMessage implements Serializable {
    final private Student student;
    final private int matchId;

    public GenesMatchedMessage(Student student, final int matchId) {
        this.student = student;
        this.matchId = matchId;
    }

    public Student getStudent() {
        return student;
    }

    public int getMatchId() {
        return matchId;
    }
}
