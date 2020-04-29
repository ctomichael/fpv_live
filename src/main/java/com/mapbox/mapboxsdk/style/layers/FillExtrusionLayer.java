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
public class FillExtrusionLayer extends Layer {
    @Keep
    @NonNull
    private native Object nativeGetFillExtrusionBase();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillExtrusionBaseTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillExtrusionColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillExtrusionColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillExtrusionHeight();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillExtrusionHeightTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillExtrusionOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillExtrusionOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillExtrusionPattern();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillExtrusionPatternTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillExtrusionTranslate();

    @Keep
    @NonNull
    private native Object nativeGetFillExtrusionTranslateAnchor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetFillExtrusionTranslateTransition();

    @Keep
    @NonNull
    private native Object nativeGetFillExtrusionVerticalGradient();

    @Keep
    private native void nativeSetFillExtrusionBaseTransition(long j, long j2);

    @Keep
    private native void nativeSetFillExtrusionColorTransition(long j, long j2);

    @Keep
    private native void nativeSetFillExtrusionHeightTransition(long j, long j2);

    @Keep
    private native void nativeSetFillExtrusionOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetFillExtrusionPatternTransition(long j, long j2);

    @Keep
    private native void nativeSetFillExtrusionTranslateTransition(long j, long j2);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, String str2);

    @Keep
    FillExtrusionLayer(long nativePtr) {
        super(nativePtr);
    }

    public FillExtrusionLayer(String layerId, String sourceId) {
        initialize(layerId, sourceId);
    }

    public void setSourceLayer(String sourceLayer) {
        checkThread();
        nativeSetSourceLayer(sourceLayer);
    }

    @NonNull
    public FillExtrusionLayer withSourceLayer(String sourceLayer) {
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
    public FillExtrusionLayer withFilter(@NonNull Expression filter) {
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
    public FillExtrusionLayer withProperties(@NonNull PropertyValue<?>... properties) {
        setProperties(properties);
        return this;
    }

    @NonNull
    public PropertyValue<Float> getFillExtrusionOpacity() {
        checkThread();
        return new PropertyValue<>("fill-extrusion-opacity", nativeGetFillExtrusionOpacity());
    }

    @NonNull
    public TransitionOptions getFillExtrusionOpacityTransition() {
        checkThread();
        return nativeGetFillExtrusionOpacityTransition();
    }

    public void setFillExtrusionOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillExtrusionOpacityTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getFillExtrusionColor() {
        checkThread();
        return new PropertyValue<>("fill-extrusion-color", nativeGetFillExtrusionColor());
    }

    @ColorInt
    public int getFillExtrusionColorAsInt() {
        checkThread();
        PropertyValue<String> value = getFillExtrusionColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("fill-extrusion-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getFillExtrusionColorTransition() {
        checkThread();
        return nativeGetFillExtrusionColorTransition();
    }

    public void setFillExtrusionColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillExtrusionColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float[]> getFillExtrusionTranslate() {
        checkThread();
        return new PropertyValue<>("fill-extrusion-translate", nativeGetFillExtrusionTranslate());
    }

    @NonNull
    public TransitionOptions getFillExtrusionTranslateTransition() {
        checkThread();
        return nativeGetFillExtrusionTranslateTransition();
    }

    public void setFillExtrusionTranslateTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillExtrusionTranslateTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getFillExtrusionTranslateAnchor() {
        checkThread();
        return new PropertyValue<>("fill-extrusion-translate-anchor", nativeGetFillExtrusionTranslateAnchor());
    }

    @NonNull
    public PropertyValue<String> getFillExtrusionPattern() {
        checkThread();
        return new PropertyValue<>("fill-extrusion-pattern", nativeGetFillExtrusionPattern());
    }

    @NonNull
    public TransitionOptions getFillExtrusionPatternTransition() {
        checkThread();
        return nativeGetFillExtrusionPatternTransition();
    }

    public void setFillExtrusionPatternTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillExtrusionPatternTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getFillExtrusionHeight() {
        checkThread();
        return new PropertyValue<>("fill-extrusion-height", nativeGetFillExtrusionHeight());
    }

    @NonNull
    public TransitionOptions getFillExtrusionHeightTransition() {
        checkThread();
        return nativeGetFillExtrusionHeightTransition();
    }

    public void setFillExtrusionHeightTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillExtrusionHeightTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getFillExtrusionBase() {
        checkThread();
        return new PropertyValue<>("fill-extrusion-base", nativeGetFillExtrusionBase());
    }

    @NonNull
    public TransitionOptions getFillExtrusionBaseTransition() {
        checkThread();
        return nativeGetFillExtrusionBaseTransition();
    }

    public void setFillExtrusionBaseTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetFillExtrusionBaseTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Boolean> getFillExtrusionVerticalGradient() {
        checkThread();
        return new PropertyValue<>("fill-extrusion-vertical-gradient", nativeGetFillExtrusionVerticalGradient());
    }
}
