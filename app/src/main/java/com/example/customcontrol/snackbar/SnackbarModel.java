package com.example.customcontrol.snackbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.function.Consumer;

public class SnackbarModel {
    private int duration;
    private String message;
    private String actionText;
    private Consumer<Void> customAction;
    private Consumer<Integer> onDismissedAction;
    private Consumer<Void> onShownAction;

    private SnackbarModel(int duration, String message, String actionText,
                          Consumer<Void> customAction,
                          Consumer<Integer> onDismissedAction,
                          Consumer<Void> onShownAction) {
        this.duration = duration;
        this.message = message;
        this.actionText = actionText;
        this.customAction = customAction;
        this.onDismissedAction = onDismissedAction;
        this.onShownAction = onShownAction;
    }

    public int getDuration() {
        return duration;
    }

    public String getMessage() {
        return message;
    }

    public String getActionText() {
        return actionText;
    }

    public Consumer<Void> getCustomAction() {
        return customAction;
    }

    public Consumer<Integer> getOnDismissedAction() {
        return onDismissedAction;
    }

    public Consumer<Void> getOnShownAction() {
        return onShownAction;
    }

    public static class Builder {
        private int duration;
        private String message;
        private String actionText;
        private Consumer<Void> customAction;
        private Consumer<Integer> onDismissedAction;
        private Consumer<Void> onShownAction;

        public Builder() {
            duration = Snackbar.LENGTH_SHORT;
            message = "";
            actionText = "";
            customAction = null;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder actionText(String actionText) {
            this.actionText = actionText;
            return this;
        }

        public Builder customAction(Consumer<Void> customAction) {
            this.customAction = customAction;
            return this;
        }

        public Builder onDismissedAction(Consumer<Integer> onDismissedAction) {
            this.onDismissedAction = onDismissedAction;
            return this;
        }

        public Builder onShownAction(Consumer<Void> onShownAction) {
            this.onShownAction = onShownAction;
            return this;
        }

        public SnackbarModel build() {
            return new SnackbarModel(duration, message, actionText,
                    customAction, onDismissedAction, onShownAction);
        }
    }
}
