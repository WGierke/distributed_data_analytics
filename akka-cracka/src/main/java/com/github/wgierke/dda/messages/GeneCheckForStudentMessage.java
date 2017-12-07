package com.github.wgierke.dda.messages;

import com.github.wgierke.dda.Student;

import java.io.Serializable;

public class GeneCheckForStudentMessage implements Serializable {
    final private Student student;
    final private String[] genes;

    public GeneCheckForStudentMessage(Student student, String[] genes) {
        this.student = student;
        this.genes = genes;
    }

    public Student getStudent() {
        return student;
    }

    public String[] getGenes() {
        return genes;
    }
}
