package com.github.wgierke.dda.messages;

import java.io.Serializable;

public class CSVFileMessage implements Serializable {
    public enum TargetData {Passwords, Genes, All}

    private String filePath;
    private TargetData targetData;

    public CSVFileMessage(String filePath, TargetData targetData) {
        this.filePath = filePath;
        this.targetData = targetData;
    }

    public String getFilePath() {
        return filePath;
    }

    public TargetData getTargetData() {
        return targetData;
    }
}
