package com.example.user.changepassword;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;
import com.example.user.Validator;
import com.google.firebase.auth.EmailAuthProvider;

import java.util.Objects;

public class ChangePasswordViewModel extends BaseViewModel {

    private static final String TAG = ChangePasswordViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateToAccountLinking = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSettings = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPasswordSet = new MutableLiveData<>();
    private final MutableLiveData<String> oldPassword = new MutableLiveData<>();
    private final MutableLiveData<String> oldPasswordError = new MutableLiveData<>();
    private final MutableLiveData<String> newPassword = new MutableLiveData<>();
    private final MutableLiveData<String> newPasswordError = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPasswordError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> canPerformUpdate = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private final AuthService authService;

    public MutableLiveData<Boolean> getNavigateToAccountLinking() {
        return navigateToAccountLinking;
    }

    public LiveData<Boolean> getNavigateToSettings() {
        return navigateToSettings;
    }

    public MutableLiveData<Boolean> getIsPasswordSet() {
        return isPasswordSet;
    }

    public MutableLiveData<String> getOldPassword() {
        return oldPassword;
    }

    public LiveData<String> getOldPasswordError() {
        return oldPasswordError;
    }

    public MutableLiveData<String> getNewPassword() {
        return newPassword;
    }

    public MutableLiveData<String> getNewPasswordError() {
        return newPasswordError;
    }

    public MutableLiveData<String> getConfirmPassword() {
        return confirmPassword;
    }

    public MutableLiveData<String> getConfirmPasswordError() {
        return confirmPasswordError;
    }

    public MutableLiveData<Boolean> getCanPerformUpdate() {
        return canPerformUpdate;
    }

    public LiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public ChangePasswordViewModel(AuthService authService) {
        this.authService = authService;

        checkPasswordSetStatus();
    }

    private void checkPasswordSetStatus() {
        authService.fetchSignInMethods()
                .addOnSuccessListener(providerIds -> {
                    boolean isPasswordSet = providerIds.stream()
                            .anyMatch(signInMethod -> Objects.equals(signInMethod, EmailAuthProvider.PROVIDER_ID));
                    this.isPasswordSet.postValue(isPasswordSet);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: ", e);
                });
    }

    public void navigateToAccountLinking() {
        navigateToAccountLinking.postValue(true);
    }

    public void navigateToSettings() {
        navigateToSettings.postValue(true);
    }

    public void validateOldPassword() {
        String oldPassword = this.oldPassword.getValue();

        String error = Validator.validatePassword(oldPassword);
        if (error != null) {
            oldPasswordError.postValue(error);
            return;
        }

        oldPasswordError.postValue("Incorrect old password");

        oldPasswordError.postValue(null);

        validFields();
    }

    public void validatePassword() {
        String newPassword = this.newPassword.getValue();
        String error = Validator.validatePassword(newPassword);
        if (error != null) {
            oldPasswordError.postValue(error);
            return;
        }
        oldPasswordError.postValue(null);
        validFields();
    }

    public void validateConfirmPassword() {
        String newPasswordValue = this.newPassword.getValue();
        String confirmPasswordValue = this.confirmPassword.getValue();

        String newPassword = newPasswordValue != null ? newPasswordValue.trim() : "";
        String confirmPassword = confirmPasswordValue != null ? confirmPasswordValue.trim() : "";

        if (!confirmPassword.equals(newPassword)) {
            confirmPasswordError.postValue("Confirm password doesn't match");
            return;
        }
        confirmPasswordError.postValue(null);

        validFields();
    }

    public void updatePassword() {
        successToastMessage.postValue("Update");

        isUpdating.postValue(true);
        String newPassword = this.newPassword.getValue();
        authService.updatePassword(newPassword)
                .addOnSuccessListener(aVoid -> {
                    successToastMessage.postValue("Update successfully");
                    isUpdating.postValue(false);
                })
                .addOnFailureListener(e -> {
                    successToastMessage.postValue("Update unsuccessfully");
                    isUpdating.postValue(false);
                });
    }

    private void validFields() {
        if (oldPasswordError.getValue() == null &&
                newPasswordError.getValue() == null &&
                confirmPasswordError.getValue() == null) {
            canPerformUpdate.postValue(true);
        } else {
            canPerformUpdate.postValue(false);
        }
    }
}
