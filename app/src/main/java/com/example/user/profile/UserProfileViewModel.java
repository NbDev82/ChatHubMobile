package com.example.user.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.EGender;
import com.example.user.User;

import java.util.Date;
import android.os.Handler;

public class UserProfileViewModel extends ViewModel {

    private static final String TAG = UserProfileViewModel.class.getSimpleName();

    private final AuthService mAuthService;
    private final MutableLiveData<String> mFullName = new MutableLiveData<>();
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mPhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<EGender> mGender = new MutableLiveData<>();
    private final MutableLiveData<String> mBirthdayStr = new MutableLiveData<>();
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private User mUser;
    private Handler mHandler = new Handler();

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

    public void setBirthday(Date birthday) {
        String birthdayStr = Utils.dateToString(birthday);
        mBirthdayStr.setValue(birthdayStr);
        mUser.setBirthday(birthday);
    }

    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    public UserProfileViewModel(AuthService authService) {
        mAuthService = authService;

        mIsLoading.postValue(true);
        authService.getCurrentUser()
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        updateUserInfo(user);

                        mHandler.postDelayed(() -> mIsLoading.setValue(false), 1000);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                    mIsLoading.postValue(true);
                });
    }

    private void updateUserInfo(User user) {
        mUser = user;
        mFullName.setValue(user.getFullName());
        mEmail.setValue(user.getEmail());
        mPhoneNumber.setValue(user.getPhoneNumber());
        mGender.setValue(user.getGender());
        Date birthday = user.getBirthday();
        mBirthdayStr.setValue(Utils.dateToString(birthday));
    }

    public void navigateToHome() {
        mNavigateToHome.postValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mHandler.removeCallbacksAndMessages(null);
    }
}
