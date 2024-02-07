package com.example.setting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.CustomToast;
import com.example.databinding.ActivitySettingsBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

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

        AuthService authService = new AuthServiceImpl();
        SettingsViewModelFactory factory = new SettingsViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(SettingsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome();
            }
        });

        viewModel.getNavigateToUserProfile().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToUserProfile();
            }
        });

        viewModel.getNavigateToChangePhoneNumber().observe(this, navigate -> {
            if (navigate) {
                showNotImplementToast();
            }
        });

        viewModel.getNavigateToChangeEmail().observe(this, navigate -> {
            if (navigate) {
                showNotImplementToast();
            }
        });

        viewModel.getNavigateToAccountLinking().observe(this, navigate -> {
            if (navigate) {
                showNotImplementToast();
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
                showNotImplementToast();
            }
        });
    }

    private void showNotImplementToast() {
        CustomToast.showErrorToast(this, "Without implementation");
    }
}