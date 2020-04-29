package com.mapbox.mapboxsdk.location;

import android.support.annotation.NonNull;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

class LayerSourceProvider {
    private static final String EMPTY_STRING = "";

    LayerSourceProvider() {
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public GeoJsonSource generateSource(Feature locationFeature) {
        return new GeoJsonSource(LocationComponentConstants.LOCATION_SOURCE, locationFeature, new GeoJsonOptions().withMaxZoom(16));
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Layer generateLayer(@NonNull String layerId) {
        SymbolLayer layer = new SymbolLayer(layerId, LocationComponentConstants.LOCATION_SOURCE);
        layer.setProperties(PropertyFactory.iconAllowOverlap((Boolean) true), PropertyFactory.iconIgnorePlacement((Boolean) true), PropertyFactory.iconRotationAlignment("map"), PropertyFactory.iconRotate(Expression.match(Expression.literal(layerId), Expression.literal((Number) Float.valueOf(0.0f)), Expression.stop(LocationComponentConstants.FOREGROUND_LAYER, Expression.get("mapbox-property-gps-bearing")), Expression.stop(LocationComponentConstants.BACKGROUND_LAYER, Expression.get("mapbox-property-gps-bearing")), Expression.stop(LocationComponentConstants.SHADOW_LAYER, Expression.get("mapbox-property-gps-bearing")), Expression.stop(LocationComponentConstants.BEARING_LAYER, Expression.get("mapbox-property-compass-bearing")))), PropertyFactory.iconImage(Expression.match(Expression.literal(layerId), Expression.literal(""), Expression.stop(LocationComponentConstants.FOREGROUND_LAYER, Expression.switchCase(Expression.get("mapbox-property-location-stale"), Expression.get("mapbox-property-foreground-stale-icon"), Expression.get("mapbox-property-foreground-icon"))), Expression.stop(LocationComponentConstants.BACKGROUND_LAYER, Expression.switchCase(Expression.get("mapbox-property-location-stale"), Expression.get("mapbox-property-background-stale-icon"), Expression.get("mapbox-property-background-icon"))), Expression.stop(LocationComponentConstants.SHADOW_LAYER, Expression.literal("mapbox-location-shadow-icon")), Expression.stop(LocationComponentConstants.BEARING_LAYER, Expression.get("mapbox-property-shadow-icon")))), PropertyFactory.iconOffset(Expression.match(Expression.literal(layerId), Expression.literal((Object[]) new Float[]{Float.valueOf(0.0f), Float.valueOf(0.0f)}), Expression.stop(Expression.literal(LocationComponentConstants.FOREGROUND_LAYER), Expression.get("mapbox-property-foreground-icon-offset")), Expression.stop(Expression.literal(LocationComponentConstants.SHADOW_LAYER), Expression.get("mapbox-property-shadow-icon-offset")))));
        return layer;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Layer generateAccuracyLayer() {
        return new CircleLayer(LocationComponentConstants.ACCURACY_LAYER, LocationComponentConstants.LOCATION_SOURCE).withProperties(PropertyFactory.circleRadius(Expression.get("mapbox-property-accuracy-radius")), PropertyFactory.circleColor(Expression.get("mapbox-property-accuracy-color")), PropertyFactory.circleOpacity(Expression.get("mapbox-property-accuracy-alpha")), PropertyFactory.circleStrokeColor(Expression.get("mapbox-property-accuracy-color")), PropertyFactory.circlePitchAlignment("map"));
    }
}
