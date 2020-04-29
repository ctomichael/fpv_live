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
public class HeatmapLayer extends Layer {
    @Keep
    @NonNull
    private native Object nativeGetHeatmapColor();

    @Keep
    @NonNull
    private native Object nativeGetHeatmapIntensity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetHeatmapIntensityTransition();

    @Keep
    @NonNull
    private native Object nativeGetHeatmapOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetHeatmapOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetHeatmapRadius();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetHeatmapRadiusTransition();

    @Keep
    @NonNull
    private native Object nativeGetHeatmapWeight();

    @Keep
    private native void nativeSetHeatmapIntensityTransition(long j, long j2);

    @Keep
    private native void nativeSetHeatmapOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetHeatmapRadiusTransition(long j, long j2);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, String str2);

    @Keep
    HeatmapLayer(long nativePtr) {
        super(nativePtr);
    }

    public HeatmapLayer(String layerId, String sourceId) {
        initialize(layerId, sourceId);
    }

    public void setSourceLayer(String sourceLayer) {
        checkThread();
        nativeSetSourceLayer(sourceLayer);
    }

    @NonNull
    public HeatmapLayer withSourceLayer(String sourceLayer) {
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
    public HeatmapLayer withFilter(@NonNull Expression filter) {
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
    public HeatmapLayer withProperties(@NonNull PropertyValue<?>... properties) {
        setProperties(properties);
        return this;
    }

    @NonNull
    public PropertyValue<Float> getHeatmapRadius() {
        checkThread();
        return new PropertyValue<>("heatmap-radius", nativeGetHeatmapRadius());
    }

    @NonNull
    public TransitionOptions getHeatmapRadiusTransition() {
        checkThread();
        return nativeGetHeatmapRadiusTransition();
    }

    public void setHeatmapRadiusTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetHeatmapRadiusTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getHeatmapWeight() {
        checkThread();
        return new PropertyValue<>("heatmap-weight", nativeGetHeatmapWeight());
    }

    @NonNull
    public PropertyValue<Float> getHeatmapIntensity() {
        checkThread();
        return new PropertyValue<>("heatmap-intensity", nativeGetHeatmapIntensity());
    }

    @NonNull
    public TransitionOptions getHeatmapIntensityTransition() {
        checkThread();
        return nativeGetHeatmapIntensityTransition();
    }

    public void setHeatmapIntensityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetHeatmapIntensityTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getHeatmapColor() {
        checkThread();
        return new PropertyValue<>("heatmap-color", nativeGetHeatmapColor());
    }

    @ColorInt
    public int getHeatmapColorAsInt() {
        checkThread();
        PropertyValue<String> value = getHeatmapColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("heatmap-color was set as a Function");
    }

    @NonNull
    public PropertyValue<Float> getHeatmapOpacity() {
        checkThread();
        return new PropertyValue<>("heatmap-opacity", nativeGetHeatmapOpacity());
    }

    @NonNull
    public TransitionOptions getHeatmapOpacityTransition() {
        checkThread();
        return nativeGetHeatmapOpacityTransition();
    }

    public void setHeatmapOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetHeatmapOpacityTransition(options.getDuration(), options.getDelay());
    }
}
