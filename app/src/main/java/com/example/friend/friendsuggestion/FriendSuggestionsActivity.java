package com.example.friend.friendsuggestion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityFriendSuggestionsBinding;
import com.example.friend.friendrequest.FriendRequestsViewModelFactory;
import com.example.friend.friendsuggestion.adapter.FriendSuggestionAdapter;
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

public class FriendSuggestionsActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private FriendSuggestionsViewModel viewModel;
    private FriendSuggestionAdapter suggestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl(userRepos, authRepos);
        FriendRequestsViewModelFactory factory = new FriendRequestsViewModelFactory(authRepos, friendRequestRepos);
        viewModel = new ViewModelProvider(this, factory).get(FriendSuggestionsViewModel.class);

        suggestionAdapter = new FriendSuggestionAdapter(new ArrayList<>(), viewModel);

        ActivityFriendSuggestionsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_friend_suggestions);
        binding.setViewModel(viewModel);
        binding.setSuggestionAdapter(suggestionAdapter);
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