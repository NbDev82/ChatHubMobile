package com.example.friend.friendsuggestion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityFriendSuggestionsBinding;
import com.example.friend.friendrequest.FriendRequestsViewModelFactory;
import com.example.friend.friendsuggestion.adapter.FriendSuggestionAdapter;
import com.example.friend.service.FriendRequestService;
import com.example.friend.service.FriendRequestServiceImpl;
import com.example.infrastructure.Utils;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.authservice.AuthService;
import com.example.user.authservice.AuthServiceImpl;

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

        AuthService authService = new AuthServiceImpl();
        FriendRequestService friendRequestService = new FriendRequestServiceImpl(authService);
        FriendRequestsViewModelFactory factory = new FriendRequestsViewModelFactory(authService, friendRequestService);
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
    }
}