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
import com.example.friend.FriendRequest;
import com.example.friend.profileviewer.EFriendshipStatus;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Utils {
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_PASSCODE = "passcode";
    public static final String KEY_FINGERPRINT_UNLOCK_ENABLED = "fingerprintUnlockEnabled";
    public static final String KEY_AUTO_LOCK_TIME = "autoLockTime";

    public static final int PASSCODE_DIGIT_COUNT = 4;
    public static final int PIN_DIGIT_COUNT = 6;

    public static long OTP_TIME_OUT_SECONDS = 60L;
    public static final String EXTRA_SELECTED_USER_ID = "selectedUserId";
    public static final String EXTRA_SELECTED_FRIEND_REQUEST_ID = "selectedFriendRequestId";
    public static final String EXTRA_PASSCODE_SET_SUCCESS = "newPasscodeSetSuccess";

    private static final String TAG = Utils.class.getSimpleName();
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final int DAYS_IN_MONTH = 30;

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

    public static String getFullPhoneNumber(@Nullable String countryCode,
                                            @Nullable String localNumber) {
        if (countryCode != null && localNumber != null) {
            return countryCode.concat(localNumber);
        }
        return null;
    }

    public static String calculateTimeAgo(Date pastDate) {
        Date currentDate = new Date();

        long timeDifference = currentDate.getTime() - pastDate.getTime();

        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / DAYS_IN_MONTH;
        long years = days / 365;

        if (years > 0) {
            return years + "y";
        } else if (weeks > 0) {
            return weeks + "w";
        }
        if (months > 0) {
            return months + "mo";
        }
        if (days > 0) {
            return days + "d";
        } else if (hours > 0) {
            return hours + "h";
        } else if (minutes > 0) {
            return minutes + "m";
        } else {
            return "Just now";
        }
    }

    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    public static boolean isValidPasscode(String passcode) {
        return !Utils.isEmpty(passcode) && passcode.length() == Utils.PASSCODE_DIGIT_COUNT;
    }

    public static boolean isCorrectPasscode(String correctPasscode, String enteredPasscode) {
        return !isEmpty(enteredPasscode) && enteredPasscode.equals(correctPasscode);
    }

    public static boolean isValidOtp(String text) {
        return text != null && text.length() == PIN_DIGIT_COUNT;
    }

    public static EFriendshipStatus convertFriendRequestStatusToFriendshipStatus(
            String curUserId,
            String senderId,
            FriendRequest.EStatus status) {

        switch (status) {
            case PENDING:
                if (Objects.equals(curUserId, senderId)) {
                    return EFriendshipStatus.SENT_REQUEST;
                } else {
                    return EFriendshipStatus.RECEIVED_REQUEST;
                }
            case ACCEPTED:
                return EFriendshipStatus.FRIEND;
            case REJECTED:
                return EFriendshipStatus.NOT_FRIEND;
            default:
                return EFriendshipStatus.NOT_FOUND;
        }
    }
}
