package com.example.user.forgotpassword;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.example.BR;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class ForgotPasswordViewModel extends BaseObservable {

    private static final String TAG = ForgotPasswordViewModel.class.getSimpleName();

    private final AuthService authService;

    private String email;

    private MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();

    public MutableLiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
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

    public ForgotPasswordViewModel() {
        authService = new AuthServiceImpl();
    }

    public void navigateToLogin() {
        mNavigateToLogin.setValue(true);
    }

    public void onSendResetPasswordClick() {
        authService.sendPasswordResetEmail(email, aVoid -> {
            setToastMessage("Password reset link sent to your Email");
            navigateToLogin();
        }, e -> {
            Log.e(TAG, "Error: " + e);
        });
    }
}
