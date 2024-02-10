package com.example.customcontrol.phonecredential;

public class PhoneCredentialDialogModel {

    private PhoneCredentialApplier verifyButtonClickListener;

    private PhoneCredentialDialogModel(PhoneCredentialApplier verifyButtonClickListener) {
        this.verifyButtonClickListener = verifyButtonClickListener;
    }

    public PhoneCredentialApplier getVerifyButtonClickListener() {
        return verifyButtonClickListener;
    }

    public static class Builder {
        private PhoneCredentialApplier verifyButtonClickListener;

        public Builder setVerifyButtonClickListener(PhoneCredentialApplier verifyButtonClickListener) {
            this.verifyButtonClickListener = verifyButtonClickListener;
            return this;
        }

        public PhoneCredentialDialogModel build() {
            return new PhoneCredentialDialogModel(verifyButtonClickListener);
        }
    }
}
