package com.example.user.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.AuthService;
import com.example.user.EGender;

public class UserProfileViewModel extends ViewModel {

    private final AuthService mAuthService;

    private final MutableLiveData<String> mFullName = new MutableLiveData<>();
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mPhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<EGender> mGender = new MutableLiveData<>();
    private final MutableLiveData<String> mBirthdayStr = new MutableLiveData<>();
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();

    public LiveData<String> getFullName() {
        return mFullName;
    }

    public LiveData<String> getEmail() {
        return mEmail;
    }

    public LiveData<String> getPhoneNumber() {
        return mPhoneNumber;
    }

    public LiveData<EGender> getGender() {
        return mGender;
    }

    public LiveData<String> getBirthdayStr() {
        return mBirthdayStr;
    }

    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public UserProfileViewModel(AuthService authService) {
        mAuthService = authService;
    }

    public void navigateToHome() {
        mNavigateToHome.setValue(true);
    }
}
