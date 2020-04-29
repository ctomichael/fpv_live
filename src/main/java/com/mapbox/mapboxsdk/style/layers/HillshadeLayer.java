package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.ColorInt;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.utils.ColorUtils;

@UiThread
public class HillshadeLayer extends Layer {
    @Keep
    @NonNull
    private native Object nativeGetHillshadeAccentColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetHillshadeAccentColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetHillshadeExaggeration();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetHillshadeExaggerationTransition();

    @Keep
    @NonNull
    private native Object nativeGetHillshadeHighlightColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetHillshadeHighlightColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetHillshadeIlluminationAnchor();

    @Keep
    @NonNull
    private native Object nativeGetHillshadeIlluminationDirection();

    @Keep
    @NonNull
    private native Object nativeGetHillshadeShadowColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetHillshadeShadowColorTransition();

    @Keep
    private native void nativeSetHillshadeAccentColorTransition(long j, long j2);

    @Keep
    private native void nativeSetHillshadeExaggerationTransition(long j, long j2);

    @Keep
    private native void nativeSetHillshadeHighlightColorTransition(long j, long j2);

    @Keep
    private native void nativeSetHillshadeShadowColorTransition(long j, long j2);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, String str2);

    @Keep
    HillshadeLayer(long nativePtr) {
        super(nativePtr);
    }

    public HillshadeLayer(String layerId, String sourceId) {
        initialize(layerId, sourceId);
    }

    public void setSourceLayer(String sourceLayer) {
        checkThread();
        nativeSetSourceLayer(sourceLayer);
    }

    @NonNull
    public HillshadeLayer withSourceLayer(String sourceLayer) {
        setSourceLayer(sourceLayer);
        return this;
    }

    @NonNull
    public String getSourceId() {
        checkThread();
        return nativeGetSourceId();
    }

    @NonNull
    public HillshadeLayer withProperties(@NonNull PropertyValue<?>... properties) {
        setProperties(properties);
        return this;
    }

    @NonNull
    public PropertyValue<Float> getHillshadeIlluminationDirection() {
        checkThread();
        return new PropertyValue<>("hillshade-illumination-direction", nativeGetHillshadeIlluminationDirection());
    }

    @NonNull
    public PropertyValue<String> getHillshadeIlluminationAnchor() {
        checkThread();
        return new PropertyValue<>("hillshade-illumination-anchor", nativeGetHillshadeIlluminationAnchor());
    }

    @NonNull
    public PropertyValue<Float> getHillshadeExaggeration() {
        checkThread();
        return new PropertyValue<>("hillshade-exaggeration", nativeGetHillshadeExaggeration());
    }

    @NonNull
    public TransitionOptions getHillshadeExaggerationTransition() {
        checkThread();
        return nativeGetHillshadeExaggerationTransition();
    }

    public void setHillshadeExaggerationTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetHillshadeExaggerationTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getHillshadeShadowColor() {
        checkThread();
        return new PropertyValue<>("hillshade-shadow-color", nativeGetHillshadeShadowColor());
    }

    @ColorInt
    public int getHillshadeShadowColorAsInt() {
        checkThread();
        PropertyValue<String> value = getHillshadeShadowColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("hillshade-shadow-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getHillshadeShadowColorTransition() {
        checkThread();
        return nativeGetHillshadeShadowColorTransition();
    }

    public void setHillshadeShadowColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetHillshadeShadowColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getHillshadeHighlightColor() {
        checkThread();
        return new PropertyValue<>("hillshade-highlight-color", nativeGetHillshadeHighlightColor());
    }

    @ColorInt
    public int getHillshadeHighlightColorAsInt() {
        checkThread();
        PropertyValue<String> value = getHillshadeHighlightColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("hillshade-highlight-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getHillshadeHighlightColorTransition() {
        checkThread();
        return nativeGetHillshadeHighlightColorTransition();
    }

    public void setHillshadeHighlightColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetHillshadeHighlightColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getHillshadeAccentColor() {
        checkThread();
        return new PropertyValue<>("hillshade-accent-color", nativeGetHillshadeAccentColor());
    }

    @ColorInt
    public int getHillshadeAccentColorAsInt() {
        checkThread();
        PropertyValue<String> value = getHillshadeAccentColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("hillshade-accent-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getHillshadeAccentColorTransition() {
        checkThread();
        return nativeGetHillshadeAccentColorTransition();
    }

    public void setHillshadeAccentColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetHillshadeAccentColorTransition(options.getDuration(), options.getDelay());
    }
}
