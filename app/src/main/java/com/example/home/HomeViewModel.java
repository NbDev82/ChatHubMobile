package com.example.home;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.User;

public class HomeViewModel extends BaseViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final AuthService mAuthService;
    private final MutableLiveData<String> mEmail = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> mNavigateToUserProfile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public MutableLiveData<Boolean> getNavigateToUserProfile() {
        return mNavigateToUserProfile;
    }

    public MutableLiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
    }

    public HomeViewModel(AuthService authService) {
        mAuthService = authService;

        mAuthService.getCurrentUser()
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        mEmail.postValue(user.getEmail());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                });
    }

    public void navigateToUserProfile() {
        mNavigateToUserProfile.postValue(true);
    }

    public void signOut() {
        mAuthService.signOut();
        mSuccessToastMessage.postValue("Sign out");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToLogin();
            }
        }, 100);
    }

    private void navigateToLogin() {
        mNavigateToLogin.postValue(true);
    }
}
