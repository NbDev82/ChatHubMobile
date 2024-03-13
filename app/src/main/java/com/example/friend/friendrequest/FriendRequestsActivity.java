package com.example.friend.friendrequest;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityFriendRequestsBinding;
import com.example.friend.friendrequest.adapter.FriendRequestAdapter;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;

public class FriendRequestsActivity extends BaseActivity<FriendRequestsViewModel, ActivityFriendRequestsBinding> {

    private FriendRequestAdapter friendRequestsAdapter;

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_friend_requests;
    }

    @Override
    protected Class<FriendRequestsViewModel> getViewModelClass() {
        return FriendRequestsViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl(userRepos, authRepos);
        return new FriendRequestsViewModelFactory(authRepos, friendRequestRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendRequestsAdapter = new FriendRequestAdapter(new ArrayList<>(), viewModel);

        binding.setFriendRequestAdapter(friendRequestsAdapter);

        setupObservers();

        viewModel.loadFriendRequests();
    }

    private void setupObservers() {
        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome(EAnimationType.FADE_OUT);
            }
        });

        viewModel.getNavigateToFriends().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToFriends(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToProfileViewer().observe(this, data -> {
            navigationManager.navigateToProfileViewer(data, EAnimationType.FADE_OUT);
        });

        viewModel.getFriendRequests().observe(this, newFriendRequests -> {
            if (newFriendRequests != null) {
                friendRequestsAdapter.setFriendRequests(newFriendRequests);
            }
        });
    }
}