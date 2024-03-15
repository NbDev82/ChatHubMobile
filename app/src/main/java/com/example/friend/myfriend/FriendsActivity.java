package com.example.friend.myfriend;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityFriendsBinding;
import com.example.friend.myfriend.adapter.FriendAdapter;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;

public class FriendsActivity extends BaseActivity<FriendsViewModel, ActivityFriendsBinding> {

    private FriendAdapter friendsAdapter;

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_friends;
    }

    @Override
    protected Class<FriendsViewModel> getViewModelClass() {
        return FriendsViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl(userRepos);
        return new FriendsViewModelFactory(authRepos, friendRequestRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendsAdapter = new FriendAdapter(new ArrayList<>(), viewModel);
        binding.setFriendAdapter(friendsAdapter);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });

        viewModel.getNavigateToProfileViewer().observe(this, data -> {
            navigationManager.navigateToProfileViewer(data, EAnimationType.FADE_IN);
        });

        viewModel.getFriendRequests().observe(this, newFriendRequests -> {
            if (newFriendRequests != null) {
                friendsAdapter.setItems(newFriendRequests);
            }
        });
    }
}