package com.example.user.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityUserProfileBinding;
import com.example.home.HomeActivity;
import com.example.home.HomeViewModel;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class UserProfileActivity extends AppCompatActivity {

    private UserProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserProfileBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_user_profile);

        AuthService authService = new AuthServiceImpl();
        UserProfileViewModelFactory factory = new UserProfileViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(UserProfileViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setObservers();
    }

    private void setObservers() {
        viewModel.getNavigateToHome().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}