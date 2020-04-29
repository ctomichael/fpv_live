package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import java.net.URI;
import java.net.URL;

@UiThread
public class RasterDemSource extends Source {
    public static final int DEFAULT_TILE_SIZE = 512;

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, Object obj, int i);

    /* access modifiers changed from: protected */
    @Keep
    @NonNull
    public native String nativeGetUrl();

    @Keep
    RasterDemSource(long nativePtr) {
        super(nativePtr);
    }

    @Deprecated
    public RasterDemSource(String id, URL url) {
        this(id, url.toExternalForm());
    }

    public RasterDemSource(String id, URI uri) {
        this(id, uri.toString());
    }

    public RasterDemSource(String id, String uri) {
        initialize(id, uri, 512);
    }

    public RasterDemSource(String id, String uri, int tileSize) {
        initialize(id, uri, tileSize);
    }

    public RasterDemSource(String id, TileSet tileSet) {
        initialize(id, tileSet.toValueObject(), 512);
    }

    public RasterDemSource(String id, TileSet tileSet, int tileSize) {
        initialize(id, tileSet.toValueObject(), tileSize);
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
