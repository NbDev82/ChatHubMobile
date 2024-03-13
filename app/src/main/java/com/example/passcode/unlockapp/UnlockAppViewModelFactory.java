package com.example.passcode.unlockapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.infrastructure.PreferenceManagerRepos;

public class UnlockAppViewModelFactory implements ViewModelProvider.Factory {

    private final PreferenceManagerRepos preferenceManagerRepos;

    public UnlockAppViewModelFactory(PreferenceManagerRepos preferenceManagerRepos) {
        this.preferenceManagerRepos = preferenceManagerRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UnlockAppViewModel.class)) {
            return (T) new UnlockAppViewModel(preferenceManagerRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
