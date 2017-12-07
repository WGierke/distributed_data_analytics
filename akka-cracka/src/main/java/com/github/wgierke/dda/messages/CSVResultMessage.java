package com.github.wgierke.dda.messages;

import com.github.wgierke.dda.Student;

import java.util.List;

public class CSVResultMessage {
    final private List<Student> students;

    public CSVResultMessage(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }
}
