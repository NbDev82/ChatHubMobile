package com.example.user.emaildetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityEmailDetailsBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class EmailDetailsActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private EmailDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        EmailDetailsViewModelFactory factory = new EmailDetailsViewModelFactory(authRepos);
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