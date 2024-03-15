package com.example.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;

import com.example.R;
import com.example.friend.friendsuggestion.FriendSuggestionsActivity;
import com.example.friend.myfriend.FriendsActivity;
import com.example.friend.profileviewer.ProfileViewerActivity;
import com.example.home.HomeActivity;
import com.example.passcode.changepasscode.ChangePasscodeActivity;
import com.example.passcode.lockapp.LockAppActivity;
import com.example.passcode.setpasscode.SetPasscodeActivity;
import com.example.passcode.unlockapp.UnlockAppActivity;
import com.example.user.accountlink.AccountLinkingActivity;
import com.example.user.changepassword.ChangePasswordActivity;
import com.example.user.emaildetails.EmailDetailsActivity;
import com.example.user.forgotpassword.ForgotPasswordActivity;
import com.example.user.login.LoginActivity;
import com.example.user.login.github.GithubAuthActivity;
import com.example.user.login.google.GoogleSignInActivity;
import com.example.user.login.otp.phonenumberinput.PhoneNumberInputActivity;
import com.example.user.login.otp.verify.VerifyOtpActivity;
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

    @Override
    public void navigateToChangePassword(EAnimationType animationType) {
        navigateToActivity(ChangePasswordActivity.class, animationType);
    }

    @Override
    public void navigateToAccountLinking(EAnimationType animationType) {
        navigateToActivity(AccountLinkingActivity.class, animationType);
    }

    @Override
    public void navigateToEmailDetails(EAnimationType animationType) {
        navigateToActivity(EmailDetailsActivity.class, animationType);
    }

    @Override
    public void navigateToPhoneNumberInput(EAnimationType animationType) {
        navigateToActivity(PhoneNumberInputActivity.class, animationType);
    }

    @Override
    public void navigateToVerifyOtp(Bundle data, EAnimationType animationType) {
        navigateToActivity(VerifyOtpActivity.class, data, animationType);
    }

    @Override
    public void navigateToProfileViewer(Bundle data, EAnimationType animationType) {
        navigateToActivity(ProfileViewerActivity.class, data, animationType);
    }

    @Override
    public void navigateBack(Bundle data, EAnimationType animationType) {
        Intent resultIntent = new Intent();
        if (data != null) {
            resultIntent.putExtras(data);
        }
        setResultAndFinish(resultIntent, animationType);
    }

    @Override
    public void navigateToFriends(EAnimationType animationType) {
        navigateToActivity(FriendsActivity.class, animationType);
    }

    @Override
    public void navigateToLockApp(EAnimationType animationType) {
        navigateToActivity(LockAppActivity.class, animationType);
    }

    @Override
    public void navigateToUnlockApp(EAnimationType animationType) {
        navigateToActivity(UnlockAppActivity.class, animationType);
    }

    @Override
    public void navigateToSetPasscodeWithActivityResultLauncher(
            ActivityResultLauncher<Intent> launcher,
            EAnimationType animationType) {

        navigateToActivityWithActivityResultLauncher(SetPasscodeActivity.class,
                launcher, null, animationType, DEFAULT_FLAGS);
    }

    @Override
    public void navigateToSetPasscode(EAnimationType animationType, int flags) {
        navigateToActivity(SetPasscodeActivity.class, null, animationType, flags);
    }

    @Override
    public void navigateToChangePasscode(EAnimationType animationType) {
        navigateToActivity(ChangePasscodeActivity.class, animationType);
    }

    @Override
    public void navigateToFriendSuggestions(EAnimationType animationType) {
        navigateToActivity(FriendSuggestionsActivity.class, animationType);
    }

    private void setResultAndFinish(Intent resultIntent, EAnimationType animationType) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.setResult(Activity.RESULT_OK, resultIntent);
            overridePendingTransitionForAnimation(animationType);
            activity.finish();
        }
    }

    private void overridePendingTransitionForAnimation(EAnimationType animationType) {
        if (context == null || !(context instanceof Activity)) {
            return;
        }

        Activity activity = (Activity) context;
        int enterAnim, exitAnim;

        switch (animationType) {
            case FADE_IN:
                enterAnim = R.anim.fade_in;
                exitAnim = R.anim.fade_out;
                break;
            case FADE_OUT:
                enterAnim = R.anim.fade_out;
                exitAnim = R.anim.fade_in;
                break;
            default:
                enterAnim = exitAnim = 0;
                break;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        } else {
            activity.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, enterAnim, exitAnim);
        }
    }

    private void navigateToActivity(Class<? extends Activity> activityClass, EAnimationType animationType) {
        navigateToActivity(activityClass, null, animationType, DEFAULT_FLAGS);
    }

    private void navigateToActivity(Class<? extends Activity> activityClass,
                                    Bundle data,
                                    EAnimationType animationType) {
        navigateToActivity(activityClass, data, animationType, DEFAULT_FLAGS);
    }

    private void navigateToActivity(Class<? extends Activity> activityClass,
                                    Bundle data,
                                    EAnimationType animationType,
                                    int flags) {

        Intent intent = new Intent(context, activityClass);
        if (data != null) {
            intent.putExtras(data);
        }
        intent.setFlags(flags);
        overridePendingTransitionForAnimation(animationType);
        context.startActivity(intent);
    }

    private void navigateToActivityWithActivityResultLauncher(
            Class<? extends Activity> activityClass,
            ActivityResultLauncher<Intent> launcher,
            Bundle data,
            EAnimationType animationType,
            int flags) {

        Intent intent = new Intent(context, activityClass);
        if (data != null) {
            intent.putExtras(data);
        }
        intent.setFlags(flags);
        overridePendingTransitionForAnimation(animationType);
        launcher.launch(intent);
    }
}
