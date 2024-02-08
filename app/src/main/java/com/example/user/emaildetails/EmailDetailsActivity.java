package com.example.user.emaildetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityAccountLinkingBinding;
import com.example.databinding.ActivityEmailDetailsBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.accountlink.AccountLinkingViewModel;
import com.example.user.accountlink.AccountLinkingViewModelFactory;

public class EmailDetailsActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private EmailDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        AuthService authService = new AuthServiceImpl();
        EmailDetailsViewModelFactory factory = new EmailDetailsViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(EmailDetailsViewModel.class);

        ActivityEmailDetailsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_email_details);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToSettings().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSettings(EAnimationType.FADE_OUT);
            }
        });
    }
}