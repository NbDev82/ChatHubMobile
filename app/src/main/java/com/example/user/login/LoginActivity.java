package com.example.user.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityLoginBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class LoginActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        ActivityLoginBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_login);

        AuthService authService = new AuthServiceImpl();
        LoginViewModelFactory factory = new LoginViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();

        viewModel.navigateIfAuthenticated();
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