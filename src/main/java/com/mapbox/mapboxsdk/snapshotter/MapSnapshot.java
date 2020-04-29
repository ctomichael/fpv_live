package com.mapbox.mapboxsdk.snapshotter;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class MapSnapshot {
    private String[] attributions;
    private Bitmap bitmap;
    @Keep
    private long nativePtr = 0;
    private boolean showLogo;

    @Keep
    private native void initialize();

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize();

    @Keep
    @NonNull
    public native LatLng latLngForPixel(PointF pointF);

    @Keep
    @NonNull
    public native PointF pixelForLatLng(LatLng latLng);

    @Keep
    private MapSnapshot(long nativePtr2, Bitmap bitmap2, String[] attributions2, boolean showLogo2) {
        this.nativePtr = nativePtr2;
        this.bitmap = bitmap2;
        this.attributions = attributions2;
        this.showLogo = showLogo2;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    /* access modifiers changed from: protected */
    public String[] getAttributions() {
        return this.attributions;
    }

    /* access modifiers changed from: package-private */
    public boolean isShowLogo() {
        return this.showLogo;
    }
}
