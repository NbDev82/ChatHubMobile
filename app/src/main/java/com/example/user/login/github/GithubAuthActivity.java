package com.example.user.login.github;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityGithubAuthBinding;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class GithubAuthActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private GithubAuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationManager = new NavigationManagerImpl(this);

        ActivityGithubAuthBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_github_auth);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        GithubAuthViewModelFactory factory = new GithubAuthViewModelFactory(this, authRepos);
        viewModel = new ViewModelProvider(this, factory).get(GithubAuthViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

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