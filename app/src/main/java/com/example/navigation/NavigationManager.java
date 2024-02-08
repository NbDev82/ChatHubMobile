package com.example.navigation;

public interface NavigationManager {
    void navigateToHome(EAnimationType animationType);
    void navigateToSettings(EAnimationType animationType);
    void navigateToUserProfile(EAnimationType animationType);
    void navigateToLogin(EAnimationType animationType);
    void navigateToForgotPassword(EAnimationType animationType);
    void navigateToSignUp(EAnimationType animationType);
    void navigateToGoogleSignIn(EAnimationType animationType);
    void navigateToGithubAuth(EAnimationType animationType);
    void navigateToChangePassword(EAnimationType animationType);
    void navigateToAccountLinking(EAnimationType animationType);
}
