package com.example.passcode.changepasscode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;

public class ChangePasscodeViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSetPasscode = new MutableLiveData<>();
    private final MutableLiveData<String> enteredOldPasscode = new MutableLiveData<>();
    private final PreferenceManagerRepos preferenceManagerRepos;
    private String correctPasscode;

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Boolean> getNavigateToSetPasscode() {
        return navigateToSetPasscode;
    }

    public MutableLiveData<String> getEnteredOldPasscode() {
        return enteredOldPasscode;
    }

    public ChangePasscodeViewModel(PreferenceManagerRepos preferenceManagerRepos) {
        this.preferenceManagerRepos = preferenceManagerRepos;
        correctPasscode = preferenceManagerRepos.getString(Utils.KEY_PASSCODE);
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    public void verifyOldPasscode(CharSequence text) {
        String enteredOldPasscode = text.toString();
        if (!Utils.isValidPasscode(enteredOldPasscode)) {
            return;
        }

        if (!Utils.isCorrectPasscode(correctPasscode, enteredOldPasscode)) {
            errorToastMessage.postValue("Your passcode is incorrect");
            this.enteredOldPasscode.postValue("");
            return;
        }

        this.navigateToSetPasscode.postValue(true);
    }
}
