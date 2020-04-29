package com.mapbox.mapboxsdk.location;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.MapboxAnimator;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import java.util.HashSet;
import java.util.Set;

final class LocationLayerController {
    private final MapboxAnimator.AnimationsValueChangeListener<Float> accuracyValueListener = new MapboxAnimator.AnimationsValueChangeListener<Float>() {
        /* class com.mapbox.mapboxsdk.location.LocationLayerController.AnonymousClass4 */

        public void onNewAnimationValue(Float value) {
            LocationLayerController.this.updateAccuracyRadius(value.floatValue());
        }
    };
    private final LayerBitmapProvider bitmapProvider;
    private final MapboxAnimator.AnimationsValueChangeListener<Float> compassBearingValueListener = new MapboxAnimator.AnimationsValueChangeListener<Float>() {
        /* class com.mapbox.mapboxsdk.location.LocationLayerController.AnonymousClass3 */

        public void onNewAnimationValue(Float value) {
            LocationLayerController.this.setBearingProperty("mapbox-property-compass-bearing", value.floatValue());
        }
    };
    private final MapboxAnimator.AnimationsValueChangeListener<Float> gpsBearingValueListener = new MapboxAnimator.AnimationsValueChangeListener<Float>() {
        /* class com.mapbox.mapboxsdk.location.LocationLayerController.AnonymousClass2 */

        public void onNewAnimationValue(Float value) {
            LocationLayerController.this.setBearingProperty("mapbox-property-gps-bearing", value.floatValue());
        }
    };
    private final OnRenderModeChangedListener internalRenderModeChangedListener;
    private boolean isHidden = true;
    private final MapboxAnimator.AnimationsValueChangeListener<LatLng> latLngValueListener = new MapboxAnimator.AnimationsValueChangeListener<LatLng>() {
        /* class com.mapbox.mapboxsdk.location.LocationLayerController.AnonymousClass1 */

        public void onNewAnimationValue(LatLng value) {
            LocationLayerController.this.setLocationPoint(Point.fromLngLat(value.getLongitude(), value.getLatitude()));
        }
    };
    @VisibleForTesting
    final Set<String> layerSet = new HashSet();
    private final LayerSourceProvider layerSourceProvider;
    private Feature locationFeature;
    private GeoJsonSource locationSource;
    private final MapboxMap mapboxMap;
    private LocationComponentOptions options;
    private LocationComponentPositionManager positionManager;
    private int renderMode;
    private Style style;

    LocationLayerController(MapboxMap mapboxMap2, Style style2, LayerSourceProvider layerSourceProvider2, LayerFeatureProvider featureProvider, LayerBitmapProvider bitmapProvider2, @NonNull LocationComponentOptions options2, @NonNull OnRenderModeChangedListener internalRenderModeChangedListener2) {
        this.mapboxMap = mapboxMap2;
        this.style = style2;
        this.layerSourceProvider = layerSourceProvider2;
        this.bitmapProvider = bitmapProvider2;
        this.locationFeature = featureProvider.generateLocationFeature(this.locationFeature, options2);
        this.internalRenderModeChangedListener = internalRenderModeChangedListener2;
        initializeComponents(style2, options2);
    }

    /* access modifiers changed from: package-private */
    public void initializeComponents(Style style2, LocationComponentOptions options2) {
        this.style = style2;
        this.positionManager = new LocationComponentPositionManager(style2, options2.layerAbove(), options2.layerBelow());
        addLocationSource();
        addLayers();
        applyStyle(options2);
        if (this.isHidden) {
            hide();
        } else {
            show();
        }
    }

    /* access modifiers changed from: package-private */
    public void applyStyle(@NonNull LocationComponentOptions options2) {
        if (this.positionManager.update(options2.layerAbove(), options2.layerBelow())) {
            removeLayers();
            addLayers();
            if (this.isHidden) {
                hide();
            }
        }
        this.options = options2;
        if (options2.elevation() > 0.0f) {
            styleShadow(options2);
        }
        styleForeground(options2);
        styleBackground(options2);
        styleBearing(options2);
        styleAccuracy(options2.accuracyAlpha(), options2.accuracyColor());
        styleScaling(options2);
        determineIconsSource(options2);
        if (!this.isHidden) {
            show();
        }
    }

