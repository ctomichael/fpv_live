package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.Keep;
import android.support.annotation.UiThread;

@Keep
@UiThread
public class UnknownSource extends Source {
    /* access modifiers changed from: protected */
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    public native void initialize();

    UnknownSource(long nativePtr) {
        super(nativePtr);
    }
}
