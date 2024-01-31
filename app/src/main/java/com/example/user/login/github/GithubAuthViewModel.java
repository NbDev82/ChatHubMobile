package com.example.user.login.github;

import android.app.Activity;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.AuthService;

import java.lang.ref.WeakReference;

public class GithubAuthViewModel extends ViewModel {

    private WeakReference<Activity> mActivityRef;
    private AuthService mAuthService;
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public MutableLiveData<String> getToastMessage() {
        return mToastMessage;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public GithubAuthViewModel(Activity activity, AuthService authService) {
        mActivityRef = new WeakReference<>(activity);
        mAuthService = authService;
    }

    public void navigateToLogin() {
        mNavigateToLogin.setValue(true);
    }

    public void onGithubLoginBtnClick() {
        String email = mEmail.getValue() != null ? mEmail.getValue() : "";
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mToastMessage.setValue("Enter a proper Email");
            return;
        }
        Activity activity = mActivityRef.get();
        if (activity != null) {
            mAuthService.signInOrSignUpWithGithub(activity, email, authResult -> {
                navigateToHome();
            }, e -> {
                mToastMessage.setValue(e.getMessage());
            });
        }
    }

    public void navigateToHome() {
        mNavigateToHome.setValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mActivityRef.clear();
    }
}
