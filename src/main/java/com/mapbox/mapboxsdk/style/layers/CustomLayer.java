package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.Keep;

public class CustomLayer extends Layer {
    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, long j);

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeUpdate();

    public CustomLayer(String id, long host) {
        initialize(id, host);
    }

    @Keep
    CustomLayer(long nativePtr) {
        super(nativePtr);
    }

    public void update() {
        nativeUpdate();
    }
}
