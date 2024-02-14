package com.example.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityHomeBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class HomeActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        ActivityHomeBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_home);

        AuthService authService = new AuthServiceImpl();
        HomeViewModelFactory factory = new HomeViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    public void setupObservers() {
        viewModel.getNavigateToSettings().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSettings(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToUserProfile().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToUserProfile(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToFriendRequests().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToFriendRequests(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToLogin().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToLogin(EAnimationType.FADE_OUT);
            }
        });
    }
}