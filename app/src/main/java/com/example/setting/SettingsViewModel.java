package com.example.setting;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.User;
import com.example.user.repository.AuthRepos;

public class SettingsViewModel extends BaseViewModel {

    private static final String TAG = SettingsViewModel.class.getSimpleName();

    private final MutableLiveData<Bitmap> profileImg = new MutableLiveData<>();
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToUserProfile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToChangePhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToChangeEmail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToAccountLinking = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToMyQrCode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLockMyApp = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToChangePassword = new MutableLiveData<>();
    private User mUser;

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

    public MutableLiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
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

    public MutableLiveData<Boolean> getNavigateToLockMyApp() {
        return navigateToLockMyApp;
    }

    public LiveData<Boolean> getNavigateToChangePassword() {
        return navigateToChangePassword;
    }

    public User getUser() {
        return mUser;
    }

    public SettingsViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;

        this.authRepos.getCurrentUser()
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        mUser = user;

                        Bitmap profileImg = Utils.decodeImage( user.getImageUrl() );
                        this.profileImg.postValue(profileImg);
                        fullName.postValue( user.getFullName() );
                        phoneNumber.postValue( user.getPhoneNumber() );
                        email.postValue( user.getEmail() );
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: ", e);
                });
    }

    public void navigateToHome() {
        navigateToHome.postValue(true);
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

    public void navigateToLockMyApp() {
        navigateToLockMyApp.postValue(true);
    }

    public void navigateToChangePassword() {
        navigateToChangePassword.postValue(true);
    }
}
