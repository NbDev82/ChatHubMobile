package com.example.user.accountlink;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

public class AccountLinkingViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    public AccountLinkingViewModelFactory(AuthRepos mAuthService) {
        this.authRepos = mAuthService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AccountLinkingViewModel.class)) {
            return (T) new AccountLinkingViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
