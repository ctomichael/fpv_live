package com.mapbox.mapboxsdk.maps.renderer;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.LibraryLoader;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@Keep
public abstract class MapRenderer implements MapRendererScheduler {
    private static final String TAG = "Mbgl-MapRenderer";
    private double expectedRenderTime = 0.0d;
    private long nativePtr = 0;
    private MapboxMap.OnFpsChangedListener onFpsChangedListener;
    private long timeElapsed;

    private native void nativeInitialize(MapRenderer mapRenderer, float f, String str);

    private native void nativeOnSurfaceChanged(int i, int i2);

    private native void nativeOnSurfaceCreated();

    private native void nativeOnSurfaceDestroyed();

    private native void nativeRender();

    /* access modifiers changed from: protected */
    @CallSuper
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    public native void nativeReset();

    static {
        LibraryLoader.load();
    }

    public MapRenderer(@NonNull Context context, String localIdeographFontFamily) {
        nativeInitialize(this, context.getResources().getDisplayMetrics().density, localIdeographFontFamily);
    }

    public void onStart() {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onStop() {
    }

    public void onDestroy() {
    }

    public void setOnFpsChangedListener(MapboxMap.OnFpsChangedListener listener) {
        this.onFpsChangedListener = listener;
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        nativeOnSurfaceCreated();
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onSurfaceChanged(@NonNull GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        nativeOnSurfaceChanged(width, height);
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onSurfaceDestroyed() {
        nativeOnSurfaceDestroyed();
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onDrawFrame(GL10 gl) {
        long startTime = System.nanoTime();
        try {
            nativeRender();
        } catch (Error error) {
            Logger.e(TAG, error.getMessage());
        }
        long renderTime = System.nanoTime() - startTime;
        if (((double) renderTime) < this.expectedRenderTime) {
            try {
                Thread.sleep((long) ((this.expectedRenderTime - ((double) renderTime)) / 1000000.0d));
            } catch (InterruptedException ex) {
                Logger.e(TAG, ex.getMessage());
            }
        }
        if (this.onFpsChangedListener != null) {
            updateFps();
        }
    }

    /* access modifiers changed from: package-private */
    @CallSuper
    public void queueEvent(MapRendererRunnable runnable) {
        queueEvent((Runnable) runnable);
    }

    private void updateFps() {
        long currentTime = System.nanoTime();
        this.onFpsChangedListener.onFpsChanged(1.0E9d / ((double) (currentTime - this.timeElapsed)));
        this.timeElapsed = currentTime;
    }

    public void setMaximumFps(int maximumFps) {
        if (maximumFps > 0) {
            this.expectedRenderTime = 1.0E9d / ((double) maximumFps);
        }
    }
}
