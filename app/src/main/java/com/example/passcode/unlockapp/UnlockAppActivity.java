package com.example.passcode.unlockapp;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.LayoutRes;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.CustomToast;
import com.example.databinding.ActivityUnlockAppBinding;
import com.example.infrastructure.BaseActivity;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.navigation.EAnimationType;

import java.util.concurrent.Executor;

public class UnlockAppActivity extends BaseActivity<UnlockAppViewModel, ActivityUnlockAppBinding> {

    private static final String TAG = UnlockAppActivity.class.getSimpleName();

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_unlock_app;
    }

    @Override
    protected Class<UnlockAppViewModel> getViewModelClass() {
        return UnlockAppViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        PreferenceManagerRepos preferenceManagerRepos = new PreferenceManagerRepos(this);
        return new UnlockAppViewModelFactory(preferenceManagerRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
        openUnlockWithFingerprint();
    }

    private void setupObservers() {
        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome(EAnimationType.FADE_OUT);
            }
        });

        viewModel.getIsOpenFingerprint().observe(this, open -> {
            if (open) {
                openUnlockWithFingerprint();
            }
        });
    }

    private void openUnlockWithFingerprint() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                CustomToast.showErrorToast(this, "Device doesn't have fingerprint");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                CustomToast.showErrorToast(this, "Not working");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                CustomToast.showErrorToast(this, "No fingerprint assigned");
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(UnlockAppActivity.this,
                executor, viewModel.getBiometricAuthenticationCallback());

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Chat Hub")
                .setDescription("Use your fingerprint to authenticate")
                .setAllowedAuthenticators(BIOMETRIC_STRONG)
                .setNegativeButtonText("Cancel")
                .build();

        try {
            biometricPrompt.authenticate(promptInfo);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}