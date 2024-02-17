package com.example.customcontrol.snackbar;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.example.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.logging.Handler;

public class CustomSnackbar {
    public static void show(Activity activity, SnackbarModel model) {
        Snackbar.Callback callback = getSnackbarCallback(model);

        View view = activity.getWindow().getDecorView().getRootView();
        Snackbar snackbar = Snackbar.make(view, model.getMessage(), model.getDuration())
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                .setBackgroundTint(ContextCompat.getColor(activity, R.color.color_toast_background))
                .addCallback(callback)
                .setActionTextColor(ContextCompat.getColor(activity, R.color.color_button_tertiary_highlight_text))
                .setAction(model.getActionText(), v -> {
                    if (model.getCustomAction() != null) {
                        model.getCustomAction().accept(null);
                    }
                });

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = getNavigationBarHeight(activity);
        snackbarView.setLayoutParams(params);

        snackbar.show();
    }

    private static Snackbar.Callback getSnackbarCallback(SnackbarModel model) {
        return new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (model.getOnDismissedAction() != null) {
                    model.getOnDismissedAction().accept(event);
                }
            }

            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
                if (model.getOnShownAction() != null) {
                    model.getOnShownAction().accept(null);
                }
            }
        };
    }

    private static int getNavigationBarHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            int usableHeight = displayMetrics.heightPixels;
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            int realHeight = displayMetrics.heightPixels;

            if (realHeight > usableHeight) {
                return realHeight - usableHeight;
            }
        }
        return 0;
    }
}
