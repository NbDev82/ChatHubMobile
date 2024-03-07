package com.example.lockapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;

public class LockAppViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPasscodeSet = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFingerprintUnlockEnabled = new MutableLiveData<>();
    private final MutableLiveData<String> selectedAutoLockTime = new MutableLiveData<>();

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Boolean> getIsPasscodeSet() {
        return isPasscodeSet;
    }

    public LiveData<Boolean> getIsFingerprintUnlockEnabled() {
        return isFingerprintUnlockEnabled;
    }

    public LiveData<String> getSelectedAutoLockTime() {
        return selectedAutoLockTime;
    }

    public LockAppViewModel() {
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    public void toggleSetPassword() {
        boolean isPasscodeSet = this.isPasscodeSet.getValue();
        this.isPasscodeSet.postValue(!isPasscodeSet);
    }

    public void toggleUnlockWithFingerprint() {
        boolean isFingerprintUnlockEnabled = this.isFingerprintUnlockEnabled.getValue();
        this.isFingerprintUnlockEnabled.postValue(!isFingerprintUnlockEnabled);
    }

    public void openChangePasscodeDialog() {
    }

    public void pickAutoLockTime() {
    }
}
