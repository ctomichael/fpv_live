package com.mapbox.mapboxsdk.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.widget.ImageViewCompat;
import android.util.TypedValue;
import android.widget.ImageView;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.exceptions.ConversionException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    @ColorInt
    public static int getPrimaryColor(@NonNull Context context) {
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(context.getResources().getIdentifier("colorPrimary", "attrs", context.getPackageName()), typedValue, true);
            return typedValue.data;
        } catch (Exception e) {
            return getColorCompat(context, R.color.mapbox_blue);
        }
    }

    @ColorInt
    public static int getPrimaryDarkColor(@NonNull Context context) {
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(context.getResources().getIdentifier("colorPrimaryDark", "attrs", context.getPackageName()), typedValue, true);
            return typedValue.data;
        } catch (Exception e) {
            return getColorCompat(context, R.color.mapbox_blue);
        }
    }

    @ColorInt
    public static int getAccentColor(@NonNull Context context) {
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(context.getResources().getIdentifier("colorAccent", "attrs", context.getPackageName()), typedValue, true);
            return typedValue.data;
        } catch (Exception e) {
            return getColorCompat(context, R.color.mapbox_gray);
        }
    }

    @NonNull
    public static ColorStateList getSelector(@ColorInt int color) {
        return new ColorStateList(new int[][]{new int[]{16842919}, new int[0]}, new int[]{color, color});
    }

    public static void setTintList(@NonNull ImageView imageView, @ColorInt int tintColor) {
        ImageViewCompat.setImageTintList(imageView, getSelector(tintColor));
    }

    @ColorInt
    public static int rgbaToColor(@NonNull String value) {
        Matcher m = Pattern.compile("rgba?\\s*\\(\\s*(\\d+\\.?\\d*)\\s*,\\s*(\\d+\\.?\\d*)\\s*,\\s*(\\d+\\.?\\d*)\\s*,?\\s*(\\d+\\.?\\d*)?\\s*\\)").matcher(value);
        if (m.matches() && m.groupCount() == 3) {
            return Color.rgb((int) Float.parseFloat(m.group(1)), (int) Float.parseFloat(m.group(2)), (int) Float.parseFloat(m.group(3)));
        }
        if (m.matches() && m.groupCount() == 4) {
            return Color.argb((int) (Float.parseFloat(m.group(4)) * 255.0f), (int) Float.parseFloat(m.group(1)), (int) Float.parseFloat(m.group(2)), (int) Float.parseFloat(m.group(3)));
        }
        throw new ConversionException("Not a valid rgb/rgba value");
    }

    public static String colorToRgbaString(@ColorInt int color) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        decimalFormat.applyPattern("#.###");
        String alpha = decimalFormat.format((double) (((float) ((color >> 24) & 255)) / 255.0f));
        return String.format(Locale.US, "rgba(%d, %d, %d, %s)", Integer.valueOf((color >> 16) & 255), Integer.valueOf((color >> 8) & 255), Integer.valueOf(color & 255), alpha);
    }

    public static float[] colorToRgbaArray(@ColorInt int color) {
        return new float[]{(float) ((color >> 16) & 255), (float) ((color >> 8) & 255), (float) (color & 255), ((float) ((color >> 24) & 255)) / 255.0f};
    }

    public static float[] colorToGlRgbaArray(@ColorInt int color) {
        return new float[]{((float) ((color >> 16) & 255)) / 255.0f, ((float) ((color >> 8) & 255)) / 255.0f, ((float) (color & 255)) / 255.0f, ((float) ((color >> 24) & 255)) / 255.0f};
    }

    private static int getColorCompat(@NonNull Context context, int id) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getResources().getColor(id, context.getTheme());
        }
        return context.getResources().getColor(id);
    }
}
