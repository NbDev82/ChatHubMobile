package com.example.customcontrol.inputdialogfragment;

import com.example.customcontrol.DialogDismissListener;

import java.util.function.Consumer;

public class InputDialogModel {
    private String title;
    private EInputType type = EInputType.NORMAL;
    private String curContent;
    private Consumer<String> submitButtonClickListener;
    private DialogDismissListener dismissListener;

    private InputDialogModel() {
    }

    public String getTitle() {
        return title;
    }

    public EInputType getType() {
        return type;
    }

    public String getCurContent() {
        return curContent;
    }

    public Consumer<String> getSubmitButtonClickListener() {
        return submitButtonClickListener;
    }

    public DialogDismissListener getDismissListener() {
        return dismissListener;
    }

    public static class Builder {
        private String title;
        private EInputType type;
        private String curContent;
        private Consumer<String> submitButtonClickListener;
        private DialogDismissListener dismissListener;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setType(EInputType type) {
            this.type = type;
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

        public Builder setDismissListener(DialogDismissListener dismissListener) {
            this.dismissListener = dismissListener;
            return this;
        }

        public InputDialogModel build() {
            InputDialogModel dialogModel = new InputDialogModel();
            dialogModel.title = this.title;
            dialogModel.type = type;
            dialogModel.curContent = this.curContent;
            dialogModel.submitButtonClickListener = this.submitButtonClickListener;
            dialogModel.dismissListener = dismissListener;
            return dialogModel;
        }
    }
}
