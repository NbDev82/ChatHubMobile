package com.example.user.signup;

import android.os.Handler;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

public class SignUpViewModel extends BaseViewModel {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mPassword = new MutableLiveData<>();
    private final MutableLiveData<String> mConfirmPassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsSigningUp = new MutableLiveData<>();

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

    public LiveData<Boolean> getIsSigningUp() {
        return mIsSigningUp;
    }

    public SignUpViewModel(AuthService authService) {
        mAuthService = authService;
    }

    public void navigateToLogin() {
        mNavigateToLogin.postValue(true);
    }

    public void signUp() {
        mIsSigningUp.postValue(true);
        String email = mEmail.getValue() != null ? mEmail.getValue() : "";
        String password = mPassword.getValue() != null ? mPassword.getValue() : "";
        String confirmPassword = mConfirmPassword.getValue() != null
                ? mConfirmPassword.getValue() : "";

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mErrorToastMessage.postValue("Enter context email");
            mIsSigningUp.postValue(false);
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            mErrorToastMessage.postValue("Enter proper password");
            mIsSigningUp.postValue(false);
            return;
        }

        if (!password.equals(confirmPassword)) {
            mErrorToastMessage.postValue("Password not match both fields");
            mIsSigningUp.postValue(false);
            return;
        }
        SignUpRequest signUpRequest = new SignUpRequest(email, password, confirmPassword);
        mAuthService.signUp(signUpRequest, aVoid -> {
            mSuccessToastMessage.postValue("Sign up successful");
            mIsSigningUp.postValue(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateToHome();
                }
            }, 500);
        }, e -> {
            mErrorToastMessage.postValue(e.getMessage());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsSigningUp.postValue(false);
                }
            }, 500);
            Log.e(TAG, "Error: " + e);
        });
    }

    private void navigateToHome() {
        mNavigateToHome.postValue(true);
    }
}
