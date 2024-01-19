package com.example.user.signup;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.databinding.BaseObservable;

import com.example.user.login.LoginActivity;

public class SignUpViewModel extends BaseObservable {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    public void onBackBtnClick(Context context) {
        Log.i(TAG, "Back to login");

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
