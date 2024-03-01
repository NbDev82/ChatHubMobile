package com.example.customcontrol.phonecredential;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.R;
import com.example.customcontrol.CustomToast;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.Validator;
import com.example.user.repository.UserRepos;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneCredentialDialogFragment extends DialogFragment {
    public static final String TAG = PhoneCredentialDialogFragment.class.getSimpleName();

    private LinearLayout inputPhoneNumberLl;
    private AutoCompleteTextView countryCodeActv;
    private TextInputLayout localNumberTil;
    private TextInputEditText localNumberEdt;
    private MaterialButton sendOtpBtn;
    private ProgressBar sendOtpPgb;

    private LinearLayout verifyOtpLl;
    private TextInputEditText otpEdt;
    private TextView resendTxv;
    private MaterialButton verifyBtn;
    private ProgressBar verifyPgb;

    private final UserRepos userRepos;
    private final AuthRepos authRepos;
    private final PhoneCredentialDialogModel model;
    private long timeoutSeconds = Utils.OTP_TIME_OUT_SECONDS;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    public PhoneCredentialDialogFragment(UserRepos userRepos,
                                         AuthRepos authRepos,
                                         PhoneCredentialDialogModel model) {
        super();
        this.userRepos = userRepos;
        this.authRepos = authRepos;
        this.model = model;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.phone_credential_dialog, null);

        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initializeViews(view);
        setupPhoneNumberInput();
        setupVerifyOtp();

        return dialog;
    }

    public void initializeViews(View view) {
        inputPhoneNumberLl = view.findViewById(R.id.inputPhoneNumberLl);
        countryCodeActv = view.findViewById(R.id.countryCodeActv);
        localNumberTil = view.findViewById(R.id.localNumberTil);
        localNumberEdt = view.findViewById(R.id.localNumberEdt);
        sendOtpBtn = view.findViewById(R.id.sendOtpBtn);
        sendOtpPgb = view.findViewById(R.id.sendOtpPgb);

        verifyOtpLl = view.findViewById(R.id.verifyOtpLl);
        otpEdt = view.findViewById(R.id.otpEdt);
        resendTxv = view.findViewById(R.id.resendTxv);
        verifyBtn = view.findViewById(R.id.verifyBtn);
        verifyPgb = view.findViewById(R.id.verifyPgb);

        inputPhoneNumberLl.setVisibility(View.VISIBLE);
        verifyOtpLl.setVisibility(View.GONE);
    }

    private void setupPhoneNumberInput() {
        countryCodeActv.setText("+84");

        localNumberEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPhoneNumber(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendOtpBtn.setOnClickListener(v -> {
            verifyPhoneNumberAndSendOtp();
        });
    }

    public void verifyPhoneNumberAndSendOtp() {
        setPhoneNumberInputInProgress(true);
        String phoneNumber = getFullPhoneNumber();
        userRepos.existsByPhoneNumber(phoneNumber)
                .addOnSuccessListener(isExists -> {
                    setPhoneNumberInputInProgress(false);
                    if (isExists) {
                        localNumberTil.setError("This phone number is already in use");
                    } else {
                        sendOtp(phoneNumber, false);

                        inputPhoneNumberLl.setVisibility(View.GONE);
                        verifyOtpLl.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    setPhoneNumberInputInProgress(false);
                    CustomToast.showErrorToast(requireActivity(), "Verify phone number unsuccessfully");
                    Log.e(TAG, "Error: ", e);
                });
    }

    private String getFullPhoneNumber() {
        String countryCode = countryCodeActv.getText().toString();
        String number = localNumberEdt.getText().toString();
        return Utils.getFullPhoneNumber(countryCode, number);
    }

    public void checkPhoneNumber(CharSequence s) {
        String countryCode = countryCodeActv.getText().toString();
        String localNumber = s.toString();
        String phoneNumber = Utils.getFullPhoneNumber(countryCode, localNumber);
        String error = Validator.validPhoneNumber(phoneNumber);
        localNumberTil.setError(error);
    }

    private void setupVerifyOtp() {
        resendTxv.setOnClickListener(v -> {
            resendOtp();
        });

        verifyBtn.setOnClickListener(v -> verifyOtp());
    }

    public void resendOtp() {
        String phoneNumber = getFullPhoneNumber();
        sendOtp(phoneNumber, true);
    }

    private void sendOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
        resendTxv.setEnabled(false);
        authRepos.sendOtp(requireActivity(),
                phoneNumber,
                isResend,
                resendingToken,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        applyCredentialAndDismiss(phoneAuthCredential);
                        setVerifyOtpInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        CustomToast.showErrorToast(requireActivity(), "OTP verification failed");
                        setVerifyOtpInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        resendingToken = forceResendingToken;
                        CustomToast.showSuccessToast(requireActivity(), "OTP sent successfully");
                        setVerifyOtpInProgress(false);
                    }
                });
    }

    public void verifyOtp() {
        String enteredOtp = otpEdt.getText().toString();
        try {
            PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(verificationId, enteredOtp);
            applyCredentialAndDismiss(phoneCredential);
        } catch (IllegalArgumentException e) {
            CustomToast.showErrorToast(requireActivity(), "Failed to verify OTP");
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
    }

    private void applyCredentialAndDismiss(PhoneAuthCredential phoneCredential) {
        if (model.getVerifyButtonClickListener() != null) {
            model.getVerifyButtonClickListener().apply(phoneCredential);
        }
        dismiss();
    }

    private void startResendTimer() {
        CountDownTimer timer = new CountDownTimer(Utils.OTP_TIME_OUT_SECONDS * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeoutSeconds = (int) (millisUntilFinished / 1000);
                String content = String.format("Resend OTP in " + timeoutSeconds + " seconds");
                resendTxv.setText(content);
            }

            @Override
            public void onFinish() {
                timeoutSeconds = Utils.OTP_TIME_OUT_SECONDS;
                resendTxv.setText("Resend");
                resendTxv.setEnabled(true);
            }
        };
        timer.start();
    }

    private void setPhoneNumberInputInProgress(boolean inProgress) {
        if (inProgress) {
            sendOtpBtn.setVisibility(View.GONE);
            sendOtpPgb.setVisibility(View.VISIBLE);
        } else {
            sendOtpBtn.setVisibility(View.VISIBLE);
            sendOtpPgb.setVisibility(View.GONE);
        }
    }

    private void setVerifyOtpInProgress(boolean inProgress) {
        if (inProgress) {
            verifyBtn.setVisibility(View.GONE);
            verifyPgb.setVisibility(View.VISIBLE);
        } else {
            verifyBtn.setVisibility(View.VISIBLE);
            verifyPgb.setVisibility(View.GONE);
        }
    }
}
