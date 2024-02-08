package com.example.navigation;

import android.os.Bundle;

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
    void navigateToEmailDetails(EAnimationType animationType);
    void navigateToSendOtp(EAnimationType animationType);
    void navigateToVerifyOtp(Bundle data, EAnimationType animationType);
}
