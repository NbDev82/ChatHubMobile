package com.example.user.forgotpassword;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

public class ForgotPasswordViewModel extends BaseViewModel {

    private static final String TAG = ForgotPasswordViewModel.class.getSimpleName();

    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsSending = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
    }

    public LiveData<Boolean> getIsSending() {
        return mIsSending;
    }

    public ForgotPasswordViewModel(AuthService authService) {
        mAuthService = authService;
    }

    public void navigateToLogin() {
        mNavigateToLogin.postValue(true);
    }

    public void onSendResetPasswordClick() {
        mIsSending.postValue(true);
        String email = mEmail.getValue();
        if (email == null || email.isEmpty()) {
            mErrorToastMessage.postValue("Enter your email");
            return;
        }
        
        mEmail.postValue( email.trim() );
        mAuthService.sendPasswordResetEmail(email, aVoid -> {
            mSuccessToastMessage.postValue("Password reset link sent to your Email");
            mIsSending.postValue(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateToLogin();
                }
            }, 500);
        }, e -> {
            mErrorToastMessage.postValue("Failed to reset password");
            mIsSending.postValue(false);
            Log.e(TAG, "Error: " + e);
        });
    }
}
