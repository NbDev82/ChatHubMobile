package com.example.user.login.otp.verify;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.ActivityVerifyOtpBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.EUserField;
import com.example.user.login.otp.send.SendOtpActivity;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;

public class VerifyOtpActivity extends AppCompatActivity {

    public static final long TIME_OUT_SECONDS = 60L;

    private NavigationManager navigationManager;
    private AuthService authService;
    private VerifyOtpViewModel viewModel;
    private static long timeoutSeconds = TIME_OUT_SECONDS;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        authService = new AuthServiceImpl();
        VerifyOtpViewModelFactory factory = new VerifyOtpViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(VerifyOtpViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String phoneNumber = extras.getString(EUserField.PHONE_NUMBER.getName(), "");
            viewModel.setPhoneNumber(phoneNumber);
        }

        ActivityVerifyOtpBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToSignUp().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSignUp(EAnimationType.FADE_IN);
            }
        });
    }

    private void onButtonClick() {
        String enteredOtp = "";
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
        signIn(credential);
    }

    void startResendTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                setResendText(String.format("Resend OTP in " + timeoutSeconds + " seconds"));
                if (timeoutSeconds <= 0) {
                    timeoutSeconds = TIME_OUT_SECONDS;
                    timer.cancel();
                    runOnUiThread(() -> {
                        setResendTextStatus(true);
                    });
                }
            }
        }, 0, 1000);
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        authService.signInWithPhoneCredential(phoneAuthCredential, aVoid -> {

        }, e -> {
            Log.e(SendOtpActivity.class.getSimpleName(), "Error: ", e);
        });
    }

    void setResendText(String text) {
    }

    void setResendTextStatus(boolean enabled) {

    }

    void setInProgress(boolean isProgress) {
    }
}