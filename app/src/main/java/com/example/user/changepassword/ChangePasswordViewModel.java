package com.example.user.changepassword;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.UserNotAuthenticatedException;
import com.example.user.Validator;
import com.example.user.repository.AuthRepos;
import com.google.firebase.auth.EmailAuthProvider;

import java.util.Objects;

public class ChangePasswordViewModel extends BaseViewModel {

    private static final String TAG = ChangePasswordViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateToAccountLinking = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPasswordSet = new MutableLiveData<>();
    private final MutableLiveData<String> oldPassword = new MutableLiveData<>();
    private final MutableLiveData<String> oldPasswordError = new MutableLiveData<>();
    private final MutableLiveData<String> newPassword = new MutableLiveData<>();
    private final MutableLiveData<String> newPasswordError = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPasswordError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> canPerformUpdate = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private final AuthRepos authRepos;

    public MutableLiveData<Boolean> getNavigateToAccountLinking() {
        return navigateToAccountLinking;
    }

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
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

    public ChangePasswordViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;

        checkPasswordSetStatus();
    }

    private void checkPasswordSetStatus() {
        authRepos.fetchSignInMethods()
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
        navigateBack.postValue(true);
    }

    public void validateOldPassword(CharSequence s) {
        String oldPassword = s.toString();

        String error = Validator.validatePassword(oldPassword);
        if (error != null) {
            oldPasswordError.postValue(error);
            return;
        }

        oldPasswordError.postValue(null);

        validFields();
    }

    public void validateNewPassword(CharSequence s) {
        String newPassword = s.toString();
        String error = Validator.validatePassword(newPassword);

        if (error != null) {
            newPasswordError.postValue(error);
            return;
        }
        newPasswordError.postValue(null);
        validFields();
    }

    public void validateConfirmPassword(CharSequence s) {
        String newPasswordValue = this.newPassword.getValue();

        String newPassword = newPasswordValue != null ? newPasswordValue.trim() : "";
        String confirmPassword = s.toString();

        if (!confirmPassword.equals(newPassword)) {
            confirmPasswordError.postValue("Confirm password doesn't match");
            return;
        }
        confirmPasswordError.postValue(null);
        validFields();
    }

    public void updatePassword() {
        isUpdating.postValue(true);
        String email = "";
        String currentPassword = this.oldPassword.getValue();
        String newPassword = this.newPassword.getValue();
        UpdatePasswordRequest request = new UpdatePasswordRequest(email, currentPassword, newPassword);
        authRepos.updatePassword(request)
                .addOnSuccessListener(aVoid -> {
                    successToastMessage.postValue("Update successfully");
                    isUpdating.postValue(false);
                })
                .addOnFailureListener(e -> {
                    if (e instanceof UserNotAuthenticatedException) {
                        oldPasswordError.postValue(e.getMessage());
                    } else {
                        errorToastMessage.postValue("Update unsuccessfully");
                    }
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
