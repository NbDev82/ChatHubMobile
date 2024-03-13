package com.example.friend.friendsuggestion;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityFriendSuggestionsBinding;
import com.example.friend.friendrequest.FriendRequestsViewModelFactory;
import com.example.friend.friendsuggestion.adapter.FriendSuggestionAdapter;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;

public class FriendSuggestionsActivity extends BaseActivity<FriendSuggestionsViewModel, ActivityFriendSuggestionsBinding> {

    private FriendSuggestionAdapter suggestionAdapter;

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_friend_suggestions;
    }

    @Override
    protected Class<FriendSuggestionsViewModel> getViewModelClass() {
        return FriendSuggestionsViewModel.class;
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

        suggestionAdapter = new FriendSuggestionAdapter(new ArrayList<>(), viewModel);
        binding.setSuggestionAdapter(suggestionAdapter);

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