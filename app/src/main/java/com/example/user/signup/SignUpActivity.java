package com.example.user.signup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.R;
import com.example.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySignUpBinding activitySignUpBinding
                = DataBindingUtil.setContentView(
                this, R.layout.activity_sign_up);
        activitySignUpBinding.setViewModel(new SignUpViewModel());
        activitySignUpBinding.executePendingBindings();
    }
}
