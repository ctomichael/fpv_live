package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import java.util.HashMap;

public class GeoJsonOptions extends HashMap<String, Object> {
    @NonNull
    public GeoJsonOptions withMinZoom(int minZoom) {
        put("minzoom", Integer.valueOf(minZoom));
        return this;
    }

    @NonNull
    public GeoJsonOptions withMaxZoom(int maxZoom) {
        put("maxzoom", Integer.valueOf(maxZoom));
        return this;
    }

    @NonNull
    public GeoJsonOptions withBuffer(int buffer) {
        put("buffer", Integer.valueOf(buffer));
        return this;
    }

    @NonNull
    public GeoJsonOptions withLineMetrics(boolean lineMetrics) {
        put("lineMetrics", Boolean.valueOf(lineMetrics));
        return this;
    }

    @NonNull
    public GeoJsonOptions withTolerance(float tolerance) {
        put("tolerance", Float.valueOf(tolerance));
        return this;
    }

    @NonNull
    public GeoJsonOptions withCluster(boolean cluster) {
        put("cluster", Boolean.valueOf(cluster));
        return this;
    }

    @NonNull
    public GeoJsonOptions withClusterMaxZoom(int clusterMaxZoom) {
        put("clusterMaxZoom", Integer.valueOf(clusterMaxZoom));
        return this;
    }

    @NonNull
    public GeoJsonOptions withClusterRadius(int clusterRadius) {
        put("clusterRadius", Integer.valueOf(clusterRadius));
        return this;
    }

    @NonNull
    public GeoJsonOptions withClusterProperty(String propertyName, Expression operatorExpr, Expression mapExpr) {
        HashMap<String, Object[]> properties = containsKey("clusterProperties") ? (HashMap) get("clusterProperties") : new HashMap<>();
        properties.put(propertyName, new Object[]{operatorExpr instanceof Expression.ExpressionLiteral ? ((Expression.ExpressionLiteral) operatorExpr).toValue() : operatorExpr.toArray(), mapExpr.toArray()});
        put("clusterProperties", properties);
        return this;
    }
}
