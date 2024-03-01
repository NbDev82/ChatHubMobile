package com.example.customcontrol;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.customcontrol.snackbar.CustomSnackbar;
import com.example.customcontrol.snackbar.SnackbarModel;
import com.example.infrastructure.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Date;

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

    @BindingAdapter("imageBase64")
    public static void setImageBase64(RoundedImageView imageView, String base64String) {
        Bitmap bitmap = Utils.decodeImage(base64String);;
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter("timeAgo")
    public static void setTimeAgo(TextView textView, Date date) {
        String timeAgo = Utils.calculateTimeAgo(date);
        textView.setText(timeAgo);
    }
}
