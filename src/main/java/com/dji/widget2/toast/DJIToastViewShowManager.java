package com.dji.widget2.toast;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Toast;
import com.dji.widget2.R;
import com.dji.widget2.toast.DJIToastView;

public class DJIToastViewShowManager {
    public static void text(Context context, String text) {
        show(context, text, 1, -1, 3);
    }

    public static void text(Context context, String text, int duration) {
        show(context, text, duration, -1, 3);
    }

    public static void text(Context context, String text, int duration, @DJIToastView.Theme int theme) {
        show(context, text, duration, -1, theme);
    }

    public static void text(Context context, @StringRes int textResId) {
        show(context, context.getString(textResId), 1, -1, 3);
    }

    public static void text(Context context, @StringRes int textResId, int duration) {
        show(context, context.getString(textResId), duration, -1, 3);
    }

    public static void text(Context context, @StringRes int textResId, int duration, @DJIToastView.Theme int theme) {
        show(context, context.getString(textResId), duration, -1, theme);
    }

    public static void icon(Context context, String text, @DrawableRes int iconResId) {
        show(context, text, 1, iconResId, 3);
    }

    public static void icon(Context context, String text, @DrawableRes int iconResId, int duration) {
        show(context, text, duration, iconResId, 3);
    }

    public static void icon(Context context, String text, @DrawableRes int iconResId, int duration, @DJIToastView.Theme int theme) {
        show(context, text, duration, iconResId, theme);
    }

    public static void icon(Context context, @StringRes int textResId, @DrawableRes int iconResId) {
        show(context, context.getString(textResId), 1, iconResId, 3);
    }

    public static void icon(Context context, @StringRes int textResId, @DrawableRes int iconResId, int duration) {
        show(context, context.getString(textResId), duration, iconResId, 3);
    }

    public static void icon(Context context, @StringRes int textResId, @DrawableRes int iconResId, int duration, @DJIToastView.Theme int theme) {
        show(context, context.getString(textResId), duration, iconResId, theme);
    }

    private static void show(Context context, String text, int duration, @DrawableRes int iconResId, @DJIToastView.Theme int theme) {
        DJIToastView toastView = new DJIToastView(context);
        if (iconResId != -1) {
            toastView.setLayoutType(2);
            toastView.setIcon(iconResId);
        } else {
            toastView.setLayoutType(1);
        }
        Toast t = Toast.makeText(context, "", duration);
        t.setView(toastView);
        toastView.setTheme(theme);
        toastView.setText(text);
        if (iconResId != -1) {
            t.setGravity(17, 0, 0);
        } else {
            t.setGravity(81, 0, context.getResources().getDimensionPixelOffset(R.dimen.s_64_dp));
        }
        t.show();
    }
}
