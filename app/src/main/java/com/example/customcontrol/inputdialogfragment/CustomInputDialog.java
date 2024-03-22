package com.example.customcontrol.inputdialogfragment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customcontrol.customalertdialog.AlertDialogFragment;

public class CustomInputDialog {
    public static void show(AppCompatActivity activity, InputDialogModel model) {
        InputDialogFragment dialog = new InputDialogFragment(model);
        dialog.show(activity.getSupportFragmentManager(), AlertDialogFragment.TAG);
    }
}
