package com.example.user.login.github;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityGithubAuthBinding;
import com.example.home.HomeActivity;
import com.example.user.login.LoginActivity;

public class GithubAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityGithubAuthBinding activityGithubAuthBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_github_auth);
        GithubAuthViewModel viewModel = new GithubAuthViewModel(this);
        activityGithubAuthBinding.setViewModel(viewModel);
        activityGithubAuthBinding.executePendingBindings();

        viewModel.getNavigateToLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(GithubAuthActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        viewModel.getNavigateToHome().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(GithubAuthActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}