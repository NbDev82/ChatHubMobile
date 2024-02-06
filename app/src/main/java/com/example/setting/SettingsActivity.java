package com.example.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivitySettingsBinding;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class SettingsActivity extends AppCompatActivity {

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySettingsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_settings);

        AuthService authService = new AuthServiceImpl();
        SettingsViewModelFactory factory = new SettingsViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(SettingsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setObservers();
    }

    private void setObservers() {
    }
}