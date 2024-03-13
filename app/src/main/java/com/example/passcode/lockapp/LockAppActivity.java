package com.example.passcode.lockapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.customalertdialog.AlertDialogFragment;
import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.databinding.ActivityLockAppBinding;
import com.example.infrastructure.BaseActivity;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LockAppActivity extends BaseActivity<LockAppViewModel, ActivityLockAppBinding> {

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_lock_app;
    }

    @Override
    protected Class<LockAppViewModel> getViewModelClass() {
        return LockAppViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        PreferenceManagerRepos preferenceManagerRepos = new PreferenceManagerRepos(getApplicationContext());
        return new LockAppViewModelFactory(preferenceManagerRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        viewModel.getNavigateToChangePasscode().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToChangePasscode(EAnimationType.FADE_IN);
            }
        });

        viewModel.getOpenSingleChoiceAutoLockTime().observe(this, this::openSingleChoiceAutoLockTime);

        viewModel.getOpenCustomAlertDialog().observe(this, this::openCustomAlertDialog);
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

    private void openSingleChoiceAutoLockTime(int selectedItem) {
        viewModel.setSelectedAutoLockTimeIndex(selectedItem);
        String[] genderStrs = EAutoLockTime.getAllDisplays();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_gender)
                .setTitle("Auto-lock time")
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

    private void openCustomAlertDialog(AlertDialogModel alertDialogModel) {
        AlertDialogFragment dialog = new AlertDialogFragment(alertDialogModel);
        dialog.show(getSupportFragmentManager(), AlertDialogFragment.TAG);
    }
}