package com.example.user;

public enum EGender {
    MALE("Male"), FEMALE("Female"), OTHER("Other");

    String display;

    EGender(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public static EGender fromDisplay(String display) {
        for (EGender gender : values()) {
            if (gender.display.equals(display)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("No enum constant with display value: " + display);
    }
}
