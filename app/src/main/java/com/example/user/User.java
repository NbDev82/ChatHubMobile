package com.example.user;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String fullName;
    private String email;
    private String phoneNumber;
    private EGender gender;
    private Date birthday;
    private String imageUrl;
    private boolean isOnline;
    private boolean isDeleted;

    public User() {
        fullName = "";
        email = "";
        phoneNumber = "";
        gender = EGender.MALE;
        birthday = new Date(90, 1, 1);
        imageUrl = "";
        isOnline = false;
        isDeleted = false;
    }

    public User(String email) {
        this();
        fullName = "Van A";
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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