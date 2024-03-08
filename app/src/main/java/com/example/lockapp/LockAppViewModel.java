package com.example.lockapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;

public class LockAppViewModel extends BaseViewModel {

    private static final String TAG = LockAppViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPasscodeSet = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFingerprintUnlockEnabled = new MutableLiveData<>();
    private final MutableLiveData<EAutoLockTime> selectedAutoLockTime = new MutableLiveData<>();

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public MutableLiveData<Boolean> getIsPasscodeSet() {
        return isPasscodeSet;
    }

    public void setIsPasscodeSet(boolean isPasscodeSet) {
        this.isPasscodeSet.postValue(isPasscodeSet);
    }

    public MutableLiveData<Boolean> getIsFingerprintUnlockEnabled() {
        return isFingerprintUnlockEnabled;
    }

    public void setIsFingerprintUnlockEnabled(boolean isFingerprintUnlockEnabled) {
        this.isFingerprintUnlockEnabled.postValue(isFingerprintUnlockEnabled);
    }

    public LiveData<EAutoLockTime> getSelectedAutoLockTime() {
        return selectedAutoLockTime;
    }

    public void setSelectedAutoLockTime(String selectedAutoLockTime) {
        if (selectedAutoLockTime == null) {
            EAutoLockTime defaultSelected = EAutoLockTime.NONE;
            this.selectedAutoLockTime.postValue(defaultSelected);
            return;
        }

        try {
            EAutoLockTime selected = EAutoLockTime.valueOf(selectedAutoLockTime);
            this.selectedAutoLockTime.postValue(selected);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
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
