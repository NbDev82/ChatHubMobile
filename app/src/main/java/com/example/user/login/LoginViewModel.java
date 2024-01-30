package com.example.user.login;

import android.util.Log;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.BR;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class LoginViewModel extends BaseObservable {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    protected final AuthService authService;
    private SignInRequest signInRequest;

    private MutableLiveData<Boolean> mNavigateToForgotPassword = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNavigateToSignUp = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNavigateToGoogleSignIn = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNavigateToGithubAuth = new MutableLiveData<>();

    public LiveData<Boolean> getNavigateToForgotPassword() {
        return mNavigateToForgotPassword;
    }

    public LiveData<Boolean> getNavigateToSignUp() {
        return mNavigateToSignUp;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public LiveData<Boolean> getNavigateToGoogleSignIn() {
        return mNavigateToGoogleSignIn;
    }

    public LiveData<Boolean> getNavigateToGithubAuth() {
        return mNavigateToGithubAuth;
    }

    @Bindable
    public String getEmail() {
        return signInRequest.getEmail();
    }

    public void setEmail(String email) {
        signInRequest.setEmail(email);
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return signInRequest.getPassword();
    }

    public void setPassword(String password) {
        signInRequest.setPassword(password);
        notifyPropertyChanged(BR.password);
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

    public LoginViewModel() {
        authService = new AuthServiceImpl();
        signInRequest = new SignInRequest();
    }

    public void onLoginBtnClick() {
        Log.i(TAG, "Login button clicked");

        String email = getEmail();
        String password = getPassword();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setToastMessage("Enter context email.");
        } else if (password.isEmpty() || password.length() < 6) {
            setToastMessage("Enter proper password.");
        } else {
            authService.signIn(signInRequest, aVoid -> {
                navigateToHome();
                setToastMessage("Login successful");
            }, e -> {
                setToastMessage("Please wait while login...");
                setToastMessage(String.valueOf(e));
            });
        }
    }

    public void navigateToForgotPassword() {
        mNavigateToForgotPassword.setValue(true);
    }

    public void navigateToSignUp() {
        mNavigateToSignUp.setValue(true);
    }

    public void navigateToHome() {
        mNavigateToHome.setValue(true);
    }

    public void navigateToGoogleSignIn() {
        mNavigateToGoogleSignIn.setValue(true);
    }

    public void navigateToGithubAuth() {
        mNavigateToGithubAuth.setValue(true);
    }

    public void onFacebookLoginClick() {
        setToastMessage("Without implementation");
    }
}