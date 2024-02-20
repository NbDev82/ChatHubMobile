package com.example.friend.profileviewer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.customalertdialog.AlertDialogFragment;
import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.databinding.ActivityProfileViewerBinding;
import com.example.friend.service.FriendRequestService;
import com.example.friend.service.FriendRequestServiceImpl;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.authservice.AuthService;
import com.example.user.authservice.AuthServiceImpl;

public class ProfileViewerActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private ProfileViewerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        AuthService authService = new AuthServiceImpl();
        FriendRequestService friendRequestService = new FriendRequestServiceImpl(authService);
        ProfileViewerViewModelFactory factory =
                new ProfileViewerViewModelFactory(authService, friendRequestService);
        viewModel = new ViewModelProvider(this, factory).get(ProfileViewerViewModel.class);

        ActivityProfileViewerBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_profile_viewer);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

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