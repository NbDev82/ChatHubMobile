package com.example.passcode.changepasscode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityChangePasscodeBinding;
import com.example.databinding.ActivityChangePasswordBinding;
import com.example.databinding.ActivityLockAppBinding;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.passcode.lockapp.LockAppViewModel;
import com.example.passcode.lockapp.LockAppViewModelFactory;
import com.example.passcode.setpasscode.SetPasscodeViewModel;

public class ChangePasscodeActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private ChangePasscodeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        PreferenceManagerRepos preferenceManagerRepos = new PreferenceManagerRepos(getApplicationContext());
        ChangePasscodeViewModelFactory factory = new ChangePasscodeViewModelFactory(preferenceManagerRepos);
        viewModel = new ViewModelProvider(this, factory).get(ChangePasscodeViewModel.class);

        ActivityChangePasscodeBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_change_passcode);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);


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