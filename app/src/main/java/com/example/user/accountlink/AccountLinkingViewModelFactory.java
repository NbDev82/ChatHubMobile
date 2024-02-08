package com.example.user.accountlink;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;
import com.example.user.changepassword.ChangePasswordViewModel;

public class AccountLinkingViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;

    public AccountLinkingViewModelFactory(AuthService mAuthService) {
        this.authService = mAuthService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AccountLinkingViewModel.class)) {
            return (T) new AccountLinkingViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
