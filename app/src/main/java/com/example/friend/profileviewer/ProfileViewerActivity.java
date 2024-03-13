package com.example.friend.profileviewer;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.customalertdialog.AlertDialogFragment;
import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.databinding.ActivityProfileViewerBinding;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.BaseActivity;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class ProfileViewerActivity extends BaseActivity<ProfileViewerViewModel, ActivityProfileViewerBinding> {

    private NavigationManager navigationManager;

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_profile_viewer;
    }

    @Override
    protected Class<ProfileViewerViewModel> getViewModelClass() {
        return ProfileViewerViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl(userRepos, authRepos);
        return new ProfileViewerViewModelFactory(userRepos, authRepos, friendRequestRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);
        binding.setViewModel(viewModel);

        setupObservers();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String selectedUserId = extras.getString(Utils.EXTRA_SELECTED_USER_ID, "");
            String selectedFriendRequestId = extras.getString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, "");
            viewModel.setDisplayedUserId(selectedUserId);
            viewModel.setFriendRequestId(selectedFriendRequestId);
            viewModel.fetchUserInformation();
            viewModel.checkFriendRequestStatus();
        }
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
        });

        viewModel.getOpenCustomAlertDialog().observe(this, this::openCustomAlertDialog);
    }

    private void openCustomAlertDialog(AlertDialogModel alertDialogModel) {
        AlertDialogFragment dialog = new AlertDialogFragment(alertDialogModel);
        dialog.show(getSupportFragmentManager(), AlertDialogFragment.TAG);
    }
}