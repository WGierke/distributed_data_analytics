package com.github.wgierke.dda.messages;

import com.github.wgierke.dda.Student;

import java.io.Serializable;

public class CrackerMessage implements Serializable {
    final private Student student;

    public CrackerMessage(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}