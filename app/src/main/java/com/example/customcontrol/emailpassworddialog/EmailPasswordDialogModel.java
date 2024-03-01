package com.example.customcontrol.emailpassworddialog;

public class EmailPasswordDialogModel {
    private String title;
    private String subTitle;
    private String email;
    private String password;
    private EmailPasswordApplier submitButtonClickListener;

    private EmailPasswordDialogModel(String title, String subTitle, String email, String password, EmailPasswordApplier submitButtonClickListener) {
        this.title = title;
        this.subTitle = subTitle;
        this.email = email;
        this.password = password;
        this.submitButtonClickListener = submitButtonClickListener;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public EmailPasswordApplier getSubmitButtonClickListener() {
        return submitButtonClickListener;
    }

    public static class Builder {
        private String title;
        private String subTitle;
        private String email;
        private String password;
        private EmailPasswordApplier submitButtonClickListener;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSubTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setSubmitButtonClickListener(EmailPasswordApplier submitButtonClickListener) {
            this.submitButtonClickListener = submitButtonClickListener;
            return this;
        }

        public EmailPasswordDialogModel build() {
            EmailPasswordDialogModel model = new EmailPasswordDialogModel(title, subTitle,
                    email, password, submitButtonClickListener);
            return model;
        }
    }
}
