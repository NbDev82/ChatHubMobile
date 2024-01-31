package com.example.user.login.google;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.R;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;

public class GoogleSignInViewModel extends LoginViewModel {

    public static final int RC_SIGN_IN = 101;
    private final GoogleSignInClient mGoogleSignInClient;
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsSignInSuccess = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public LiveData<Boolean> isSignInSuccess() {
        return mIsSignInSuccess;
    }

    public GoogleSignInViewModel(AuthService authService, GoogleSignInClient googleSignInClient) {
        super(authService);
        mGoogleSignInClient = googleSignInClient;
    }

    public void signIn(Activity activity) {
        mIsLoading.setValue(true);
        mGoogleSignInClient.signOut();
        Intent intent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(intent, RC_SIGN_IN);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            mAuthService.signInOrSignUpWithGoogle(idToken, aVoid -> {
                mIsLoading.setValue(true);
                mIsSignInSuccess.setValue(true);
            }, e -> {
                mIsLoading.setValue(false);
                mIsSignInSuccess.setValue(false);
            });
        } catch (ApiException e) {
            mIsLoading.setValue(false);
            mIsSignInSuccess.setValue(false);
        }
    }
}
