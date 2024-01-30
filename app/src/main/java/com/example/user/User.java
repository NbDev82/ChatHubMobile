package com.example.user;

public class User {

    public enum EGender {
        MALE, FEMALE, OTHER
    }

    private String fullName;
    private String email;
    private EGender gender;
    private boolean isOnline;
    private boolean isDeleted;

    public User() {
        fullName = "";
        email = "";
        gender = EGender.MALE;
        isOnline = false;
        isDeleted = false;
    }

    public User(String email) {
        this();
        fullName = "Van A";
        this.email = email;
    }

    public User(String fullName, String email, EGender gender,
                boolean isOnline, boolean isDeleted) {
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.isOnline = isOnline;
        this.isDeleted = isDeleted;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}