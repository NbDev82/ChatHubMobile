package com.example.user.login.github;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityGithubAuthBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class GithubAuthActivity extends BaseActivity<GithubAuthViewModel, ActivityGithubAuthBinding> {

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_github_auth;
    }

    @Override
    protected Class<GithubAuthViewModel> getViewModelClass() {
        return GithubAuthViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new GithubAuthViewModelFactory(this, authRepos);
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

        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome(EAnimationType.FADE_IN);
            }
        });
    }
}