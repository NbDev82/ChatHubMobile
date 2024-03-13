package com.example.user.login.otp.phonenumberinput;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityPhoneNumberInputBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class PhoneNumberInputActivity extends BaseActivity<PhoneNumberInputViewModel, ActivityPhoneNumberInputBinding> {

    private AuthRepos authRepos;

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_phone_number_input;
    }

    @Override
    protected Class<PhoneNumberInputViewModel> getViewModelClass() {
        return PhoneNumberInputViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        authRepos = new AuthReposImpl(userRepos);
        return new PhoneNumberInputViewModelFactory(userRepos, authRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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