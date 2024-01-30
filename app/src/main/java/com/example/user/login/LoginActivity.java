package com.example.user.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.example.R;
import com.example.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding activityLoginBinding
                = DataBindingUtil.setContentView(
                this, R.layout.activity_login);
        LoginViewModel viewModel = new LoginViewModel( getApplicationContext() );
        activityLoginBinding.setViewModel(viewModel);
        activityLoginBinding.executePendingBindings();
    }
}