package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.ColorInt;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.utils.ColorUtils;

@UiThread
public class BackgroundLayer extends Layer {
    @Keep
    @NonNull
    private native Object nativeGetBackgroundColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetBackgroundColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetBackgroundOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetBackgroundOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetBackgroundPattern();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetBackgroundPatternTransition();

    @Keep
    private native void nativeSetBackgroundColorTransition(long j, long j2);

    @Keep
    private native void nativeSetBackgroundOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetBackgroundPatternTransition(long j, long j2);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str);

    @Keep
    BackgroundLayer(long nativePtr) {
        super(nativePtr);
    }

    public BackgroundLayer(String layerId) {
        initialize(layerId);
    }

    @NonNull
    public BackgroundLayer withProperties(@NonNull PropertyValue<?>... properties) {
        setProperties(properties);
        return this;
    }

    @NonNull
    public PropertyValue<String> getBackgroundColor() {
        checkThread();
        return new PropertyValue<>("background-color", nativeGetBackgroundColor());
    }

    @ColorInt
    public int getBackgroundColorAsInt() {
        checkThread();
        PropertyValue<String> value = getBackgroundColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("background-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getBackgroundColorTransition() {
        checkThread();
        return nativeGetBackgroundColorTransition();
    }

    public void setBackgroundColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetBackgroundColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getBackgroundPattern() {
        checkThread();
        return new PropertyValue<>("background-pattern", nativeGetBackgroundPattern());
    }

    @NonNull
    public TransitionOptions getBackgroundPatternTransition() {
        checkThread();
        return nativeGetBackgroundPatternTransition();
    }

    public void setBackgroundPatternTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetBackgroundPatternTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getBackgroundOpacity() {
        checkThread();
        return new PropertyValue<>("background-opacity", nativeGetBackgroundOpacity());
    }

    @NonNull
    public TransitionOptions getBackgroundOpacityTransition() {
        checkThread();
        return nativeGetBackgroundOpacityTransition();
    }

    public void setBackgroundOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetBackgroundOpacityTransition(options.getDuration(), options.getDelay());
    }
}
