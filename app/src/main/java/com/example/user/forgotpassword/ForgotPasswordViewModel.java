package com.example.user.forgotpassword;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.AuthService;

public class ForgotPasswordViewModel extends ViewModel {

    private static final String TAG = ForgotPasswordViewModel.class.getSimpleName();

    private final AuthService mAuthService;
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public MutableLiveData<String> getToastMessage() {
        return mToastMessage;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
    }

    public ForgotPasswordViewModel(AuthService authService) {
        mAuthService = authService;
    }

    public void navigateToLogin() {
        mNavigateToLogin.setValue(true);
    }

    public void onSendResetPasswordClick() {
        String email = mEmail.getValue();
        if (email == null || email.isEmpty()) {
            mToastMessage.setValue("Enter your email");
            return;
        }
        mEmail.setValue( email.trim() );
        mAuthService.sendPasswordResetEmail(email, aVoid -> {
            mToastMessage.setValue("Password reset link sent to your Email");
            navigateToLogin();
        }, e -> {
            Log.e(TAG, "Error: " + e);
            mToastMessage.setValue("Error: " + e);
        });
    }
}
