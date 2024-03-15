package com.example.friend.friendrequest;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.snackbar.SnackbarModel;
import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.example.friend.friendrequest.adapter.FriendRequestListener;
import com.example.friend.repository.FriendRequestRepos;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsViewModel extends BaseViewModel implements FriendRequestListener {

    private static final String TAG = FriendRequestsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateToFriends = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToFriendSuggestions = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isRequestsLoading = new MutableLiveData<>();
    private final MutableLiveData<SnackbarModel> snackbarModel = new MutableLiveData<>();
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    public LiveData<Boolean> getNavigateToFriends() {
        return navigateToFriends;
    }

    public LiveData<Boolean> getNavigateToFriendSuggestions() {
        return navigateToFriendSuggestions;
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

    public FriendRequestsViewModel(AuthRepos authRepos,
                                   FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadFriendRequests();
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

                    friendRequestRepos
                            .updateFriendRequestStatus(request.getFriendRequest().getId(), status)
                            .addOnSuccessListener(aVoid -> {
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                            });
                })
                .build();
        snackbarModel.postValue(model);
    }

    private void loadFriendRequests() {
        this.isRequestsLoading.postValue(true);
        String uid = authRepos.getCurrentUid();
        friendRequestRepos.getPendingFriendRequestsByRecipientId(uid)
                .addOnSuccessListener(friendRequests -> {
                    this.isRequestsLoading.postValue(false);
                    this.friendRequests.postValue(friendRequests);
                })
                .addOnFailureListener(e -> {
                    this.isRequestsLoading.postValue(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                });
    }

    public void navigateToFriendSuggestions() {
        this.navigateToFriendSuggestions.postValue(true);
    }

    public void navigateToFriends() {
        this.navigateToFriends.postValue(true);
    }
}
