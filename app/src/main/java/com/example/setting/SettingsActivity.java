package com.example.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySettingsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_settings);

        SettingsViewModelFactory factory = new SettingsViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(SettingsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setObservers();
    }

    private void setObservers() {
    }
}