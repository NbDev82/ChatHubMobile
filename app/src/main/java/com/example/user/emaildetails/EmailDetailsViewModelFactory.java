package com.example.user.emaildetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

public class EmailDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    public EmailDetailsViewModelFactory(AuthRepos mAuthService) {
        this.authRepos = mAuthService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EmailDetailsViewModel.class)) {
            return (T) new EmailDetailsViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
