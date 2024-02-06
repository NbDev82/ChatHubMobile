package com.example.user.login.github;

import android.app.Activity;
import android.os.Handler;
import android.util.Half;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

import java.lang.ref.WeakReference;

public class GithubAuthViewModel extends BaseViewModel {

    private static final String TAG = GithubAuthViewModel.class.getSimpleName();

    private WeakReference<Activity> mActivityRef;
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsLogging = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public LiveData<Boolean> getIsLogging() {
        return mIsLogging;
    }

    public GithubAuthViewModel(Activity activity, AuthService authService) {
        mActivityRef = new WeakReference<>(activity);
        mAuthService = authService;
    }

    public void navigateToLogin() {
        mNavigateToLogin.postValue(true);
    }

    public void onGithubLoginBtnClick() {
        mIsLogging.postValue(true);
        String email = mEmail.getValue() != null ? mEmail.getValue() : "";
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mErrorToastMessage.postValue("Enter a proper Email");
            mIsLogging.postValue(false);
            return;
        }
        Activity activity = mActivityRef.get();
        if (activity != null) {
            mAuthService.signInOrSignUpWithGithub(activity, email, authResult -> {
                mIsLogging.postValue(false);
                mSuccessToastMessage.postValue("Login successfully");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigateToHome();
                    }
                }, 500);
            }, e -> {
                mIsLogging.postValue(false);
                mErrorToastMessage.postValue(e.getMessage());
                Log.e(TAG, "Error: ", e);
            });
        }
    }

    public void navigateToHome() {
        mNavigateToHome.postValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mActivityRef.clear();
    }
}
