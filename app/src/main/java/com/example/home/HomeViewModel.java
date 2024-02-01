package com.example.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.User;

public class HomeViewModel extends ViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final AuthService mAuthService;
    private User mUser = new User();
    private final MutableLiveData<String> mEmail = new MutableLiveData<>("");
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToUserProfile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public MutableLiveData<String> getToastMessage() {
        return mToastMessage;
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
                    this.mUser = user;

                    if (user != null) {
                        mEmail.setValue(user.getEmail());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                });
    }

    public void navigateToUserProfile() {
        mNavigateToUserProfile.setValue(true);
    }

    public void signOut() {
        mToastMessage.setValue("Sign out");
        mAuthService.signOut();
        navigateToLogin();
    }

    private void navigateToLogin() {
        mNavigateToLogin.setValue(true);
    }
}
