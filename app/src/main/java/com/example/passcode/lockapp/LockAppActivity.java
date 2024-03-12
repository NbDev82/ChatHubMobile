package com.example.passcode.lockapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.inputdialogfragment.InputDialogFragment;
import com.example.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.databinding.ActivityLockAppBinding;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.EGender;
import com.example.user.profile.UserProfileActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LockAppActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private LockAppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        PreferenceManagerRepos preferenceManagerRepos = new PreferenceManagerRepos(getApplicationContext());
        LockAppViewModelFactory factory = new LockAppViewModelFactory(preferenceManagerRepos);
        viewModel = new ViewModelProvider(this, factory).get(LockAppViewModel.class);

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

        viewModel.getNavigateToSetPasscode().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSetPasscodeWithActivityResultLauncher(
                        setPasscodeLauncher, EAnimationType.FADE_IN);
            }
        });
    }

    private ActivityResultLauncher<Intent> setPasscodeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != RESULT_OK) {
                    return;
                }

                Intent data = result.getData();
                if (data == null) {
                    return;
                }

                Boolean isPasscodeSetSuccess = data
                        .getBooleanExtra(Utils.EXTRA_PASSCODE_SET_SUCCESS, false);
                if (isPasscodeSetSuccess) {
                    viewModel.loadPreferences();
                }
            }
    );

    private void openSingleChoiceGender(int selectedItem) {
        viewModel.setSelectedAutoLockTimeIndex(selectedItem);
        String[] genderStrs = EGender.getAllDisplays();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_gender)
                .setTitle("Gender")
                .setSingleChoiceItems(genderStrs, selectedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.setSelectedAutoLockTimeIndex(which);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EAutoLockTime curSelected = EAutoLockTime.values()[viewModel.getSelectedAutoLockTimeIndex()];
                        viewModel.setSelectedAutoLockTime(curSelected);
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.loadPreferences();
    }
}