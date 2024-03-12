package com.example.passcode.setpasscode;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;

public class SetPasscodeViewModel extends BaseViewModel {

    private final MutableLiveData<Bundle> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<EPasscodeSetState> setPasscodeState = new MutableLiveData<>();
    private final MutableLiveData<String> newPasscode = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPasscode = new MutableLiveData<>();

    public LiveData<Bundle> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<EPasscodeSetState> getSetPasscodeState() {
        return setPasscodeState;
    }

    public MutableLiveData<String> getNewPasscode() {
        return newPasscode;
    }

    public MutableLiveData<String> getConfirmPasscode() {
        return confirmPasscode;
    }

    public SetPasscodeViewModel() {
        this.setPasscodeState.postValue(EPasscodeSetState.NEW_PASSCODE);
    }

    public void verifyNewPasscode(CharSequence text) {
        String newPasscode = text.toString();
        if (!Utils.isValidPasscode(newPasscode)) {
            return;
        }

        this.setPasscodeState.postValue(EPasscodeSetState.CONFIRM_PASSCODE);
    }

    public void confirmPasscode(CharSequence text) {
        String confirmPasscode = text.toString();
        if (!Utils.isValidPasscode(confirmPasscode)) {
            return;
        }

        String newPasscode = this.newPasscode.getValue();
        if (!confirmPasscode.equals(newPasscode)) {
            this.errorToastMessage.postValue("Your confirm passcode is incorrect");
            this.confirmPasscode.postValue("");
            return;
        }

        this.setPasscodeState.postValue(EPasscodeSetState.COMPLETED);
        Bundle data = new Bundle();
        data.putBoolean(Utils.EXTRA_PASSCODE_SET_SUCCESS, true);
        this.navigateBack.postValue(data);
    }

    public void saveNewPasscode(PreferenceManagerRepos preferenceManager) {
        String passcode = this.getNewPasscode().getValue();
        if (passcode == null) {
            return;
        }
        preferenceManager.putString(Utils.KEY_PASSCODE, passcode);
    }

    public void navigateBack() {
        this.navigateBack.postValue(null);
    }
}
