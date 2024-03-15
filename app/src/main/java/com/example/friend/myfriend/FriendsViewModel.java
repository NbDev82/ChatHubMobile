package com.example.friend.myfriend;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.snackbar.SnackbarModel;
import com.example.friend.FriendRequestView;
import com.example.friend.myfriend.adapter.FriendListener;
import com.example.friend.repository.FriendRequestRepos;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;

import java.util.ArrayList;
import java.util.List;

public class FriendsViewModel extends BaseViewModel implements FriendListener {

    private static final String TAG = FriendsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isFriendsLoading = new MutableLiveData<>(true);
    private MutableLiveData<SnackbarModel> snackbarModel = new MutableLiveData<>();
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Bundle> getNavigateToProfileViewer() {
        return navigateToProfileViewer;
    }

    public LiveData<List<FriendRequestView>> getFriendRequests() {
        return friendRequests;
    }

    public LiveData<Boolean> getIsFriendsLoading() {
        return isFriendsLoading;
    }

    public LiveData<SnackbarModel> getSnackbarModel() {
        return snackbarModel;
    }

    public FriendsViewModel(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadAcceptedFriendRequests();
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    private void loadAcceptedFriendRequests() {
        this.isFriendsLoading.postValue(true);
        String uid = authRepos.getCurrentUid();
        friendRequestRepos.getAcceptedFriendRequests(uid)
                .addOnSuccessListener(friendRequests -> {
                    this.isFriendsLoading.postValue(false);
                    this.friendRequests.postValue(friendRequests);
                })
                .addOnFailureListener(e -> {
                    this.isFriendsLoading.postValue(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                });
    }

    @Override
    public void onItemClick(int position) {
        FriendRequestView request = this.friendRequests.getValue().get(position);
        Bundle data = new Bundle();
        data.putString(Utils.EXTRA_SELECTED_USER_ID, request.getFriendRequest().getSenderId());
        data.putString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, request.getFriendRequest().getSenderId());
        this.navigateToProfileViewer.postValue(data);
    }

    @Override
    public void onOpenChatClick(int position) {
        errorToastMessage.postValue("Feature is under development");
    }
}
