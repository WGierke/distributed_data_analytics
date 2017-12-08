package com.github.wgierke.dda.messages;

import com.github.wgierke.dda.Student;

import java.io.Serializable;

public class StudentUpdatedMessage implements Serializable {

    final protected Student student;

    public StudentUpdatedMessage(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
