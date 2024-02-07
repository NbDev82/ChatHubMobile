package com.example.user.login;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

public class LoginViewModel extends BaseViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToForgotPassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSignUp = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToGoogleSignIn = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToGithubAuth = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLogging = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public LiveData<Boolean> getNavigateToForgotPassword() {
        return navigateToForgotPassword;
    }

    public LiveData<Boolean> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    public LiveData<Boolean> getNavigateToGoogleSignIn() {
        return navigateToGoogleSignIn;
    }

    public LiveData<Boolean> getNavigateToGithubAuth() {
        return navigateToGithubAuth;
    }

    public MutableLiveData<Boolean> getIsLogging() {
        return isLogging;
    }

    public LoginViewModel(AuthService authService) {
        this.authService = authService;
    }

    public void onLoginBtnClick() {
        isLogging.postValue(true);
        Log.i(TAG, "Login button clicked");

        trimAllInputs();
        String email = this.email.getValue() != null ? this.email.getValue() : "";
        String password = this.password.getValue() != null ? this.password.getValue() : "";

        if (!isValidEmail(email)) {
            errorToastMessage.postValue("Enter your email.");
            isLogging.postValue(false);
            return;
        }

        if (!isValidPassword(password)) {
            errorToastMessage.postValue("Enter proper password.");
            isLogging.postValue(false);
            return;
        }

        SignInRequest signInRequest = new SignInRequest(email, password);
        authService.signIn(signInRequest, aVoid -> {
            isLogging.postValue(false);
            successToastMessage.postValue("Login successfully");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateToHome();
                }
            }, 100);
        }, e -> {
            errorToastMessage.postValue(e.getMessage());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLogging.postValue(false);
                }
            }, 500);
            Log.e(TAG, "Error: " + e);
        });
    }

    private void trimAllInputs() {
        String email = this.email.getValue() != null ? this.email.getValue() : "";
        String password = this.password.getValue() != null ? this.password.getValue() : "";

        this.email.postValue(email.trim());
        this.password.postValue(password.trim());
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public void navigateToForgotPassword() {
        navigateToForgotPassword.postValue(true);
    }

    public void navigateToSignUp() {
        navigateToSignUp.postValue(true);
    }

    public void navigateIfAuthenticated() {
        if (authService.isLoggedIn()) {
            navigateToHome.postValue(true);
        }
    }

    public void navigateToHome() {
        navigateToHome.postValue(true);
    }

    public void navigateToFingerprintSignIn() {
        errorToastMessage.postValue("Without implementation");
    }

    public void navigateToSmsSignIn() {
        errorToastMessage.postValue("Without implementation");
    }

    public void navigateToGoogleSignIn() {
        navigateToGoogleSignIn.postValue(true);
    }

    public void navigateToGithubAuth() {
        navigateToGithubAuth.postValue(true);
    }
}