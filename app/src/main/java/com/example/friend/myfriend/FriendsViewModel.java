package com.example.friend.myfriend;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.snackbar.SnackbarModel;
import com.example.friend.FriendRequestView;
import com.example.friend.myfriend.adapter.FriendListener;
import com.example.friend.service.FriendRequestService;
import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;

import java.util.ArrayList;
import java.util.List;

public class FriendsViewModel extends BaseViewModel implements FriendListener {

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isFriendsLoading = new MutableLiveData<>(true);
    private MutableLiveData<SnackbarModel> snackbarModel = new MutableLiveData<>();
    private final AuthService authService;
    private final FriendRequestService friendRequestService;

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
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

    public FriendsViewModel(AuthService authService, FriendRequestService friendRequestService) {
        this.authService = authService;
        this.friendRequestService = friendRequestService;
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDetailsIconClick(int position) {

    }
}
