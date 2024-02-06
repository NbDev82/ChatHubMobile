package com.example.user.forgotpassword;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

public class ForgotPasswordViewModel extends BaseViewModel {

    private static final String TAG = ForgotPasswordViewModel.class.getSimpleName();

    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
    }

    public ForgotPasswordViewModel(AuthService authService) {
        mAuthService = authService;
    }

    public void navigateToLogin() {
        mNavigateToLogin.postValue(true);
    }

    public void onSendResetPasswordClick() {
        String email = mEmail.getValue();
        if (email == null || email.isEmpty()) {
            mErrorToastMessage.postValue("Enter your email");
            return;
        }
        
        mEmail.postValue( email.trim() );
        mAuthService.sendPasswordResetEmail(email, aVoid -> {
            mSuccessToastMessage.postValue("Password reset link sent to your Email");
            navigateToLogin();
        }, e -> {
            mErrorToastMessage.postValue("Failed to reset password");
            Log.e(TAG, "Error: " + e);
        });
    }
}
