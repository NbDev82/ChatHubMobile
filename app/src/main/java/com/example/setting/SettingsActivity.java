package com.example.setting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.CustomToast;
import com.example.databinding.ActivitySettingsBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class SettingsActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        ActivitySettingsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_settings);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        SettingsViewModelFactory factory = new SettingsViewModelFactory(authRepos);
        viewModel = new ViewModelProvider(this, factory).get(SettingsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome(EAnimationType.FADE_OUT);
            }
        });

        viewModel.getNavigateToUserProfile().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToUserProfile(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToChangePhoneNumber().observe(this, navigate -> {
            if (navigate) {
                showNotImplementToast();
            }
        });

        viewModel.getNavigateToChangeEmail().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToEmailDetails(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToAccountLinking().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToAccountLinking(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToMyQrCode().observe(this, navigate -> {
            if (navigate) {
                showNotImplementToast();
            }
        });

        viewModel.getNavigateToLockMyApp().observe(this, navigate -> {
            if (navigate) {
                showNotImplementToast();
            }
        });

        viewModel.getNavigateToChangePassword().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToChangePassword(EAnimationType.FADE_IN);
            }
        });
    }

    private void showNotImplementToast() {
        CustomToast.showErrorToast(this, "Without implementation");
    }
}