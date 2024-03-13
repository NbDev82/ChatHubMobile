package com.example.passcode.setpasscode;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivitySetPasscodeBinding;
import com.example.infrastructure.BaseActivity;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.navigation.EAnimationType;

public class SetPasscodeActivity extends BaseActivity<SetPasscodeViewModel, ActivitySetPasscodeBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_set_passcode;
    }

    @Override
    protected Class<SetPasscodeViewModel> getViewModelClass() {
        return SetPasscodeViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        PreferenceManagerRepos preferenceManagerRepos = new PreferenceManagerRepos(getApplicationContext());
        return new SetPasscodeViewModelFactory(preferenceManagerRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, data -> {
            navigationManager.navigateBack(data, EAnimationType.FADE_OUT);
        });

        viewModel.getSetPasscodeState().observe(this, passcodeSetState -> {
            if (passcodeSetState == EPasscodeSetState.COMPLETED) {
                viewModel.saveNewPasscode();
            }
        });
    }

}