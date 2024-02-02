package com.example.infrastructure;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.res.ResourcesCompat;

import com.example.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";

    private static final String TAG = Utils.class.getSimpleName();
    private static final String DATE_PATTERN = "dd/MM/yyyy";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable bg = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_color_primary, activity.getTheme());
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(bg);
        }
    }

    public static String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        return df.format(date);
    }

    public static Date stringToDate(String dateStr) {
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            Log.e(TAG, "ERROR: " + e);
            return null;
        }
    }
}
