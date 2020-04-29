package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.amap.location.common.model.AmapLoc;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UiThread
public class GeoJsonSource extends Source {
    @Keep
    private native Feature[] nativeGetClusterChildren(Feature feature);

    @Keep
    private native int nativeGetClusterExpansionZoom(Feature feature);

    @Keep
    private native Feature[] nativeGetClusterLeaves(Feature feature, long j, long j2);

    @Keep
    private native void nativeSetFeature(Feature feature);

    @Keep
    private native void nativeSetFeatureCollection(FeatureCollection featureCollection);

    @Keep
    private native void nativeSetGeoJsonString(String str);

    @Keep
    private native void nativeSetGeometry(Geometry geometry);

    @Keep
    @NonNull
    private native Feature[] querySourceFeatures(Object[] objArr);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, Object obj);

    /* access modifiers changed from: protected */
    @Keep
    @NonNull
    public native String nativeGetUrl();

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeSetUrl(String str);

    @Keep
    GeoJsonSource(long nativePtr) {
        super(nativePtr);
    }

    public GeoJsonSource(String id) {
        initialize(id, null);
        setGeoJson(FeatureCollection.fromFeatures(new ArrayList()));
    }

    public GeoJsonSource(String id, GeoJsonOptions options) {
        initialize(id, options);
        setGeoJson(FeatureCollection.fromFeatures(new ArrayList()));
    }

    public GeoJsonSource(String id, @Nullable String geoJson) {
        if (geoJson == null || geoJson.startsWith("http")) {
            throw new IllegalArgumentException("Expected a raw json body");
        }
        initialize(id, null);
        setGeoJson(geoJson);
    }

    public GeoJsonSource(String id, @Nullable String geoJson, GeoJsonOptions options) {
        if (geoJson == null || geoJson.startsWith("http") || geoJson.startsWith("asset") || geoJson.startsWith(AmapLoc.TYPE_OFFLINE_CELL)) {
            throw new IllegalArgumentException("Expected a raw json body");
        }
        initialize(id, options);
        setGeoJson(geoJson);
    }

    @Deprecated
    public GeoJsonSource(String id, URL url) {
        initialize(id, null);
        nativeSetUrl(url.toExternalForm());
    }

    @Deprecated
    public GeoJsonSource(String id, URL url, GeoJsonOptions options) {
        initialize(id, options);
        nativeSetUrl(url.toExternalForm());
    }

    public GeoJsonSource(String id, URI uri) {
        initialize(id, null);
        nativeSetUrl(uri.toString());
    }

    public GeoJsonSource(String id, URI uri, GeoJsonOptions options) {
        initialize(id, options);
        nativeSetUrl(uri.toString());
    }

    public GeoJsonSource(String id, FeatureCollection features) {
        initialize(id, null);
        setGeoJson(features);
    }

    public GeoJsonSource(String id, FeatureCollection features, GeoJsonOptions options) {
        initialize(id, options);
        setGeoJson(features);
    }

    public GeoJsonSource(String id, Feature feature) {
        initialize(id, null);
        setGeoJson(feature);
    }

    public GeoJsonSource(String id, Feature feature, GeoJsonOptions options) {
        initialize(id, options);
        setGeoJson(feature);
    }

    public GeoJsonSource(String id, Geometry geometry) {
        initialize(id, null);
        setGeoJson(geometry);
    }

    public GeoJsonSource(String id, Geometry geometry, GeoJsonOptions options) {
        initialize(id, options);
        setGeoJson(geometry);
    }

    public void setGeoJson(Feature feature) {
        if (!this.detached) {
            checkThread();
            nativeSetFeature(feature);
        }
    }

    public void setGeoJson(Geometry geometry) {
        if (!this.detached) {
            checkThread();
            nativeSetGeometry(geometry);
        }
    }

    public void setGeoJson(@Nullable FeatureCollection featureCollection) {
        if (!this.detached) {
            checkThread();
            if (featureCollection == null || featureCollection.features() == null) {
                nativeSetFeatureCollection(featureCollection);
            } else {
                nativeSetFeatureCollection(FeatureCollection.fromFeatures(new ArrayList<>(featureCollection.features())));
            }
        }
    }

    public void setGeoJson(String json) {
        if (!this.detached) {
            checkThread();
            nativeSetGeoJsonString(json);
        }
    }

    @Deprecated
    public void setUrl(@NonNull URL url) {
        checkThread();
        setUrl(url.toExternalForm());
    }

    public void setUri(@NonNull URI uri) {
        setUri(uri.toString());
    }

    @Deprecated
    public void setUrl(String url) {
        checkThread();
        nativeSetUrl(url);
    }

    public void setUri(String uri) {
        checkThread();
        nativeSetUrl(uri);
    }

    @Nullable
    public String getUrl() {
        checkThread();
        return nativeGetUrl();
    }

    @Nullable
    public String getUri() {
        checkThread();
        return nativeGetUrl();
    }

    @NonNull
    public List<Feature> querySourceFeatures(@Nullable Expression filter) {
        checkThread();
        Feature[] features = querySourceFeatures(filter != null ? filter.toArray() : null);
        return features != null ? Arrays.asList(features) : new ArrayList();
    }

    @NonNull
    public FeatureCollection getClusterChildren(@NonNull Feature cluster) {
        checkThread();
        return FeatureCollection.fromFeatures(nativeGetClusterChildren(cluster));
    }

    @NonNull
    public FeatureCollection getClusterLeaves(@NonNull Feature cluster, long limit, long offset) {
        checkThread();
        return FeatureCollection.fromFeatures(nativeGetClusterLeaves(cluster, limit, offset));
    }

    public int getClusterExpansionZoom(@NonNull Feature cluster) {
        checkThread();
        return nativeGetClusterExpansionZoom(cluster);
    }
}
