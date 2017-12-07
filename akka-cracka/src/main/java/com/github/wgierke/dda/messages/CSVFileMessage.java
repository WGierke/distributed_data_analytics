package com.github.wgierke.dda.messages;

import java.io.Serializable;

public class CSVFileMessage implements Serializable {
    private String filePath;

    public CSVFileMessage(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
