package com.mapbox.mapboxsdk.location;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

class LayerBitmapProvider {
    private final Context context;

    LayerBitmapProvider(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: package-private */
    public Bitmap generateBitmap(@DrawableRes int drawableRes, @ColorInt Integer tintColor) {
        return BitmapUtils.getBitmapFromDrawable(BitmapUtils.getDrawableFromRes(this.context, drawableRes, tintColor));
    }

    /* access modifiers changed from: package-private */
    public Bitmap generateShadowBitmap(@NonNull LocationComponentOptions options) {
        return Utils.generateShadow(BitmapUtils.getDrawableFromRes(this.context, R.drawable.mapbox_user_icon_shadow), options.elevation());
    }
}
