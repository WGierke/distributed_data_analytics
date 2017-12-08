package com.github.wgierke.dda;

public class Student {
    final private int id;
    final private String name;
    final private String hash;
    final private String genes;
    private String password;
    private Integer genePartner;
    private String longestGeneMatch;


    public Student(int id, String name, String hash, String genes) {
        this.id = id;
        this.name = name;
        this.hash = hash;
        this.genes = genes;
        this.password = null;
        this.genePartner = null;
        this.longestGeneMatch = null;
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

    public String getPassword() { return password; }

    public Integer getGenePartner() { return genePartner; }

    public String getLongestGeneMatch() { return longestGeneMatch; }

    public void setPassword(String password) { this.password = password; }

    public void setGenePartner(Integer genePartner) { this.genePartner = genePartner; }

    public void setLongestGeneMatch(String longestGeneMatch) { this.longestGeneMatch = longestGeneMatch; }
}
