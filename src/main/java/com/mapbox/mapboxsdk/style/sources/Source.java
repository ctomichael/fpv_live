package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.LibraryLoader;
import com.mapbox.mapboxsdk.utils.ThreadUtils;

public abstract class Source {
    private static final String TAG = "Mbgl-Source";
    protected boolean detached;
    @Keep
    private long nativePtr;

    /* access modifiers changed from: protected */
    @Keep
    @NonNull
    public native String nativeGetAttribution();

    /* access modifiers changed from: protected */
    @Keep
    @NonNull
    public native String nativeGetId();

    static {
        LibraryLoader.load();
    }

    @Keep
    protected Source(long nativePtr2) {
        checkThread();
        this.nativePtr = nativePtr2;
    }

    public Source() {
        checkThread();
    }

    /* access modifiers changed from: protected */
    public void checkThread() {
        ThreadUtils.checkThread(TAG);
    }

    @NonNull
    public String getId() {
        checkThread();
        return nativeGetId();
    }

    @NonNull
    public String getAttribution() {
        checkThread();
        return nativeGetAttribution();
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public void setDetached() {
        this.detached = true;
    }
}
