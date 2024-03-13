package com.example.user.login.otp.verify;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityVerifyOtpBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.EUserField;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.Timer;
import java.util.TimerTask;

public class VerifyOtpActivity extends BaseActivity<VerifyOtpViewModel, ActivityVerifyOtpBinding> {

    private AuthRepos authRepos;

    @Override
    protected int getLayout() {
        return R.layout.activity_verify_otp;
    }

    @Override
    protected Class<VerifyOtpViewModel> getViewModelClass() {
        return VerifyOtpViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        authRepos = new AuthReposImpl(userRepos);
        return new VerifyOtpViewModelFactory(authRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String phoneNumber = extras.getString(EUserField.PHONE_NUMBER.getName(), "");
            viewModel.setPhoneNumber(phoneNumber);
            sendOtp(phoneNumber, false);
        }

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