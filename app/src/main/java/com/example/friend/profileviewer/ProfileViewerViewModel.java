package com.example.friend.profileviewer;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.friend.FriendRequest;
import com.example.friend.service.FriendRequestService;
import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;
import com.example.user.EGender;

import java.util.Date;

public class ProfileViewerViewModel extends BaseViewModel {

    private static final String TAG = ProfileViewerViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
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
                    if (user != null) {
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
}
