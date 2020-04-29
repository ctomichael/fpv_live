package com.mapbox.mapboxsdk.net;

import android.support.annotation.Keep;
import com.mapbox.mapboxsdk.LibraryLoader;

class NativeConnectivityListener implements ConnectivityListener {
    @Keep
    private boolean invalidated;
    @Keep
    private long nativePtr;

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize();

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeOnConnectivityStateChanged(boolean z);

    static {
        LibraryLoader.load();
    }

    @Keep
    NativeConnectivityListener(long nativePtr2) {
        this.nativePtr = nativePtr2;
    }

    NativeConnectivityListener() {
        initialize();
    }

    public void onNetworkStateChanged(boolean connected) {
        nativeOnConnectivityStateChanged(connected);
    }
}
