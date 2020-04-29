package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.ColorInt;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.google.gson.JsonElement;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.types.Formatted;
import com.mapbox.mapboxsdk.utils.ColorUtils;

@UiThread
public class SymbolLayer extends Layer {
    @Keep
    @NonNull
    private native Object nativeGetIconAllowOverlap();

    @Keep
    @NonNull
    private native Object nativeGetIconAnchor();

    @Keep
    @NonNull
    private native Object nativeGetIconColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetIconColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetIconHaloBlur();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetIconHaloBlurTransition();

    @Keep
    @NonNull
    private native Object nativeGetIconHaloColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetIconHaloColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetIconHaloWidth();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetIconHaloWidthTransition();

    @Keep
    @NonNull
    private native Object nativeGetIconIgnorePlacement();

    @Keep
    @NonNull
    private native Object nativeGetIconImage();

    @Keep
    @NonNull
    private native Object nativeGetIconKeepUpright();

    @Keep
    @NonNull
    private native Object nativeGetIconOffset();

    @Keep
    @NonNull
    private native Object nativeGetIconOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetIconOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetIconOptional();

    @Keep
    @NonNull
    private native Object nativeGetIconPadding();

    @Keep
    @NonNull
    private native Object nativeGetIconPitchAlignment();

    @Keep
    @NonNull
    private native Object nativeGetIconRotate();

    @Keep
    @NonNull
    private native Object nativeGetIconRotationAlignment();

    @Keep
    @NonNull
    private native Object nativeGetIconSize();

    @Keep
    @NonNull
    private native Object nativeGetIconTextFit();

    @Keep
    @NonNull
    private native Object nativeGetIconTextFitPadding();

    @Keep
    @NonNull
    private native Object nativeGetIconTranslate();

    @Keep
    @NonNull
    private native Object nativeGetIconTranslateAnchor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetIconTranslateTransition();

    @Keep
    @NonNull
    private native Object nativeGetSymbolAvoidEdges();

    @Keep
    @NonNull
    private native Object nativeGetSymbolPlacement();

    @Keep
    @NonNull
    private native Object nativeGetSymbolSortKey();

    @Keep
    @NonNull
    private native Object nativeGetSymbolSpacing();

    @Keep
    @NonNull
    private native Object nativeGetSymbolZOrder();

    @Keep
    @NonNull
    private native Object nativeGetTextAllowOverlap();

    @Keep
    @NonNull
    private native Object nativeGetTextAnchor();

    @Keep
    @NonNull
    private native Object nativeGetTextColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetTextColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetTextField();

    @Keep
    @NonNull
    private native Object nativeGetTextFont();

    @Keep
    @NonNull
    private native Object nativeGetTextHaloBlur();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetTextHaloBlurTransition();

    @Keep
    @NonNull
    private native Object nativeGetTextHaloColor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetTextHaloColorTransition();

    @Keep
    @NonNull
    private native Object nativeGetTextHaloWidth();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetTextHaloWidthTransition();

    @Keep
    @NonNull
    private native Object nativeGetTextIgnorePlacement();

    @Keep
    @NonNull
    private native Object nativeGetTextJustify();

    @Keep
    @NonNull
    private native Object nativeGetTextKeepUpright();

    @Keep
    @NonNull
    private native Object nativeGetTextLetterSpacing();

    @Keep
    @NonNull
    private native Object nativeGetTextLineHeight();

    @Keep
    @NonNull
    private native Object nativeGetTextMaxAngle();

    @Keep
    @NonNull
    private native Object nativeGetTextMaxWidth();

    @Keep
    @NonNull
    private native Object nativeGetTextOffset();

    @Keep
    @NonNull
    private native Object nativeGetTextOpacity();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetTextOpacityTransition();

    @Keep
    @NonNull
    private native Object nativeGetTextOptional();

    @Keep
    @NonNull
    private native Object nativeGetTextPadding();

    @Keep
    @NonNull
    private native Object nativeGetTextPitchAlignment();

    @Keep
    @NonNull
    private native Object nativeGetTextRadialOffset();

    @Keep
    @NonNull
    private native Object nativeGetTextRotate();

    @Keep
    @NonNull
    private native Object nativeGetTextRotationAlignment();

    @Keep
    @NonNull
    private native Object nativeGetTextSize();

    @Keep
    @NonNull
    private native Object nativeGetTextTransform();

    @Keep
    @NonNull
    private native Object nativeGetTextTranslate();

    @Keep
    @NonNull
    private native Object nativeGetTextTranslateAnchor();

    @Keep
    @NonNull
    private native TransitionOptions nativeGetTextTranslateTransition();

    @Keep
    @NonNull
    private native Object nativeGetTextVariableAnchor();

    @Keep
    @NonNull
    private native Object nativeGetTextWritingMode();

