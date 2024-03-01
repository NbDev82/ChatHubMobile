package com.example.user.login.otp.phonenumberinput;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityPhoneNumberInputBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class PhoneNumberInputActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private AuthRepos authRepos;
    private PhoneNumberInputViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        UserRepos userRepos = new UserReposImpl();
        authRepos = new AuthReposImpl(userRepos);
        PhoneNumberInputViewModelFactory factory = new PhoneNumberInputViewModelFactory(userRepos, authRepos);
        viewModel = new ViewModelProvider(this, factory).get(PhoneNumberInputViewModel.class);

        ActivityPhoneNumberInputBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_phone_number_input);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToVerifyOtpWithPhoneNumber().observe(this, data -> {
            navigationManager.navigateToVerifyOtp(data, EAnimationType.FADE_IN);
        });

        viewModel.getNavigateToSignUp().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSignUp(EAnimationType.FADE_IN);
            }
        });
    }
}