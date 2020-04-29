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
public class CircleLayer extends Layer {
    @Keep
    @NonNull
    private native Object nativeGetCircleBlur();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetCircleBlurTransition();

    @Keep
    @NonNull
    private native Object nativeGetCircleColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetCircleColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetCircleOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetCircleOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetCirclePitchAlignment();

    @Keep
    @NonNull
    private native Object nativeGetCirclePitchScale();

    @Keep
    @NonNull
    private native Object nativeGetCircleRadius();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetCircleRadiusTransition();

    @Keep
    @NonNull
    private native Object nativeGetCircleStrokeColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetCircleStrokeColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetCircleStrokeOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetCircleStrokeOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetCircleStrokeWidth();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetCircleStrokeWidthTransition();

    @Keep
    @NonNull
    private native Object nativeGetCircleTranslate();

    @Keep
    @NonNull
    private native Object nativeGetCircleTranslateAnchor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetCircleTranslateTransition();

    @Keep
    private native void nativeSetCircleBlurTransition(long j, long j2);

    @Keep
    private native void nativeSetCircleColorTransition(long j, long j2);

    @Keep
    private native void nativeSetCircleOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetCircleRadiusTransition(long j, long j2);

    @Keep
    private native void nativeSetCircleStrokeColorTransition(long j, long j2);

    @Keep
    private native void nativeSetCircleStrokeOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetCircleStrokeWidthTransition(long j, long j2);

    @Keep
    private native void nativeSetCircleTranslateTransition(long j, long j2);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, String str2);

    @Keep
    CircleLayer(long nativePtr) {
        super(nativePtr);
    }

    public CircleLayer(String layerId, String sourceId) {
        initialize(layerId, sourceId);
    }

    public void setSourceLayer(String sourceLayer) {
        checkThread();
        nativeSetSourceLayer(sourceLayer);
    }

    @NonNull
    public CircleLayer withSourceLayer(String sourceLayer) {
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
    public CircleLayer withFilter(@NonNull Expression filter) {
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
    public CircleLayer withProperties(@NonNull PropertyValue<?>... properties) {
        setProperties(properties);
        return this;
    }

    @NonNull
    public PropertyValue<Float> getCircleRadius() {
        checkThread();
        return new PropertyValue<>("circle-radius", nativeGetCircleRadius());
    }

    @NonNull
    public TransitionOptions getCircleRadiusTransition() {
        checkThread();
        return nativeGetCircleRadiusTransition();
    }

    public void setCircleRadiusTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetCircleRadiusTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getCircleColor() {
        checkThread();
        return new PropertyValue<>("circle-color", nativeGetCircleColor());
    }

    @ColorInt
    public int getCircleColorAsInt() {
        checkThread();
        PropertyValue<String> value = getCircleColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("circle-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getCircleColorTransition() {
        checkThread();
        return nativeGetCircleColorTransition();
    }

    public void setCircleColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetCircleColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getCircleBlur() {
        checkThread();
        return new PropertyValue<>("circle-blur", nativeGetCircleBlur());
    }

    @NonNull
    public TransitionOptions getCircleBlurTransition() {
        checkThread();
        return nativeGetCircleBlurTransition();
    }

    public void setCircleBlurTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetCircleBlurTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getCircleOpacity() {
        checkThread();
        return new PropertyValue<>("circle-opacity", nativeGetCircleOpacity());
    }

    @NonNull
    public TransitionOptions getCircleOpacityTransition() {
        checkThread();
        return nativeGetCircleOpacityTransition();
    }

    public void setCircleOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetCircleOpacityTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float[]> getCircleTranslate() {
        checkThread();
        return new PropertyValue<>("circle-translate", nativeGetCircleTranslate());
    }

    @NonNull
    public TransitionOptions getCircleTranslateTransition() {
        checkThread();
        return nativeGetCircleTranslateTransition();
    }

    public void setCircleTranslateTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetCircleTranslateTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getCircleTranslateAnchor() {
        checkThread();
        return new PropertyValue<>("circle-translate-anchor", nativeGetCircleTranslateAnchor());
    }

    @NonNull
    public PropertyValue<String> getCirclePitchScale() {
        checkThread();
        return new PropertyValue<>("circle-pitch-scale", nativeGetCirclePitchScale());
    }

    @NonNull
    public PropertyValue<String> getCirclePitchAlignment() {
        checkThread();
        return new PropertyValue<>("circle-pitch-alignment", nativeGetCirclePitchAlignment());
    }

    @NonNull
    public PropertyValue<Float> getCircleStrokeWidth() {
        checkThread();
        return new PropertyValue<>("circle-stroke-width", nativeGetCircleStrokeWidth());
    }

    @NonNull
    public TransitionOptions getCircleStrokeWidthTransition() {
        checkThread();
        return nativeGetCircleStrokeWidthTransition();
    }

    public void setCircleStrokeWidthTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetCircleStrokeWidthTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getCircleStrokeColor() {
        checkThread();
        return new PropertyValue<>("circle-stroke-color", nativeGetCircleStrokeColor());
    }

    @ColorInt
    public int getCircleStrokeColorAsInt() {
        checkThread();
        PropertyValue<String> value = getCircleStrokeColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("circle-stroke-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getCircleStrokeColorTransition() {
        checkThread();
        return nativeGetCircleStrokeColorTransition();
    }

    public void setCircleStrokeColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetCircleStrokeColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getCircleStrokeOpacity() {
        checkThread();
        return new PropertyValue<>("circle-stroke-opacity", nativeGetCircleStrokeOpacity());
    }

    @NonNull
    public TransitionOptions getCircleStrokeOpacityTransition() {
        checkThread();
        return nativeGetCircleStrokeOpacityTransition();
    }

    public void setCircleStrokeOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetCircleStrokeOpacityTransition(options.getDuration(), options.getDelay());
    }
}
