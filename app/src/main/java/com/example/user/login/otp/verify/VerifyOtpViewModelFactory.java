package com.example.user.login.otp.verify;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

public class VerifyOtpViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    public VerifyOtpViewModelFactory(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VerifyOtpViewModel.class)) {
            return (T) new VerifyOtpViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
