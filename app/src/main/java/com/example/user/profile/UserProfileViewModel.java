package com.example.user.profile;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.EGender;
import com.example.user.User;

import java.util.Calendar;
import java.util.Date;

public class UserProfileViewModel extends BaseViewModel {

    private static final String TAG = UserProfileViewModel.class.getSimpleName();

    private final AuthService mAuthService;
    private final MutableLiveData<Bitmap> mImageBitmap = new MutableLiveData<>();
    private final MutableLiveData<String> mFullName = new MutableLiveData<>();
    private final MutableLiveData<EGender> mGender = new MutableLiveData<>();
    private final MutableLiveData<String> mBirthdayStr = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsUserInitializing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsDataChanged = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsUserUpdating = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mOpenImagePicker = new MutableLiveData<>();
    private final MutableLiveData<InputDialogModel> mOpenInputDialog = new MutableLiveData<>();
    private final MutableLiveData<Calendar> mOpenDatePickerDialog = new MutableLiveData<>();
    private final MutableLiveData<AlertDialogModel> mOpenCustomAlertDialog = new MutableLiveData<>();
    private final MutableLiveData<Integer> mOpenSingleChoiceGender = new MutableLiveData<>();
    private User mOriginalUser;
    private String mEncodedImage;
    private Handler mHandler = new Handler();

    public LiveData<Bitmap> getImageBitmap() {
        return mImageBitmap;
    }

    public void setImageBitmap(Bitmap bitmap) {
        mImageBitmap.postValue(bitmap);
        mEncodedImage = Utils.encodeImage(bitmap);
        mIsDataChanged.postValue(true);
    }

    public LiveData<String> getFullName() {
        return mFullName;
    }

    public LiveData<EGender> getGender() {
        return mGender;
    }

    public void setGender(EGender gender) {
        mGender.postValue(gender);
        mIsDataChanged.postValue(true);
    }

    public LiveData<String> getBirthdayStr() {
        return mBirthdayStr;
    }

    public void setBirthday(Date birthday) {
        String birthdayStr = Utils.dateToString(birthday);
        mBirthdayStr.postValue(birthdayStr);
        mIsDataChanged.postValue(true);
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

    public LiveData<Boolean> getOpenImagePicker() {
        return mOpenImagePicker;
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

        mIsDataChanged.postValue(false);
        mIsUserInitializing.postValue(true);
        authService.getCurrentUser()
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        setUser(user);

                        mHandler.postDelayed(() -> mIsUserInitializing.postValue(false), 500);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                    mIsUserInitializing.postValue(true);
                });
    }

    private void setUser(User user) {
        mEncodedImage = user.getImageUrl();
        Bitmap imageBitmap = Utils.decodeImage(mEncodedImage);
        mImageBitmap.postValue(imageBitmap);
        mOriginalUser = user;
        mFullName.postValue(user.getFullName());
        mGender.postValue(user.getGender());
        Date birthday = user.getBirthday();
        mBirthdayStr.postValue(Utils.dateToString(birthday));
    }

    public void navigateToHome() {
        mNavigateToHome.postValue(true);
    }

    public void openImagePicker() {
        mOpenImagePicker.postValue(true);
    }

    public void openCustomInputDialog() {
        InputDialogModel model = new InputDialogModel.Builder()
                .setTitle("Full name")
                .setCurrentContent( mFullName.getValue() )
                .setSubmitButtonClickListener(newName -> {
                    if (!newName.isEmpty()) {
                        mFullName.postValue(newName);
                        mIsDataChanged.postValue(true);
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
        if (!mOriginalUser.getImageUrl().equals(mEncodedImage) ||
                !mOriginalUser.getFullName().equals( mFullName.getValue() ) ||
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
                .setPositiveButton("Ok", aVoid -> {
                    updateUserToFirebase();
                })
                .setNegativeButton("Reset", aVoid -> {
                    resetAllFields();
                })
                .build();
        mOpenCustomAlertDialog.postValue(model);
    }

    public void updateUserToFirebase() {
        mIsUserUpdating.postValue(true);

        mOriginalUser.setImageUrl(mEncodedImage);
        mOriginalUser.setFullName( mFullName.getValue() );
        mOriginalUser.setGender( mGender.getValue() );
        Date birthday = Utils.stringToDate( mBirthdayStr.getValue() );
        mOriginalUser.setBirthday(birthday);

        String uid = mAuthService.getCurrentUid();
        mAuthService.updateBasicUser(uid, mOriginalUser)
                .addOnSuccessListener(aVoid -> {
                    mSuccessToastMessage.postValue("Update successfully");
                    mIsDataChanged.postValue(false);
                    mIsUserUpdating.postValue(false);
                })
                .addOnFailureListener(e -> {
                    mErrorToastMessage.postValue("Update unsuccessfully");
                    mIsUserUpdating.postValue(false);
                    Log.e(TAG, "Error" + e);
                });
    }

    private void resetAllFields() {
        setUser(mOriginalUser);
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