package com.example.user.login.otp.verify;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.CustomToast;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.login.otp.phonenumberinput.PhoneNumberInputActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOtpViewModel extends BaseViewModel {

    private static final String TAG = VerifyOtpViewModel.class.getSimpleName();

    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> otp = new MutableLiveData<>();
    private final MutableLiveData<String> otpError = new MutableLiveData<>();
    private final MutableLiveData<String> resendOtp = new MutableLiveData<>();
    private final MutableLiveData<String> resendContent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> resendContentStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isOtpVerifying = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSignUp = new MutableLiveData<>();
    private final AuthService authService;
    private long timeoutSeconds;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.postValue(phoneNumber);
    }

    public MutableLiveData<String> getOtp() {
        return otp;
    }

    public LiveData<String> getOtpError() {
        return otpError;
    }

    public LiveData<String> getResendOtp() {
        return resendOtp;
    }

    public LiveData<String> getResendContent() {
        return resendContent;
    }

    public void setResendContent(String content) {
        resendContent.postValue(content);
    }

    public LiveData<Boolean> getResendContentStatus() {
        return resendContentStatus;
    }

    public void setResendContentStatus(boolean status) {
        resendContentStatus.postValue(status);
    }

    public LiveData<Boolean> getIsOtpVerifying() {
        return isOtpVerifying;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    public LiveData<Boolean> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    public VerifyOtpViewModel(AuthService authService) {
        this.authService = authService;

        this.resendContentStatus.postValue(false);
        resetTimeoutSeconds();
    }

    public void resendOtp() {
        String phoneNumber = this.phoneNumber.getValue();
        this.resendOtp.postValue(phoneNumber);
    }

    public void verifyOtp() {
        String enteredOtp = otp.getValue() != null ? otp.getValue() : "";
        try {
            PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(verificationId, enteredOtp);
            signIn(phoneCredential);
        } catch (IllegalArgumentException e) {
            errorToastMessage.postValue("Failed to verify OTP");
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
    }

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks getOnVerificationStateChangedCallbacks() {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
                isOtpVerifying.postValue(false);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                errorToastMessage.postValue("OTP verification failed");
                isOtpVerifying.postValue(false);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                resendingToken = forceResendingToken;
                successToastMessage.postValue("OTP sent successfully");
                isOtpVerifying.postValue(false);
            }
        };
    }

    private void signIn(PhoneAuthCredential phoneCredential) {
        this.isOtpVerifying.postValue(true);
        authService.signInWithCredential(phoneCredential)
                .addOnSuccessListener(aVoid -> {
                    this.isOtpVerifying.postValue(false);
                    successToastMessage.postValue("Verify successfully");
                    new Handler().postDelayed(() -> {
                        navigateToHome();
                    }, 500);
                })
                .addOnFailureListener(e -> {
                    this.isOtpVerifying.postValue(false);
                    errorToastMessage.postValue("Verify unsuccessfully");
                    Log.e(PhoneNumberInputActivity.class.getSimpleName(), "Error: ", e);
                });
    }

    public void navigateToHome() {
        this.navigateToHome.postValue(true);
    }

    public void navigateToSignUp() {
        this.navigateToSignUp.postValue(true);
    }

    public long getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public void resetTimeoutSeconds() {
        timeoutSeconds = Utils.OTP_TIME_OUT_SECONDS;
    }

    public PhoneAuthProvider.ForceResendingToken getResendingToken() {
        return resendingToken;
    }
}
