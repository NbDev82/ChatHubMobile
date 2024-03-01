package com.example.user.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityForgotPasswordBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class ForgotPasswordActivity extends AppCompatActivity {

    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        ActivityForgotPasswordBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        ForgotPasswordViewModelFactory factory = new ForgotPasswordViewModelFactory(authRepos);
        ForgotPasswordViewModel viewModel = new ViewModelProvider(this, factory)
                .get(ForgotPasswordViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getNavigateToLogin().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToLogin(EAnimationType.FADE_OUT);
            }
        });
    }
}