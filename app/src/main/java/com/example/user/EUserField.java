package com.example.user;

public enum EUserField {
    COLLECTION_NAME("users"),
    FULL_NAME("fullName"),
    EMAIL("email"),
    PHONE_NUMBER("phoneNumber"),
    GENDER("gender"),
    BIRTHDAY("birthday"),
    IMAGE_URL("imageUrl"),
    IS_ONLINE("isOnline"),
    IS_DELETED("isDeleted");

    private String name;

    EUserField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
