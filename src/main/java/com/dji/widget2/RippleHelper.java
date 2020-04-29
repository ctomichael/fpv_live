package com.dji.widget2;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;

public class RippleHelper {
    public static Drawable rippleBackground(Context context, Drawable backgroundDrawable) {
        return new RippleDrawable(getPressedState(context.getResources().getColor(R.color.grey_8)), backgroundDrawable, null);
    }

    public static Drawable rippleBackground(int pressedColor, Drawable backgroundDrawable) {
        return new RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null);
    }

    public static ColorStateList getPressedState(int pressedColor) {
        return new ColorStateList(new int[][]{new int[0]}, new int[]{pressedColor}).withAlpha(51);
    }
}