    /* access modifiers changed from: package-private */
    public void setRenderMode(int renderMode2) {
        if (this.renderMode != renderMode2) {
            this.renderMode = renderMode2;
            styleForeground(this.options);
            determineIconsSource(this.options);
            if (!this.isHidden) {
                show();
            }
            this.internalRenderModeChangedListener.onRenderModeChanged(renderMode2);
        }
    }

    /* access modifiers changed from: package-private */
    public int getRenderMode() {
        return this.renderMode;
    }

    /* access modifiers changed from: package-private */
    public void show() {
        boolean z = false;
        boolean z2 = true;
        this.isHidden = false;
        boolean isStale = this.locationFeature.getBooleanProperty("mapbox-property-location-stale").booleanValue();
        switch (this.renderMode) {
            case 4:
                setLayerVisibility(LocationComponentConstants.SHADOW_LAYER, true);
                setLayerVisibility(LocationComponentConstants.FOREGROUND_LAYER, true);
                setLayerVisibility(LocationComponentConstants.BACKGROUND_LAYER, true);
                if (!isStale) {
                    z = true;
                }
                setLayerVisibility(LocationComponentConstants.ACCURACY_LAYER, z);
                setLayerVisibility(LocationComponentConstants.BEARING_LAYER, true);
                return;
            case 8:
                setLayerVisibility(LocationComponentConstants.SHADOW_LAYER, false);
                setLayerVisibility(LocationComponentConstants.FOREGROUND_LAYER, true);
                setLayerVisibility(LocationComponentConstants.BACKGROUND_LAYER, true);
                setLayerVisibility(LocationComponentConstants.ACCURACY_LAYER, false);
                setLayerVisibility(LocationComponentConstants.BEARING_LAYER, false);
                return;
            case 18:
                setLayerVisibility(LocationComponentConstants.SHADOW_LAYER, true);
                setLayerVisibility(LocationComponentConstants.FOREGROUND_LAYER, true);
                setLayerVisibility(LocationComponentConstants.BACKGROUND_LAYER, true);
                if (isStale) {
                    z2 = false;
                }
                setLayerVisibility(LocationComponentConstants.ACCURACY_LAYER, z2);
                setLayerVisibility(LocationComponentConstants.BEARING_LAYER, false);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void hide() {
        this.isHidden = true;
        for (String layerId : this.layerSet) {
            setLayerVisibility(layerId, false);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateForegroundOffset(double tilt) {
        JsonArray foregroundJsonArray = new JsonArray();
        foregroundJsonArray.add(Float.valueOf(0.0f));
        foregroundJsonArray.add(Float.valueOf((float) (-0.05d * tilt)));
        this.locationFeature.addProperty("mapbox-property-foreground-icon-offset", foregroundJsonArray);
        JsonArray backgroundJsonArray = new JsonArray();
        backgroundJsonArray.add(Float.valueOf(0.0f));
        backgroundJsonArray.add(Float.valueOf((float) (0.05d * tilt)));
        this.locationFeature.addProperty("mapbox-property-shadow-icon-offset", backgroundJsonArray);
        refreshSource();
    }

    /* access modifiers changed from: package-private */
    public void updateForegroundBearing(float bearing) {
        if (this.renderMode != 8) {
            setBearingProperty("mapbox-property-gps-bearing", bearing);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isHidden() {
        return this.isHidden;
    }

    /* access modifiers changed from: package-private */
    public boolean isConsumingCompass() {
        return this.renderMode == 4;
    }

    private void setLayerVisibility(@NonNull String layerId, boolean visible) {
        Layer layer = this.style.getLayer(layerId);
        if (layer != null) {
            if (!((String) layer.getVisibility().value).equals(visible ? Property.VISIBLE : "none")) {
                PropertyValue[] propertyValueArr = new PropertyValue[1];
                propertyValueArr[0] = PropertyFactory.visibility(visible ? Property.VISIBLE : "none");
                layer.setProperties(propertyValueArr);
            }
        }
    }

    private void addLayers() {
        Layer layer = this.layerSourceProvider.generateLayer(LocationComponentConstants.BEARING_LAYER);
        this.positionManager.addLayerToMap(layer);
        this.layerSet.add(layer.getId());
        addSymbolLayer(LocationComponentConstants.FOREGROUND_LAYER, LocationComponentConstants.BEARING_LAYER);
        addSymbolLayer(LocationComponentConstants.BACKGROUND_LAYER, LocationComponentConstants.FOREGROUND_LAYER);
        addSymbolLayer(LocationComponentConstants.SHADOW_LAYER, LocationComponentConstants.BACKGROUND_LAYER);
        addAccuracyLayer();
    }

    private void addSymbolLayer(@NonNull String layerId, @NonNull String beforeLayerId) {
        addLayerToMap(this.layerSourceProvider.generateLayer(layerId), beforeLayerId);
    }

    private void addAccuracyLayer() {
        addLayerToMap(this.layerSourceProvider.generateAccuracyLayer(), LocationComponentConstants.BACKGROUND_LAYER);
    }

    private void addLayerToMap(Layer layer, @NonNull String idBelowLayer) {
        this.style.addLayerBelow(layer, idBelowLayer);
        this.layerSet.add(layer.getId());
    }

    private void removeLayers() {
        for (String layerId : this.layerSet) {
            this.style.removeLayer(layerId);
        }
        this.layerSet.clear();
    }

    /* access modifiers changed from: private */
    public void setBearingProperty(@NonNull String propertyId, float bearing) {
        this.locationFeature.addNumberProperty(propertyId, Float.valueOf(bearing));
        refreshSource();
    }

    /* access modifiers changed from: private */
    public void updateAccuracyRadius(float accuracy) {
        this.locationFeature.addNumberProperty("mapbox-property-accuracy-radius", Float.valueOf(accuracy));
        refreshSource();
    }

    private void addLocationSource() {
        this.locationSource = this.layerSourceProvider.generateSource(this.locationFeature);
        this.style.addSource(this.locationSource);
    }

    private void refreshSource() {
        if (((GeoJsonSource) this.style.getSourceAs(LocationComponentConstants.LOCATION_SOURCE)) != null) {
            this.locationSource.setGeoJson(this.locationFeature);
        }
    }

    /* access modifiers changed from: private */
    public void setLocationPoint(Point locationPoint) {
        JsonObject properties = this.locationFeature.properties();
        if (properties != null) {
            this.locationFeature = Feature.fromGeometry(locationPoint, properties);
            refreshSource();
        }
    }

    private void styleBackground(LocationComponentOptions options2) {
        Bitmap backgroundBitmap = this.bitmapProvider.generateBitmap(options2.backgroundDrawable(), options2.backgroundTintColor());
        Bitmap backgroundStaleBitmap = this.bitmapProvider.generateBitmap(options2.backgroundDrawableStale(), options2.backgroundStaleTintColor());
        this.style.addImage("mapbox-location-stroke-icon", backgroundBitmap);
        this.style.addImage("mapbox-location-background-stale-icon", backgroundStaleBitmap);
    }

    private void styleShadow(@NonNull LocationComponentOptions options2) {
        this.style.addImage("mapbox-location-shadow-icon", this.bitmapProvider.generateShadowBitmap(options2));
    }

    private void styleBearing(LocationComponentOptions options2) {
        this.style.addImage("mapbox-location-bearing-icon", this.bitmapProvider.generateBitmap(options2.bearingDrawable(), options2.bearingTintColor()));
    }

    private void styleAccuracy(float accuracyAlpha, @ColorInt int accuracyColor) {
        this.locationFeature.addNumberProperty("mapbox-property-accuracy-alpha", Float.valueOf(accuracyAlpha));
        this.locationFeature.addStringProperty("mapbox-property-accuracy-color", ColorUtils.colorToRgbaString(accuracyColor));
        refreshSource();
    }

    private void styleForeground(LocationComponentOptions options2) {
        Bitmap foregroundBitmap = this.bitmapProvider.generateBitmap(options2.foregroundDrawable(), options2.foregroundTintColor());
        Bitmap foregroundBitmapStale = this.bitmapProvider.generateBitmap(options2.foregroundDrawableStale(), options2.foregroundStaleTintColor());
        if (this.renderMode == 8) {
            foregroundBitmap = this.bitmapProvider.generateBitmap(options2.gpsDrawable(), options2.foregroundTintColor());
            foregroundBitmapStale = this.bitmapProvider.generateBitmap(options2.gpsDrawable(), options2.foregroundStaleTintColor());
        }
        this.style.addImage("mapbox-location-icon", foregroundBitmap);
        this.style.addImage("mapbox-location-stale-icon", foregroundBitmapStale);
    }

    private void styleScaling(@NonNull LocationComponentOptions options2) {
        for (String layerId : this.layerSet) {
            Layer layer = this.style.getLayer(layerId);
            if (layer instanceof SymbolLayer) {
                layer.setProperties(PropertyFactory.iconSize(Expression.interpolate(Expression.linear(), Expression.zoom(), Expression.stop(Double.valueOf(this.mapboxMap.getMinZoomLevel()), Float.valueOf(options2.minZoomIconScale())), Expression.stop(Double.valueOf(this.mapboxMap.getMaxZoomLevel()), Float.valueOf(options2.maxZoomIconScale())))));
            }
        }
    }

    private void determineIconsSource(LocationComponentOptions options2) {
        String foregroundIconString = buildIconString(this.renderMode == 8 ? options2.gpsName() : options2.foregroundName(), "mapbox-location-icon");
        String foregroundStaleIconString = buildIconString(options2.foregroundStaleName(), "mapbox-location-stale-icon");
        String backgroundIconString = buildIconString(options2.backgroundName(), "mapbox-location-stroke-icon");
        String backgroundStaleIconString = buildIconString(options2.backgroundStaleName(), "mapbox-location-background-stale-icon");
        String bearingIconString = buildIconString(options2.bearingName(), "mapbox-location-bearing-icon");
        this.locationFeature.addStringProperty("mapbox-property-foreground-icon", foregroundIconString);
        this.locationFeature.addStringProperty("mapbox-property-background-icon", backgroundIconString);
        this.locationFeature.addStringProperty("mapbox-property-foreground-stale-icon", foregroundStaleIconString);
        this.locationFeature.addStringProperty("mapbox-property-background-stale-icon", backgroundStaleIconString);
        this.locationFeature.addStringProperty("mapbox-property-shadow-icon", bearingIconString);
        refreshSource();
    }

    @NonNull
    private String buildIconString(@Nullable String bitmapName, @NonNull String drawableName) {
        return bitmapName != null ? bitmapName : drawableName;
    }

    /* access modifiers changed from: package-private */
    public void setLocationsStale(boolean isStale) {
        this.locationFeature.addBooleanProperty("mapbox-property-location-stale", Boolean.valueOf(isStale));
        refreshSource();
        if (this.renderMode != 8) {
            setLayerVisibility(LocationComponentConstants.ACCURACY_LAYER, !isStale);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onMapClick(@NonNull LatLng point) {
        PointF screenLoc = this.mapboxMap.getProjection().toScreenLocation(point);
        if (!this.mapboxMap.queryRenderedFeatures(screenLoc, LocationComponentConstants.BACKGROUND_LAYER, LocationComponentConstants.FOREGROUND_LAYER, LocationComponentConstants.BEARING_LAYER).isEmpty()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public Set<AnimatorListenerHolder> getAnimationListeners() {
        Set<AnimatorListenerHolder> holders = new HashSet<>();
        holders.add(new AnimatorListenerHolder(0, this.latLngValueListener));
        if (this.renderMode == 8) {
            holders.add(new AnimatorListenerHolder(2, this.gpsBearingValueListener));
        } else if (this.renderMode == 4) {
            holders.add(new AnimatorListenerHolder(3, this.compassBearingValueListener));
        }
        if (this.renderMode == 4 || this.renderMode == 18) {
            holders.add(new AnimatorListenerHolder(6, this.accuracyValueListener));
        }
        return holders;
    }
}
