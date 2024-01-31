package com.example.user.login.github;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityGithubAuthBinding;
import com.example.home.HomeActivity;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginActivity;

public class GithubAuthActivity extends AppCompatActivity {

    private GithubAuthViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityGithubAuthBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_github_auth);

        AuthService authService = new AuthServiceImpl();
        GithubAuthViewModelFactory factory = new GithubAuthViewModelFactory(this, authService);
        mViewModel = new ViewModelProvider(this, factory).get(GithubAuthViewModel.class);

        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);

        setObservers();
    }

    private void setObservers() {
        mViewModel.getNavigateToLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(GithubAuthActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        mViewModel.getNavigateToHome().observe(this, new Observer<Boolean>() {
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