    @Keep
    private native void nativeSetIconColorTransition(long j, long j2);

    @Keep
    private native void nativeSetIconHaloBlurTransition(long j, long j2);

    @Keep
    private native void nativeSetIconHaloColorTransition(long j, long j2);

    @Keep
    private native void nativeSetIconHaloWidthTransition(long j, long j2);

    @Keep
    private native void nativeSetIconOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetIconTranslateTransition(long j, long j2);

    @Keep
    private native void nativeSetTextColorTransition(long j, long j2);

    @Keep
    private native void nativeSetTextHaloBlurTransition(long j, long j2);

    @Keep
    private native void nativeSetTextHaloColorTransition(long j, long j2);

    @Keep
    private native void nativeSetTextHaloWidthTransition(long j, long j2);

    @Keep
    private native void nativeSetTextOpacityTransition(long j, long j2);

    @Keep
    private native void nativeSetTextTranslateTransition(long j, long j2);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, String str2);

    @Keep
    SymbolLayer(long nativePtr) {
        super(nativePtr);
    }

    public SymbolLayer(String layerId, String sourceId) {
        initialize(layerId, sourceId);
    }

    public void setSourceLayer(String sourceLayer) {
        checkThread();
        nativeSetSourceLayer(sourceLayer);
    }

    @NonNull
    public SymbolLayer withSourceLayer(String sourceLayer) {
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
    public SymbolLayer withFilter(@NonNull Expression filter) {
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
    public SymbolLayer withProperties(@NonNull PropertyValue<?>... properties) {
        setProperties(properties);
        return this;
    }

    @NonNull
    public PropertyValue<String> getSymbolPlacement() {
        checkThread();
        return new PropertyValue<>("symbol-placement", nativeGetSymbolPlacement());
    }

    @NonNull
    public PropertyValue<Float> getSymbolSpacing() {
        checkThread();
        return new PropertyValue<>("symbol-spacing", nativeGetSymbolSpacing());
    }

    @NonNull
    public PropertyValue<Boolean> getSymbolAvoidEdges() {
        checkThread();
        return new PropertyValue<>("symbol-avoid-edges", nativeGetSymbolAvoidEdges());
    }

    @NonNull
    public PropertyValue<Float> getSymbolSortKey() {
        checkThread();
        return new PropertyValue<>("symbol-sort-key", nativeGetSymbolSortKey());
    }

    @NonNull
    public PropertyValue<String> getSymbolZOrder() {
        checkThread();
        return new PropertyValue<>("symbol-z-order", nativeGetSymbolZOrder());
    }

    @NonNull
    public PropertyValue<Boolean> getIconAllowOverlap() {
        checkThread();
        return new PropertyValue<>("icon-allow-overlap", nativeGetIconAllowOverlap());
    }

    @NonNull
    public PropertyValue<Boolean> getIconIgnorePlacement() {
        checkThread();
        return new PropertyValue<>("icon-ignore-placement", nativeGetIconIgnorePlacement());
    }

    @NonNull
    public PropertyValue<Boolean> getIconOptional() {
        checkThread();
        return new PropertyValue<>("icon-optional", nativeGetIconOptional());
    }

    @NonNull
    public PropertyValue<String> getIconRotationAlignment() {
        checkThread();
        return new PropertyValue<>("icon-rotation-alignment", nativeGetIconRotationAlignment());
    }

    @NonNull
    public PropertyValue<Float> getIconSize() {
        checkThread();
        return new PropertyValue<>("icon-size", nativeGetIconSize());
    }

    @NonNull
    public PropertyValue<String> getIconTextFit() {
        checkThread();
        return new PropertyValue<>("icon-text-fit", nativeGetIconTextFit());
    }

    @NonNull
    public PropertyValue<Float[]> getIconTextFitPadding() {
        checkThread();
        return new PropertyValue<>("icon-text-fit-padding", nativeGetIconTextFitPadding());
    }

    @NonNull
    public PropertyValue<String> getIconImage() {
        checkThread();
        return new PropertyValue<>("icon-image", nativeGetIconImage());
    }

    @NonNull
    public PropertyValue<Float> getIconRotate() {
        checkThread();
        return new PropertyValue<>("icon-rotate", nativeGetIconRotate());
    }

    @NonNull
    public PropertyValue<Float> getIconPadding() {
        checkThread();
        return new PropertyValue<>("icon-padding", nativeGetIconPadding());
    }

    @NonNull
    public PropertyValue<Boolean> getIconKeepUpright() {
        checkThread();
        return new PropertyValue<>("icon-keep-upright", nativeGetIconKeepUpright());
    }

    @NonNull
    public PropertyValue<Float[]> getIconOffset() {
        checkThread();
        return new PropertyValue<>("icon-offset", nativeGetIconOffset());
    }

    @NonNull
    public PropertyValue<String> getIconAnchor() {
        checkThread();
        return new PropertyValue<>("icon-anchor", nativeGetIconAnchor());
    }

    @NonNull
    public PropertyValue<String> getIconPitchAlignment() {
        checkThread();
        return new PropertyValue<>("icon-pitch-alignment", nativeGetIconPitchAlignment());
    }

    @NonNull
    public PropertyValue<String> getTextPitchAlignment() {
        checkThread();
        return new PropertyValue<>("text-pitch-alignment", nativeGetTextPitchAlignment());
    }

    @NonNull
    public PropertyValue<String> getTextRotationAlignment() {
        checkThread();
        return new PropertyValue<>("text-rotation-alignment", nativeGetTextRotationAlignment());
    }

    @NonNull
    public PropertyValue<Formatted> getTextField() {
        checkThread();
        return new PropertyValue<>("text-field", nativeGetTextField());
    }

    @NonNull
    public PropertyValue<String[]> getTextFont() {
        checkThread();
        return new PropertyValue<>("text-font", nativeGetTextFont());
    }

    @NonNull
    public PropertyValue<Float> getTextSize() {
        checkThread();
        return new PropertyValue<>("text-size", nativeGetTextSize());
    }

    @NonNull
    public PropertyValue<Float> getTextMaxWidth() {
        checkThread();
        return new PropertyValue<>("text-max-width", nativeGetTextMaxWidth());
    }

    @NonNull
    public PropertyValue<Float> getTextLineHeight() {
        checkThread();
        return new PropertyValue<>("text-line-height", nativeGetTextLineHeight());
    }

    @NonNull
    public PropertyValue<Float> getTextLetterSpacing() {
        checkThread();
        return new PropertyValue<>("text-letter-spacing", nativeGetTextLetterSpacing());
    }

    @NonNull
    public PropertyValue<String> getTextJustify() {
        checkThread();
        return new PropertyValue<>("text-justify", nativeGetTextJustify());
    }

    @NonNull
    public PropertyValue<Float> getTextRadialOffset() {
        checkThread();
        return new PropertyValue<>("text-radial-offset", nativeGetTextRadialOffset());
    }

    @NonNull
    public PropertyValue<String[]> getTextVariableAnchor() {
        checkThread();
        return new PropertyValue<>("text-variable-anchor", nativeGetTextVariableAnchor());
    }

    @NonNull
    public PropertyValue<String> getTextAnchor() {
        checkThread();
        return new PropertyValue<>("text-anchor", nativeGetTextAnchor());
    }

    @NonNull
    public PropertyValue<Float> getTextMaxAngle() {
        checkThread();
        return new PropertyValue<>("text-max-angle", nativeGetTextMaxAngle());
    }

    @NonNull
    public PropertyValue<String[]> getTextWritingMode() {
        checkThread();
        return new PropertyValue<>("text-writing-mode", nativeGetTextWritingMode());
    }

    @NonNull
    public PropertyValue<Float> getTextRotate() {
        checkThread();
        return new PropertyValue<>("text-rotate", nativeGetTextRotate());
    }

    @NonNull
    public PropertyValue<Float> getTextPadding() {
        checkThread();
        return new PropertyValue<>("text-padding", nativeGetTextPadding());
    }

    @NonNull
    public PropertyValue<Boolean> getTextKeepUpright() {
        checkThread();
        return new PropertyValue<>("text-keep-upright", nativeGetTextKeepUpright());
    }

    @NonNull
    public PropertyValue<String> getTextTransform() {
        checkThread();
        return new PropertyValue<>("text-transform", nativeGetTextTransform());
    }

    @NonNull
    public PropertyValue<Float[]> getTextOffset() {
        checkThread();
        return new PropertyValue<>("text-offset", nativeGetTextOffset());
    }

    @NonNull
    public PropertyValue<Boolean> getTextAllowOverlap() {
        checkThread();
        return new PropertyValue<>("text-allow-overlap", nativeGetTextAllowOverlap());
    }

    @NonNull
    public PropertyValue<Boolean> getTextIgnorePlacement() {
        checkThread();
        return new PropertyValue<>("text-ignore-placement", nativeGetTextIgnorePlacement());
    }

    @NonNull
    public PropertyValue<Boolean> getTextOptional() {
        checkThread();
        return new PropertyValue<>("text-optional", nativeGetTextOptional());
    }

    @NonNull
    public PropertyValue<Float> getIconOpacity() {
        checkThread();
        return new PropertyValue<>("icon-opacity", nativeGetIconOpacity());
    }

    @NonNull
    public TransitionOptions getIconOpacityTransition() {
        checkThread();
        return nativeGetIconOpacityTransition();
    }

    public void setIconOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetIconOpacityTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getIconColor() {
        checkThread();
        return new PropertyValue<>("icon-color", nativeGetIconColor());
    }

    @ColorInt
    public int getIconColorAsInt() {
        checkThread();
        PropertyValue<String> value = getIconColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("icon-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getIconColorTransition() {
        checkThread();
        return nativeGetIconColorTransition();
    }

    public void setIconColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetIconColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getIconHaloColor() {
        checkThread();
        return new PropertyValue<>("icon-halo-color", nativeGetIconHaloColor());
    }

    @ColorInt
    public int getIconHaloColorAsInt() {
        checkThread();
        PropertyValue<String> value = getIconHaloColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("icon-halo-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getIconHaloColorTransition() {
        checkThread();
        return nativeGetIconHaloColorTransition();
    }

    public void setIconHaloColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetIconHaloColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getIconHaloWidth() {
        checkThread();
        return new PropertyValue<>("icon-halo-width", nativeGetIconHaloWidth());
    }

    @NonNull
    public TransitionOptions getIconHaloWidthTransition() {
        checkThread();
        return nativeGetIconHaloWidthTransition();
    }

    public void setIconHaloWidthTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetIconHaloWidthTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getIconHaloBlur() {
        checkThread();
        return new PropertyValue<>("icon-halo-blur", nativeGetIconHaloBlur());
    }

    @NonNull
    public TransitionOptions getIconHaloBlurTransition() {
        checkThread();
        return nativeGetIconHaloBlurTransition();
    }

    public void setIconHaloBlurTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetIconHaloBlurTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float[]> getIconTranslate() {
        checkThread();
        return new PropertyValue<>("icon-translate", nativeGetIconTranslate());
    }

    @NonNull
    public TransitionOptions getIconTranslateTransition() {
        checkThread();
        return nativeGetIconTranslateTransition();
    }

    public void setIconTranslateTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetIconTranslateTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getIconTranslateAnchor() {
        checkThread();
        return new PropertyValue<>("icon-translate-anchor", nativeGetIconTranslateAnchor());
    }

    @NonNull
    public PropertyValue<Float> getTextOpacity() {
        checkThread();
        return new PropertyValue<>("text-opacity", nativeGetTextOpacity());
    }

    @NonNull
    public TransitionOptions getTextOpacityTransition() {
        checkThread();
        return nativeGetTextOpacityTransition();
    }

    public void setTextOpacityTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetTextOpacityTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getTextColor() {
        checkThread();
        return new PropertyValue<>("text-color", nativeGetTextColor());
    }

    @ColorInt
    public int getTextColorAsInt() {
        checkThread();
        PropertyValue<String> value = getTextColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("text-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getTextColorTransition() {
        checkThread();
        return nativeGetTextColorTransition();
    }

    public void setTextColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetTextColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getTextHaloColor() {
        checkThread();
        return new PropertyValue<>("text-halo-color", nativeGetTextHaloColor());
    }

    @ColorInt
    public int getTextHaloColorAsInt() {
        checkThread();
        PropertyValue<String> value = getTextHaloColor();
        if (value.isValue()) {
            return ColorUtils.rgbaToColor(value.getValue());
        }
        throw new RuntimeException("text-halo-color was set as a Function");
    }

    @NonNull
    public TransitionOptions getTextHaloColorTransition() {
        checkThread();
        return nativeGetTextHaloColorTransition();
    }

    public void setTextHaloColorTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetTextHaloColorTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getTextHaloWidth() {
        checkThread();
        return new PropertyValue<>("text-halo-width", nativeGetTextHaloWidth());
    }

    @NonNull
    public TransitionOptions getTextHaloWidthTransition() {
        checkThread();
        return nativeGetTextHaloWidthTransition();
    }

    public void setTextHaloWidthTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetTextHaloWidthTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float> getTextHaloBlur() {
        checkThread();
        return new PropertyValue<>("text-halo-blur", nativeGetTextHaloBlur());
    }

    @NonNull
    public TransitionOptions getTextHaloBlurTransition() {
        checkThread();
        return nativeGetTextHaloBlurTransition();
    }

    public void setTextHaloBlurTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetTextHaloBlurTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<Float[]> getTextTranslate() {
        checkThread();
        return new PropertyValue<>("text-translate", nativeGetTextTranslate());
    }

    @NonNull
    public TransitionOptions getTextTranslateTransition() {
        checkThread();
        return nativeGetTextTranslateTransition();
    }

    public void setTextTranslateTransition(@NonNull TransitionOptions options) {
        checkThread();
        nativeSetTextTranslateTransition(options.getDuration(), options.getDelay());
    }

    @NonNull
    public PropertyValue<String> getTextTranslateAnchor() {
        checkThread();
        return new PropertyValue<>("text-translate-anchor", nativeGetTextTranslateAnchor());
    }
}
