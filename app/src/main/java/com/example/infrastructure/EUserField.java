package com.example.infrastructure;

public enum EUserField {
    COLLECTION_NAME("users"),
    USERNAME("username"),
    HASHED_PASS("hashedPass"),
    FULL_NAME("fullName"),
    EMAIL("email"),
    GENDER("gender"),
    IS_DELETED("isDeleted"),
    AVAILABILITY("availability");

    private String name;

    EUserField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
