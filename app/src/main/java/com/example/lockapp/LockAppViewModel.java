package com.example.lockapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.inputdialogfragment.EInputType;
import com.example.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.PreferenceManager;
import com.example.infrastructure.Utils;

public class LockAppViewModel extends BaseViewModel {

    private static final String TAG = LockAppViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<String> passcode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFingerprintUnlockEnabled = new MutableLiveData<>();
    private final MutableLiveData<EAutoLockTime> selectedAutoLockTime = new MutableLiveData<>();
    private final MutableLiveData<InputDialogModel> openInputDialog = new MutableLiveData<>();

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<String> getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode.postValue(passcode);
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

    public LiveData<InputDialogModel> getOpenInputDialog() {
        return openInputDialog;
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

    public void loadPreferences(PreferenceManager preferenceManager) {
        String passcode = preferenceManager.getString(Utils.KEY_PASSCODE);
        Boolean isFingerprintUnlockEnabled = preferenceManager
                .getBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED);
        String selectedAutoLockTime = preferenceManager.getString(Utils.KEY_AUTO_LOCK_TIME);

        setPasscode(passcode);
        setIsFingerprintUnlockEnabled(isFingerprintUnlockEnabled);
        setSelectedAutoLockTime(selectedAutoLockTime);
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    public void toggleSetPasscode() {
        String passcode = this.passcode.getValue();
        if (Utils.isEmpty(passcode)) {
            openPasscodeDialog();
            return;
        }

        this.passcode.postValue("");
    }

    private void openPasscodeDialog() {
        InputDialogModel model = new InputDialogModel.Builder()
                .setTitle("New passcode")
                .setType(EInputType.NORMAL)
                .setCurrentContent("")
                .setSubmitButtonClickListener(passcode -> {
                    this.passcode.postValue(passcode);
                })
                .build();
        openInputDialog.postValue(model);
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
