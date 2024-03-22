package com.example.user.signup;

import android.os.Handler;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.repository.AuthRepos;

public class SignUpViewModel extends BaseViewModel {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSigningUp = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getConfirmPassword() {
        return confirmPassword;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    public LiveData<Boolean> getIsSigningUp() {
        return isSigningUp;
    }

    public SignUpViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    public void navigateToLogin() {
        navigateToLogin.postValue(true);
    }

    public void signUp() {
        isSigningUp.postValue(true);
        String email = this.email.getValue() != null ? this.email.getValue() : "";
        String password = this.password.getValue() != null ? this.password.getValue() : "";
        String confirmPassword = this.confirmPassword.getValue() != null
                ? this.confirmPassword.getValue() : "";

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorToastMessage.postValue("Enter context email");
            isSigningUp.postValue(false);
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            errorToastMessage.postValue("Enter proper password");
            isSigningUp.postValue(false);
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorToastMessage.postValue("Password not match both fields");
            isSigningUp.postValue(false);
            return;
        }
        SignUpRequest signUpRequest = new SignUpRequest(email, password, confirmPassword);
        authRepos.signUp(signUpRequest)
                .thenAccept(aVoid -> {
                    successToastMessage.postValue("Sign up successful");
                    isSigningUp.postValue(false);
                    new Handler().postDelayed(this::navigateToHome, 500);
                })
                .exceptionally(e -> {
                    errorToastMessage.postValue(e.getMessage());
                    new Handler().postDelayed(() -> isSigningUp.postValue(false), 500);
                    Log.e(TAG, "Error: " + e);
                    return null;
                });
    }

    private void navigateToHome() {
        navigateToHome.postValue(true);
    }
}
