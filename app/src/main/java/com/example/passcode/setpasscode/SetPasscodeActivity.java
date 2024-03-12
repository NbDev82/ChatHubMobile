package com.example.passcode.setpasscode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivitySetPasscodeBinding;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;

public class SetPasscodeActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private SetPasscodeViewModel viewModel;
    private PreferenceManagerRepos preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        viewModel = new ViewModelProvider(this).get(SetPasscodeViewModel.class);

        ActivitySetPasscodeBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_set_passcode);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        preferenceManager = new PreferenceManagerRepos(getApplicationContext());

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, data -> {
            navigationManager.navigateBack(data, EAnimationType.FADE_OUT);
        });

        viewModel.getSetPasscodeState().observe(this, passcodeSetState -> {
            if (passcodeSetState == EPasscodeSetState.COMPLETED) {
                viewModel.saveNewPasscode(preferenceManager);
            }
        });
    }

}