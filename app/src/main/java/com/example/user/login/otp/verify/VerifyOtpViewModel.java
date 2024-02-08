package com.example.user.login.otp.verify;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;
import com.google.android.gms.auth.api.Auth;

public class VerifyOtpViewModel extends BaseViewModel {

    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> otp = new MutableLiveData<>();
    private final MutableLiveData<String> otpError = new MutableLiveData<>();
    private final MutableLiveData<String> resendContent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isOtpVerifying = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSignUp = new MutableLiveData<>();
    private final AuthService authService;

    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.postValue(phoneNumber);
    }

    public MutableLiveData<String> getOtp() {
        return otp;
    }

    public MutableLiveData<String> getOtpError() {
        return otpError;
    }

    public MutableLiveData<String> getResendContent() {
        return resendContent;
    }

    public MutableLiveData<Boolean> getIsOtpVerifying() {
        return isOtpVerifying;
    }

    public MutableLiveData<Boolean> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    public VerifyOtpViewModel(AuthService authService) {
        this.authService = authService;
    }

    public void resendOtp() {
    }

    public void verifyOtp() {
    }

    public void navigateToSignUp() {
        this.navigateToSignUp.postValue(true);
    }
}
