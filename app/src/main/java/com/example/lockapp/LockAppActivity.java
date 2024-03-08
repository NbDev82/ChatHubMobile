package com.example.lockapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityLockAppBinding;
import com.example.infrastructure.PreferenceManager;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;

public class LockAppActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private LockAppViewModel viewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        viewModel = new ViewModelProvider(this).get(LockAppViewModel.class);

        ActivityLockAppBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_lock_app);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        preferenceManager = new PreferenceManager(getApplicationContext());

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });

        viewModel.getIsPasscodeSet().observe(this, isPasscodeSet -> {
            if (isPasscodeSet != null) {
                preferenceManager.putBoolean(Utils.KEY_PASSCODE_ENABLED, isPasscodeSet);
            }
        });

        viewModel.getIsFingerprintUnlockEnabled().observe(this, isFingerprintUnlockEnabled -> {
            if (isFingerprintUnlockEnabled != null) {
                preferenceManager.putBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED, isFingerprintUnlockEnabled);
            }
        });

        viewModel.getSelectedAutoLockTime().observe(this, selectedAutoLockTime -> {
            if (selectedAutoLockTime != null) {
                preferenceManager.putString(Utils.KEY_AUTO_LOCK_TIME, selectedAutoLockTime.toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Boolean isPasscodeSet = preferenceManager.getBoolean(Utils.KEY_PASSCODE_ENABLED);
        Boolean isFingerprintUnlockEnabled = preferenceManager
                .getBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED);
        String selectedAutoLockTime = preferenceManager.getString(Utils.KEY_AUTO_LOCK_TIME);

        viewModel.setIsPasscodeSet(isPasscodeSet);
        viewModel.setIsFingerprintUnlockEnabled(isFingerprintUnlockEnabled);
        viewModel.setSelectedAutoLockTime(selectedAutoLockTime);
    }
}