package com.example.customcontrol.inputdialogfragment;

import java.util.function.Consumer;

public class InputDialogModel {
    private String title;
    private String curContent;
    private Consumer<String> submitButtonClickListener;

    private InputDialogModel() {
    }

    public String getTitle() {
        return title;
    }

    public String getCurContent() {
        return curContent;
    }

    public Consumer<String> getSubmitButtonClickListener() {
        return submitButtonClickListener;
    }

    public static class Builder {
        private String title;
        private String curContent;
        private Consumer<String> submitButtonClickListener;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCurrentContent(String curContent) {
            this.curContent = curContent;
            return this;
        }

        public Builder setSubmitButtonClickListener(Consumer<String> submitButtonClickListener) {
            this.submitButtonClickListener = submitButtonClickListener;
            return this;
        }

        public InputDialogModel build() {
            InputDialogModel dialogModel = new InputDialogModel();
            dialogModel.title = this.title;
            dialogModel.curContent = this.curContent;
            dialogModel.submitButtonClickListener = this.submitButtonClickListener;
            return dialogModel;
        }
    }
}
