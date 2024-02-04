package com.example.customcontrol.customalertdialog;

import java.util.function.Consumer;

public class AlertDialogModel {
    private String title;
    private String message;
    private String positiveBtnTitle;
    private String negativeBtnTitle;
    private Consumer<Void> positiveButtonClickListener;
    private Consumer<Void> negativeButtonClickListener;

    private AlertDialogModel() {}

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getPositiveBtnTitle() {
        return positiveBtnTitle;
    }

    public String getNegativeBtnTitle() {
        return negativeBtnTitle;
    }

    public Consumer<Void> getPositiveButtonClickListener() {
        return positiveButtonClickListener;
    }

    public Consumer<Void> getNegativeButtonClickListener() {
        return negativeButtonClickListener;
    }

    public static class Builder {
        private String title;
        private String message;
        private String positiveBtnTitle;
        private String negativeBtnTitle;
        private Consumer<Void> positiveButtonClickListener;
        private Consumer<Void> negativeButtonClickListener;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButton(String title, Consumer<Void> listener) {
            positiveBtnTitle = title;
            positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String title, Consumer<Void> listener) {
            negativeBtnTitle = title;
            negativeButtonClickListener = listener;
            return this;
        }

        public AlertDialogModel build() {
            AlertDialogModel alertDialogModel = new AlertDialogModel();
            alertDialogModel.title = this.title;
            alertDialogModel.message = this.message;
            alertDialogModel.positiveBtnTitle = this.positiveBtnTitle;
            alertDialogModel.negativeBtnTitle = this.negativeBtnTitle;
            alertDialogModel.positiveButtonClickListener = this.positiveButtonClickListener;
            alertDialogModel.negativeButtonClickListener = this.negativeButtonClickListener;
            return alertDialogModel;
        }
    }
}
