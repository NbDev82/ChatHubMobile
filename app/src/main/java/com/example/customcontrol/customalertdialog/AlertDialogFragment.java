package com.example.customcontrol.customalertdialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.R;
import com.google.android.material.button.MaterialButton;

import java.util.function.Consumer;

public class AlertDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = AlertDialogFragment.class.getSimpleName();

    private final AlertDialogModel model;
    private TextView titleTxv;
    private TextView messageTxv;
    private MaterialButton positiveBtn;
    private MaterialButton negativeBtn;

    public AlertDialogFragment(AlertDialogModel model) {
        super();
        this.model = model;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_alert_dialog, null);

        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initializeViews(view);
        setupContents();

        return dialog;
    }

    private void initializeViews(View view) {
        titleTxv = view.findViewById(R.id.titleTxv);
        messageTxv = view.findViewById(R.id.messageTxv);
        positiveBtn = view.findViewById(R.id.positiveBtn);
        negativeBtn = view.findViewById(R.id.negativeBtn);
    }

    private void setupContents() {
        titleTxv.setText( model.getTitle() );
        messageTxv.setText( model.getMessage() );
        setupButton(positiveBtn, model.getPositiveBtnTitle(), model.getPositiveButtonClickListener());
        setupButton(negativeBtn, model.getNegativeBtnTitle(), model.getNegativeButtonClickListener());
    }

    private void setupButton(Button button, String title, Consumer<Void> clickListener) {
        if (title != null && clickListener != null) {
            button.setVisibility(View.VISIBLE);
            button.setText(title);
            button.setOnClickListener(v -> {
                clickListener.accept(null);
                dismiss();
            });
        }
    }
}
