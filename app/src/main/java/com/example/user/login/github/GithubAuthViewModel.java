package com.example.user.login.github;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GithubAuthCredential;

import java.lang.ref.WeakReference;

public class GithubAuthViewModel extends BaseViewModel {

    private static final String TAG = GithubAuthViewModel.class.getSimpleName();

    private final WeakReference<Activity> activityRef;
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLogging = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    public LiveData<Boolean> getIsLogging() {
        return isLogging;
    }

    public GithubAuthViewModel(Activity activity, AuthService authService) {
        activityRef = new WeakReference<>(activity);
        this.authService = authService;
    }

    public void navigateToLogin() {
        navigateToLogin.postValue(true);
    }

    public void onGithubLoginBtnClick() {
        isLogging.postValue(true);
        String email = this.email.getValue() != null ? this.email.getValue() : "";
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorToastMessage.postValue("Enter a proper Email");
            isLogging.postValue(false);
            return;
        }
        Activity activity = activityRef.get();
        if (activity != null) {
            authService.signInWithGithub(activity, email)
                    .addOnSuccessListener(authResult -> {
                        isLogging.postValue(false);
                        successToastMessage.postValue("Login successfully");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                navigateToHome();
                            }
                        }, 500);
                    })
                    .addOnFailureListener(e -> {
                        isLogging.postValue(false);
                        errorToastMessage.postValue(e.getMessage());
                        Log.e(TAG, "Error: ", e);
                    });
        }
    }

    public void navigateToHome() {
        navigateToHome.postValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        activityRef.clear();
    }
}
