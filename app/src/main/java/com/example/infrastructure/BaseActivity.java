package com.example.infrastructure;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.BR;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.passcode.unlockapp.UnlockAppViewModel;

public abstract class BaseActivity<VM extends BaseViewModel, B extends ViewDataBinding> extends AppCompatActivity {

    protected VM viewModel;
    protected B binding;
    protected NavigationManager navigationManager;

    protected abstract @LayoutRes int getLayout();

    protected abstract Class<VM> getViewModelClass();

    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);
        init();

        viewModel.onCreate();
    }

    protected void init() {
        ViewModelProvider.Factory factory = getViewModelFactory();
        Class<VM> vmClass = getViewModelClass();
        viewModel = factory == null
                ? new ViewModelProvider(this).get(vmClass)
                : new ViewModelProvider(this, factory).get(vmClass);

        binding = DataBindingUtil.setContentView(this, getLayout());
        binding.setLifecycleOwner(this);
        binding.setVariable(BR.viewModel, viewModel);

        navigationManager = new NavigationManagerImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
