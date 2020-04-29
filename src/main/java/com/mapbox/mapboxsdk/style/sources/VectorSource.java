package com.mapbox.mapboxsdk.style.sources;

import android.net.Uri;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.annotation.UiThread;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UiThread
public class VectorSource extends Source {
    @Keep
    @NonNull
    private native Feature[] querySourceFeatures(String[] strArr, Object[] objArr);

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

    @Keep
    VectorSource(long nativePtr) {
        super(nativePtr);
    }

    @Deprecated
    public VectorSource(String id, URL url) {
        this(id, url.toExternalForm());
    }

    public VectorSource(String id, Uri uri) {
        this(id, uri.toString());
    }

    public VectorSource(String id, String uri) {
        initialize(id, uri);
    }

    public VectorSource(String id, TileSet tileSet) {
        initialize(id, tileSet.toValueObject());
    }

    @NonNull
    public List<Feature> querySourceFeatures(@Size(min = 1) String[] sourceLayerIds, @Nullable Expression filter) {
        checkThread();
        Feature[] features = querySourceFeatures(sourceLayerIds, filter != null ? filter.toArray() : null);
        return features != null ? Arrays.asList(features) : new ArrayList();
    }

    @Nullable
    @Deprecated
    public String getUrl() {
        checkThread();
        return nativeGetUrl();
    }

    @Nullable
    public String getUri() {
        checkThread();
        return nativeGetUrl();
    }
}
