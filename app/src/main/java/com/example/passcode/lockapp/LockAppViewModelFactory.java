package com.example.passcode.lockapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.infrastructure.PreferenceManagerRepos;

public class LockAppViewModelFactory implements ViewModelProvider.Factory {

    private final PreferenceManagerRepos preferenceManagerRepos;

    public LockAppViewModelFactory(PreferenceManagerRepos preferenceManagerRepos) {
        this.preferenceManagerRepos = preferenceManagerRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LockAppViewModel.class)) {
            return (T) new LockAppViewModel(preferenceManagerRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
