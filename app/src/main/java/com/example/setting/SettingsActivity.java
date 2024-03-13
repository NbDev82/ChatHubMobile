package com.example.setting;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.CustomToast;
import com.example.databinding.ActivitySettingsBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class SettingsActivity extends BaseActivity<SettingsViewModel, ActivitySettingsBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected Class<SettingsViewModel> getViewModelClass() {
        return SettingsViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new SettingsViewModelFactory(authRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        viewModel.getNavigateToLockApp().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToLockApp(EAnimationType.FADE_IN);
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