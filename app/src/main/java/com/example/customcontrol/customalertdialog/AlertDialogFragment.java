package com.example.customcontrol.customalertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.R;

public class AlertDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = AlertDialogFragment.class.getSimpleName();

    private AlertDialogModel model;
    private TextView titleTxv;
    private TextView messageTxv;
    private Button positiveBtn;
    private Button negativeBtn;

    public AlertDialogFragment(AlertDialogModel model) {
        super();
        this.model = model;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_alert_dialog, null);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        titleTxv = view.findViewById(R.id.titleTxv);
        messageTxv = view.findViewById(R.id.messageTxv);
        positiveBtn = view.findViewById(R.id.positiveBtn);
        negativeBtn = view.findViewById(R.id.negativeBtn);

        titleTxv.setText( model.getTitle() );
        messageTxv.setText( model.getMessage() );
        positiveBtn.setText( model.getPositiveBtnTitle() );
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getPositiveButtonClickListener().accept(null);
                dialog.dismiss();
            }
        });
        negativeBtn.setText( model.getNegativeBtnTitle() );
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getNegativeButtonClickListener().accept(null);
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
