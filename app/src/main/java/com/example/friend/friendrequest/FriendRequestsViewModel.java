package com.example.friend.friendrequest;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.friend.FriendRequest;
import com.example.friend.friendrequest.adapter.FriendRequestListener;
import com.example.friend.friendrequest.adapter.FriendRequestView;
import com.example.friend.service.FriendRequestService;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsViewModel extends BaseViewModel implements FriendRequestListener {

    private static final String TAG = FriendRequestsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isRequestsLoading = new MutableLiveData<>();
    private final AuthService authService;
    private final FriendRequestService friendRequestService;

    public MutableLiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    public LiveData<Bundle> getNavigateToProfileViewer() {
        return navigateToProfileViewer;
    }

    public LiveData<List<FriendRequestView>> getFriendRequests() {
        return friendRequests;
    }

    public LiveData<Boolean> getIsRequestsLoading() {
        return isRequestsLoading;
    }

    public FriendRequestsViewModel(AuthService authService,
                                   FriendRequestService friendRequestService) {
        this.authService = authService;
        this.friendRequestService = friendRequestService;
    }

    public void loadFriendRequests() {
        this.isRequestsLoading.postValue(true);
        String uid = authService.getCurrentUid();
        friendRequestService.getPendingFriendRequestsByRecipientId(uid)
                .addOnSuccessListener(friendRequests -> {
                    this.isRequestsLoading.postValue(false);
                    this.friendRequests.postValue(friendRequests);
                })
                .addOnFailureListener(e -> {
                    this.isRequestsLoading.postValue(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                });
    }

    public void navigateToHome() {
        this.navigateToHome.postValue(true);
    }

    @Override
    public void onItemClick(int position) {
        FriendRequestView request = this.friendRequests.getValue().get(position);
        Bundle data = new Bundle();
        data.putString(Utils.EXTRA_SELECTED_USER_ID, request.getSenderId());
        data.putString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, request.getSenderId());
        this.navigateToProfileViewer.postValue(data);
    }

    @Override
    public void onAcceptClick(int position) {
        List<FriendRequestView> friendRequestViews = this.friendRequests.getValue();
        FriendRequestView request = friendRequestViews.get(position);
        request.setLoading(true);
        this.friendRequests.postValue(friendRequestViews);
        friendRequestService
                .updateFriendRequestStatus(request.getId(), FriendRequest.EStatus.ACCEPTED)
                .addOnSuccessListener(aVoid -> {
                    new Handler().postDelayed(() -> {
                        request.setLoading(false);
                        friendRequestViews.remove(position);
                        this.friendRequests.postValue(friendRequestViews);
                        successToastMessage.postValue("Accept successfully");
                    }, 200);
                })
                .addOnFailureListener(e -> {
                    errorToastMessage.postValue("Accept unsuccessfully");
                    request.setLoading(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                });
    }

    @Override
    public void onRejectClick(int position) {
        List<FriendRequestView> friendRequestViews = this.friendRequests.getValue();
        FriendRequestView request = friendRequestViews.get(position);
        request.setLoading(true);
        this.friendRequests.postValue(friendRequestViews);
        friendRequestService
                .updateFriendRequestStatus(request.getId(), FriendRequest.EStatus.REJECTED)
                .addOnSuccessListener(aVoid -> {
                    new Handler().postDelayed(() -> {
                        request.setLoading(false);
                        friendRequestViews.remove(position);
                        this.friendRequests.postValue(friendRequestViews);
                        successToastMessage.postValue("Reject successfully");
                    }, 200);
                })
                .addOnFailureListener(e -> {
                    errorToastMessage.postValue("Reject unsuccessfully");
                    request.setLoading(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                });
    }
}
