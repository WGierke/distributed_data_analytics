package com.github.wgierke.dda;

public class Student {
    final private int id;
    final private String name;
    final private String hash;
    final private String genes;

    public Student(int id, String name, String hash, String genes) {
        this.id = id;
        this.name = name;
        this.hash = hash;
        this.genes = genes;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
    }

    public String getGenes() {
        return genes;
    }
}
