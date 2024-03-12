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

public abstract class BaseActivity<VM extends BaseViewModel, B extends ViewDataBinding> extends AppCompatActivity {

    protected VM viewModel;
    protected B binding;
    protected NavigationManager navigationManager;

    protected abstract @LayoutRes int getLayout();

    protected abstract Class<VM> getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
        viewModel = new ViewModelProvider(this).get(getViewModel());
        binding.setLifecycleOwner(this);
        binding.setVariable(BR.viewModel, viewModel);

        navigationManager = new NavigationManagerImpl(this);
    }
}
