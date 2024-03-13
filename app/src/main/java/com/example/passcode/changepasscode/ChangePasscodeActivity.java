package com.example.passcode.changepasscode;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityChangePasscodeBinding;
import com.example.infrastructure.BaseActivity;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.navigation.EAnimationType;

public class ChangePasscodeActivity extends BaseActivity<ChangePasscodeViewModel, ActivityChangePasscodeBinding> {

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_change_passcode;
    }

    @Override
    protected Class<ChangePasscodeViewModel> getViewModelClass() {
        return ChangePasscodeViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        PreferenceManagerRepos preferenceManagerRepos = new PreferenceManagerRepos(getApplicationContext());
        return new ChangePasscodeViewModelFactory(preferenceManagerRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });

        viewModel.getNavigateToSetPasscode().observe(this, navigate -> {
            if (navigate) {
                int flags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP;
                navigationManager.navigateToSetPasscode(EAnimationType.FADE_IN, flags);
                finish();
            }
        });
    }
}