package com.example.setting;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

public class SettingsViewModel extends BaseViewModel {

    private final MutableLiveData<Bitmap> mProfileImg = new MutableLiveData<>();
    private final MutableLiveData<String> mFullName = new MutableLiveData<>();
    private final MutableLiveData<String> mPhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();

    public LiveData<Bitmap> getProfileImg() {
        return mProfileImg;
    }

    public LiveData<String> getFullName() {
        return mFullName;
    }

    public LiveData<String> getPhoneNumber() {
        return mFullName;
    }

    public LiveData<String> getEmail() {
        return mFullName;
    }

    public SettingsViewModel(AuthService authService) {
        mAuthService = authService;
    }

    public void navigateToHome() {
    }

    public void navigateToUserProfile() {
    }

    public void navigateToChangePhoneNumber() {
    }

    public void navigateToChangeEmail() {
    }

    public void navigateToAccountLinking() {
    }

    public void navigateToMyQrCode() {
    }

    public void navigateToLockMyApp() {
    }

    public void navigateToChangePassword() {
    }
}
