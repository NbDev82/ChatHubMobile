package com.example.customcontrol.phonecredential;

import com.google.firebase.auth.PhoneAuthCredential;

public interface PhoneCredentialApplier {
    void apply(PhoneAuthCredential phoneAuthCredential);
}
