package com.example.customcontrol.inputdialogfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class InputDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = InputDialogFragment.class.getSimpleName();

    private final InputDialogModel model;
    private TextView titleTxv;
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

        titleTxv = view.findViewById(R.id.titleTxv);
        textEdt = view.findViewById(R.id.textEdt);
        submitTextBtn = view.findViewById(R.id.submitTextBtn);

        titleTxv.setText( model.getTitle() );
        textEdt.setText( model.getCurContent() );
        submitTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = textEdt.getText();
                String textValue = text != null ? text.toString() : "";
                model.getSubmitButtonClickListener().accept(textValue);
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
