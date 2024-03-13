package com.example.user.changepassword;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityChangePasswordBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class ChangePasswordActivity extends BaseActivity<ChangePasswordViewModel, ActivityChangePasswordBinding> {

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_change_password;
    }

    @Override
    protected Class<ChangePasswordViewModel> getViewModelClass() {
        return ChangePasswordViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new ChangePasswordViewModelFactory(authRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToAccountLinking().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToAccountLinking(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });
    }
}