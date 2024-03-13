package com.example.passcode.lockapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;

public class LockAppViewModel extends BaseViewModel {

    private static final String TAG = LockAppViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSetPasscode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToChangePasscode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSetPasscodeChecked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFingerprintUnlockChecked = new MutableLiveData<>();
    private final MutableLiveData<EAutoLockTime> selectedAutoLockTime = new MutableLiveData<>();
    private final MutableLiveData<Integer> openSingleChoiceAutoLockTime = new MutableLiveData<>();
    private final MutableLiveData<AlertDialogModel> openCustomAlertDialog = new MutableLiveData<>();
    private final PreferenceManagerRepos preferenceManagerRepos;
    private int selectedAutoLockTimeIndex = 0;

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Boolean> getNavigateToSetPasscode() {
        return navigateToSetPasscode;
    }

    public LiveData<Boolean> getNavigateToChangePasscode() {
        return navigateToChangePasscode;
    }

    public LiveData<Boolean> getIsSetPasscodeChecked() {
        return isSetPasscodeChecked;
    }

    public void setIsSetPasscodeChecked(String curPasscode) {
        boolean isSetPasscodeChecked = Utils.isValidPasscode(curPasscode);
        this.isSetPasscodeChecked.postValue(isSetPasscodeChecked);
    }

    public LiveData<Boolean> getIsFingerprintUnlockChecked() {
        return isFingerprintUnlockChecked;
    }

    public void setIsFingerprintUnlockChecked(boolean isFingerprintUnlockChecked) {
        this.isFingerprintUnlockChecked.postValue(isFingerprintUnlockChecked);
    }

    public LiveData<EAutoLockTime> getSelectedAutoLockTime() {
        return selectedAutoLockTime;
    }

    public LiveData<Integer> getOpenSingleChoiceAutoLockTime() {
        return openSingleChoiceAutoLockTime;
    }

    public LiveData<AlertDialogModel> getOpenCustomAlertDialog() {
        return openCustomAlertDialog;
    }

    public void setSelectedAutoLockTime(String selectedAutoLockTimeStr) {
        if (selectedAutoLockTimeStr == null) {
            EAutoLockTime defaultSelected = EAutoLockTime.NONE;
            this.selectedAutoLockTime.postValue(defaultSelected);
            return;
        }

        try {
            EAutoLockTime selected = EAutoLockTime.valueOf(selectedAutoLockTimeStr);
            this.selectedAutoLockTime.postValue(selected);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void setSelectedAutoLockTime(EAutoLockTime selectedAutoLockTime) {
        preferenceManagerRepos.putString(Utils.KEY_AUTO_LOCK_TIME, selectedAutoLockTime.toString());
        this.selectedAutoLockTime.postValue(selectedAutoLockTime);
    }

    public LockAppViewModel(PreferenceManagerRepos preferenceManager) {
        this.preferenceManagerRepos = preferenceManager;
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadPreferences();
    }

    public void loadPreferences() {
        String curPasscode = preferenceManagerRepos.getString(Utils.KEY_PASSCODE);
        Boolean isFingerprintUnlockEnabled = preferenceManagerRepos
                .getBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED);
        String selectedAutoLockTime = preferenceManagerRepos.getString(Utils.KEY_AUTO_LOCK_TIME);

        setIsSetPasscodeChecked(curPasscode);
        setIsFingerprintUnlockChecked(isFingerprintUnlockEnabled);
        setSelectedAutoLockTime(selectedAutoLockTime);
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    public void navigateToSetPasscode() {
        this.navigateToSetPasscode.postValue(true);
    }

    public void toggleSetPasscode() {
        String passcode = preferenceManagerRepos.getString(Utils.KEY_PASSCODE);
        if (Utils.isEmpty(passcode)) {
            navigateToSetPasscode();
            return;
        }

        showTurnOffPasscodeDialog();
    }

    private void showTurnOffPasscodeDialog() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Turn-off passcode")
                .setMessage("Are you sure you want to turn off the passcode?\n" +
                        "This action will remove the current passcode and disable " +
                        "passcode protection for your app.")
                .setPositiveButton("Ok", aVoid -> turnOffPasscode())
                .setNegativeButton("Cancel", null)
                .build();
        openCustomAlertDialog.postValue(model);
    }

    private void turnOffPasscode() {
        preferenceManagerRepos.putString(Utils.KEY_PASSCODE, null);
        preferenceManagerRepos.putBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED, false);
        isFingerprintUnlockChecked.postValue(false);
        isSetPasscodeChecked.postValue(false);
    }

    public void toggleUnlockWithFingerprint() {
        boolean isFingerprintUnlockEnabled = preferenceManagerRepos
                .getBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED);
        preferenceManagerRepos.putBoolean(Utils.KEY_FINGERPRINT_UNLOCK_ENABLED, !isFingerprintUnlockEnabled);
        this.isFingerprintUnlockChecked.postValue(!isFingerprintUnlockEnabled);
    }

    public void navigateToChangePasscode() {
        String passcode = preferenceManagerRepos.getString(Utils.KEY_PASSCODE);
        if (Utils.isEmpty(passcode)) {
            errorToastMessage.postValue("Your passcode does not exist");
            return;
        }

        this.navigateToChangePasscode.postValue(true);
    }

    public void openSingleChoiceAutoLockTime() {
        preferenceManagerRepos.putString(Utils.KEY_AUTO_LOCK_TIME, this.selectedAutoLockTime.toString());

        EAutoLockTime selectedAutoLockTime = this.selectedAutoLockTime.getValue();
        int curIndexSelected = EAutoLockTime.getCurrentIndex(selectedAutoLockTime);
        openSingleChoiceAutoLockTime.postValue(curIndexSelected);
    }

    public int getSelectedAutoLockTimeIndex() {
        return selectedAutoLockTimeIndex;
    }

    public void setSelectedAutoLockTimeIndex(int selectedAutoLockTimeIndex) {
        this.selectedAutoLockTimeIndex = selectedAutoLockTimeIndex;
    }
}
