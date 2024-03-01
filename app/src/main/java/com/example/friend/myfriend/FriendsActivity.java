package com.example.friend.myfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityFriendsBinding;
import com.example.friend.myfriend.adapter.FriendAdapter;
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

public class FriendsActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private FriendsViewModel viewModel;
    private FriendAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl(userRepos, authRepos);
        FriendsViewModelFactory factory = new FriendsViewModelFactory(authRepos, friendRequestRepos);
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