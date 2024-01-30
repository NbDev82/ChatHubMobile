package com.example.user.signup;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.BR;
import com.example.home.HomeActivity;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginActivity;

public class SignUpViewModel extends BaseObservable {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private final Context context;
    private AuthService authService;

    private SignUpRequest signUpRequest;

    @Bindable
    public String getEmail() {
        return signUpRequest.getEmail();
    }

    public void setEmail(String email) {
        signUpRequest.setEmail(email);
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return signUpRequest.getPassword();
    }

    public void setPassword(String password) {
        signUpRequest.setPassword(password);
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getConfirmPassword() {
        return signUpRequest.getConfirmPassword();
    }

    public void setConfirmPassword(String confirmPassword) {
        signUpRequest.setConfirmPassword(confirmPassword);
        notifyPropertyChanged(BR.confirmPassword);
    }

    @Bindable
    private String toastMessage;

    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public SignUpViewModel(Context context) {
        this.context = context;
        authService = new AuthServiceImpl();

        signUpRequest = new SignUpRequest();
    }

    public void onBackBtnClick(Context context) {
        Log.i(TAG, "Back to login");

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void onSignUpBtnClick() {
        performAuth();
    }

    private void performAuth() {
        String email = getEmail();
        String password = getPassword();
        String confirmPassword = getConfirmPassword();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setToastMessage("Enter context email.");
        } else if (password.isEmpty() || password.length() < 6) {
            setToastMessage("Enter proper password.");
        } else if (!password.equals(confirmPassword)) {
            setToastMessage("Password not match both fields.");
        } else {
            authService.signUp(signUpRequest, aVoid -> {
                sendUserToNextActivity();
                setToastMessage("Registration successful");
            }, e -> {
                setToastMessage("Please wait while sign up...");
                setToastMessage(String.valueOf(e));
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
