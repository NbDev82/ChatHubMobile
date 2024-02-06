package com.example.user.login.google;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.user.AuthService;
import com.example.user.login.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleSignInViewModel extends LoginViewModel {

    private static final String TAG = GoogleSignInViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public GoogleSignInViewModel(AuthService authService) {
        super(authService);

        mIsLoading.postValue(true);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            signInWithIdToken(idToken);
        } catch (ApiException e) {
            mIsLoading.postValue(false);
        }
    }

    private void signInWithIdToken(String idToken) {
        mAuthService.signInOrSignUpWithGoogle(idToken, aVoid -> {
            mIsLoading.postValue(false);
            navigateToHome();
        }, e -> {
            mIsLoading.postValue(false);
            Log.e(TAG, "Error: ", e);
        });
    }

    public void navigateToHome() {
        mNavigateToHome.postValue(true);
    }
}
