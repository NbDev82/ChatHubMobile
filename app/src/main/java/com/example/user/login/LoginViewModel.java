package com.example.user.login;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infrastructure.PreferenceManager;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;

public class LoginViewModel extends ViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    protected final AuthService mAuthService;
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mPassword = new MutableLiveData<>();
    protected final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToForgotPassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToSignUp = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToGoogleSignIn = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToGithubAuth = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public MutableLiveData<String> getPassword() {
        return mPassword;
    }

    public MutableLiveData<String> getToastMessage() {
        return mToastMessage;
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

    public LoginViewModel(AuthService authService) {
        mAuthService = authService;
    }

    public void onLoginBtnClick() {
        Log.i(TAG, "Login button clicked");

        String email = mEmail.getValue() != null ? mEmail.getValue() : "";
        String password = mPassword.getValue() != null ? mPassword.getValue() : "";

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mToastMessage.setValue("Enter context email.");
        } else if (password.isEmpty() || password.length() < 6) {
            mToastMessage.setValue("Enter proper password.");
        } else {
            SignInRequest signInRequest = new SignInRequest(email, password);
            mAuthService.signIn(signInRequest, aVoid -> {
                mToastMessage.setValue("Login successful");
                navigateToHome();
            }, e -> {
                mToastMessage.setValue("Please wait while login...");
                mToastMessage.setValue(String.valueOf(e));
            });
        }
    }

    public void navigateToForgotPassword() {
        mNavigateToForgotPassword.setValue(true);
    }

    public void navigateToSignUp() {
        mNavigateToSignUp.setValue(true);
    }

    public void navigateIfAuthenticated() {
        if (mAuthService.isLoggedIn()) {
            mNavigateToHome.setValue(true);
        }
    }

    public void navigateToHome() {
        mNavigateToHome.setValue(true);
    }

    public void navigateToGoogleSignIn() {
        mNavigateToGoogleSignIn.setValue(true);
    }

    public void navigateToGithubAuth() {
        mNavigateToGithubAuth.setValue(true);
    }

    public void onFacebookLoginClick() {
        mToastMessage.setValue("Without implementation");
    }
}