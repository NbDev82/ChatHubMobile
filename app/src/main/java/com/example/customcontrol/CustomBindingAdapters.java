package com.example.customcontrol;

import android.app.Activity;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

import com.example.R;
import com.example.customcontrol.snackbar.CustomSnackbar;
import com.example.customcontrol.snackbar.SnackbarModel;

public class CustomBindingAdapters {
    @BindingAdapter({"successToastMessage"})
    public static void showSuccessToast(View view, String message) {
        if (message != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            CustomToast.showSuccessToast(activity, message);
        }
    }

    @BindingAdapter({"errorToastMessage"})
    public static void showErrorToast(View view, String message) {
        if (message != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            CustomToast.showErrorToast(activity, message);
        }
    }

    @BindingAdapter("customSnackbar")
    public static void showCustomSnackbar(View view, SnackbarModel model) {
        if (model != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            CustomSnackbar.show(activity, model);
        }
    }
}
