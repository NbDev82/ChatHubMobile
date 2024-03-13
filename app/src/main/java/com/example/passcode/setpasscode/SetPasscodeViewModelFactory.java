package com.example.passcode.setpasscode;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.infrastructure.PreferenceManagerRepos;

public class SetPasscodeViewModelFactory implements ViewModelProvider.Factory {

    private final PreferenceManagerRepos preferenceManagerRepos;

    public SetPasscodeViewModelFactory(PreferenceManagerRepos preferenceManagerRepos) {
        this.preferenceManagerRepos = preferenceManagerRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SetPasscodeViewModel.class)) {
            return (T) new SetPasscodeViewModel(preferenceManagerRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
