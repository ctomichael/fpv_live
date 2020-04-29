package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.ColorInt;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.google.gson.JsonElement;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.utils.ColorUtils;

@UiThread
public class FillLayer extends Layer {
    @Keep
    @NonNull
    private native Object nativeGetFillAntialias();

    @Keep
    @NonNull
    private native Object nativeGetFillColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillOutlineColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillOutlineColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillPattern();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillPatternTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillTranslate();

    @Keep
    @NonNull
    private native Object nativeGetFillTranslateAnchor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillTranslateTransition();

    @Keep
    private native void nativeSetFillColorTransition(long j, long j2);

    @Keep
    private native void nativeSetFillOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetFillOutlineColorTransition(long j, long j2);

    @Keep
    private native void nativeSetFillPatternTransition(long j, long j2);

    @Keep
    private native void nativeSetFillTranslateTransition(long j, long j2);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, String str2);

    @Keep
    FillLayer(long nativePtr) {
        super(nativePtr);
    }

    public FillLayer(String layerId, String sourceId) {
        initialize(layerId, sourceId);
    }

    public void setSourceLayer(String sourceLayer) {
        checkThread();
        nativeSetSourceLayer(sourceLayer);
    }

    @NonNull
    public FillLayer withSourceLayer(String sourceLayer) {
        setSourceLayer(sourceLayer);
        return this;
    }

    @NonNull
    public String getSourceId() {
        checkThread();
        return nativeGetSourceId();
    }

    @NonNull
    public String getSourceLayer() {
        checkThread();
        return nativeGetSourceLayer();
    }

    public void setFilter(@NonNull Expression filter) {
        checkThread();
        nativeSetFilter(filter.toArray());
    }

    @NonNull
    public FillLayer withFilter(@NonNull Expression filter) {
        setFilter(filter);
        return this;
    }

    @Nullable
    public Expression getFilter() {
        checkThread();
        JsonElement jsonElement = nativeGetFilter();
        if (jsonElement != null) {
            return Expression.Converter.convert(jsonElement);
        }
        return null;
    }

    @NonNull
    public FillLayer withProperties(@NonNull PropertyValue<?>... properties) {
        setProperties(properties);
        return this;
    }

    @NonNull
    public PropertyValue<Boolean> getFillAntialias() {
        checkThread();
        return new PropertyValue<>("fill-antialias", nativeGetFillAntialias());
    }

    @NonNull
    public PropertyValue<Float> getFillOpacity() {
        checkThread();
        return new PropertyValue<>("fill-opacity", nativeGetFillOpacity());
    }

    @NonNull
    public TransitionOptions getFillOpacityTransition() {
        checkThread();
        return nativeGetFillOpacityTransition();
    }

    public void setFillOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillOpacityTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getFillColor() {
        checkThread();
        return new PropertyValue<>("fill-color", nativeGetFillColor());
    }

    @ColorInt
    public int getFillColorAsInt() {
        checkThread();
        PropertyValue<String> value = getFillColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("fill-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getFillColorTransition() {
        checkThread();
        return nativeGetFillColorTransition();
    }

    public void setFillColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getFillOutlineColor() {
        checkThread();
        return new PropertyValue<>("fill-outline-color", nativeGetFillOutlineColor());
    }

    @ColorInt
    public int getFillOutlineColorAsInt() {
        checkThread();
        PropertyValue<String> value = getFillOutlineColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("fill-outline-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getFillOutlineColorTransition() {
        checkThread();
        return nativeGetFillOutlineColorTransition();
    }

    public void setFillOutlineColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillOutlineColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float[]> getFillTranslate() {
        checkThread();
        return new PropertyValue<>("fill-translate", nativeGetFillTranslate());
    }

    @NonNull
    public TransitionOptions getFillTranslateTransition() {
        checkThread();
        return nativeGetFillTranslateTransition();
    }

    public void setFillTranslateTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillTranslateTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getFillTranslateAnchor() {
        checkThread();
        return new PropertyValue<>("fill-translate-anchor", nativeGetFillTranslateAnchor());
    }

    @NonNull
    public PropertyValue<String> getFillPattern() {
        checkThread();
        return new PropertyValue<>("fill-pattern", nativeGetFillPattern());
    }

    @NonNull
    public TransitionOptions getFillPatternTransition() {
        checkThread();
        return nativeGetFillPatternTransition();
    }

    public void setFillPatternTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillPatternTransition(options.getDuration(), options.getDelay());
    }
}
