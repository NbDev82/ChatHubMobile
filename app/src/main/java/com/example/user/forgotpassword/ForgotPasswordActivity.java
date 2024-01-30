package com.example.user.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityForgotPasswordBinding activityForgotPasswordBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        ForgotPasswordViewModel viewModel = new ForgotPasswordViewModel();
        activityForgotPasswordBinding.setViewModel(viewModel);
        activityForgotPasswordBinding.executePendingBindings();
    }
}