package com.example.lockapp;

public enum EAutoLockTime {
    NONE(0, "None"),
    ONE_MINUTE(60 * 1000, "1 minute"),
    FIVE_MINUTES(5 * 60 * 1000, "5 minutes"),
    TEN_MINUTES(10 * 60 * 1000, "10 minutes"),
    FIFTEEN_MINUTES(15 * 60 * 1000, "15 minutes"),
    THIRTY_MINUTES(30 * 60 * 1000, "30 minutes"),
    ONE_HOUR(60 * 60 * 1000, "1 hour");

    private final long milliseconds;
    private final String displayName;

    EAutoLockTime(long milliseconds, String displayName) {
        this.milliseconds = milliseconds;
        this.displayName = displayName;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public String getDisplayName() {
        return displayName;
    }
}
