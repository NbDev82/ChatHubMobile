package com.example.friend.profileviewer;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.friend.FriendRequest;
import com.example.friend.service.FriendRequestService;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.EGender;
import com.example.user.User;

import java.util.Date;

public class ProfileViewerViewModel extends BaseViewModel {

    private static final String TAG = ProfileViewerViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<AlertDialogModel> openCustomAlertDialog = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFriend = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> userImageBitmap = new MutableLiveData<>();
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<EGender> gender = new MutableLiveData<>();
    private final MutableLiveData<String> birthdayStr = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserInitializing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSentFriendRequest = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSendingFriendRequest = new MutableLiveData<>();
    private final AuthService authService;
    private final FriendRequestService friendRequestService;
    private String displayedUserId = "";

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<AlertDialogModel> getOpenCustomAlertDialog() {
        return openCustomAlertDialog;
    }

    public LiveData<Boolean> getIsFriend() {
        return isFriend;
    }

    public LiveData<Bitmap> getUserImageBitmap() {
        return userImageBitmap;
    }

    public LiveData<String> getFullName() {
        return fullName;
    }

    public LiveData<EGender> getGender() {
        return gender;
    }

    public LiveData<String> getBirthdayStr() {
        return birthdayStr;
    }

    public LiveData<Boolean> getIsUserInitializing() {
        return isUserInitializing;
    }

    public LiveData<Boolean> getIsSentFriendRequest() {
        return isSentFriendRequest;
    }

    public LiveData<Boolean> getIsSendingFriendRequest() {
        return isSendingFriendRequest;
    }

    public ProfileViewerViewModel(AuthService authService, FriendRequestService friendRequestService) {
        this.authService = authService;
        this.friendRequestService = friendRequestService;
    }

    public void fetchUserInformation() {
        isUserInitializing.postValue(true);
        authService.getUserByUid(displayedUserId)
                .addOnSuccessListener(user -> {
                    setUser(user);
                    new Handler().postDelayed(() -> {
                        isUserInitializing.postValue(false);
                    }, 200);
                })
                .addOnFailureListener(e -> {
                    openUserNotFoundDialog();
                    Log.e(TAG, "Error: " + e);
                });
    }

    private void setUser(User user) {
        Bitmap userImageBitmap = Utils.decodeImage(user.getImageUrl());
        this.userImageBitmap.postValue(userImageBitmap);
        fullName.postValue(user.getFullName());
        gender.postValue(user.getGender());
        Date birthday = user.getBirthday();
        birthdayStr.postValue(Utils.dateToString(birthday));
    }

    private void openUserNotFoundDialog() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("User Not Found")
                .setMessage("The user you are trying to access was not found! Click OK to quit!")
                .setPositiveButton("Ok", aVoid -> {
                    navigateBack();
                })
                .build();
        openCustomAlertDialog.postValue(model);
    }

    public void checkFriendRequestStatus() {
        friendRequestService.getFriendRequestStatus(authService.getCurrentUid(), displayedUserId)
                .addOnSuccessListener(friendStatus -> {
                    if (friendStatus == FriendRequest.EStatus.ACCEPTED) {

                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                });
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    public void setDisplayedUserId(String displayedUserId) {
        this.displayedUserId = displayedUserId;
    }

    public void navigateToChat() {
        errorToastMessage.postValue("Not implement navigateToChat()");
    }

    public void sendFriendRequest() {
        String loggedUserId = authService.getCurrentUid();
        FriendRequest friendRequest =
                new FriendRequest(loggedUserId, displayedUserId, FriendRequest.EStatus.PENDING, new Date());
        friendRequestService.addFriendRequest(friendRequest);
    }

    public void recallRequest() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Recall Friend Request")
                .setMessage("Are you sure you want to recall this friend request?")
                .setPositiveButton("Recall", aVoid -> {
                })
                .setNegativeButton("Cancel", null)
                .build();
        openCustomAlertDialog.postValue(model);
    }
}
