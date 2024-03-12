package com.example.passcode.lockapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;

public class LockAppViewModel extends BaseViewModel {

    private static final String TAG = LockAppViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSetPasscode = new MutableLiveData<>();
    private final MutableLiveData<String> passcode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFingerprintUnlockEnabled = new MutableLiveData<>();
    private final MutableLiveData<EAutoLockTime> selectedAutoLockTime = new MutableLiveData<>();
    private final MutableLiveData<InputDialogModel> openInputDialog = new MutableLiveData<>();
    private final MutableLiveData<Integer> openSingleChoiceGender = new MutableLiveData<>();
    private final PreferenceManagerRepos preferenceManagerRepos;
    private int selectedAutoLockTimeIndex = 0;

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Boolean> getNavigateToSetPasscode() {
        return navigateToSetPasscode;
    }

    public LiveData<String> getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        preferenceManagerRepos.putString(Utils.KEY_PASSCODE, passcode);
        this.passcode.setValue(passcode);
    }

    public MutableLiveData<Boolean> getIsFingerprintUnlockEnabled() {
        return isFingerprintUnlockEnabled;
    }

    public void setIsFingerprintUnlockEnabled(boolean isFingerprintUnlockEnabled) {
        this.isFingerprintUnlockEnabled.setValue(isFingerprintUnlockEnabled);
    }

    public LiveData<EAutoLockTime> getSelectedAutoLockTime() {
        return selectedAutoLockTime;
    }

    public LiveData<InputDialogModel> getOpenInputDialog() {
        return openInputDialog;
    }

    public void setSelectedAutoLockTime(String selectedAutoLockTimeStr) {
        if (selectedAutoLockTimeStr == null) {
            EAutoLockTime defaultSelected = EAutoLockTime.NONE;
            this.selectedAutoLockTime.setValue(defaultSelected);
            return;
        }

        try {
            EAutoLockTime selected = EAutoLockTime.valueOf(selectedAutoLockTimeStr);
            this.selectedAutoLockTime.setValue(selected);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void setSelectedAutoLockTime(EAutoLockTime selectedAutoLockTime) {
        this.selectedAutoLockTime.setValue(selectedAutoLockTime);
    }

    public LockAppViewModel(PreferenceManagerRepos preferenceManager) {
        this.preferenceManagerRepos = preferenceManager;
    }

    public void loadPreferences() {
        String passcode = preferenceManagerRepos.getString(Utils.KEY_PASSCODE);
        Boolean isFingerprintUnlockEnabled = preferenceManagerRepos
                .getBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED);
        String selectedAutoLockTime = preferenceManagerRepos.getString(Utils.KEY_AUTO_LOCK_TIME);

        setPasscode(passcode);
        setIsFingerprintUnlockEnabled(isFingerprintUnlockEnabled);
        setSelectedAutoLockTime(selectedAutoLockTime);
    }

    public void navigateBack() {
        this.navigateBack.setValue(true);
    }

    public void navigateToSetPasscode() {
        this.navigateToSetPasscode.setValue(true);
    }

    public void toggleSetPasscode() {
        String passcode = preferenceManagerRepos.getString(Utils.KEY_PASSCODE);
        if (Utils.isEmpty(passcode)) {
            navigateToSetPasscode();
            return;
        }

        preferenceManagerRepos.putString(Utils.KEY_PASSCODE, null);
        this.passcode.setValue(null);
    }

    public void toggleUnlockWithFingerprint() {
        boolean isFingerprintUnlockEnabled = this.isFingerprintUnlockEnabled.getValue();
        preferenceManagerRepos.putBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED, isFingerprintUnlockEnabled);
        this.isFingerprintUnlockEnabled.setValue(!isFingerprintUnlockEnabled);
    }

    public void openChangePasscodeDialog() {
    }

    public void openSingleChoiceAutoLockTime() {
        preferenceManagerRepos.putString(Utils.KEY_AUTO_LOCK_TIME, this.selectedAutoLockTime.toString());

        EAutoLockTime selectedAutoLockTime = this.selectedAutoLockTime.getValue();
        int curIndexSelected = EAutoLockTime.getCurrentIndex(selectedAutoLockTime);
        openSingleChoiceGender.setValue(curIndexSelected);
    }

    public int getSelectedAutoLockTimeIndex() {
        return selectedAutoLockTimeIndex;
    }

    public void setSelectedAutoLockTimeIndex(int selectedAutoLockTimeIndex) {
        this.selectedAutoLockTimeIndex = selectedAutoLockTimeIndex;
    }
}
