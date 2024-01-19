package com.example.user.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.BR;
import com.example.user.User;
import com.example.user.signup.SignUpActivity;

public class LoginViewModel extends BaseObservable {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    private User user;

    private String successMessage = "Login successful";
    private String errorMessage = "Email or Password is not valid";

    @Bindable
    private String toastMessage = null;

    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    @Bindable
    public String getUserEmail() {
        return user.getEmail();
    }

    public void setUserEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserPassword() {
        return user.getHashedPass();
    }

    public void setUserPassword(String password) {
        user.setHashedPass(password);
        notifyPropertyChanged(BR.userPassword);
    }

    public LoginViewModel() {
        user = new User();
    }

    public void onButtonClicked() {
        Log.i(TAG, "Login button clicked");
        if (isValid())
            setToastMessage(successMessage);
        else
            setToastMessage(errorMessage);
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(getUserEmail()) && Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches()
                && getUserPassword().length() > 5;
    }

    public void onSignUpTextClick(Context context) {
        Log.i(TAG, "Sign Up clicked");

        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }
}