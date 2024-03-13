package com.example.user.signup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivitySignUpBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class SignUpActivity extends BaseActivity<SignUpViewModel, ActivitySignUpBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected Class<SignUpViewModel> getViewModelClass() {
        return SignUpViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new SignUpViewModelFactory(authRepos);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToLogin().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToLogin(EAnimationType.FADE_OUT);
            }
        });

        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome(EAnimationType.FADE_IN);
            }
        });
    }
}
