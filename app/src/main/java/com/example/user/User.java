package com.example.user;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String id;
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

    public User(String id, String fullName, String email, String phoneNumber, EGender gender,
                Date birthday, String imageUrl, boolean isOnline, boolean isDeleted) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
        this.isOnline = isOnline;
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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