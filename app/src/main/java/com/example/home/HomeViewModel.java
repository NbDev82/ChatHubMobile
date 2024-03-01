package com.example.home;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.repository.AuthRepos;

public class HomeViewModel extends BaseViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> navigateToUserProfile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSettings = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToFriendRequests = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<Boolean> getNavigateToUserProfile() {
        return navigateToUserProfile;
    }

    public LiveData<Boolean> getNavigateToSettings() {
        return navigateToSettings;
    }

    public LiveData<Boolean> getNavigateToFriendRequests() {
        return navigateToFriendRequests;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    public HomeViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;

        this.authRepos.getCurrentUser()
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        email.postValue(user.getEmail());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                });
    }

    public void navigateToSettings() {
        navigateToSettings.postValue(true);
    }

    public void navigateToUserProfile() {
        navigateToUserProfile.postValue(true);
    }

    public void navigateToFriendRequests() {
        this.navigateToFriendRequests.postValue(true);
    }

    public void signOut() {
        authRepos.signOut();
        successToastMessage.postValue("Sign out");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToLogin();
            }
        }, 100);
    }

    private void navigateToLogin() {
        navigateToLogin.postValue(true);
    }
}
