package com.example.friend.sentrequest;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivitySentRequestsBinding;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.friend.sentrequest.adapter.SentRequestAdapter;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;

public class SentRequestsActivity extends BaseActivity<SentRequestsViewModel, ActivitySentRequestsBinding> {

    private SentRequestAdapter sentRequestAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_sent_requests;
    }

    @Override
    protected Class<SentRequestsViewModel> getViewModelClass() {
        return SentRequestsViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl(userRepos);
        return new SentRequestsViewModelFactory(authRepos, friendRequestRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sentRequestAdapter = new SentRequestAdapter(new ArrayList<>(), viewModel);
        binding.setRequestAdapter(sentRequestAdapter);

        observers();
    }

    private void observers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });

        viewModel.getNavigateToProfileViewer().observe(this, data -> {
            navigationManager.navigateToProfileViewer(data, EAnimationType.FADE_IN);
        });

        viewModel.getSentFriendRequests().observe(this, newSentRequests -> {
            if (newSentRequests != null) {
                sentRequestAdapter.setItems(newSentRequests);
            }
        });
    }
}