package com.example.infrastructure;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.example.R;
import com.marcoscg.materialtoast.MaterialToast;

public class CustomBindingAdapters {
    @BindingAdapter({"successToastMessage"})
    public static void showSuccessToast(View view, String message) {
        if (message != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            MaterialToast.makeText(activity, message, R.drawable.ic_circle_check_solid, Toast.LENGTH_LONG)
                    .setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.toast_background))
                    .show();
        }
    }

    @BindingAdapter({"errorToastMessage"})
    public static void showErrorToast(View view, String message) {
        if (message != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            MaterialToast.makeText(activity, message, R.drawable.ic_circle_xmark_solid, Toast.LENGTH_LONG)
                    .setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.toast_background))
                    .show();
        }
    }
}
