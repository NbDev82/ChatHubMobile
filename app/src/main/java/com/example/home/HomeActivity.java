package com.example.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityHomeBinding;
import com.example.user.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActivityHomeBinding activityHomeBinding
                = DataBindingUtil.setContentView(
                        this, R.layout.activity_home);
        HomeViewModel viewModel = new HomeViewModel();
        activityHomeBinding.setViewModel(viewModel);
        activityHomeBinding.executePendingBindings();

        viewModel.getNavigateToLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}