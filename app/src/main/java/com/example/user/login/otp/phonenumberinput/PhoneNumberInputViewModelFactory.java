package com.example.user.login.otp.phonenumberinput;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;

public class PhoneNumberInputViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;

    public PhoneNumberInputViewModelFactory(AuthService authService) {
        this.authService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PhoneNumberInputViewModel.class)) {
            return (T) new PhoneNumberInputViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
