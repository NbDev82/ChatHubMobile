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

    public static String[] getAllDisplays() {
        EGender[] values = values();
        String[] displays = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            displays[i] = values[i].display;
        }
        return displays;
    }

    public static int getCurrentIndex(EGender currentGender) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i] == currentGender) {
                return i;
            }
        }
        return -1;
    }
}
