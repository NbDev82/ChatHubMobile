package com.example.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.AnimRes;

import com.example.R;
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
    public void navigateToHome(EAnimationType animationType) {
        navigateToActivity(HomeActivity.class, animationType);
    }

    @Override
    public void navigateToSettings(EAnimationType animationType) {
        navigateToActivity(SettingsActivity.class, animationType);
    }

    @Override
    public void navigateToUserProfile(EAnimationType animationType) {
        navigateToActivity(UserProfileActivity.class, animationType);
    }

    @Override
    public void navigateToLogin(EAnimationType animationType) {
        navigateToActivity(LoginActivity.class, animationType);
    }

    @Override
    public void navigateToForgotPassword(EAnimationType animationType) {
        navigateToActivity(ForgotPasswordActivity.class, animationType);
    }

    @Override
    public void navigateToSignUp(EAnimationType animationType) {
        navigateToActivity(SignUpActivity.class, animationType);
    }

    @Override
    public void navigateToGoogleSignIn(EAnimationType animationType) {
        navigateToActivity(GoogleSignInActivity.class, animationType);
    }

    @Override
    public void navigateToGithubAuth(EAnimationType animationType) {
        navigateToActivity(GithubAuthActivity.class, animationType);
    }

    private void navigateToActivity(Class<? extends Activity> activityClass, EAnimationType animationType) {
        switch (animationType) {
            case FADE_IN:
                navigateFadeForward(activityClass);
                break;
            case FADE_OUT:
                navigateFadeBackward(activityClass);
                break;
            default:
                navigateWithoutAnimation(activityClass);
                break;
        }
    }

    private void navigateFadeForward(Class<? extends Activity> activityClass) {
        navigateFadeBackward(activityClass, R.anim.fade_in, R.anim.fade_out);
    }

    private <T> void navigateFadeBackward(Class<T> activityClass) {
        navigateFadeBackward(activityClass, R.anim.fade_out, R.anim.fade_in);
    }

    private <T> void navigateWithoutAnimation(Class<T> activityClass) {
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private <T> void navigateFadeBackward(Class<T> activityClass,
                                          @AnimRes int enterAnim,
                                          @AnimRes int exitAnim) {
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            } else {
                activity.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, enterAnim, exitAnim);
            }
        }

        context.startActivity(intent);
    }
}
