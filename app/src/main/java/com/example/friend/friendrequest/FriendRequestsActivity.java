package com.example.friend.friendrequest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityFriendRequestsBinding;
import com.example.friend.friendrequest.adapter.FriendRequestAdapter;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;

public class FriendRequestsActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private FriendRequestsViewModel viewModel;
    private FriendRequestAdapter friendRequestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl(userRepos, authRepos);
        FriendRequestsViewModelFactory factory = new FriendRequestsViewModelFactory(authRepos, friendRequestRepos);
        viewModel = new ViewModelProvider(this, factory).get(FriendRequestsViewModel.class);

        friendRequestsAdapter = new FriendRequestAdapter(new ArrayList<>(), viewModel);

        ActivityFriendRequestsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_friend_requests);
        binding.setViewModel(viewModel);
        binding.setFriendRequestAdapter(friendRequestsAdapter);
        binding.setLifecycleOwner(this);

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