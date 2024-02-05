package com.example.user.signup;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

public class SignUpViewModel extends BaseViewModel {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private final AuthService mAuthService;
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mPassword = new MutableLiveData<>();
    private final MutableLiveData<String> mConfirmPassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public MutableLiveData<String> getPassword() {
        return mPassword;
    }

    public MutableLiveData<String> getConfirmPassword() {
        return mConfirmPassword;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public SignUpViewModel(AuthService authService) {
        mAuthService = authService;
    }

    public void navigateToLogin() {
        mNavigateToLogin.postValue(true);
    }

    public void performAuth() {
        String email = mEmail.getValue() != null ? mEmail.getValue() : "";
        String password = mPassword.getValue() != null ? mPassword.getValue() : "";
        String confirmPassword = mConfirmPassword.getValue() != null
                ? mConfirmPassword.getValue() : "";

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mErrorToastMessage.postValue("Enter context email.");
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            mErrorToastMessage.postValue("Enter proper password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            mErrorToastMessage.postValue("Password not match both fields.");
            return;
        }
        SignUpRequest signUpRequest = new SignUpRequest(email, password, confirmPassword);
        mAuthService.signUp(signUpRequest, aVoid -> {
            mSuccessToastMessage.postValue("Sign up successful");
            navigateToHome();
        }, e -> {
            mErrorToastMessage.postValue("Sign up unsuccessful");
            Log.e(TAG, "Error: " + e);
        });
    }

    private void navigateToHome() {
        mNavigateToHome.postValue(true);
    }
}
