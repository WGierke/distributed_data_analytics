package com.github.wgierke.dda.messages;

import java.io.Serializable;

public class WriteMessage implements Serializable {
    private String message;

    public WriteMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
