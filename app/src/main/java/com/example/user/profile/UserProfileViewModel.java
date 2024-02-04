package com.example.user.profile;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.EGender;
import com.example.user.User;

import java.util.Calendar;
import java.util.Date;

public class UserProfileViewModel extends ViewModel {

    private static final String TAG = UserProfileViewModel.class.getSimpleName();

    private final AuthService mAuthService;
    private final MutableLiveData<String> mFullName = new MutableLiveData<>();
    private final MutableLiveData<EGender> mGender = new MutableLiveData<>();
    private final MutableLiveData<String> mBirthdayStr = new MutableLiveData<>();
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsUserInitializing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsDataChanged = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsUserUpdating = new MutableLiveData<>();
    private final MutableLiveData<InputDialogModel> mOpenInputDialog = new MutableLiveData<>();
    private final MutableLiveData<Calendar> mOpenDatePickerDialog = new MutableLiveData<>();
    private final MutableLiveData<AlertDialogModel> mOpenCustomAlertDialog = new MutableLiveData<>();
    private final MutableLiveData<Integer> mOpenSingleChoiceGender = new MutableLiveData<>();
    private User mOriginalUser;
    private Handler mHandler = new Handler();

    public LiveData<String> getFullName() {
        return mFullName;
    }

    public LiveData<EGender> getGender() {
        return mGender;
    }

    public void setGender(EGender gender) {
        mGender.postValue(gender);
        mOriginalUser.setGender(gender);
    }

    public LiveData<String> getBirthdayStr() {
        return mBirthdayStr;
    }

    public void setBirthday(Date birthday) {
        String birthdayStr = Utils.dateToString(birthday);
        mBirthdayStr.postValue(birthdayStr);
        mOriginalUser.setBirthday(birthday);
    }

    public MutableLiveData<String> getToastMessage() {
        return mToastMessage;
    }

    public LiveData<Boolean> getNavigateToHome() {
        return mNavigateToHome;
    }

    public MutableLiveData<Boolean> getIsUserInitializing() {
        return mIsUserInitializing;
    }

    public MutableLiveData<Boolean> getIsDataChanged() {
        return mIsDataChanged;
    }

    public MutableLiveData<Boolean> getIsUserUpdating() {
        return mIsUserUpdating;
    }

    public LiveData<InputDialogModel> getOpenCustomInputDialog() {
        return mOpenInputDialog;
    }

    public MutableLiveData<Calendar> getOpenDatePickerDialog() {
        return mOpenDatePickerDialog;
    }

    public LiveData<AlertDialogModel> getOpenCustomAlertDialog() {
        return mOpenCustomAlertDialog;
    }

    public LiveData<Integer> getOpenSingleChoiceGender() {
        return mOpenSingleChoiceGender;
    }

    public UserProfileViewModel(AuthService authService) {
        mAuthService = authService;

        mIsDataChanged.setValue(false);
        mIsUserInitializing.postValue(true);
        authService.getCurrentUser()
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        setUser(user);

                        mHandler.postDelayed(() -> mIsUserInitializing.setValue(false), 1000);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                    mIsUserInitializing.postValue(true);
                });
    }

    private void setUser(User user) {
        mOriginalUser = user;
        mFullName.setValue(user.getFullName());
        mGender.setValue(user.getGender());
        Date birthday = user.getBirthday();
        mBirthdayStr.setValue(Utils.dateToString(birthday));
    }

    public void navigateToHome() {
        mNavigateToHome.postValue(true);
    }

    public void openCustomInputDialog() {
        InputDialogModel model = new InputDialogModel.Builder()
                .setTitle("Full name")
                .setCurrentContent( mFullName.getValue() )
                .setSubmitButtonClickListener(newName -> {
                    if (!newName.isEmpty()) {
                        mFullName.postValue(newName);
                    }
                })
                .build();
        mOpenInputDialog.postValue(model);
    }

    public void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( mOriginalUser.getBirthday() );
        mOpenDatePickerDialog.postValue(calendar);
    }

    private void checkChangeStatus() {
        if (!mOriginalUser.getFullName().equals( mFullName.getValue() ) ||
                !mOriginalUser.getGender().equals( mGender.getValue() ) ||
                !Utils.compareDateWithDateStr(mOriginalUser.getBirthday(), mBirthdayStr.getValue())
        ) {
            mIsDataChanged.postValue(true);
        } else {
            mIsDataChanged.postValue(false);
        }
    }

    public void openUpdateDialog() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Update!")
                .setMessage("When you click \"OK\", your information will be updated.")
                .setPositiveButton("Yes", aVoid -> {
                    mToastMessage.postValue("Yes");
                    updateUser();
                })
                .setNegativeButton("No", aVoid -> {
                    mToastMessage.postValue("No");
                    refreshAllFields();
                })
                .build();
        mOpenCustomAlertDialog.postValue(model);
    }

    public void updateUser() {
        mOriginalUser.setFullName( mFullName.getValue() );
        mOriginalUser.setGender( mGender.getValue() );
        Date birthday = Utils.stringToDate( mBirthdayStr.getValue() );
        mOriginalUser.setBirthday(birthday);

        mAuthService.updateBasicUserByEmail(mOriginalUser);
    }

    private void refreshAllFields() {
    }

    public void openSingleChoiceGender() {
        EGender curGenderSelected = mGender.getValue();
        int curIndexSelected = EGender.getCurrentIndex(curGenderSelected);
        mOpenSingleChoiceGender.postValue(curIndexSelected);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mHandler.removeCallbacksAndMessages(null);
    }
}
