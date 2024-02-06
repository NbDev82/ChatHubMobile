package com.example.infrastructure;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.res.ResourcesCompat;

import com.example.R;

import java.io.ByteArrayOutputStream;
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

    public static boolean compareDateWithDateStr(Date date, String dateStr) {
        String tempDateStr = dateToString(date);
        return dateStr.equals(tempDateStr);
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }
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

    public static String encodeImage(Bitmap bitmap) {
        int previewWith = 150;
        int previewHeight = bitmap.getHeight() * previewWith / bitmap.getWidth();
        Bitmap prevewBitmap = Bitmap.createScaledBitmap(bitmap, previewWith, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        prevewBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
