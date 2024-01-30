package com.example.user.forgotpassword;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.BR;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginActivity;

public class ForgotPasswordViewModel extends BaseObservable {

    private static final String TAG = ForgotPasswordViewModel.class.getSimpleName();

    private final AuthService authService;

    private String email;

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    private String toastMessage = null;

    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public ForgotPasswordViewModel() {
        authService = new AuthServiceImpl();
    }

    public void onBackToLoginBtnClick(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void onSendResetPasswordClick(Context context) {
        authService.sendPasswordResetEmail(email, aVoid -> {
            setToastMessage("Password reset link sent to your Email");
            onBackToLoginBtnClick(context);
        }, e -> {
            Log.e(TAG, "Error: " + e);
        });
    }
}
