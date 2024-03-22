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
import com.example.user.EGender;
import com.example.user.User;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

import java.util.Calendar;
import java.util.Date;

public class UserProfileViewModel extends BaseViewModel {

    private static final String TAG = UserProfileViewModel.class.getSimpleName();

    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<EGender> gender = new MutableLiveData<>();
    private final MutableLiveData<String> birthdayStr = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserInitializing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isDataChanged = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserUpdating = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openImagePicker = new MutableLiveData<>();
    private final MutableLiveData<Calendar> openDatePickerDialog = new MutableLiveData<>();
    private final MutableLiveData<Integer> openSingleChoiceGender = new MutableLiveData<>();
    private final UserRepos userRepos;
    private User originalUser;
    private String encodedImage;

    public LiveData<Bitmap> getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap bitmap) {
        imageBitmap.postValue(bitmap);
        encodedImage = Utils.encodeImage(bitmap);
        if (encodedImage.equals(originalUser.getImageUrl())) {
            this.isDataChanged.postValue(false);
        } else {
            this.isDataChanged.postValue(true);
        }
    }

    public LiveData<String> getFullName() {
        return fullName;
    }

    public LiveData<EGender> getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender.postValue(gender);
        if (gender.equals(originalUser.getGender())) {
            this.isDataChanged.postValue(false);
        } else {
            this.isDataChanged.postValue(true);
        }
    }

    public LiveData<String> getBirthdayStr() {
        return birthdayStr;
    }

    public void setBirthday(Date birthday) {
        String originalBirthdayStr = Utils.dateToString(originalUser.getBirthday());
        String birthdayStr = Utils.dateToString(birthday);
        this.birthdayStr.postValue(birthdayStr);
        if (birthdayStr.equals(originalBirthdayStr)) {
            this.isDataChanged.postValue(false);
        } else {
            this.isDataChanged.postValue(true);
        }
    }

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public MutableLiveData<Boolean> getIsUserInitializing() {
        return isUserInitializing;
    }

    public MutableLiveData<Boolean> getIsDataChanged() {
        return isDataChanged;
    }

    public MutableLiveData<Boolean> getIsUserUpdating() {
        return isUserUpdating;
    }

    public LiveData<Boolean> getOpenImagePicker() {
        return openImagePicker;
    }

    public MutableLiveData<Calendar> getOpenDatePickerDialog() {
        return openDatePickerDialog;
    }

    public LiveData<Integer> getOpenSingleChoiceGender() {
        return openSingleChoiceGender;
    }

    public UserProfileViewModel(UserRepos userRepos, AuthRepos authRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
    }

    @Override
    protected void onStart() {
        super.onStart();

        isDataChanged.postValue(false);
        fetchUserProfile();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void navigateToHome() {
        navigateBack.postValue(true);
    }

    public void openImagePicker() {
        openImagePicker.postValue(true);
    }

    public void openCustomInputDialog() {
        InputDialogModel model = new InputDialogModel.Builder()
                .setTitle("Full name")
                .setCurrentContent(fullName.getValue())
                .setSubmitButtonClickListener(newName -> {
                    if (newName.isEmpty()) {
                        return;
                    }
                    fullName.postValue(newName);
                    if (newName.equals(originalUser.getFullName())) {
                        this.isDataChanged.postValue(false);
                    } else {
                        this.isDataChanged.postValue(true);
                    }
                })
                .build();
        inputDialogModel.postValue(model);
    }

    public void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        Date selectedBirthday = Utils.stringToDate(birthdayStr.getValue());
        calendar.setTime(selectedBirthday != null ? selectedBirthday : originalUser.getBirthday());
        openDatePickerDialog.postValue(calendar);
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
        alertDialogModel.postValue(model);
    }

    public void updateUserToFirebase() {
        isUserUpdating.postValue(true);

        originalUser.setImageUrl(encodedImage);
        originalUser.setFullName(fullName.getValue());
        originalUser.setGender(gender.getValue());
        Date birthday = Utils.stringToDate(birthdayStr.getValue());
        originalUser.setBirthday(birthday);

        String uid = authRepos.getCurrentUid();
        userRepos.updateBasicUser(uid, originalUser)
                .addOnSuccessListener(aVoid -> {
                    successToastMessage.postValue("Update successfully");
                    isDataChanged.postValue(false);
                    isUserUpdating.postValue(false);
                })
                .addOnFailureListener(e -> {
                    errorToastMessage.postValue("Update unsuccessfully");
                    isUserUpdating.postValue(false);
                    Log.e(TAG, "Error" + e);
                });
    }

    public void openSingleChoiceGender() {
        EGender curGenderSelected = gender.getValue();
        int curIndexSelected = EGender.getCurrentIndex(curGenderSelected);
        openSingleChoiceGender.postValue(curIndexSelected);
    }

    private void fetchUserProfile() {
        isUserInitializing.postValue(true);
        authRepos.getCurrentUser()
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        setUser(user);

                        new Handler().postDelayed(() -> {
                            isUserInitializing.postValue(false);
                        }, 500);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                    isUserInitializing.postValue(true);
                });
    }

    private void setUser(User user) {
        encodedImage = user.getImageUrl();
        Bitmap imageBitmap = Utils.decodeImage(encodedImage);
        this.imageBitmap.postValue(imageBitmap);
        originalUser = user;
        fullName.postValue(user.getFullName());
        gender.postValue(user.getGender());
        Date birthday = user.getBirthday();
        birthdayStr.postValue(Utils.dateToString(birthday));
    }

    private void checkChangeStatus(User user) {
        if (!originalUser.getImageUrl().equals(encodedImage) ||
                !originalUser.getFullName().equals(fullName.getValue()) ||
                !originalUser.getGender().equals(gender.getValue()) ||
                !Utils.compareDateWithDateStr(originalUser.getBirthday(), birthdayStr.getValue())
        ) {
            isDataChanged.postValue(true);
        } else {
            isDataChanged.postValue(false);
        }
    }

    private void resetAllFields() {
        setUser(originalUser);
        isDataChanged.postValue(false);
    }
}