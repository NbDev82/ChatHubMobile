package com.example.friend.myfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityFriendsBinding;
import com.example.friend.friendrequest.adapter.FriendRequestAdapter;
import com.example.friend.myfriend.adapter.FriendAdapter;
import com.example.friend.service.FriendRequestService;
import com.example.friend.service.FriendRequestServiceImpl;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private FriendsViewModel viewModel;
    private FriendAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        AuthService authService = new AuthServiceImpl();
        FriendRequestService friendRequestService = new FriendRequestServiceImpl(authService);
        FriendsViewModelFactory factory = new FriendsViewModelFactory(authService, friendRequestService);
        viewModel = new ViewModelProvider(this, factory).get(FriendsViewModel.class);

        friendsAdapter = new FriendAdapter(new ArrayList<>(), viewModel);

        ActivityFriendsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_friends);
        binding.setViewModel(viewModel);
        binding.setFriendAdapter(friendsAdapter);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });
    }
}