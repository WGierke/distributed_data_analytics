package com.github.wgierke.dda.messages;

import com.github.wgierke.dda.Student;

public class PasswordCrackedMessage extends StudentUpdatedMessage {
    public PasswordCrackedMessage(Student student) {
        super(student);
    }
}
