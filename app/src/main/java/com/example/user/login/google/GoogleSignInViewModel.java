package com.example.user.login.google;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.user.AuthService;
import com.example.user.login.LoginViewModel;

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

    public void signInWithIdToken(String idToken) {
        mAuthService.signInOrSignUpWithGoogle(idToken, aVoid -> {
            mSuccessToastMessage.postValue("Google login successfully");
            mIsLoading.postValue(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateToHome();
                }
            }, 100);
        }, e -> {
            mErrorToastMessage.postValue("Google login unsuccessfully");
            mIsLoading.postValue(false);
            Log.e(TAG, "Error: ", e);
        });
    }

    public void navigateToHome() {
        mNavigateToHome.postValue(true);
    }
}
