package com.example.user.emaildetails;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.authservice.AuthService;

public class EmailDetailsViewModel extends BaseViewModel {

    private static final String TAG = EmailDetailsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateToSettings = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEmailVerified = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isVerifyingEmail = new MutableLiveData<>();
    private final AuthService authService;

    public LiveData<Boolean> getNavigateToSettings() {
        return navigateToSettings;
    }

    public LiveData<Boolean> getIsRefreshing() {
        return isRefreshing;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public LiveData<Boolean> getIsEmailVerified() {
        return isEmailVerified;
    }

    public LiveData<Boolean> getIsVerifyingEmail() {
        return isVerifyingEmail;
    }

    public EmailDetailsViewModel(AuthService authService) {
        this.authService = authService;

        loadData();
    }

    public void loadData() {
        String email = authService.getCurrentEmail();
        this.email.postValue(email);

        authService.checkCurrentEmailVerificationStatus()
                .addOnSuccessListener(isEmailVerified -> {
                    this.isEmailVerified.postValue(isEmailVerified);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: ", e);
                });

        this.isRefreshing.postValue(false);
    }

    public void navigateToSettings() {
        navigateToSettings.postValue(true);
    }

    public void sendEmailVerification() {
        authService.sendCurrentUserEmailVerification()
                .addOnSuccessListener(aVoid -> {
                    successToastMessage.postValue("Email verification sent successfully");
                }).addOnFailureListener(e -> {
                    errorToastMessage.postValue("Failed to send");
                    Log.e(TAG, "Error: ", e);
                });
    }
}
