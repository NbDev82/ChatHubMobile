package com.example.home;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.repository.AuthRepos;

public class HomeViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();

    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    public HomeViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    public void signOut() {
        authRepos.signOut();
        successToastMessage.postValue("Sign out");
        new Handler().postDelayed(() -> navigateToLogin(), 100);
    }

    private void navigateToLogin() {
        navigateToLogin.postValue(true);
    }
}
