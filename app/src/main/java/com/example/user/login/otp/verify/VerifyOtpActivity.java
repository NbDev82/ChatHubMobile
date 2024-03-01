package com.example.user.login.otp.verify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityVerifyOtpBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.EUserField;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.Timer;
import java.util.TimerTask;

public class VerifyOtpActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private AuthRepos authRepos;
    private VerifyOtpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        UserRepos userRepos = new UserReposImpl();
        authRepos = new AuthReposImpl(userRepos);
        VerifyOtpViewModelFactory factory = new VerifyOtpViewModelFactory(authRepos);
        viewModel = new ViewModelProvider(this, factory).get(VerifyOtpViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String phoneNumber = extras.getString(EUserField.PHONE_NUMBER.getName(), "");
            viewModel.setPhoneNumber(phoneNumber);
            sendOtp(phoneNumber, false);
        }

        ActivityVerifyOtpBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getResendOtp().observe(this, phoneNumber -> sendOtp(phoneNumber, true));

        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToSignUp().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSignUp(EAnimationType.FADE_IN);
            }
        });
    }

    private void sendOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
        viewModel.setResendContentStatus(false);

        authRepos.sendOtp(this,
                phoneNumber,
                isResend,
                viewModel.getResendingToken(),
                viewModel.getOnVerificationStateChangedCallbacks());
    }

    private void startResendTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long timeoutSeconds = viewModel.getTimeoutSeconds();
                timeoutSeconds--;
                viewModel.setTimeoutSeconds(timeoutSeconds);
                String content = String.format("Resend OTP in " + timeoutSeconds + " seconds");
                viewModel.setResendContent(content);
                if (timeoutSeconds <= 0) {
                    viewModel.resetTimeoutSeconds();
                    timer.cancel();
                    runOnUiThread(() -> {
                        viewModel.setResendContent("Resend");
                        viewModel.setResendContentStatus(true);
                    });
                }
            }
        }, 0, 1000);
    }
}