package com.example.friend.sentrequest;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.snackbar.SnackbarModel;
import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.sentrequest.adapter.SentRequestListener;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SentRequestsViewModel extends BaseViewModel implements SentRequestListener {

    private static final String TAG = SentRequestsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> sentFriendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isSentRequestsLoading = new MutableLiveData<>(true);
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Bundle> getNavigateToProfileViewer() {
        return navigateToProfileViewer;
    }

    public LiveData<List<FriendRequestView>> getSentFriendRequests() {
        return sentFriendRequests;
    }

    public LiveData<Boolean> getIsSentRequestsLoading() {
        return isSentRequestsLoading;
    }

    public SentRequestsViewModel(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadSentFriendRequests();
    }

    @Override
    public void onItemClick(int position) {
        FriendRequestView requestView = this.sentFriendRequests.getValue().get(position);
        FriendRequest friendRequest = requestView.getFriendRequest();
        Bundle data = new Bundle();
        data.putString(Utils.EXTRA_SELECTED_USER_ID, friendRequest.getSenderId());
        data.putString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, friendRequest.getId());
        this.navigateToProfileViewer.postValue(data);
    }

    @Override
    public void onRecallClick(int position) {
        List<FriendRequestView> sentFriendRequests = this.sentFriendRequests.getValue();
        FriendRequestView request = sentFriendRequests.get(position);
        request.setLoading(true);
        this.sentFriendRequests.postValue(sentFriendRequests);

        new Handler().postDelayed(() -> {
            request.setLoading(false);
            sentFriendRequests.remove(position);
            this.sentFriendRequests.postValue(sentFriendRequests);
        }, 200);

        SnackbarModel model = new SnackbarModel.Builder()
                .duration(7000)
                .message("Friend request sent")
                .actionText("Undo")
                .customAction(aSnackbarVoid -> {
                    sentFriendRequests.add(position, request);
                    this.sentFriendRequests.postValue(sentFriendRequests);
                })
                .onDismissedAction(dismissEvent -> {
                    if (dismissEvent == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        return;
                    }

                    String friendRequestId = request.getFriendRequest().getId();
                    friendRequestRepos
                            .delete(friendRequestId)
                            .thenAccept(aVoid -> {
                            })
                            .exceptionally(e -> {
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                                return null;
                            });
                })
                .build();
        snackbarModel.postValue(model);
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    private void loadSentFriendRequests() {
        this.isSentRequestsLoading.postValue(true);
        String curUserId = authRepos.getCurrentUid();
        friendRequestRepos.getPendingFriendRequestsBySenderId(curUserId)
                .thenAccept(friendRequests -> {
                    this.isSentRequestsLoading.postValue(false);
                    this.sentFriendRequests.postValue(friendRequests);
                })
                .exceptionally(e -> {
                    this.isSentRequestsLoading.postValue(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    return null;
                });
    }
}
