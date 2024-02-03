package com.example.user.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.customalertdialog.AlertDialogFragment;
import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.customcontrol.customalertdialog.AlertDialogViewModel;
import com.example.databinding.ActivityUserProfileBinding;
import com.example.home.HomeActivity;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

import java.util.Calendar;

public class UserProfileActivity extends AppCompatActivity {

    private UserProfileViewModel mProfileViewModel;
    private AlertDialogViewModel mAlertDialogViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        ActivityUserProfileBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_user_profile);

        AuthService authService = new AuthServiceImpl();
        UserProfileViewModelFactory factory = new UserProfileViewModelFactory(authService);
        mProfileViewModel = new ViewModelProvider(this, factory).get(UserProfileViewModel.class);

        binding.setViewModel(mProfileViewModel);
        binding.setLifecycleOwner(this);

        setObservers();

        mAlertDialogViewModel = new ViewModelProvider(this).get(AlertDialogViewModel.class);
    }

    private void setObservers() {
        mProfileViewModel.getNavigateToHome().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        mProfileViewModel.getOpenDatePickerDialog().observe(this, this::openDatePickerDialog);

        mProfileViewModel.getOpenCustomAlertDialog().observe(this, this::openCustomAlertDialog);
    }

    private void openDatePickerDialog(Calendar currentDate) {
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                mProfileViewModel.setBirthday(selectedDate.getTime());
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void openCustomAlertDialog(AlertDialogModel alertDialogModel) {
        mAlertDialogViewModel.setTitle("Update user!");
        mAlertDialogViewModel.setMessage("Update user!");
        mAlertDialogViewModel.setPositiveButton("Yes", aVoid -> {
            mProfileViewModel.getToastMessage().postValue("Yes");
        });
        mAlertDialogViewModel.setNegativeButton("No", aVoid -> {
            mProfileViewModel.getToastMessage().postValue("No");
        });
        mAlertDialogViewModel.setAlertDialogModel(alertDialogModel);
        AlertDialogFragment dialogFragment = new AlertDialogFragment(mAlertDialogViewModel);

        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "AlertDialogFragment");
    }
}