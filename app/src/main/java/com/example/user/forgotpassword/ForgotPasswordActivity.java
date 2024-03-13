package com.example.user.forgotpassword;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityForgotPasswordBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class ForgotPasswordActivity extends BaseActivity<ForgotPasswordViewModel, ActivityForgotPasswordBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected Class<ForgotPasswordViewModel> getViewModelClass() {
        return ForgotPasswordViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new ForgotPasswordViewModelFactory(authRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToLogin().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToLogin(EAnimationType.FADE_OUT);
            }
        });
    }
}