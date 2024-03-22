package com.example.friend.profileviewer;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.friend.FriendRequest;
import com.example.friend.repository.FriendRequestRepos;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.EGender;
import com.example.user.User;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

import java.util.Date;

public class ProfileViewerViewModel extends BaseViewModel {

    private static final String TAG = ProfileViewerViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<AlertDialogModel> openCustomAlertDialog = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> userImageBitmap = new MutableLiveData<>();
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<EGender> gender = new MutableLiveData<>();
    private final MutableLiveData<String> birthdayStr = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserInitializing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isButtonLoading = new MutableLiveData<>();
    private final MutableLiveData<EFriendshipStatus> friendshipStatus = new MutableLiveData<>(EFriendshipStatus.NOT_FOUND);
    private final UserRepos userRepos;
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;
    private String loggedUserId = "";
    private String displayedUserId = "";
    private String friendRequestId = "";

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<AlertDialogModel> getOpenCustomAlertDialog() {
        return openCustomAlertDialog;
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

    public LiveData<Boolean> getIsButtonLoading() {
        return isButtonLoading;
    }

    public LiveData<EFriendshipStatus> getFriendshipStatus() {
        return friendshipStatus;
    }

    public ProfileViewerViewModel(UserRepos userRepos,
                                  AuthRepos authRepos,
                                  FriendRequestRepos friendRequestRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    public void fetchLoggedUserId() {
        this.loggedUserId = authRepos.getCurrentUid();
    }

    public void fetchUserProfile(String userId) {
        isUserInitializing.postValue(true);
        userRepos.getUserByUid(userId)
                .addOnSuccessListener(user -> {
                    setUser(user);
                    new Handler().postDelayed(() -> {
                        isUserInitializing.postValue(false);
                    }, 200);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, e.getMessage(), e);
                    openUserNotFoundDialog();
                });
    }

    public void fetchFriendRequestStatus() {
        this.isButtonLoading.postValue(true);
        if (Utils.isEmpty(friendRequestId)) {
            this.isButtonLoading.postValue(false);
            this.friendshipStatus.postValue(EFriendshipStatus.NOT_FOUND);
            return;
        }

        friendRequestRepos
                .getFriendRequest(friendRequestId)
                .addOnSuccessListener(friendRequest -> {
                    String senderId = friendRequest.getSenderId();
                    FriendRequest.EStatus status = friendRequest.getStatus();
                    handleFriendRequestStatus(loggedUserId, senderId, status);
                })
                .addOnFailureListener(e -> {
                    this.isButtonLoading.postValue(false);
                    Log.e(TAG, e.getMessage(), e);
                });
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    public void setDisplayedUserId(String displayedUserId) {
        this.displayedUserId = displayedUserId;
    }

    public void setFriendRequestId(String friendRequestId) {
        this.friendRequestId = friendRequestId;
    }

    public void navigateToChat() {
        errorToastMessage.postValue("Not implement navigateToChat()");
    }

    public void sendFriendRequest() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Send Friend Request")
                .setMessage("Are you sure you want to send a friend request?")
                .setPositiveButton("Ok", aVoid -> {
                    String loggedUserId = authRepos.getCurrentUid();
                    FriendRequest friendRequest =
                            new FriendRequest(loggedUserId, displayedUserId, FriendRequest.EStatus.PENDING, new Date());
                    friendRequestRepos.addFriendRequest(friendRequest);
                })
                .setNegativeButton("Cancel", null)
                .build();
        openCustomAlertDialog.postValue(model);
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

    public void acceptFriendRequest() {
        isButtonLoading.postValue(true);
        friendRequestRepos
                .updateFriendRequestStatus(friendRequestId, FriendRequest.EStatus.ACCEPTED)
                .addOnSuccessListener(aVoid -> {
                    successToastMessage.postValue("Accept successfully");
                    isButtonLoading.postValue(false);
                })
                .addOnFailureListener(e -> {
                    errorToastMessage.postValue("Accept unsuccessfully");
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    isButtonLoading.postValue(false);
                });
    }

    public void rejectFriendRequest() {
        isButtonLoading.postValue(true);
        friendRequestRepos
                .updateFriendRequestStatus(friendRequestId, FriendRequest.EStatus.REJECTED)
                .addOnSuccessListener(aVoid -> {
                    successToastMessage.postValue("Reject successfully");
                    isButtonLoading.postValue(false);
                })
                .addOnFailureListener(e -> {
                    errorToastMessage.postValue("Reject unsuccessfully");
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    isButtonLoading.postValue(false);
                });
    }

    public void unfriend() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Unfriend")
                .setMessage("Are you sure you want to unfriend?")
                .setPositiveButton("Ok", aVoid -> {
                    isButtonLoading.postValue(true);
                    friendRequestRepos
                            .delete(friendRequestId)
                            .addOnSuccessListener(aUpdateVoid -> {
                                friendRequestId = "";
                                friendshipStatus.postValue(EFriendshipStatus.NOT_FRIEND);
                                successToastMessage.postValue("Unfriend successfully");
                                isButtonLoading.postValue(false);
                            })
                            .addOnFailureListener(e -> {
                                errorToastMessage.postValue("Unfriend unsuccessfully");
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                                isButtonLoading.postValue(false);
                            });
                })
                .setNegativeButton("Cancel", null)
                .build();
        openCustomAlertDialog.postValue(model);
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

    private void handleFriendRequestStatus(String currentUserId,
                                           String senderId,
                                           FriendRequest.EStatus status) {
        EFriendshipStatus friendshipStatus = Utils
                .convertFriendRequestStatusToFriendshipStatus(currentUserId, senderId, status);
        this.friendshipStatus.postValue(friendshipStatus);
        this.isButtonLoading.postValue(false);
    }
}