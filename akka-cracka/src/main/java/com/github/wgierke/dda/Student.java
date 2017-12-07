package com.github.wgierke.dda;

public class Student {
    final private int id;
    final private String name;
    final private String password;
    final private String genes;

    public Student(int id, String name, String password, String genes) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.genes = genes;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getGenes() {
        return genes;
    }
}
