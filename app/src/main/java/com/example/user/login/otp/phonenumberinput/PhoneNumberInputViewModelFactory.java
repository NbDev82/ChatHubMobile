package com.example.user.login.otp.phonenumberinput;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

public class PhoneNumberInputViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepos userRepos;
    private final AuthRepos authRepos;

    public PhoneNumberInputViewModelFactory(UserRepos userRepos, AuthRepos authRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PhoneNumberInputViewModel.class)) {
            return (T) new PhoneNumberInputViewModel(userRepos, authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
