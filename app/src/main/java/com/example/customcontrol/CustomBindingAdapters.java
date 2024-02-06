package com.example.customcontrol;

import android.app.Activity;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

import com.example.R;
import com.example.CustomToast;

public class CustomBindingAdapters {
    @BindingAdapter({"successToastMessage"})
    public static void showSuccessToast(View view, String message) {
        if (message != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            CustomToast.showToastMessage(activity, message, icon -> {
                icon.setBackground(ResourcesCompat.getDrawable(activity.getResources(),
                        R.drawable.ic_circle_check_solid,
                        activity.getTheme()));
            });
        }
    }

    @BindingAdapter({"errorToastMessage"})
    public static void showErrorToast(View view, String message) {
        if (message != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            CustomToast.showToastMessage(activity, message, icon -> {
                icon.setBackground(ResourcesCompat.getDrawable(activity.getResources(),
                        R.drawable.ic_circle_xmark_solid,
                        activity.getTheme()));
            });
        }
    }
}
