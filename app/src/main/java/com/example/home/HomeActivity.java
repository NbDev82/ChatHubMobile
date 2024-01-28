package com.example.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActivityHomeBinding activityHomeBinding
                = DataBindingUtil.setContentView(
                        this, R.layout.activity_home);
        HomeViewModel viewModel = new HomeViewModel( getApplicationContext() );
        activityHomeBinding.setViewModel(viewModel);
        activityHomeBinding.executePendingBindings();
    }
}