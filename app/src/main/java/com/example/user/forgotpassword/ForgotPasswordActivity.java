package com.example.user.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityForgotPasswordBinding;
import com.example.user.login.LoginActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityForgotPasswordBinding activityForgotPasswordBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        ForgotPasswordViewModel viewModel = new ForgotPasswordViewModel();
        activityForgotPasswordBinding.setViewModel(viewModel);
        activityForgotPasswordBinding.executePendingBindings();

        viewModel.getNavigateToLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}