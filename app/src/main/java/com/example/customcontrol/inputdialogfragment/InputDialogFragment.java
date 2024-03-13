package com.example.customcontrol.inputdialogfragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InputDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = InputDialogFragment.class.getSimpleName();

    private final InputDialogModel model;
    private TextView txvTitle;
    private TextInputLayout tilText;
    private TextInputEditText edtText;
    private MaterialButton btnSubmit;

    public InputDialogFragment(InputDialogModel model) {
        super();
        this.model = model;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_input_dialog, null);

        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initializeViews(view);
        setupEvents();

        return dialog;
    }

    private void initializeViews(View view) {
        txvTitle = view.findViewById(R.id.txv_title);
        tilText = view.findViewById(R.id.til_text);
        edtText = view.findViewById(R.id.edt_text);
        btnSubmit = view.findViewById(R.id.btn_submit);

        txvTitle.setText(model.getTitle());
        edtText.setText(model.getCurContent());

        switch (model.getType()) {
            case EMAIL:
                tilText.setStartIconDrawable(R.drawable.ic_mail);
                edtText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
        }
    }

    private void setupEvents() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = edtText.getText();
                String textValue = text != null ? text.toString() : "";
                model.getSubmitButtonClickListener().accept(textValue);
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        setCancelable(model.isCancelable());
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (model.getDismissListener() != null) {
            model.getDismissListener().onDialogDismissed();
        }
    }
}
