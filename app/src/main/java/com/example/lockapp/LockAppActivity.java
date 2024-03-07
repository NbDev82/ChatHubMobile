package com.example.lockapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityLockAppBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;

public class LockAppActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private LockAppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        viewModel = new ViewModelProvider(this).get(LockAppViewModel.class);

        ActivityLockAppBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_lock_app);
        binding.setViewModel(viewModel);
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