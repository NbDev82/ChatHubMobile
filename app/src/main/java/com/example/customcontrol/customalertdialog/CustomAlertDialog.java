package com.example.customcontrol.customalertdialog;

import androidx.appcompat.app.AppCompatActivity;

public class CustomAlertDialog {
    public static void show(AppCompatActivity activity, AlertDialogModel model) {
        AlertDialogFragment dialog = new AlertDialogFragment(model);
        dialog.show(activity.getSupportFragmentManager(), AlertDialogFragment.TAG);
    }
}
