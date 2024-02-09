package com.example.user.login.google;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.user.AuthService;
import com.example.user.login.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignInViewModel extends LoginViewModel {

    private static final String TAG = GoogleSignInViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    public GoogleSignInViewModel(AuthService authService) {
        super(authService);

        isLoading.postValue(true);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            signInWithIdToken(idToken);
        } catch (ApiException e) {
            isLoading.postValue(false);
        }
    }

    private void signInWithIdToken(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        authService.signInWithCredential(credential, aVoid -> {
            isLoading.postValue(false);
            navigateToHome();
        }, e -> {
            isLoading.postValue(false);
            Log.e(TAG, "Error: ", e);
        });
    }

    public void navigateToHome() {
        navigateToHome.postValue(true);
    }
}
