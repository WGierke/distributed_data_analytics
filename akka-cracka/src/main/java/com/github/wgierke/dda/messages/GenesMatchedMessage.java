package com.github.wgierke.dda.messages;

import com.github.wgierke.dda.Student;

import java.io.Serializable;

public class GenesMatchedMessage implements Serializable {
    final private Student student;
    final private int matchId;
    final private String longestMatch;

    public GenesMatchedMessage(Student student, final int matchId, final String longestMatch) {
        this.student = student;
        this.matchId = matchId;
        this.longestMatch = longestMatch;
    }

    public Student getStudent() {
        return student;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getLongestMatch() { return longestMatch; }
}
