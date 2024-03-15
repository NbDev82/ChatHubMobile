package com.example.friend.friendrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.databinding.FragmentFriendRequestsBinding;
import com.example.friend.friendrequest.adapter.FriendRequestAdapter;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.BaseFragment;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;

public class FriendRequestsFragment extends BaseFragment<FriendRequestsViewModel, FragmentFriendRequestsBinding> {

    private FriendRequestAdapter friendRequestsAdapter;

    @Override
    protected FragmentFriendRequestsBinding getViewDataBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFriendRequestsBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<FriendRequestsViewModel> getViewModelClass() {
        return FriendRequestsViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl(userRepos);
        return new FriendRequestsViewModelFactory(authRepos, friendRequestRepos);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFriendRequestAdapter();
        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToFriendSuggestions().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToFriendSuggestions(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToFriends().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToFriends(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToProfileViewer().observe(requireActivity(), data -> {
            navigationManager.navigateToProfileViewer(data, EAnimationType.FADE_OUT);
        });

        viewModel.getFriendRequests().observe(requireActivity(), newFriendRequests -> {
            if (newFriendRequests != null) {
                friendRequestsAdapter.setFriendRequests(newFriendRequests);
            }
        });
    }

    private void setupFriendRequestAdapter() {
        friendRequestsAdapter = new FriendRequestAdapter(new ArrayList<>(), viewModel);
        binding.setFriendRequestAdapter(friendRequestsAdapter);
    }
}