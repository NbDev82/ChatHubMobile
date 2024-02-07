package com.example.navigation;

import android.content.Context;
import android.content.Intent;

import com.example.home.HomeActivity;
import com.example.setting.SettingsActivity;
import com.example.user.forgotpassword.ForgotPasswordActivity;
import com.example.user.login.LoginActivity;
import com.example.user.login.github.GithubAuthActivity;
import com.example.user.login.google.GoogleSignInActivity;
import com.example.user.profile.UserProfileActivity;
import com.example.user.signup.SignUpActivity;

public class NavigationManagerImpl implements NavigationManager {

    private final Context context;

    public NavigationManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void navigateToHome() {
        navigateToActivity(HomeActivity.class);
    }

    @Override
    public void navigateToSettings() {
        navigateToActivity(SettingsActivity.class);
    }

    @Override
    public void navigateToUserProfile() {
        navigateToActivity(UserProfileActivity.class);
    }

    @Override
    public void navigateToLogin() {
        navigateToActivity(LoginActivity.class);
    }

    @Override
    public void navigateToForgotPassword() {
        navigateToActivity(ForgotPasswordActivity.class);
    }

    @Override
    public void navigateToSignUp() {
        navigateToActivity(SignUpActivity.class);
    }

    @Override
    public void navigateToGoogleSignIn() {
        navigateToActivity(GoogleSignInActivity.class);
    }

    @Override
    public void navigateToGithubAuth() {
        navigateToActivity(GithubAuthActivity.class);
    }

    private <T> void navigateToActivity(Class<T> activityClass) {
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
