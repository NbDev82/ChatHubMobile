package com.example.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.customcontrol.CustomToast;
import com.example.R;
import com.example.databinding.ActivitySettingsBinding;
import com.example.home.HomeActivity;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.profile.UserProfileActivity;

public class SettingsActivity extends AppCompatActivity {

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        ActivitySettingsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_settings);

        AuthService authService = new AuthServiceImpl();
        SettingsViewModelFactory factory = new SettingsViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(SettingsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setObservers();

    }

    private void setObservers() {
        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
        });

        viewModel.getNavigateToUserProfile().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
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