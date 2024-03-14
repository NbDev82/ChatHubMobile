package com.example.infrastructure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.BR;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;

public abstract class BaseFragment<VM extends BaseViewModel, B extends ViewDataBinding> extends Fragment {

    protected VM viewModel;
    protected B binding;
    protected NavigationManager navigationManager;

    protected abstract B getViewDataBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract Class<VM> getViewModelClass();

    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewDataBinding(inflater, container);
        init();
        viewModel.onCreate();
        return binding.getRoot();
    }

    protected void init() {
        ViewModelProvider.Factory factory = getViewModelFactory();
        Class<VM> vmClass = getViewModelClass();
        viewModel = factory == null
                ? new ViewModelProvider(this).get(vmClass)
                : new ViewModelProvider(this, factory).get(vmClass);

        binding.setLifecycleOwner(this);
        binding.setVariable(BR.viewModel, viewModel);

        navigationManager = new NavigationManagerImpl(requireActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}