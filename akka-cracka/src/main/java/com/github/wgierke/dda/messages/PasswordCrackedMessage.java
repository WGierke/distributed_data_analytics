package com.github.wgierke.dda.messages;

import com.github.wgierke.dda.Student;

public class PasswordCrackedMessage {
    final private Student student;
    final private String password;

    public PasswordCrackedMessage(Student student, String password) {
        this.student = student;
        this.password = password;
    }

    public Student getStudent() {
        return student;
    }

    public String getPassword() {
        return password;
    }
}
