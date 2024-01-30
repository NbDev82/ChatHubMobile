package com.example.user.login.github;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.BR;
import com.example.home.HomeActivity;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginActivity;

public class GithubAuthViewModel extends BaseObservable {

    private final Activity activity;
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

    public GithubAuthViewModel(Activity activity) {
        this.activity = activity;
        authService = new AuthServiceImpl();
    }

    public void onBackToLoginBtnClick(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void onGithubLoginBtnClick(Context context) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setToastMessage("Enter a proper Email");
        } else {
            authService.signInOrSignUpWithGithub(activity, email, authResult -> {
                openHomeActivity(context);
            }, e -> {
                setToastMessage(e.getMessage());
            });
        }
    }

    private void openHomeActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
