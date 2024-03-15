package com.example.friend.friendsuggestion;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.snackbar.SnackbarModel;
import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.example.friend.friendsuggestion.adapter.FriendSuggestionListener;
import com.example.friend.repository.FriendRequestRepos;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FriendSuggestionsViewModel extends BaseViewModel implements FriendSuggestionListener {

    private static final String TAG = FriendSuggestionsViewModel.class.getSimpleName();

    private final MutableLiveData<SnackbarModel> snackbarModel = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendSuggestions = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isSuggestionsLoading = new MutableLiveData<>();
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    public LiveData<SnackbarModel> getSnackbarModel() {
        return snackbarModel;
    }

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Bundle> getNavigateToProfileViewer() {
        return navigateToProfileViewer;
    }

    public LiveData<List<FriendRequestView>> getFriendSuggestions() {
        return friendSuggestions;
    }

    public LiveData<Boolean> getIsSuggestionsLoading() {
        return isSuggestionsLoading;
    }

    public FriendSuggestionsViewModel(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadRecommendedFriends();
    }

    @Override
    public void onItemClick(int position) {
        FriendRequestView request = this.friendSuggestions.getValue().get(position);
        Bundle data = new Bundle();
        data.putString(Utils.EXTRA_SELECTED_USER_ID, request.getFriendRequest().getSenderId());
        data.putString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, request.getFriendRequest().getSenderId());
        this.navigateToProfileViewer.postValue(data);
    }

    @Override
    public void onAddFriendClick(int position) {
        List<FriendRequestView> friendSuggestions = this.friendSuggestions.getValue();
        FriendRequestView suggestionView = friendSuggestions.get(position);
        suggestionView.setLoading(true);
        this.friendSuggestions.postValue(friendSuggestions);

        new Handler().postDelayed(() -> {
            suggestionView.setLoading(false);
            friendSuggestions.remove(position);
            this.friendSuggestions.postValue(friendSuggestions);
        }, 200);

        SnackbarModel model = new SnackbarModel.Builder()
                .duration(7000)
                .message("Friend request sent")
                .actionText("Undo")
                .customAction(aSnackbarVoid -> {
                    friendSuggestions.add(position, suggestionView);
                    this.friendSuggestions.postValue(friendSuggestions);
                })
                .onDismissedAction(dismissEvent -> {
                    if (dismissEvent == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        return;
                    }

                    FriendRequest friendRequest = suggestionView.getFriendRequest();
                    friendRequest.setStatus(FriendRequest.EStatus.PENDING);
                    friendRequest.setCreatedTime(new Date());
                    friendRequestRepos.addFriendRequest(friendRequest)
                            .addOnSuccessListener(aVoid -> {
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                            });
                })
                .build();
        snackbarModel.postValue(model);
    }

    @Override
    public void onRemoveClick(int position) {
        List<FriendRequestView> friendSuggestions = this.friendSuggestions.getValue();
        FriendRequestView request = friendSuggestions.get(position);

        SnackbarModel model = new SnackbarModel.Builder()
                .duration(7000)
                .message("Suggestion removed")
                .actionText("Undo")
                .customAction(aSnackbarVoid -> {
                    friendSuggestions.add(position, request);
                    this.friendSuggestions.postValue(friendSuggestions);
                })
                .build();
        snackbarModel.postValue(model);
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    private void loadRecommendedFriends() {
        this.isSuggestionsLoading.postValue(true);
        String uid = authRepos.getCurrentUid();
        friendRequestRepos.getRecommendedFriends(uid)
                .addOnSuccessListener(friendRequests -> {
                    this.isSuggestionsLoading.postValue(false);
                    this.friendSuggestions.postValue(friendRequests);
                })
                .addOnFailureListener(e -> {
                    this.isSuggestionsLoading.postValue(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                });
    }
}
