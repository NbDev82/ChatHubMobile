package com.example.friend.profileviewer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityProfileViewerBinding;
import com.example.friend.service.FriendRequestService;
import com.example.friend.service.FriendRequestServiceImpl;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

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
            String selectedUser = extras.getString(Utils.EXTRA_SELECTED_USER_ID, "");
            viewModel.setDisplayedUserId(selectedUser);
            viewModel.fetchUserInformation();
        }
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
        });
    }
}