package com.example.user.login.otp.send;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;
import com.example.user.EUserField;
import com.example.user.Validator;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class SendOtpViewModel extends BaseViewModel {

    private final MutableLiveData<String> prefixPhoneNumber = new MutableLiveData<>("+84");
    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumberError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isOtpSending = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToVerifyOtpWithPhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSignUp = new MutableLiveData<>();
    private AuthService authService;

    public LiveData<String> getPrefixPhoneNumber() {
        return prefixPhoneNumber;
    }

    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public LiveData<String> getPhoneNumberError() {
        return phoneNumberError;
    }

    public LiveData<Boolean> getIsOtpSending() {
        return isOtpSending;
    }

    public LiveData<Bundle> getNavigateToVerifyOtpWithPhoneNumber() {
        return navigateToVerifyOtpWithPhoneNumber;
    }

    public LiveData<Boolean> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    public SendOtpViewModel(AuthService authService) {
        this.authService = authService;
    }

    public void validatePhoneNumber(CharSequence s) {
        String phoneNumber = s.toString();
        String error = Validator.validPhoneNumber(phoneNumber);
        this.phoneNumberError.postValue(error);
    }

    public void sendOtp() {
        this.isOtpSending.postValue(true);
        if (true) {
            return;
        }
        this.isOtpSending.postValue(false);
        navigateToVerifyOtpWithPhoneNumber();
    }

    private String getFullPhoneNumber() {
        String prefix = prefixPhoneNumber.getValue();
        String number = phoneNumber.getValue();
        if (prefix != null && number != null) {
            return prefix.concat(number);
        }
        return null;
    }

    private void navigateToVerifyOtpWithPhoneNumber() {
        String phoneNumber = getFullPhoneNumber();
        Bundle data = new Bundle();
        data.putString(EUserField.PHONE_NUMBER.getName(), phoneNumber);
        navigateToVerifyOtpWithPhoneNumber.postValue(data);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks getOnVerificationStateChangedCallbacks() {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };
    }

    public void navigateToSignUp() {
        navigateToSignUp.postValue(true);
    }
}
