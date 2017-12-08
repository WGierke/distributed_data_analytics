package com.github.wgierke.dda.messages;

import com.github.wgierke.dda.Student;

import java.io.Serializable;

public class GenesMatchedMessage extends StudentUpdatedMessage {

    public GenesMatchedMessage(Student student) {
         super(student);
    }

}
