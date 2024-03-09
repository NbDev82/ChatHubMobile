package com.example.lockapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.inputdialogfragment.InputDialogFragment;
import com.example.customcontrol.inputdialogfragment.InputDialogModel;
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

        viewModel.getPasscode().observe(this, passcode -> {
            preferenceManager.putString(Utils.KEY_PASSCODE, passcode);
        });

        viewModel.getIsFingerprintUnlockEnabled().observe(this, isFingerprintUnlockEnabled -> {
            preferenceManager.putBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED, isFingerprintUnlockEnabled);
        });

        viewModel.getSelectedAutoLockTime().observe(this, selectedAutoLockTime -> {
            preferenceManager.putString(Utils.KEY_AUTO_LOCK_TIME, selectedAutoLockTime.toString());
        });

        viewModel.getOpenInputDialog().observe(this, this::openCustomInputDialog);
    }

    private void openCustomInputDialog(InputDialogModel inputDialogModel) {
        InputDialogFragment dialog = new InputDialogFragment(inputDialogModel);
        dialog.show(getSupportFragmentManager(), InputDialogFragment.TAG);
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.loadPreferences(preferenceManager);
    }
}