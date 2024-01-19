package com.example.user;

import androidx.annotation.Nullable;

public class User {
    public enum EGender {
        MALE, FEMALE
    }

    @Nullable
    private String username, hashedPass, fullName, email;
    private EGender gender;

    public User() {
    }

    public User(@Nullable String username, @Nullable String passHash, @Nullable String fullName,
                @Nullable String email, EGender gender) {
        this.username = username;
        this.hashedPass = passHash;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getHashedPass() {
        return hashedPass;
    }

    public void setHashedPass(@Nullable String hashedPass) {
        this.hashedPass = hashedPass;
    }

    @Nullable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@Nullable String fullName) {
        this.fullName = fullName;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }
}