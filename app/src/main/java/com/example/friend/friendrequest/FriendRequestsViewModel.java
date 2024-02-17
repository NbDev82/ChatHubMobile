package com.example.friend.friendrequest;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.snackbar.SnackbarModel;
import com.example.friend.FriendRequest;
import com.example.friend.friendrequest.adapter.FriendRequestListener;
import com.example.friend.friendrequest.adapter.FriendRequestView;
import com.example.friend.service.FriendRequestService;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsViewModel extends BaseViewModel implements FriendRequestListener {

    private static final String TAG = FriendRequestsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isRequestsLoading = new MutableLiveData<>();
    private MutableLiveData<SnackbarModel> snackbarModel = new MutableLiveData<>();
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

    public LiveData<SnackbarModel> getSnackbarModel() {
        return snackbarModel;
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
        updateFriendRequestStatus(position, FriendRequest.EStatus.ACCEPTED,
                "Friend request accepted");
    }

    @Override
    public void onRejectClick(int position) {
        updateFriendRequestStatus(position, FriendRequest.EStatus.REJECTED,
                "Friend request rejected");
    }

    private void updateFriendRequestStatus(int position, FriendRequest.EStatus status, String message) {
        List<FriendRequestView> friendRequestViews = this.friendRequests.getValue();
        FriendRequestView request = friendRequestViews.get(position);
        request.setLoading(true);
        this.friendRequests.postValue(friendRequestViews);

        new Handler().postDelayed(() -> {
            request.setLoading(false);
            friendRequestViews.remove(position);
            this.friendRequests.postValue(friendRequestViews);
        }, 200);

        SnackbarModel model = new SnackbarModel.Builder()
                .duration(7000)
                .message(message)
                .actionText("Undo")
                .customAction(aSnackbarVoid -> {
                    friendRequestViews.add(position, request);
                    this.friendRequests.postValue(friendRequestViews);
                })
                .onDismissedAction(dismissEvent -> {
                    if (dismissEvent == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        return;
                    }

                    friendRequestService
                            .updateFriendRequestStatus(request.getId(), status)
                            .addOnSuccessListener(aVoid -> {})
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                            });
                })
                .build();
        snackbarModel.postValue(model);
    }
}
