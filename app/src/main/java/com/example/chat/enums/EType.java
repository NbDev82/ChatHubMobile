package com.example.chat.enums;

public enum EType {
    PRIVATE("private"),
    GROUP("group"),
    CHANNEL("channel");
    private String name;

    EType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
