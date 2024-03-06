package com.example.chat.enums;

public enum Evisible {
    ACTIVE("active"),
    DELETE("delete"),
    HIDDEN("hidden");
    private String name;

    Evisible(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
