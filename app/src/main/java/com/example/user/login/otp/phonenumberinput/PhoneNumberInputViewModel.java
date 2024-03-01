package com.example.user.login.otp.phonenumberinput;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.EUserField;
import com.example.user.Validator;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

public class PhoneNumberInputViewModel extends BaseViewModel {

    private static final String TAG = PhoneNumberInputViewModel.class.getSimpleName();

    private final MutableLiveData<String> countryCode = new MutableLiveData<>("+84");
    private final MutableLiveData<String> localNumber = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumberError = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isPhoneVerifying = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToVerifyOtpWithPhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSignUp = new MutableLiveData<>();
    private final UserRepos userRepos;

    public MutableLiveData<String> getCountryCode() {
        return countryCode;
    }

    public MutableLiveData<String> getLocalNumber() {
        return localNumber;
    }

    public LiveData<String> getPhoneNumberError() {
        return phoneNumberError;
    }

    public LiveData<Boolean> getIsPhoneVerifying() {
        return isPhoneVerifying;
    }

    public LiveData<Bundle> getNavigateToVerifyOtpWithPhoneNumber() {
        return navigateToVerifyOtpWithPhoneNumber;
    }

    public LiveData<Boolean> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    public PhoneNumberInputViewModel(UserRepos userRepos, AuthRepos authRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
    }

    public void checkPhoneNumber(CharSequence s) {
        String countryCode = this.countryCode.getValue();
        String localNumber = s.toString();
        String phoneNumber = Utils.getFullPhoneNumber(countryCode, localNumber);
        String error = Validator.validPhoneNumber(phoneNumber);
        this.phoneNumberError.postValue(error);
    }

    public void verifyPhoneNumberAndNavigate() {
        this.isPhoneVerifying.postValue(true);
        String phoneNumber = getFullPhoneNumber();
        userRepos.existsByPhoneNumber(phoneNumber)
                .addOnSuccessListener(isExists -> {
                    this.isPhoneVerifying.postValue(false);
                    if (isExists) {
                        navigateToVerifyOtpWithPhoneNumber();
                    } else {
                        phoneNumberError.postValue("Your phone number is not sign up. Let's sign up");
                    }
                })
                .addOnFailureListener(e -> {
                    this.isPhoneVerifying.postValue(false);
                    errorToastMessage.postValue("Verify phone number unsuccessfully");
                    Log.e(TAG, "Error: ", e);
                });
    }

    private String getFullPhoneNumber() {
        String countryCode = this.countryCode.getValue();
        String number = localNumber.getValue();
        return Utils.getFullPhoneNumber(countryCode, number);
    }

    private void navigateToVerifyOtpWithPhoneNumber() {
        String phoneNumber = getFullPhoneNumber();
        Bundle data = new Bundle();
        data.putString(EUserField.PHONE_NUMBER.getName(), phoneNumber);
        navigateToVerifyOtpWithPhoneNumber.postValue(data);
    }

    public void navigateToSignUp() {
        navigateToSignUp.postValue(true);
    }
}
