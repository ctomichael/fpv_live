package com.mapbox.mapboxsdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BitmapUtils {
    public static Bitmap createBitmapFromView(@NonNull View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(524288);
        view.buildDrawingCache();
        if (view.getDrawingCache() == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return createBitmap;
    }

    public static Bitmap mergeBitmap(@NonNull Bitmap background, @NonNull Bitmap foreground) {
        Bitmap result = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(background, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(foreground, 10.0f, 10.0f, (Paint) null);
        return result;
    }

    @Nullable
    public static Bitmap getBitmapFromDrawable(@Nullable Drawable sourceDrawable) {
        if (sourceDrawable == null) {
            return null;
        }
        if (sourceDrawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) sourceDrawable).getBitmap();
        }
        Drawable.ConstantState constantState = sourceDrawable.getConstantState();
        if (constantState == null) {
            return null;
        }
        Drawable drawable = constantState.newDrawable().mutate();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Nullable
    public static byte[] getByteArrayFromDrawable(@Nullable Drawable drawable) {
        Bitmap bitmap;
        if (drawable == null || (bitmap = getBitmapFromDrawable(drawable)) == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Nullable
    public static Drawable getDrawableFromByteArray(@NonNull Context context, @Nullable byte[] array) {
        if (array == null) {
            return null;
        }
        return new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(array, 0, array.length));
    }

    @Nullable
    public static Drawable getDrawableFromRes(@NonNull Context context, @DrawableRes int drawableRes) {
        return getDrawableFromRes(context, drawableRes, null);
    }

    @Nullable
    public static Drawable getDrawableFromRes(@NonNull Context context, @DrawableRes int drawableRes, @ColorInt @Nullable Integer tintColor) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        if (drawable == null) {
            return null;
        }
        if (tintColor == null) {
            return drawable;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            drawable.setTint(tintColor.intValue());
            return drawable;
        }
        drawable.mutate().setColorFilter(tintColor.intValue(), PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    @VisibleForTesting
    public static boolean equals(Bitmap bitmap, Bitmap other) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getHeight() * bitmap.getRowBytes());
        bitmap.copyPixelsToBuffer(buffer);
        ByteBuffer bufferOther = ByteBuffer.allocate(other.getHeight() * other.getRowBytes());
        other.copyPixelsToBuffer(bufferOther);
        return Arrays.equals(buffer.array(), bufferOther.array());
    }
}
