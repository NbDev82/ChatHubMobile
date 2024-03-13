package com.example.customcontrol;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;

import java.util.function.Consumer;

public class CustomToast {

    private static final int SHORT_MESSAGE_LENGTH_THRESHOLD = 20;

    public static void showSuccessToast(Activity activity, String message) {
        CustomToast.showToastMessage(activity, message, icon -> {
            icon.setBackground(ResourcesCompat.getDrawable(activity.getResources(),
                    R.drawable.ic_circle_check_solid,
                    activity.getTheme()));
        });
    }

    public static void showErrorToast(Activity activity, String message) {
        CustomToast.showToastMessage(activity, message, icon -> {
            icon.setBackground(ResourcesCompat.getDrawable(activity.getResources(),
                    R.drawable.ic_circle_xmark_solid,
                    activity.getTheme()));
        });
    }

    public static void showToastMessage(Activity activity,
                                        String message,
                                        Consumer<ImageView> customIcon) {
        int duration = getDurationByMessageLength(message);
        showToastMessage(activity, duration, customIcon, messageTxv -> messageTxv.setText(message));
    }

    public static void showToastMessage(Activity activity,
                                        @BaseTransientBottomBar.Duration int duration,
                                        Consumer<ImageView> customIcon,
                                        Consumer<TextView> customMessage) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_message,
                activity.findViewById(R.id.toastLayoutRoot));

        ImageView icon = layout.findViewById(R.id.icon);
        customIcon.accept(icon);

        TextView txvMessage = layout.findViewById(R.id.txv_message);
        customMessage.accept(txvMessage);

        Toast toast = new Toast(activity.getApplication());
        toast.setView(layout);
        toast.setDuration(duration);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setMargin(0, 0.05f);
        toast.show();
    }

    private static @BaseTransientBottomBar.Duration int getDurationByMessageLength(String message) {
        if (isShortMessage(message)) {
            return Toast.LENGTH_SHORT;
        } else {
            return Toast.LENGTH_LONG;
        }
    }

    private static boolean isShortMessage(String message) {
        return message.length() <= SHORT_MESSAGE_LENGTH_THRESHOLD;
    }
}
