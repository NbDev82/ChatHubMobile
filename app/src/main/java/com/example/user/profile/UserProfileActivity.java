package com.example.user.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityUserProfileBinding;
import com.example.home.HomeActivity;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

import java.util.Calendar;
import java.util.Date;

public class UserProfileActivity extends AppCompatActivity {

    private UserProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

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

    private void openDatePickerDialog(Calendar currentDate) {
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                viewModel.setBirthday(selectedDate.getTime());
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
}