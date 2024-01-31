package com.example.user.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityForgotPasswordBinding;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityForgotPasswordBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        AuthService authService = new AuthServiceImpl();
        ForgotPasswordViewModelFactory factory = new ForgotPasswordViewModelFactory(authService);
        ForgotPasswordViewModel viewModel = new ViewModelProvider(this, factory)
                .get(ForgotPasswordViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getNavigateToLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}