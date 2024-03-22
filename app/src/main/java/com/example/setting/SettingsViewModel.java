package com.example.setting;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.User;
import com.example.user.repository.AuthRepos;

import android.os.Handler;

public class SettingsViewModel extends BaseViewModel {

    private static final String TAG = SettingsViewModel.class.getSimpleName();

    private final MutableLiveData<Bitmap> profileImg = new MutableLiveData<>();
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToUserProfile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToChangePhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToChangeEmail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToAccountLinking = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToMyQrCode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLockApp = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToChangePassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();
    private User originalUser;

    public LiveData<Bitmap> getProfileImg() {
        return profileImg;
    }

    public LiveData<String> getFullName() {
        return fullName;
    }

    public LiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<Boolean> getNavigateToUserProfile() {
        return navigateToUserProfile;
    }

    public MutableLiveData<Boolean> getNavigateToChangePhoneNumber() {
        return navigateToChangePhoneNumber;
    }

    public MutableLiveData<Boolean> getNavigateToChangeEmail() {
        return navigateToChangeEmail;
    }

    public MutableLiveData<Boolean> getNavigateToAccountLinking() {
        return navigateToAccountLinking;
    }

    public MutableLiveData<Boolean> getNavigateToMyQrCode() {
        return navigateToMyQrCode;
    }

    public MutableLiveData<Boolean> getNavigateToLockApp() {
        return navigateToLockApp;
    }

    public LiveData<Boolean> getNavigateToChangePassword() {
        return navigateToChangePassword;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    public User getUser() {
        return originalUser;
    }

    public SettingsViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;

        this.authRepos.getCurrentUser()
                .thenAccept(user -> {
                    if (user != null) {
                        originalUser = user;

                        Bitmap profileImg = Utils.decodeImage(user.getImageUrl());
                        this.profileImg.postValue(profileImg);
                        fullName.postValue(user.getFullName());
                        phoneNumber.postValue(user.getPhoneNumber());
                        email.postValue(user.getEmail());
                    }
                })
                .exceptionally(e -> {
                    Log.e(TAG, "Error: ", e);
                    return null;
                });
    }

    public void navigateToUserProfile() {
        navigateToUserProfile.postValue(true);
    }

    public void navigateToChangePhoneNumber() {
        navigateToChangePhoneNumber.postValue(true);
    }

    public void navigateToChangeEmail() {
        navigateToChangeEmail.postValue(true);
    }

    public void navigateToAccountLinking() {
        navigateToAccountLinking.postValue(true);
    }

    public void navigateToMyQrCode() {
        navigateToMyQrCode.postValue(true);
    }

    public void navigateToLockApp() {
        navigateToLockApp.postValue(true);
    }

    public void navigateToChangePassword() {
        navigateToChangePassword.postValue(true);
    }

    public void signOut() {
        authRepos.signOut();
        successToastMessage.postValue("Sign out");
        new Handler().postDelayed(() -> navigateToLogin(), 100);
    }

    private void navigateToLogin() {
        this.navigateToLogin.postValue(true);
    }
}
