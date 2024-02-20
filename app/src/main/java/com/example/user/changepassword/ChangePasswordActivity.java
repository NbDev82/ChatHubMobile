package com.example.user.changepassword;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityChangePasswordBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.authservice.AuthService;
import com.example.user.authservice.AuthServiceImpl;

public class ChangePasswordActivity extends AppCompatActivity {

    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        AuthService authService = new AuthServiceImpl();
        ChangePasswordViewModelFactory factory = new ChangePasswordViewModelFactory(authService);
        ChangePasswordViewModel viewModel = new ViewModelProvider(this, factory)
                .get(ChangePasswordViewModel.class);

        ActivityChangePasswordBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getNavigateToAccountLinking().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToAccountLinking(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToSettings().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSettings(EAnimationType.FADE_OUT);
            }
        });
    }
}