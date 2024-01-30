package com.example.user.login.github;

import android.app.Activity;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.BR;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class GithubAuthViewModel extends BaseObservable {

    private final Activity activity;
    private final AuthService authService;

    private String email;

    private MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();

    public LiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    private String toastMessage = null;

    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public GithubAuthViewModel(Activity activity) {
        this.activity = activity;
        authService = new AuthServiceImpl();
    }

    public void navigateToLogin() {
        mNavigateToLogin.setValue(true);
    }

    public void onGithubLoginBtnClick() {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setToastMessage("Enter a proper Email");
        } else {
            authService.signInOrSignUpWithGithub(activity, email, authResult -> {
                navigateToHome();
            }, e -> {
                setToastMessage(e.getMessage());
            });
        }
    }

    public void navigateToHome() {
        mNavigateToHome.setValue(true);
    }
}
