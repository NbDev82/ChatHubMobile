package com.example.navigation;

public enum EAnimationType {
    /**
     * Indicates that the activity should fade in when it appears.
     * This animation type is typically used for forward navigation transitions.
     */
    FADE_IN,

    /**
     * Indicates that the activity should fade out when it disappears.
     * This animation type is typically used for backward navigation transitions.
     */
    FADE_OUT,

    /**
     * Indicates that no animation should be applied to the activity transition.
     */
    NONE
}
