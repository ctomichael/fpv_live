package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.ColorInt;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.types.Formatted;
import com.mapbox.mapboxsdk.utils.ColorUtils;

public class PropertyFactory {
    public static PropertyValue<String> visibility(String value) {
        return new LayoutPropertyValue("visibility", value);
    }

    public static PropertyValue<Boolean> fillAntialias(Boolean value) {
        return new PaintPropertyValue("fill-antialias", value);
    }

    public static PropertyValue<Expression> fillAntialias(Expression expression) {
        return new PaintPropertyValue("fill-antialias", expression);
    }

    public static PropertyValue<Float> fillOpacity(Float value) {
        return new PaintPropertyValue("fill-opacity", value);
    }

    public static PropertyValue<Expression> fillOpacity(Expression expression) {
        return new PaintPropertyValue("fill-opacity", expression);
    }

    public static PropertyValue<String> fillColor(@ColorInt int value) {
        return new PaintPropertyValue("fill-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> fillColor(String value) {
        return new PaintPropertyValue("fill-color", value);
    }

    public static PropertyValue<Expression> fillColor(Expression expression) {
        return new PaintPropertyValue("fill-color", expression);
    }

    public static PropertyValue<String> fillOutlineColor(@ColorInt int value) {
        return new PaintPropertyValue("fill-outline-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> fillOutlineColor(String value) {
        return new PaintPropertyValue("fill-outline-color", value);
    }

    public static PropertyValue<Expression> fillOutlineColor(Expression expression) {
        return new PaintPropertyValue("fill-outline-color", expression);
    }

    public static PropertyValue<Float[]> fillTranslate(Float[] value) {
        return new PaintPropertyValue("fill-translate", value);
    }

    public static PropertyValue<Expression> fillTranslate(Expression expression) {
        return new PaintPropertyValue("fill-translate", expression);
    }

    public static PropertyValue<String> fillTranslateAnchor(String value) {
        return new PaintPropertyValue("fill-translate-anchor", value);
    }

    public static PropertyValue<Expression> fillTranslateAnchor(Expression expression) {
        return new PaintPropertyValue("fill-translate-anchor", expression);
    }

    public static PropertyValue<String> fillPattern(String value) {
        return new PaintPropertyValue("fill-pattern", value);
    }

    public static PropertyValue<Expression> fillPattern(Expression expression) {
        return new PaintPropertyValue("fill-pattern", expression);
    }

    public static PropertyValue<Float> lineOpacity(Float value) {
        return new PaintPropertyValue("line-opacity", value);
    }

    public static PropertyValue<Expression> lineOpacity(Expression expression) {
        return new PaintPropertyValue("line-opacity", expression);
    }

    public static PropertyValue<String> lineColor(@ColorInt int value) {
        return new PaintPropertyValue("line-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> lineColor(String value) {
        return new PaintPropertyValue("line-color", value);
    }

    public static PropertyValue<Expression> lineColor(Expression expression) {
        return new PaintPropertyValue("line-color", expression);
    }

    public static PropertyValue<Float[]> lineTranslate(Float[] value) {
        return new PaintPropertyValue("line-translate", value);
    }

    public static PropertyValue<Expression> lineTranslate(Expression expression) {
        return new PaintPropertyValue("line-translate", expression);
    }

    public static PropertyValue<String> lineTranslateAnchor(String value) {
        return new PaintPropertyValue("line-translate-anchor", value);
    }

    public static PropertyValue<Expression> lineTranslateAnchor(Expression expression) {
        return new PaintPropertyValue("line-translate-anchor", expression);
    }

    public static PropertyValue<Float> lineWidth(Float value) {
        return new PaintPropertyValue("line-width", value);
    }

    public static PropertyValue<Expression> lineWidth(Expression expression) {
        return new PaintPropertyValue("line-width", expression);
    }

    public static PropertyValue<Float> lineGapWidth(Float value) {
        return new PaintPropertyValue("line-gap-width", value);
    }

    public static PropertyValue<Expression> lineGapWidth(Expression expression) {
        return new PaintPropertyValue("line-gap-width", expression);
    }

    public static PropertyValue<Float> lineOffset(Float value) {
        return new PaintPropertyValue("line-offset", value);
    }

    public static PropertyValue<Expression> lineOffset(Expression expression) {
        return new PaintPropertyValue("line-offset", expression);
    }

    public static PropertyValue<Float> lineBlur(Float value) {
        return new PaintPropertyValue("line-blur", value);
    }

    public static PropertyValue<Expression> lineBlur(Expression expression) {
        return new PaintPropertyValue("line-blur", expression);
    }

    public static PropertyValue<Float[]> lineDasharray(Float[] value) {
        return new PaintPropertyValue("line-dasharray", value);
    }

    public static PropertyValue<Expression> lineDasharray(Expression expression) {
        return new PaintPropertyValue("line-dasharray", expression);
    }

    public static PropertyValue<String> linePattern(String value) {
        return new PaintPropertyValue("line-pattern", value);
    }

    public static PropertyValue<Expression> linePattern(Expression expression) {
        return new PaintPropertyValue("line-pattern", expression);
    }

    public static PropertyValue<String> lineGradient(@ColorInt int value) {
        return new PaintPropertyValue("line-gradient", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> lineGradient(String value) {
        return new PaintPropertyValue("line-gradient", value);
    }

    public static PropertyValue<Expression> lineGradient(Expression expression) {
        return new PaintPropertyValue("line-gradient", expression);
    }

    public static PropertyValue<Float> iconOpacity(Float value) {
        return new PaintPropertyValue("icon-opacity", value);
    }

    public static PropertyValue<Expression> iconOpacity(Expression expression) {
        return new PaintPropertyValue("icon-opacity", expression);
    }

    public static PropertyValue<String> iconColor(@ColorInt int value) {
        return new PaintPropertyValue("icon-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> iconColor(String value) {
        return new PaintPropertyValue("icon-color", value);
    }

    public static PropertyValue<Expression> iconColor(Expression expression) {
        return new PaintPropertyValue("icon-color", expression);
    }

    public static PropertyValue<String> iconHaloColor(@ColorInt int value) {
        return new PaintPropertyValue("icon-halo-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> iconHaloColor(String value) {
        return new PaintPropertyValue("icon-halo-color", value);
    }

    public static PropertyValue<Expression> iconHaloColor(Expression expression) {
        return new PaintPropertyValue("icon-halo-color", expression);
    }

    public static PropertyValue<Float> iconHaloWidth(Float value) {
        return new PaintPropertyValue("icon-halo-width", value);
    }

    public static PropertyValue<Expression> iconHaloWidth(Expression expression) {
        return new PaintPropertyValue("icon-halo-width", expression);
    }

    public static PropertyValue<Float> iconHaloBlur(Float value) {
        return new PaintPropertyValue("icon-halo-blur", value);
    }

    public static PropertyValue<Expression> iconHaloBlur(Expression expression) {
        return new PaintPropertyValue("icon-halo-blur", expression);
    }

    public static PropertyValue<Float[]> iconTranslate(Float[] value) {
        return new PaintPropertyValue("icon-translate", value);
    }

    public static PropertyValue<Expression> iconTranslate(Expression expression) {
        return new PaintPropertyValue("icon-translate", expression);
    }

    public static PropertyValue<String> iconTranslateAnchor(String value) {
        return new PaintPropertyValue("icon-translate-anchor", value);
    }

    public static PropertyValue<Expression> iconTranslateAnchor(Expression expression) {
        return new PaintPropertyValue("icon-translate-anchor", expression);
    }

    public static PropertyValue<Float> textOpacity(Float value) {
        return new PaintPropertyValue("text-opacity", value);
    }

    public static PropertyValue<Expression> textOpacity(Expression expression) {
        return new PaintPropertyValue("text-opacity", expression);
    }

    public static PropertyValue<String> textColor(@ColorInt int value) {
        return new PaintPropertyValue("text-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> textColor(String value) {
        return new PaintPropertyValue("text-color", value);
    }

    public static PropertyValue<Expression> textColor(Expression expression) {
        return new PaintPropertyValue("text-color", expression);
    }

    public static PropertyValue<String> textHaloColor(@ColorInt int value) {
        return new PaintPropertyValue("text-halo-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> textHaloColor(String value) {
        return new PaintPropertyValue("text-halo-color", value);
    }

    public static PropertyValue<Expression> textHaloColor(Expression expression) {
        return new PaintPropertyValue("text-halo-color", expression);
    }

    public static PropertyValue<Float> textHaloWidth(Float value) {
        return new PaintPropertyValue("text-halo-width", value);
    }

    public static PropertyValue<Expression> textHaloWidth(Expression expression) {
        return new PaintPropertyValue("text-halo-width", expression);
    }

    public static PropertyValue<Float> textHaloBlur(Float value) {
        return new PaintPropertyValue("text-halo-blur", value);
    }

    public static PropertyValue<Expression> textHaloBlur(Expression expression) {
        return new PaintPropertyValue("text-halo-blur", expression);
    }

    public static PropertyValue<Float[]> textTranslate(Float[] value) {
        return new PaintPropertyValue("text-translate", value);
    }

    public static PropertyValue<Expression> textTranslate(Expression expression) {
        return new PaintPropertyValue("text-translate", expression);
    }

    public static PropertyValue<String> textTranslateAnchor(String value) {
        return new PaintPropertyValue("text-translate-anchor", value);
    }

    public static PropertyValue<Expression> textTranslateAnchor(Expression expression) {
        return new PaintPropertyValue("text-translate-anchor", expression);
    }

    public static PropertyValue<Float> circleRadius(Float value) {
        return new PaintPropertyValue("circle-radius", value);
    }

    public static PropertyValue<Expression> circleRadius(Expression expression) {
        return new PaintPropertyValue("circle-radius", expression);
    }

    public static PropertyValue<String> circleColor(@ColorInt int value) {
        return new PaintPropertyValue("circle-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> circleColor(String value) {
        return new PaintPropertyValue("circle-color", value);
    }

    public static PropertyValue<Expression> circleColor(Expression expression) {
        return new PaintPropertyValue("circle-color", expression);
    }

    public static PropertyValue<Float> circleBlur(Float value) {
        return new PaintPropertyValue("circle-blur", value);
    }

    public static PropertyValue<Expression> circleBlur(Expression expression) {
        return new PaintPropertyValue("circle-blur", expression);
    }

    public static PropertyValue<Float> circleOpacity(Float value) {
        return new PaintPropertyValue("circle-opacity", value);
    }

    public static PropertyValue<Expression> circleOpacity(Expression expression) {
        return new PaintPropertyValue("circle-opacity", expression);
    }

    public static PropertyValue<Float[]> circleTranslate(Float[] value) {
        return new PaintPropertyValue("circle-translate", value);
    }

    public static PropertyValue<Expression> circleTranslate(Expression expression) {
        return new PaintPropertyValue("circle-translate", expression);
    }

    public static PropertyValue<String> circleTranslateAnchor(String value) {
        return new PaintPropertyValue("circle-translate-anchor", value);
    }

    public static PropertyValue<Expression> circleTranslateAnchor(Expression expression) {
        return new PaintPropertyValue("circle-translate-anchor", expression);
    }

    public static PropertyValue<String> circlePitchScale(String value) {
        return new PaintPropertyValue("circle-pitch-scale", value);
    }

    public static PropertyValue<Expression> circlePitchScale(Expression expression) {
        return new PaintPropertyValue("circle-pitch-scale", expression);
    }

    public static PropertyValue<String> circlePitchAlignment(String value) {
        return new PaintPropertyValue("circle-pitch-alignment", value);
    }

    public static PropertyValue<Expression> circlePitchAlignment(Expression expression) {
        return new PaintPropertyValue("circle-pitch-alignment", expression);
    }

    public static PropertyValue<Float> circleStrokeWidth(Float value) {
        return new PaintPropertyValue("circle-stroke-width", value);
    }

    public static PropertyValue<Expression> circleStrokeWidth(Expression expression) {
        return new PaintPropertyValue("circle-stroke-width", expression);
    }

    public static PropertyValue<String> circleStrokeColor(@ColorInt int value) {
        return new PaintPropertyValue("circle-stroke-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> circleStrokeColor(String value) {
        return new PaintPropertyValue("circle-stroke-color", value);
    }

    public static PropertyValue<Expression> circleStrokeColor(Expression expression) {
        return new PaintPropertyValue("circle-stroke-color", expression);
    }

    public static PropertyValue<Float> circleStrokeOpacity(Float value) {
        return new PaintPropertyValue("circle-stroke-opacity", value);
    }

    public static PropertyValue<Expression> circleStrokeOpacity(Expression expression) {
        return new PaintPropertyValue("circle-stroke-opacity", expression);
    }

    public static PropertyValue<Float> heatmapRadius(Float value) {
        return new PaintPropertyValue("heatmap-radius", value);
    }

    public static PropertyValue<Expression> heatmapRadius(Expression expression) {
        return new PaintPropertyValue("heatmap-radius", expression);
    }

    public static PropertyValue<Float> heatmapWeight(Float value) {
        return new PaintPropertyValue("heatmap-weight", value);
    }

    public static PropertyValue<Expression> heatmapWeight(Expression expression) {
        return new PaintPropertyValue("heatmap-weight", expression);
    }

    public static PropertyValue<Float> heatmapIntensity(Float value) {
        return new PaintPropertyValue("heatmap-intensity", value);
    }

    public static PropertyValue<Expression> heatmapIntensity(Expression expression) {
        return new PaintPropertyValue("heatmap-intensity", expression);
    }

    public static PropertyValue<String> heatmapColor(@ColorInt int value) {
        return new PaintPropertyValue("heatmap-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> heatmapColor(String value) {
        return new PaintPropertyValue("heatmap-color", value);
    }

    public static PropertyValue<Expression> heatmapColor(Expression expression) {
        return new PaintPropertyValue("heatmap-color", expression);
    }

    public static PropertyValue<Float> heatmapOpacity(Float value) {
        return new PaintPropertyValue("heatmap-opacity", value);
    }

    public static PropertyValue<Expression> heatmapOpacity(Expression expression) {
        return new PaintPropertyValue("heatmap-opacity", expression);
    }

    public static PropertyValue<Float> fillExtrusionOpacity(Float value) {
        return new PaintPropertyValue("fill-extrusion-opacity", value);
    }

    public static PropertyValue<Expression> fillExtrusionOpacity(Expression expression) {
        return new PaintPropertyValue("fill-extrusion-opacity", expression);
    }

    public static PropertyValue<String> fillExtrusionColor(@ColorInt int value) {
        return new PaintPropertyValue("fill-extrusion-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> fillExtrusionColor(String value) {
        return new PaintPropertyValue("fill-extrusion-color", value);
    }

    public static PropertyValue<Expression> fillExtrusionColor(Expression expression) {
        return new PaintPropertyValue("fill-extrusion-color", expression);
    }

    public static PropertyValue<Float[]> fillExtrusionTranslate(Float[] value) {
        return new PaintPropertyValue("fill-extrusion-translate", value);
    }

    public static PropertyValue<Expression> fillExtrusionTranslate(Expression expression) {
        return new PaintPropertyValue("fill-extrusion-translate", expression);
    }

    public static PropertyValue<String> fillExtrusionTranslateAnchor(String value) {
        return new PaintPropertyValue("fill-extrusion-translate-anchor", value);
    }

    public static PropertyValue<Expression> fillExtrusionTranslateAnchor(Expression expression) {
        return new PaintPropertyValue("fill-extrusion-translate-anchor", expression);
    }

    public static PropertyValue<String> fillExtrusionPattern(String value) {
        return new PaintPropertyValue("fill-extrusion-pattern", value);
    }

    public static PropertyValue<Expression> fillExtrusionPattern(Expression expression) {
        return new PaintPropertyValue("fill-extrusion-pattern", expression);
    }

    public static PropertyValue<Float> fillExtrusionHeight(Float value) {
        return new PaintPropertyValue("fill-extrusion-height", value);
    }

    public static PropertyValue<Expression> fillExtrusionHeight(Expression expression) {
        return new PaintPropertyValue("fill-extrusion-height", expression);
    }

    public static PropertyValue<Float> fillExtrusionBase(Float value) {
        return new PaintPropertyValue("fill-extrusion-base", value);
    }

    public static PropertyValue<Expression> fillExtrusionBase(Expression expression) {
        return new PaintPropertyValue("fill-extrusion-base", expression);
    }

    public static PropertyValue<Boolean> fillExtrusionVerticalGradient(Boolean value) {
        return new PaintPropertyValue("fill-extrusion-vertical-gradient", value);
    }

    public static PropertyValue<Expression> fillExtrusionVerticalGradient(Expression expression) {
        return new PaintPropertyValue("fill-extrusion-vertical-gradient", expression);
    }

    public static PropertyValue<Float> rasterOpacity(Float value) {
        return new PaintPropertyValue("raster-opacity", value);
    }

    public static PropertyValue<Expression> rasterOpacity(Expression expression) {
        return new PaintPropertyValue("raster-opacity", expression);
    }

    public static PropertyValue<Float> rasterHueRotate(Float value) {
        return new PaintPropertyValue("raster-hue-rotate", value);
    }

    public static PropertyValue<Expression> rasterHueRotate(Expression expression) {
        return new PaintPropertyValue("raster-hue-rotate", expression);
    }

    public static PropertyValue<Float> rasterBrightnessMin(Float value) {
        return new PaintPropertyValue("raster-brightness-min", value);
    }

    public static PropertyValue<Expression> rasterBrightnessMin(Expression expression) {
        return new PaintPropertyValue("raster-brightness-min", expression);
    }

    public static PropertyValue<Float> rasterBrightnessMax(Float value) {
        return new PaintPropertyValue("raster-brightness-max", value);
    }

    public static PropertyValue<Expression> rasterBrightnessMax(Expression expression) {
        return new PaintPropertyValue("raster-brightness-max", expression);
    }

    public static PropertyValue<Float> rasterSaturation(Float value) {
        return new PaintPropertyValue("raster-saturation", value);
    }

    public static PropertyValue<Expression> rasterSaturation(Expression expression) {
        return new PaintPropertyValue("raster-saturation", expression);
    }

    public static PropertyValue<Float> rasterContrast(Float value) {
        return new PaintPropertyValue("raster-contrast", value);
    }

    public static PropertyValue<Expression> rasterContrast(Expression expression) {
        return new PaintPropertyValue("raster-contrast", expression);
    }

    public static PropertyValue<String> rasterResampling(String value) {
        return new PaintPropertyValue("raster-resampling", value);
    }

    public static PropertyValue<Expression> rasterResampling(Expression expression) {
        return new PaintPropertyValue("raster-resampling", expression);
    }

    public static PropertyValue<Float> rasterFadeDuration(Float value) {
        return new PaintPropertyValue("raster-fade-duration", value);
    }

    public static PropertyValue<Expression> rasterFadeDuration(Expression expression) {
        return new PaintPropertyValue("raster-fade-duration", expression);
    }

    public static PropertyValue<Float> hillshadeIlluminationDirection(Float value) {
        return new PaintPropertyValue("hillshade-illumination-direction", value);
    }

    public static PropertyValue<Expression> hillshadeIlluminationDirection(Expression expression) {
        return new PaintPropertyValue("hillshade-illumination-direction", expression);
    }

    public static PropertyValue<String> hillshadeIlluminationAnchor(String value) {
        return new PaintPropertyValue("hillshade-illumination-anchor", value);
    }

    public static PropertyValue<Expression> hillshadeIlluminationAnchor(Expression expression) {
        return new PaintPropertyValue("hillshade-illumination-anchor", expression);
    }

    public static PropertyValue<Float> hillshadeExaggeration(Float value) {
        return new PaintPropertyValue("hillshade-exaggeration", value);
    }

    public static PropertyValue<Expression> hillshadeExaggeration(Expression expression) {
        return new PaintPropertyValue("hillshade-exaggeration", expression);
    }

    public static PropertyValue<String> hillshadeShadowColor(@ColorInt int value) {
        return new PaintPropertyValue("hillshade-shadow-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> hillshadeShadowColor(String value) {
        return new PaintPropertyValue("hillshade-shadow-color", value);
    }

    public static PropertyValue<Expression> hillshadeShadowColor(Expression expression) {
        return new PaintPropertyValue("hillshade-shadow-color", expression);
    }

    public static PropertyValue<String> hillshadeHighlightColor(@ColorInt int value) {
        return new PaintPropertyValue("hillshade-highlight-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> hillshadeHighlightColor(String value) {
        return new PaintPropertyValue("hillshade-highlight-color", value);
    }

    public static PropertyValue<Expression> hillshadeHighlightColor(Expression expression) {
        return new PaintPropertyValue("hillshade-highlight-color", expression);
    }

    public static PropertyValue<String> hillshadeAccentColor(@ColorInt int value) {
        return new PaintPropertyValue("hillshade-accent-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> hillshadeAccentColor(String value) {
        return new PaintPropertyValue("hillshade-accent-color", value);
    }

    public static PropertyValue<Expression> hillshadeAccentColor(Expression expression) {
        return new PaintPropertyValue("hillshade-accent-color", expression);
    }

    public static PropertyValue<String> backgroundColor(@ColorInt int value) {
        return new PaintPropertyValue("background-color", ColorUtils.colorToRgbaString(value));
    }

    public static PropertyValue<String> backgroundColor(String value) {
        return new PaintPropertyValue("background-color", value);
    }

    public static PropertyValue<Expression> backgroundColor(Expression expression) {
        return new PaintPropertyValue("background-color", expression);
    }

    public static PropertyValue<String> backgroundPattern(String value) {
        return new PaintPropertyValue("background-pattern", value);
    }

    public static PropertyValue<Expression> backgroundPattern(Expression expression) {
        return new PaintPropertyValue("background-pattern", expression);
    }

    public static PropertyValue<Float> backgroundOpacity(Float value) {
        return new PaintPropertyValue("background-opacity", value);
    }

    public static PropertyValue<Expression> backgroundOpacity(Expression expression) {
        return new PaintPropertyValue("background-opacity", expression);
    }

    public static PropertyValue<String> lineCap(String value) {
        return new LayoutPropertyValue("line-cap", value);
    }

    public static PropertyValue<Expression> lineCap(Expression value) {
        return new LayoutPropertyValue("line-cap", value);
    }

    public static PropertyValue<String> lineJoin(String value) {
        return new LayoutPropertyValue("line-join", value);
    }

    public static PropertyValue<Expression> lineJoin(Expression value) {
        return new LayoutPropertyValue("line-join", value);
    }

    public static PropertyValue<Float> lineMiterLimit(Float value) {
        return new LayoutPropertyValue("line-miter-limit", value);
    }

    public static PropertyValue<Expression> lineMiterLimit(Expression value) {
        return new LayoutPropertyValue("line-miter-limit", value);
    }

    public static PropertyValue<Float> lineRoundLimit(Float value) {
        return new LayoutPropertyValue("line-round-limit", value);
    }

    public static PropertyValue<Expression> lineRoundLimit(Expression value) {
        return new LayoutPropertyValue("line-round-limit", value);
    }

    public static PropertyValue<String> symbolPlacement(String value) {
        return new LayoutPropertyValue("symbol-placement", value);
    }

    public static PropertyValue<Expression> symbolPlacement(Expression value) {
        return new LayoutPropertyValue("symbol-placement", value);
    }

    public static PropertyValue<Float> symbolSpacing(Float value) {
        return new LayoutPropertyValue("symbol-spacing", value);
    }

    public static PropertyValue<Expression> symbolSpacing(Expression value) {
        return new LayoutPropertyValue("symbol-spacing", value);
    }

    public static PropertyValue<Boolean> symbolAvoidEdges(Boolean value) {
        return new LayoutPropertyValue("symbol-avoid-edges", value);
    }

    public static PropertyValue<Expression> symbolAvoidEdges(Expression value) {
        return new LayoutPropertyValue("symbol-avoid-edges", value);
    }

    public static PropertyValue<Float> symbolSortKey(Float value) {
        return new LayoutPropertyValue("symbol-sort-key", value);
    }

    public static PropertyValue<Expression> symbolSortKey(Expression value) {
        return new LayoutPropertyValue("symbol-sort-key", value);
    }

    public static PropertyValue<String> symbolZOrder(String value) {
        return new LayoutPropertyValue("symbol-z-order", value);
    }

    public static PropertyValue<Expression> symbolZOrder(Expression value) {
        return new LayoutPropertyValue("symbol-z-order", value);
    }

    public static PropertyValue<Boolean> iconAllowOverlap(Boolean value) {
        return new LayoutPropertyValue("icon-allow-overlap", value);
    }

    public static PropertyValue<Expression> iconAllowOverlap(Expression value) {
        return new LayoutPropertyValue("icon-allow-overlap", value);
    }

    public static PropertyValue<Boolean> iconIgnorePlacement(Boolean value) {
        return new LayoutPropertyValue("icon-ignore-placement", value);
    }

    public static PropertyValue<Expression> iconIgnorePlacement(Expression value) {
        return new LayoutPropertyValue("icon-ignore-placement", value);
    }

    public static PropertyValue<Boolean> iconOptional(Boolean value) {
        return new LayoutPropertyValue("icon-optional", value);
    }

    public static PropertyValue<Expression> iconOptional(Expression value) {
        return new LayoutPropertyValue("icon-optional", value);
    }

    public static PropertyValue<String> iconRotationAlignment(String value) {
        return new LayoutPropertyValue("icon-rotation-alignment", value);
    }

    public static PropertyValue<Expression> iconRotationAlignment(Expression value) {
        return new LayoutPropertyValue("icon-rotation-alignment", value);
    }

    public static PropertyValue<Float> iconSize(Float value) {
        return new LayoutPropertyValue("icon-size", value);
    }

    public static PropertyValue<Expression> iconSize(Expression value) {
        return new LayoutPropertyValue("icon-size", value);
    }

    public static PropertyValue<String> iconTextFit(String value) {
        return new LayoutPropertyValue("icon-text-fit", value);
    }

    public static PropertyValue<Expression> iconTextFit(Expression value) {
        return new LayoutPropertyValue("icon-text-fit", value);
    }

    public static PropertyValue<Float[]> iconTextFitPadding(Float[] value) {
        return new LayoutPropertyValue("icon-text-fit-padding", value);
    }

    public static PropertyValue<Expression> iconTextFitPadding(Expression value) {
        return new LayoutPropertyValue("icon-text-fit-padding", value);
    }

    public static PropertyValue<String> iconImage(String value) {
        return new LayoutPropertyValue("icon-image", value);
    }

    public static PropertyValue<Expression> iconImage(Expression value) {
        return new LayoutPropertyValue("icon-image", value);
    }

    public static PropertyValue<Float> iconRotate(Float value) {
        return new LayoutPropertyValue("icon-rotate", value);
    }

    public static PropertyValue<Expression> iconRotate(Expression value) {
        return new LayoutPropertyValue("icon-rotate", value);
    }

    public static PropertyValue<Float> iconPadding(Float value) {
        return new LayoutPropertyValue("icon-padding", value);
    }

    public static PropertyValue<Expression> iconPadding(Expression value) {
        return new LayoutPropertyValue("icon-padding", value);
    }

    public static PropertyValue<Boolean> iconKeepUpright(Boolean value) {
        return new LayoutPropertyValue("icon-keep-upright", value);
    }

    public static PropertyValue<Expression> iconKeepUpright(Expression value) {
        return new LayoutPropertyValue("icon-keep-upright", value);
    }

    public static PropertyValue<Float[]> iconOffset(Float[] value) {
        return new LayoutPropertyValue("icon-offset", value);
    }

    public static PropertyValue<Expression> iconOffset(Expression value) {
        return new LayoutPropertyValue("icon-offset", value);
    }

    public static PropertyValue<String> iconAnchor(String value) {
        return new LayoutPropertyValue("icon-anchor", value);
    }

    public static PropertyValue<Expression> iconAnchor(Expression value) {
        return new LayoutPropertyValue("icon-anchor", value);
    }

    public static PropertyValue<String> iconPitchAlignment(String value) {
        return new LayoutPropertyValue("icon-pitch-alignment", value);
    }

    public static PropertyValue<Expression> iconPitchAlignment(Expression value) {
        return new LayoutPropertyValue("icon-pitch-alignment", value);
    }

    public static PropertyValue<String> textPitchAlignment(String value) {
        return new LayoutPropertyValue("text-pitch-alignment", value);
    }

    public static PropertyValue<Expression> textPitchAlignment(Expression value) {
        return new LayoutPropertyValue("text-pitch-alignment", value);
    }

    public static PropertyValue<String> textRotationAlignment(String value) {
        return new LayoutPropertyValue("text-rotation-alignment", value);
    }

    public static PropertyValue<Expression> textRotationAlignment(Expression value) {
        return new LayoutPropertyValue("text-rotation-alignment", value);
    }

    public static PropertyValue<String> textField(String value) {
        return new LayoutPropertyValue("text-field", value);
    }

    public static PropertyValue<Formatted> textField(Formatted value) {
        return new LayoutPropertyValue("text-field", value);
    }

    public static PropertyValue<Expression> textField(Expression value) {
        return new LayoutPropertyValue("text-field", value);
    }

    public static PropertyValue<String[]> textFont(String[] value) {
        return new LayoutPropertyValue("text-font", value);
    }

    public static PropertyValue<Expression> textFont(Expression value) {
        return new LayoutPropertyValue("text-font", value);
    }

    public static PropertyValue<Float> textSize(Float value) {
        return new LayoutPropertyValue("text-size", value);
    }

    public static PropertyValue<Expression> textSize(Expression value) {
        return new LayoutPropertyValue("text-size", value);
    }

    public static PropertyValue<Float> textMaxWidth(Float value) {
        return new LayoutPropertyValue("text-max-width", value);
    }

    public static PropertyValue<Expression> textMaxWidth(Expression value) {
        return new LayoutPropertyValue("text-max-width", value);
    }

    public static PropertyValue<Float> textLineHeight(Float value) {
        return new LayoutPropertyValue("text-line-height", value);
    }

    public static PropertyValue<Expression> textLineHeight(Expression value) {
        return new LayoutPropertyValue("text-line-height", value);
    }

    public static PropertyValue<Float> textLetterSpacing(Float value) {
        return new LayoutPropertyValue("text-letter-spacing", value);
    }

    public static PropertyValue<Expression> textLetterSpacing(Expression value) {
        return new LayoutPropertyValue("text-letter-spacing", value);
    }

    public static PropertyValue<String> textJustify(String value) {
        return new LayoutPropertyValue("text-justify", value);
    }

    public static PropertyValue<Expression> textJustify(Expression value) {
        return new LayoutPropertyValue("text-justify", value);
    }

    public static PropertyValue<Float> textRadialOffset(Float value) {
        return new LayoutPropertyValue("text-radial-offset", value);
    }

    public static PropertyValue<Expression> textRadialOffset(Expression value) {
        return new LayoutPropertyValue("text-radial-offset", value);
    }

    public static PropertyValue<String[]> textVariableAnchor(String[] value) {
        return new LayoutPropertyValue("text-variable-anchor", value);
    }

    public static PropertyValue<Expression> textVariableAnchor(Expression value) {
        return new LayoutPropertyValue("text-variable-anchor", value);
    }

    public static PropertyValue<String> textAnchor(String value) {
        return new LayoutPropertyValue("text-anchor", value);
    }

    public static PropertyValue<Expression> textAnchor(Expression value) {
        return new LayoutPropertyValue("text-anchor", value);
    }

    public static PropertyValue<Float> textMaxAngle(Float value) {
        return new LayoutPropertyValue("text-max-angle", value);
    }

    public static PropertyValue<Expression> textMaxAngle(Expression value) {
        return new LayoutPropertyValue("text-max-angle", value);
    }

    public static PropertyValue<String[]> textWritingMode(String[] value) {
        return new LayoutPropertyValue("text-writing-mode", value);
    }

    public static PropertyValue<Expression> textWritingMode(Expression value) {
        return new LayoutPropertyValue("text-writing-mode", value);
    }

    public static PropertyValue<Float> textRotate(Float value) {
        return new LayoutPropertyValue("text-rotate", value);
    }

    public static PropertyValue<Expression> textRotate(Expression value) {
        return new LayoutPropertyValue("text-rotate", value);
    }

    public static PropertyValue<Float> textPadding(Float value) {
        return new LayoutPropertyValue("text-padding", value);
    }

    public static PropertyValue<Expression> textPadding(Expression value) {
        return new LayoutPropertyValue("text-padding", value);
    }

    public static PropertyValue<Boolean> textKeepUpright(Boolean value) {
        return new LayoutPropertyValue("text-keep-upright", value);
    }

    public static PropertyValue<Expression> textKeepUpright(Expression value) {
        return new LayoutPropertyValue("text-keep-upright", value);
    }

    public static PropertyValue<String> textTransform(String value) {
        return new LayoutPropertyValue("text-transform", value);
    }

    public static PropertyValue<Expression> textTransform(Expression value) {
        return new LayoutPropertyValue("text-transform", value);
    }

    public static PropertyValue<Float[]> textOffset(Float[] value) {
        return new LayoutPropertyValue("text-offset", value);
    }

    public static PropertyValue<Expression> textOffset(Expression value) {
        return new LayoutPropertyValue("text-offset", value);
    }

    public static PropertyValue<Boolean> textAllowOverlap(Boolean value) {
        return new LayoutPropertyValue("text-allow-overlap", value);
    }

    public static PropertyValue<Expression> textAllowOverlap(Expression value) {
        return new LayoutPropertyValue("text-allow-overlap", value);
    }

    public static PropertyValue<Boolean> textIgnorePlacement(Boolean value) {
        return new LayoutPropertyValue("text-ignore-placement", value);
    }

    public static PropertyValue<Expression> textIgnorePlacement(Expression value) {
        return new LayoutPropertyValue("text-ignore-placement", value);
    }

    public static PropertyValue<Boolean> textOptional(Boolean value) {
        return new LayoutPropertyValue("text-optional", value);
    }

    public static PropertyValue<Expression> textOptional(Expression value) {
        return new LayoutPropertyValue("text-optional", value);
    }
}
