package com.example.user.login.google;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.user.AuthService;
import com.example.user.login.LoginViewModelFactory;

public class GoogleSignInViewModelFactory extends LoginViewModelFactory {

    public GoogleSignInViewModelFactory(AuthService authService) {
        super(authService);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GoogleSignInViewModel.class)) {
            return (T) new GoogleSignInViewModel(mAuthService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
