package com.example.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.customcontrol.CustomToast;
import com.example.databinding.FragmentSettingsBinding;
import com.example.infrastructure.BaseFragment;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class SettingsFragment extends BaseFragment<SettingsViewModel, FragmentSettingsBinding> {

    @Override
    protected Class<SettingsViewModel> getViewModelClass() {
        return SettingsViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new SettingsViewModelFactory(authRepos);
    }

    @Override
    protected FragmentSettingsBinding getViewDataBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSettingsBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToUserProfile().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToUserProfile(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToChangePhoneNumber().observe(requireActivity(), navigate -> {
            if (navigate) {
                showNotImplementToast();
            }
        });

        viewModel.getNavigateToChangeEmail().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToEmailDetails(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToAccountLinking().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToAccountLinking(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToMyQrCode().observe(requireActivity(), navigate -> {
            if (navigate) {
                showNotImplementToast();
            }
        });

        viewModel.getNavigateToLockApp().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToLockApp(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToChangePassword().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToChangePassword(EAnimationType.FADE_IN);
            }
        });
    }

    private void showNotImplementToast() {
        CustomToast.showErrorToast(requireActivity(), "Without implementation");
    }
}