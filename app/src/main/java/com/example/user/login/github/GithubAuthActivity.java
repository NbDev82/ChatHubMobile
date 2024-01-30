package com.example.user.login.github;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityGithubAuthBinding;

public class GithubAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityGithubAuthBinding activityGithubAuthBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_github_auth);
        GithubAuthViewModel viewModel = new GithubAuthViewModel(this);
        activityGithubAuthBinding.setViewModel(viewModel);
        activityGithubAuthBinding.executePendingBindings();
    }
}