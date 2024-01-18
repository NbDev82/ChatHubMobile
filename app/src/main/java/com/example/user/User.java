package com.example.user;

import androidx.annotation.Nullable;

public class User {

    @Nullable
    private String email, password;

    public User(@Nullable String email, @Nullable String password) {
        this.email = email;
        this.password = password;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }
}