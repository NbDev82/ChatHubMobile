package com.example.user.signup;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private final AuthService mAuthService;
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mPassword = new MutableLiveData<>();
    private final MutableLiveData<String> mConfirmPassword = new MutableLiveData<>();
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
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

    public MutableLiveData<String> getToastMessage() {
        return mToastMessage;
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
        mNavigateToLogin.setValue(true);
    }

    public void performAuth() {
        String email = mEmail.getValue() != null ? mEmail.getValue() : "";
        String password = mPassword.getValue() != null ? mPassword.getValue() : "";
        String confirmPassword = mConfirmPassword.getValue() != null
                ? mConfirmPassword.getValue() : "";

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mToastMessage.setValue("Enter context email.");
        } else if (password.isEmpty() || password.length() < 6) {
            mToastMessage.setValue("Enter proper password.");
        } else if (!password.equals(confirmPassword)) {
            mToastMessage.setValue("Password not match both fields.");
        } else {
            SignUpRequest signUpRequest = new SignUpRequest(email, password, confirmPassword);
            mAuthService.signUp(signUpRequest, aVoid -> {
                navigateToHome();
                mToastMessage.setValue("Registration successful");
            }, e -> {
                mToastMessage.setValue(String.valueOf(e));
                Log.e(TAG, "Error: " + e);
            });
        }
    }

    private void navigateToHome() {
        mNavigateToHome.setValue(true);
    }
}
