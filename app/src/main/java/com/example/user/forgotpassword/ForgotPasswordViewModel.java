package com.example.user.forgotpassword;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.repository.AuthRepos;

public class ForgotPasswordViewModel extends BaseViewModel {

    private static final String TAG = ForgotPasswordViewModel.class.getSimpleName();

    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSending = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    public LiveData<Boolean> getIsSending() {
        return isSending;
    }

    public ForgotPasswordViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    public void navigateToLogin() {
        navigateToLogin.postValue(true);
    }

    public void onSendResetPasswordClick() {
        isSending.postValue(true);
        String email = this.email.getValue();
        if (email == null || email.isEmpty()) {
            errorToastMessage.postValue("Enter your email");
            return;
        }
        
        this.email.postValue( email.trim() );
        authRepos.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    successToastMessage.postValue("Password reset link sent to your Email");
                    isSending.postValue(false);
                    new Handler().postDelayed(this::navigateToLogin, 500);
                })
                .addOnFailureListener(e -> {
                    errorToastMessage.postValue("Failed to reset password");
                    isSending.postValue(false);
                    Log.e(TAG, "Error: " + e);
                });
    }
}
