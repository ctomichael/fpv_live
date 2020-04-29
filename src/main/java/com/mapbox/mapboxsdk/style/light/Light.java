package com.mapbox.mapboxsdk.style.light;

import android.support.annotation.ColorInt;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.mapbox.mapboxsdk.utils.ThreadUtils;

@UiThread
public class Light {
    private static final String TAG = "Mbgl-Light";
    @Keep
    private long nativePtr;

    @Keep
    @NonNull
    private native String nativeGetAnchor();

    @Keep
    @NonNull
    private native String nativeGetColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetColorTransition();

    @Keep
    @NonNull
    private native float nativeGetIntensity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetIntensityTransition();

    @Keep
    @NonNull
    private native Position nativeGetPosition();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetPositionTransition();

    @Keep
    private native void nativeSetAnchor(String str);

    @Keep
    private native void nativeSetColor(String str);

    @Keep
    private native void nativeSetColorTransition(long j, long j2);

    @Keep
    private native void nativeSetIntensity(float f);

    @Keep
    private native void nativeSetIntensityTransition(long j, long j2);

    @Keep
    private native void nativeSetPosition(Position position);

    @Keep
    private native void nativeSetPositionTransition(long j, long j2);

    @Keep
    Light(long nativePtr2) {
        checkThread();
        this.nativePtr = nativePtr2;
    }

    public void setAnchor(String anchor) {
        checkThread();
        nativeSetAnchor(anchor);
    }

    @NonNull
    public String getAnchor() {
        checkThread();
        return nativeGetAnchor();
    }

    public void setPosition(@NonNull Position position) {
        checkThread();
        nativeSetPosition(position);
    }

    @NonNull
    public Position getPosition() {
        checkThread();
        return nativeGetPosition();
    }

    @NonNull
    public TransitionOptions getPositionTransition() {
        checkThread();
        return nativeGetPositionTransition();
    }

    public void setPositionTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetPositionTransition(options.getDuration(), options.getDelay());
    }

    public void setColor(@ColorInt int color) {
        checkThread();
        nativeSetColor(ColorUtils.colorToRgbaString(color));
    }

    public void setColor(String color) {
        checkThread();
        nativeSetColor(color);
    }

    @NonNull
    public String getColor() {
        checkThread();
        return nativeGetColor();
    }

    @NonNull
    public TransitionOptions getColorTransition() {
        checkThread();
        return nativeGetColorTransition();
    }

    public void setColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetColorTransition(options.getDuration(), options.getDelay());
    }

    public void setIntensity(float intensity) {
        checkThread();
        nativeSetIntensity(intensity);
    }

    @NonNull
    public float getIntensity() {
        checkThread();
        return nativeGetIntensity();
    }

    @NonNull
    public TransitionOptions getIntensityTransition() {
        checkThread();
        return nativeGetIntensityTransition();
    }

    public void setIntensityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetIntensityTransition(options.getDuration(), options.getDelay());
    }

    private void checkThread() {
        ThreadUtils.checkThread(TAG);
    }
}
