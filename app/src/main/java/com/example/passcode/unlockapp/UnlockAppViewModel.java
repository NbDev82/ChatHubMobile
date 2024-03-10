package com.example.passcode.unlockapp;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.PreferenceManager;
import com.example.infrastructure.Utils;

public class UnlockAppViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<String> enteredPasscode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFingerprintUnlockEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isOpenFingerprint = new MutableLiveData<>();
    private String correctPasscode;

    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    public LiveData<String> getEnteredPasscode() {
        return enteredPasscode;
    }

    public LiveData<Boolean> getIsFingerprintUnlockEnabled() {
        return isFingerprintUnlockEnabled;
    }

    private void setIsFingerprintUnlockEnabled(boolean isFingerprintUnlockEnabled) {
        this.isFingerprintUnlockEnabled.postValue(isFingerprintUnlockEnabled);
    }

    public LiveData<Boolean> getIsOpenFingerprint() {
        return isOpenFingerprint;
    }

    public UnlockAppViewModel() {
    }

    public void loadPreferences(PreferenceManager preferenceManager) {
        String passcode = preferenceManager.getString(Utils.KEY_PASSCODE);
        Boolean isFingerprintUnlockEnabled = preferenceManager
                .getBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED);

        setCorrectPasscode(passcode);
        setIsFingerprintUnlockEnabled(isFingerprintUnlockEnabled);
    }

    private void setCorrectPasscode(String correctPasscode) {
        this.correctPasscode = correctPasscode;
    }

    public void addDigit(ENumber number) {
        String curEnteredPasscode = this.enteredPasscode.getValue();
        if (curEnteredPasscode == null) {
            curEnteredPasscode = "";
        }

        if (curEnteredPasscode.length() >= Utils.PASSCODE_DIGIT_COUNT) {
            return;
        }

        curEnteredPasscode += number.getValue();
        this.enteredPasscode.postValue(curEnteredPasscode);

        if (curEnteredPasscode.length() == Utils.PASSCODE_DIGIT_COUNT) {
            verifyPasscode(curEnteredPasscode);
        }
    }

    private void verifyPasscode(String curEnteredPasscode) {
        if (!Utils.isValidPasscode(correctPasscode, curEnteredPasscode)) {
            errorToastMessage.postValue("Entered passcode is wrong");
            return;
        }

        successToastMessage.postValue("Unlock successfully");
        new Handler().postDelayed(() -> {
            navigateToHome.postValue(true);
        }, 50);
    }

    public void openFingerprint() {
        this.isOpenFingerprint.postValue(true);
    }

    public void deleteLastDigit() {
        String curEnteredPasscode = this.enteredPasscode.getValue();
        if (Utils.isEmpty(curEnteredPasscode)) {
            return;
        }

        curEnteredPasscode = curEnteredPasscode.substring(0, curEnteredPasscode.length() - 1);
        this.enteredPasscode.postValue(curEnteredPasscode);
    }

    public BiometricPrompt.AuthenticationCallback getBiometricAuthenticationCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                enteredPasscode.postValue(correctPasscode);
                successToastMessage.postValue("Unlock successfully");
                new Handler().postDelayed(() -> {
                    navigateToHome.postValue(true);
                }, 50);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        };
    }
}
