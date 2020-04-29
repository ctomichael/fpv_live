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
public class LineLayer extends Layer {
    @Keep
    @NonNull
    private native Object nativeGetLineBlur();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetLineBlurTransition();

    @Keep
    @NonNull
    private native Object nativeGetLineCap();

    @Keep
    @NonNull
    private native Object nativeGetLineColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetLineColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetLineDasharray();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetLineDasharrayTransition();

    @Keep
    @NonNull
    private native Object nativeGetLineGapWidth();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetLineGapWidthTransition();

    @Keep
    @NonNull
    private native Object nativeGetLineGradient();

    @Keep
    @NonNull
    private native Object nativeGetLineJoin();

    @Keep
    @NonNull
    private native Object nativeGetLineMiterLimit();

    @Keep
    @NonNull
    private native Object nativeGetLineOffset();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetLineOffsetTransition();

    @Keep
    @NonNull
    private native Object nativeGetLineOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetLineOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetLinePattern();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetLinePatternTransition();

    @Keep
    @NonNull
    private native Object nativeGetLineRoundLimit();

    @Keep
    @NonNull
    private native Object nativeGetLineTranslate();

    @Keep
    @NonNull
    private native Object nativeGetLineTranslateAnchor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetLineTranslateTransition();

    @Keep
    @NonNull
    private native Object nativeGetLineWidth();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetLineWidthTransition();

    @Keep
    private native void nativeSetLineBlurTransition(long j, long j2);

    @Keep
    private native void nativeSetLineColorTransition(long j, long j2);

    @Keep
    private native void nativeSetLineDasharrayTransition(long j, long j2);

    @Keep
    private native void nativeSetLineGapWidthTransition(long j, long j2);

    @Keep
    private native void nativeSetLineOffsetTransition(long j, long j2);

    @Keep
    private native void nativeSetLineOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetLinePatternTransition(long j, long j2);

    @Keep
    private native void nativeSetLineTranslateTransition(long j, long j2);

    @Keep
    private native void nativeSetLineWidthTransition(long j, long j2);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, String str2);

    @Keep
    LineLayer(long nativePtr) {
        super(nativePtr);
    }

    public LineLayer(String layerId, String sourceId) {
        initialize(layerId, sourceId);
    }

    public void setSourceLayer(String sourceLayer) {
        checkThread();
        nativeSetSourceLayer(sourceLayer);
    }

    @NonNull
    public LineLayer withSourceLayer(String sourceLayer) {
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
    public LineLayer withFilter(@NonNull Expression filter) {
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
    public LineLayer withProperties(@NonNull PropertyValue<?>... properties) {
        setProperties(properties);
        return this;
    }

    @NonNull
    public PropertyValue<String> getLineCap() {
        checkThread();
        return new PropertyValue<>("line-cap", nativeGetLineCap());
    }

    @NonNull
    public PropertyValue<String> getLineJoin() {
        checkThread();
        return new PropertyValue<>("line-join", nativeGetLineJoin());
    }

    @NonNull
    public PropertyValue<Float> getLineMiterLimit() {
        checkThread();
        return new PropertyValue<>("line-miter-limit", nativeGetLineMiterLimit());
    }

    @NonNull
    public PropertyValue<Float> getLineRoundLimit() {
        checkThread();
        return new PropertyValue<>("line-round-limit", nativeGetLineRoundLimit());
    }

    @NonNull
    public PropertyValue<Float> getLineOpacity() {
        checkThread();
        return new PropertyValue<>("line-opacity", nativeGetLineOpacity());
    }

    @NonNull
    public TransitionOptions getLineOpacityTransition() {
        checkThread();
        return nativeGetLineOpacityTransition();
    }

    public void setLineOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetLineOpacityTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getLineColor() {
        checkThread();
        return new PropertyValue<>("line-color", nativeGetLineColor());
    }

    @ColorInt
    public int getLineColorAsInt() {
        checkThread();
        PropertyValue<String> value = getLineColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("line-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getLineColorTransition() {
        checkThread();
        return nativeGetLineColorTransition();
    }

    public void setLineColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetLineColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float[]> getLineTranslate() {
        checkThread();
        return new PropertyValue<>("line-translate", nativeGetLineTranslate());
    }

    @NonNull
    public TransitionOptions getLineTranslateTransition() {
        checkThread();
        return nativeGetLineTranslateTransition();
    }

    public void setLineTranslateTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetLineTranslateTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getLineTranslateAnchor() {
        checkThread();
        return new PropertyValue<>("line-translate-anchor", nativeGetLineTranslateAnchor());
    }

    @NonNull
    public PropertyValue<Float> getLineWidth() {
        checkThread();
        return new PropertyValue<>("line-width", nativeGetLineWidth());
    }

    @NonNull
    public TransitionOptions getLineWidthTransition() {
        checkThread();
        return nativeGetLineWidthTransition();
    }

    public void setLineWidthTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetLineWidthTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getLineGapWidth() {
        checkThread();
        return new PropertyValue<>("line-gap-width", nativeGetLineGapWidth());
    }

    @NonNull
    public TransitionOptions getLineGapWidthTransition() {
        checkThread();
        return nativeGetLineGapWidthTransition();
    }

    public void setLineGapWidthTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetLineGapWidthTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getLineOffset() {
        checkThread();
        return new PropertyValue<>("line-offset", nativeGetLineOffset());
    }

    @NonNull
    public TransitionOptions getLineOffsetTransition() {
        checkThread();
        return nativeGetLineOffsetTransition();
    }

    public void setLineOffsetTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetLineOffsetTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getLineBlur() {
        checkThread();
        return new PropertyValue<>("line-blur", nativeGetLineBlur());
    }

    @NonNull
    public TransitionOptions getLineBlurTransition() {
        checkThread();
        return nativeGetLineBlurTransition();
    }

    public void setLineBlurTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetLineBlurTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float[]> getLineDasharray() {
        checkThread();
        return new PropertyValue<>("line-dasharray", nativeGetLineDasharray());
    }

    @NonNull
    public TransitionOptions getLineDasharrayTransition() {
        checkThread();
        return nativeGetLineDasharrayTransition();
    }

    public void setLineDasharrayTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetLineDasharrayTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getLinePattern() {
        checkThread();
        return new PropertyValue<>("line-pattern", nativeGetLinePattern());
    }

    @NonNull
    public TransitionOptions getLinePatternTransition() {
        checkThread();
        return nativeGetLinePatternTransition();
    }

    public void setLinePatternTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetLinePatternTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getLineGradient() {
        checkThread();
        return new PropertyValue<>("line-gradient", nativeGetLineGradient());
    }

    @ColorInt
    public int getLineGradientAsInt() {
        checkThread();
        PropertyValue<String> value = getLineGradient();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("line-gradient was set as a Function");
    }
}
