package com.example.friend.friendsuggestion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.snackbar.SnackbarModel;
import com.example.friend.friendsuggestion.adapter.FriendSuggestionListener;
import com.example.friend.repository.FriendRequestRepos;
import com.example.infrastructure.BaseViewModel;
import com.example.user.repository.AuthRepos;

public class FriendSuggestionsViewModel extends BaseViewModel implements FriendSuggestionListener {

    private final MutableLiveData<SnackbarModel> snackbarModel = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuggestionsLoading = new MutableLiveData<>();
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    public LiveData<SnackbarModel> getSnackbarModel() {
        return snackbarModel;
    }

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Boolean> getIsSuggestionsLoading() {
        return isSuggestionsLoading;
    }

    public FriendSuggestionsViewModel(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onAddFriendClick(int position) {

    }

    @Override
    public void onRemoveClick(int position) {

    }
}
