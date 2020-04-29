package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

@UiThread
public class RasterLayer extends Layer {
    @Keep
    @NonNull
    private native Object nativeGetRasterBrightnessMax();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetRasterBrightnessMaxTransition();

    @Keep
    @NonNull
    private native Object nativeGetRasterBrightnessMin();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetRasterBrightnessMinTransition();

    @Keep
    @NonNull
    private native Object nativeGetRasterContrast();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetRasterContrastTransition();

    @Keep
    @NonNull
    private native Object nativeGetRasterFadeDuration();

    @Keep
    @NonNull
    private native Object nativeGetRasterHueRotate();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetRasterHueRotateTransition();

    @Keep
    @NonNull
    private native Object nativeGetRasterOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetRasterOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetRasterResampling();

    @Keep
    @NonNull
    private native Object nativeGetRasterSaturation();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetRasterSaturationTransition();

    @Keep
    private native void nativeSetRasterBrightnessMaxTransition(long j, long j2);

    @Keep
    private native void nativeSetRasterBrightnessMinTransition(long j, long j2);

    @Keep
    private native void nativeSetRasterContrastTransition(long j, long j2);

    @Keep
    private native void nativeSetRasterHueRotateTransition(long j, long j2);

    @Keep
    private native void nativeSetRasterOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetRasterSaturationTransition(long j, long j2);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, String str2);

    @Keep
    RasterLayer(long nativePtr) {
        super(nativePtr);
    }

    public RasterLayer(String layerId, String sourceId) {
        initialize(layerId, sourceId);
    }

    public void setSourceLayer(String sourceLayer) {
        checkThread();
        nativeSetSourceLayer(sourceLayer);
    }

    @NonNull
    public RasterLayer withSourceLayer(String sourceLayer) {
        setSourceLayer(sourceLayer);
        return this;
    }

    @NonNull
    public String getSourceId() {
        checkThread();
        return nativeGetSourceId();
    }

    @NonNull
    public RasterLayer withProperties(@NonNull PropertyValue<?>... properties) {
        setProperties(properties);
        return this;
    }

    @NonNull
    public PropertyValue<Float> getRasterOpacity() {
        checkThread();
        return new PropertyValue<>("raster-opacity", nativeGetRasterOpacity());
    }

    @NonNull
    public TransitionOptions getRasterOpacityTransition() {
        checkThread();
        return nativeGetRasterOpacityTransition();
    }

    public void setRasterOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetRasterOpacityTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getRasterHueRotate() {
        checkThread();
        return new PropertyValue<>("raster-hue-rotate", nativeGetRasterHueRotate());
    }

    @NonNull
    public TransitionOptions getRasterHueRotateTransition() {
        checkThread();
        return nativeGetRasterHueRotateTransition();
    }

    public void setRasterHueRotateTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetRasterHueRotateTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getRasterBrightnessMin() {
        checkThread();
        return new PropertyValue<>("raster-brightness-min", nativeGetRasterBrightnessMin());
    }

    @NonNull
    public TransitionOptions getRasterBrightnessMinTransition() {
        checkThread();
        return nativeGetRasterBrightnessMinTransition();
    }

    public void setRasterBrightnessMinTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetRasterBrightnessMinTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getRasterBrightnessMax() {
        checkThread();
        return new PropertyValue<>("raster-brightness-max", nativeGetRasterBrightnessMax());
    }

    @NonNull
    public TransitionOptions getRasterBrightnessMaxTransition() {
        checkThread();
        return nativeGetRasterBrightnessMaxTransition();
    }

    public void setRasterBrightnessMaxTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetRasterBrightnessMaxTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getRasterSaturation() {
        checkThread();
        return new PropertyValue<>("raster-saturation", nativeGetRasterSaturation());
    }

    @NonNull
    public TransitionOptions getRasterSaturationTransition() {
        checkThread();
        return nativeGetRasterSaturationTransition();
    }

    public void setRasterSaturationTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetRasterSaturationTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getRasterContrast() {
        checkThread();
        return new PropertyValue<>("raster-contrast", nativeGetRasterContrast());
    }

    @NonNull
    public TransitionOptions getRasterContrastTransition() {
        checkThread();
        return nativeGetRasterContrastTransition();
    }

    public void setRasterContrastTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetRasterContrastTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getRasterResampling() {
        checkThread();
        return new PropertyValue<>("raster-resampling", nativeGetRasterResampling());
    }

    @NonNull
    public PropertyValue<Float> getRasterFadeDuration() {
        checkThread();
        return new PropertyValue<>("raster-fade-duration", nativeGetRasterFadeDuration());
    }
}
