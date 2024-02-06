package com.example;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.function.Consumer;

public class CustomToast {
    public static void showToastMessage(Activity activity,
                                         String message,
                                         Consumer<ImageView> customIcon) {
        showToastMessage(activity, customIcon, messageTxv -> messageTxv.setText(message));
    }

    public static void showToastMessage(Activity activity,
                                         Consumer<ImageView> customIcon,
                                         Consumer<TextView> customMessage) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_message,
                activity.findViewById(R.id.toastLayoutRoot));

        ImageView icon = layout.findViewById(R.id.icon);
        customIcon.accept(icon);

        TextView messageTxv = layout.findViewById(R.id.messageTxv);
        customMessage.accept(messageTxv);

        Toast toast = new Toast(activity.getApplication());
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setMargin(0, 0.05f);
        toast.show();
    }
}
