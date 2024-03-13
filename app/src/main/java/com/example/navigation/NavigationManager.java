package com.example.navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;

public interface NavigationManager {
    int DEFAULT_FLAGS = 0;

    void navigateToHome(EAnimationType animationType);

    void navigateToUserProfile(EAnimationType animationType);

    void navigateToLogin(EAnimationType animationType);

    void navigateToForgotPassword(EAnimationType animationType);

    void navigateToSignUp(EAnimationType animationType);

    void navigateToGoogleSignIn(EAnimationType animationType);

    void navigateToGithubAuth(EAnimationType animationType);

    void navigateToChangePassword(EAnimationType animationType);

    void navigateToAccountLinking(EAnimationType animationType);

    void navigateToEmailDetails(EAnimationType animationType);

    void navigateToPhoneNumberInput(EAnimationType animationType);

    void navigateToVerifyOtp(Bundle data, EAnimationType animationType);

    void navigateToProfileViewer(Bundle data, EAnimationType animationType);

    void navigateBack(Bundle data, EAnimationType animationType);

    void navigateToFriends(EAnimationType animationType);

    void navigateToLockApp(EAnimationType animationType);

    void navigateToUnlockApp(EAnimationType animationType);

    void navigateToSetPasscodeWithActivityResultLauncher(
            ActivityResultLauncher<Intent> launcher,
            EAnimationType animationType);

    void navigateToSetPasscode(EAnimationType animationType, int flags);

    void navigateToChangePasscode(EAnimationType animationType);
}
