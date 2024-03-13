package com.example.passcode.changepasscode;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.infrastructure.PreferenceManagerRepos;

public class ChangePasscodeViewModelFactory implements ViewModelProvider.Factory {

    private final PreferenceManagerRepos preferenceManagerRepos;

    public ChangePasscodeViewModelFactory(PreferenceManagerRepos preferenceManagerRepos) {
        this.preferenceManagerRepos = preferenceManagerRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChangePasscodeViewModel.class)) {
            return (T) new ChangePasscodeViewModel(preferenceManagerRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
