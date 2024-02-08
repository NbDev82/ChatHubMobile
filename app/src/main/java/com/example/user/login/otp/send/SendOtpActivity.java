package com.example.user.login.otp.send;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.example.R;
import com.example.customcontrol.CustomToast;
import com.example.databinding.ActivitySendOtpBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.otp.verify.VerifyOtpActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOtpActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private AuthService authService;
    private SendOtpViewModel viewModel;
    private String verificationCode;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        authService = new AuthServiceImpl();
        SendOtpViewModelFactory factory = new SendOtpViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(SendOtpViewModel.class);

        ActivitySendOtpBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_send_otp);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToVerifyOtpWithPhoneNumber().observe(this, data -> {
            navigationManager.navigateToVerifyOtp(data, EAnimationType.FADE_IN);
        });

        viewModel.getNavigateToSignUp().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSignUp(EAnimationType.FADE_IN);
            }
        });
    }

    private void sendOtp(String phoneNumber, boolean isResend) {
        setInProgress(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions
                .newBuilder( authService.getFirebaseAuth() )
                .setPhoneNumber(phoneNumber)
                .setTimeout(VerifyOtpActivity.TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        CustomToast.showErrorToast(SendOtpActivity.this, "OTP verification failed");
                        setInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        CustomToast.showSuccessToast(SendOtpActivity.this, "OTP sent successfully");
                        setInProgress(false);
                    }
                });
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder
                    .setForceResendingToken(resendingToken)
                    .build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        authService.signInWithPhoneCredential(phoneAuthCredential, aVoid -> {

        }, e -> {
            Log.e(SendOtpActivity.class.getSimpleName(), "Error: ", e);
        });
    }

    void setInProgress(boolean isProgress) {
    }
}