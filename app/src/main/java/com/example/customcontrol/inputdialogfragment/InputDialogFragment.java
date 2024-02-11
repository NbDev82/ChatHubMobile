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
    private TextView titleTxv;
    private TextInputLayout textTil;
    private TextInputEditText textEdt;
    private MaterialButton submitTextBtn;

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
        titleTxv = view.findViewById(R.id.titleTxv);
        textTil = view.findViewById(R.id.textTil);
        textEdt = view.findViewById(R.id.textEdt);
        submitTextBtn = view.findViewById(R.id.submitTextBtn);

        titleTxv.setText( model.getTitle() );
        textEdt.setText( model.getCurContent() );

        switch (model.getType()) {
            case EMAIL:
                textTil.setStartIconDrawable(R.drawable.ic_mail);
                textEdt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
        }
    }

    private void setupEvents() {
        submitTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = textEdt.getText();
                String textValue = text != null ? text.toString() : "";
                model.getSubmitButtonClickListener().accept(textValue);
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (model.getDismissListener() != null) {
            model.getDismissListener().onDialogDismissed();
        }
    }
}
