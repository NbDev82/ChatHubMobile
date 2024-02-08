package com.example.customcontrol.emailpassworddialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.R;
import com.example.user.Validator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EmailPasswordDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = EmailPasswordDialogFragment.class.getSimpleName();

    private final EmailPasswordDialogModel model;
    private TextView titleTxv;
    private TextView subTitleTxv;
    private TextInputLayout emailTIL;
    private TextInputEditText emailEdt;
    private TextInputLayout passwordTIL;
    private TextInputEditText passwordEdt;
    private MaterialButton submitBtn;

    public EmailPasswordDialogFragment(EmailPasswordDialogModel model) {
        super();
        this.model = model;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.email_password_dialog, null);

        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        titleTxv = view.findViewById(R.id.titleTxv);
        subTitleTxv = view.findViewById(R.id.subTitleTxv);
        emailTIL = view.findViewById(R.id.emailTIL);
        emailEdt = view.findViewById(R.id.emailEdt);
        passwordTIL = view.findViewById(R.id.passwordTIL);
        passwordEdt = view.findViewById(R.id.passwordEdt);
        submitBtn = view.findViewById(R.id.submitBtn);

        titleTxv.setText(model.getTitle());
        subTitleTxv.setText(model.getSubTitle());
        emailEdt.setText(model.getEmail());
        emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString();
                String error = Validator.validateEmail(email);
                if (error != null) {
                    emailTIL.setError(error);
                    return;
                }
                emailTIL.setError(null);

                Log.i(TAG, "onTextChanged (email): " + email);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEdt.setText(model.getPassword());
        passwordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                String error = Validator.validatePassword(password);
                if (error != null) {
                    passwordTIL.setError(error);
                    return;
                }
                passwordTIL.setError(null);

                Log.i(TAG, "onTextChanged (password): " + password);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence emailText = emailEdt.getText();
                CharSequence passwordText = passwordEdt.getText();

                String email = emailText != null ? emailText.toString() : "";
                String password = passwordText != null ? passwordText.toString() : "";

                model.getSubmitButtonClickListener().apply(email, password);
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
