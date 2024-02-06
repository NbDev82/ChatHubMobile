package com.example.setting;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.User;

public class SettingsViewModel extends BaseViewModel {

    private static final String TAG = SettingsViewModel.class.getSimpleName();

    private final MutableLiveData<Bitmap> mProfileImg = new MutableLiveData<>();
    private final MutableLiveData<String> mFullName = new MutableLiveData<>();
    private final MutableLiveData<String> mPhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToUserProfile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToChangePhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToChangeEmail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToAccountLinking = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToMyQrCode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLockMyApp = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToChangePassword = new MutableLiveData<>();
    private User mUser;

    public LiveData<Bitmap> getProfileImg() {
        return mProfileImg;
    }

    public LiveData<String> getFullName() {
        return mFullName;
    }

    public LiveData<String> getPhoneNumber() {
        return mPhoneNumber;
    }

    public LiveData<String> getEmail() {
        return mEmail;
    }

    public MutableLiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public MutableLiveData<Boolean> getNavigateToUserProfile() {
        return mNavigateToUserProfile;
    }

    public MutableLiveData<Boolean> getNavigateToChangePhoneNumber() {
        return mNavigateToChangePhoneNumber;
    }

    public MutableLiveData<Boolean> getNavigateToChangeEmail() {
        return mNavigateToChangeEmail;
    }

    public MutableLiveData<Boolean> getNavigateToAccountLinking() {
        return mNavigateToAccountLinking;
    }

    public MutableLiveData<Boolean> getNavigateToMyQrCode() {
        return mNavigateToMyQrCode;
    }

    public MutableLiveData<Boolean> getNavigateToLockMyApp() {
        return mNavigateToLockMyApp;
    }

    public MutableLiveData<Boolean> getNavigateToChangePassword() {
        return mNavigateToChangePassword;
    }

    public User getUser() {
        return mUser;
    }

    public SettingsViewModel(AuthService authService) {
        mAuthService = authService;

        mAuthService.getCurrentUser()
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        mUser = user;

                        Bitmap profileImg = Utils.decodeImage( user.getImageUrl() );
                        mProfileImg.postValue(profileImg);
                        mFullName.postValue( user.getFullName() );
                        mPhoneNumber.postValue( user.getPhoneNumber() );
                        mEmail.postValue( user.getEmail() );
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: ", e);
                });
    }

    public void navigateToHome() {
        mNavigateToHome.postValue(true);
    }

    public void navigateToUserProfile() {
        mNavigateToUserProfile.postValue(true);
    }

    public void navigateToChangePhoneNumber() {
        mNavigateToChangePhoneNumber.postValue(true);
    }

    public void navigateToChangeEmail() {
        mNavigateToChangePhoneNumber.postValue(true);
    }

    public void navigateToAccountLinking() {
        mNavigateToAccountLinking.postValue(true);
    }

    public void navigateToMyQrCode() {
        mNavigateToMyQrCode.postValue(true);
    }

    public void navigateToLockMyApp() {
        mNavigateToLockMyApp.postValue(true);
    }

    public void navigateToChangePassword() {
        mNavigateToChangePhoneNumber.postValue(true);
    }
}
