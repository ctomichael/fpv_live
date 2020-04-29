package com.mapbox.android.gestures;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.MotionEvent;

public class Utils {
    public static PointF determineFocalPoint(@NonNull MotionEvent motionEvent) {
        int pointersCount = motionEvent.getPointerCount();
        float x = 0.0f;
        float y = 0.0f;
        for (int i = 0; i < pointersCount; i++) {
            x += motionEvent.getX(i);
            y += motionEvent.getY(i);
        }
        return new PointF(x / ((float) pointersCount), y / ((float) pointersCount));
    }

    public static float getRawX(MotionEvent event, int pointerIndex) {
        float offset = event.getRawX() - event.getX();
        if (pointerIndex < event.getPointerCount()) {
            return event.getX(pointerIndex) + offset;
        }
        return 0.0f;
    }

    public static float getRawY(MotionEvent event, int pointerIndex) {
        float offset = event.getRawY() - event.getY();
        if (pointerIndex < event.getPointerCount()) {
            return event.getY(pointerIndex) + offset;
        }
        return 0.0f;
    }

    public static float dpToPx(float dp) {
        return Resources.getSystem().getDisplayMetrics().density * dp;
    }

    public static float pxToDp(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    public static float pxToMm(float px, Context context) {
        return px / TypedValue.applyDimension(5, 1.0f, context.getResources().getDisplayMetrics());
    }
}
