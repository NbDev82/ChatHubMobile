package com.example.user.emaildetails;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.repository.AuthRepos;

public class EmailDetailsViewModel extends BaseViewModel {

    private static final String TAG = EmailDetailsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEmailVerified = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isVerifyingEmail = new MutableLiveData<>();
    private final AuthRepos authRepos;

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
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

    public EmailDetailsViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;

        loadData();
    }

    public void loadData() {
        String email = authRepos.getCurrentEmail();
        this.email.postValue(email);

        authRepos.checkCurrentEmailVerificationStatus()
                .addOnSuccessListener(isEmailVerified -> {
                    this.isEmailVerified.postValue(isEmailVerified);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: ", e);
                });

        this.isRefreshing.postValue(false);
    }

    public void navigateToSettings() {
        navigateBack.postValue(true);
    }

    public void sendEmailVerification() {
        authRepos.sendCurrentUserEmailVerification()
                .addOnSuccessListener(aVoid -> {
                    successToastMessage.postValue("Email verification sent successfully");
                }).addOnFailureListener(e -> {
                    errorToastMessage.postValue("Failed to send");
                    Log.e(TAG, "Error: ", e);
                });
    }
}
