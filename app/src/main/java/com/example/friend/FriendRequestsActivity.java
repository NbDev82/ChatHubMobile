package com.example.friend;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityFriendRequestsBinding;
import com.example.friend.adapter.FriendRequestsAdapter;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;

import java.util.ArrayList;

public class FriendRequestsActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private FriendRequestsViewModel viewModel;
    private FriendRequestsAdapter friendRequestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        FriendRequestsViewModelFactory factory = new FriendRequestsViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(FriendRequestsViewModel.class);

        friendRequestsAdapter = new FriendRequestsAdapter(new ArrayList<>(), viewModel);

        ActivityFriendRequestsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_friend_requests);
        binding.setViewModel(viewModel);
        binding.setFriendRequestsAdapter(friendRequestsAdapter);
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

        viewModel.getFriendRequests().observe(this, newFriendRequests -> {
            if (newFriendRequests != null) {
                friendRequestsAdapter.setFriendRequests(newFriendRequests);
            }
        });
    }
}