package com.example.user.login;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

public class LoginViewModel extends BaseViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    protected final AuthService mAuthService;
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mPassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToForgotPassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToSignUp = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToGoogleSignIn = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToGithubAuth = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsLogging = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public MutableLiveData<String> getPassword() {
        return mPassword;
    }

    public LiveData<Boolean> getNavigateToForgotPassword() {
        return mNavigateToForgotPassword;
    }

    public LiveData<Boolean> getNavigateToSignUp() {
        return mNavigateToSignUp;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public LiveData<Boolean> getNavigateToGoogleSignIn() {
        return mNavigateToGoogleSignIn;
    }

    public LiveData<Boolean> getNavigateToGithubAuth() {
        return mNavigateToGithubAuth;
    }

    public LiveData<Boolean> getIsLogging() {
        return mIsLogging;
    }

    public LoginViewModel(AuthService authService) {
        mAuthService = authService;
    }

    public void onLoginBtnClick() {
        mIsLogging.postValue(true);
        Log.i(TAG, "Login button clicked");

        trimAllInputs();
        String email = mEmail.getValue() != null ? mEmail.getValue() : "";
        String password = mPassword.getValue() != null ? mPassword.getValue() : "";

        if (!isValidEmail(email)) {
            mErrorToastMessage.postValue("Enter context email.");
            mIsLogging.postValue(false);
            return;
        }

        if (!isValidPassword(password)) {
            mErrorToastMessage.postValue("Enter proper password.");
            mIsLogging.postValue(false);
            return;
        }

        SignInRequest signInRequest = new SignInRequest(email, password);
        mAuthService.signIn(signInRequest, aVoid -> {
            mIsLogging.postValue(false);
            mSuccessToastMessage.postValue("Login successfully");
            navigateToHome();
        }, e -> {
            mIsLogging.postValue(false);
            mErrorToastMessage.postValue("Login unsuccessfully");
            Log.e(TAG, "Error: " + e);
        });
    }

    private void trimAllInputs() {
        String email = mEmail.getValue() != null ? mEmail.getValue() : "";
        String password = mPassword.getValue() != null ? mPassword.getValue() : "";

        mEmail.postValue(email.trim());
        mPassword.postValue(password.trim());
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public void navigateToForgotPassword() {
        mNavigateToForgotPassword.postValue(true);
    }

    public void navigateToSignUp() {
        mNavigateToSignUp.postValue(true);
    }

    public void navigateIfAuthenticated() {
        if (mAuthService.isLoggedIn()) {
            mNavigateToHome.postValue(true);
        }
    }

    public void navigateToHome() {
        mNavigateToHome.postValue(true);
    }

    public void navigateToFingerprintSignIn() {
        mErrorToastMessage.postValue("Without implementation");
    }

    public void navigateToSmsSignIn() {
        mErrorToastMessage.postValue("Without implementation");
    }

    public void navigateToGoogleSignIn() {
        mNavigateToGoogleSignIn.postValue(true);
    }

    public void navigateToGithubAuth() {
        mNavigateToGithubAuth.postValue(true);
    }
}