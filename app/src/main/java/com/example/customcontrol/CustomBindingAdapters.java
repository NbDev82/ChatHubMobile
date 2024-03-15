package com.example.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.chaos.view.PinView;
import com.example.customcontrol.snackbar.CustomSnackbar;
import com.example.customcontrol.snackbar.SnackbarModel;
import com.example.infrastructure.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Date;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

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
        Bitmap bitmap = Utils.decodeImage(base64String);
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter("timeAgo")
    public static void setTimeAgo(TextView textView, Date date) {
        String timeAgo = Utils.calculateTimeAgo(date);
        textView.setText(timeAgo);
    }

    @BindingAdapter("animation")
    public static void setAnimation(PinView pinView, boolean animation) {
        if (animation) {
            pinView.setAnimationEnable(true);
        }
    }

    @BindingAdapter("app:showKeyboardIfTrue")
    public static void autoShowKeyboard(View view, boolean isKeyboardVisible) {
        if (isKeyboardVisible) {
            showKeyboard(view);
        } else {
            hideKeyboard(view);
        }
    }

    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(RecyclerView recyclerView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        }
    }

    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(GridView gridView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(gridView);
        }
    }

    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(ListView listView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(listView);
        }
    }

    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(ScrollView scrollView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        }
    }

    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(HorizontalScrollView horizontalScrollView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(horizontalScrollView);
        }
    }

    @BindingAdapter("setupStaticOverScroll")
    public static void setupStaticOverScroll(View view, int orientation) {
        if (orientation == OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL) {
            OverScrollDecoratorHelper.setUpStaticOverScroll(view, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        } else if (orientation == OverScrollDecoratorHelper.ORIENTATION_VERTICAL) {
            OverScrollDecoratorHelper.setUpStaticOverScroll(view, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        }
    }

    private static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
