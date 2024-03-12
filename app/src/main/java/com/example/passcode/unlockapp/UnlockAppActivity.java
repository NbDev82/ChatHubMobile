package com.example.passcode.unlockapp;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.CustomToast;
import com.example.databinding.ActivityUnlockAppBinding;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;

import java.util.concurrent.Executor;

public class UnlockAppActivity extends AppCompatActivity {

    private static final String TAG = UnlockAppActivity.class.getSimpleName();

    private NavigationManager navigationManager;
    private UnlockAppViewModel viewModel;
    private PreferenceManagerRepos preferenceManager;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        viewModel = new ViewModelProvider(this).get(UnlockAppViewModel.class);

        ActivityUnlockAppBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_unlock_app);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        preferenceManager = new PreferenceManagerRepos(getApplicationContext());

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
        biometricPrompt = new BiometricPrompt(UnlockAppActivity.this,
                executor, viewModel.getBiometricAuthenticationCallback());

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
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

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.loadPreferences(preferenceManager);
    }
}