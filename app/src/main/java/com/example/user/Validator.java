package com.example.user;

import org.jetbrains.annotations.Nullable;

public class Validator {

    private static final int MIN_PASSWORD_LENGTH = 8;

    public static String validatePassword(@Nullable String password) {
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long";
        }

        boolean containsLetter = false;
        boolean containsNumber = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                containsLetter = true;
            } else if (Character.isDigit(c)) {
                containsNumber = true;
            }
        }

        if (!containsLetter) {
            return "Password must contain at least one letter";
        }

        if (!containsNumber) {
            return "Password must contain at least one number";
        }

        return null;
    }

    public static String validateConfirmPassword(@Nullable String password,
                                                 @Nullable String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return "Passwords cannot be null";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        } else {
            return null;
        }
    }

    public static String validateEmail(@Nullable String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex)) {
            return "Invalid email format";
        }

        return null;
    }

    public static String validPhoneNumber(@Nullable String phoneNumber) {
        if (phoneNumber == null) {
            return "Phone number is required";
        }

        String regex = "^[+]?[0-9]{8,15}$";
        if (!phoneNumber.matches(regex)) {
            return "Invalid phone number";
        }

        return null;
    }
}
