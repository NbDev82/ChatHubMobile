package com.example.user.login.github;

import android.app.Activity;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

import java.lang.ref.WeakReference;

public class GithubAuthViewModel extends BaseViewModel {

    private WeakReference<Activity> mActivityRef;
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
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
        mNavigateToLogin.postValue(true);
    }

    public void onGithubLoginBtnClick() {
        String email = mEmail.getValue() != null ? mEmail.getValue() : "";
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mErrorToastMessage.postValue("Enter a proper Email");
            return;
        }
        Activity activity = mActivityRef.get();
        if (activity != null) {
            mAuthService.signInOrSignUpWithGithub(activity, email, authResult -> {
                mSuccessToastMessage.postValue("Login successfully");
                navigateToHome();
            }, e -> {
                mErrorToastMessage.postValue(e.getMessage());
            });
        }
    }

    public void navigateToHome() {
        mNavigateToHome.postValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mActivityRef.clear();
    }
}
