package com.example.user.login;

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
import com.example.user.signup.SignUpActivity;

public class LoginViewModel extends BaseObservable {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    private final Context context;
    private final AuthService authService;

    private SignInRequest signInRequest;

    @Bindable
    public String getEmail() {
        return signInRequest.getEmail();
    }

    public void setEmail(String email) {
        signInRequest.setEmail(email);
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return signInRequest.getPassword();
    }

    public void setPassword(String password) {
        signInRequest.setPassword(password);
        notifyPropertyChanged(BR.password);
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

    public LoginViewModel(Context context) {
        this.context = context;
        authService = new AuthServiceImpl();

        signInRequest = new SignInRequest();
    }

    public void onButtonClicked() {
        Log.i(TAG, "Login button clicked");
        performLogin();
    }

    private void performLogin() {
        String email = getEmail();
        String password = getPassword();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setToastMessage("Enter context email.");
        } else if (password.isEmpty() || password.length() < 6) {
            setToastMessage("Enter proper password.");
        } else {
            authService.signIn(signInRequest, aVoid -> {
                sendUserToHomeActivity();
                setToastMessage("Login successful");
            }, e -> {
                setToastMessage("Please wait while login...");
                setToastMessage(String.valueOf(e));
            });
        }
    }

    private void sendUserToHomeActivity() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void onSignUpTextClick(Context context) {
        Log.i(TAG, "Sign Up clicked");

        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    public void onGoogleLoginClick() {
        Intent intent = new Intent(context, GoogleSignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}