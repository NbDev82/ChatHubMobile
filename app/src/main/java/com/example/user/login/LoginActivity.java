package com.example.user.login;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityLoginBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class LoginActivity extends BaseActivity<LoginViewModel, ActivityLoginBinding> {

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new LoginViewModelFactory(authRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.navigateIfAuthenticated();
        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToForgotPassword().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToForgotPassword(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToSignUp().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSignUp(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToSendOtp().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToPhoneNumberInput(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToGoogleSignIn().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToGoogleSignIn(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToGithubAuth().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToGithubAuth(EAnimationType.FADE_IN);
            }
        });
    }
}