package com.example.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.example.R;
import com.example.databinding.ActivityLoginBinding;
import com.example.home.HomeActivity;
import com.example.user.forgotpassword.ForgotPasswordActivity;
import com.example.user.login.github.GithubAuthActivity;
import com.example.user.login.google.GoogleSignInActivity;
import com.example.user.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding activityLoginBinding
                = DataBindingUtil.setContentView(
                this, R.layout.activity_login);
        viewModel = new LoginViewModel();
        activityLoginBinding.setViewModel(viewModel);
        activityLoginBinding.executePendingBindings();

        setObservers();
    }

    private void setObservers() {
        viewModel.getNavigateToForgotPassword().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
                }
            }
        });

        viewModel.getNavigateToSignUp().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            }
        });

        viewModel.getNavigateToHome().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        viewModel.getNavigateToGoogleSignIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(LoginActivity.this, GoogleSignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        viewModel.getNavigateToGithubAuth().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(LoginActivity.this, GithubAuthActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}