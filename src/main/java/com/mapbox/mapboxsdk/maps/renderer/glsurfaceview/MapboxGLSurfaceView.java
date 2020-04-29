package com.mapbox.mapboxsdk.maps.renderer.glsurfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

public class MapboxGLSurfaceView extends GLSurfaceView {
    private OnGLSurfaceViewDetachedListener detachedListener;

    public interface OnGLSurfaceViewDetachedListener {
        void onGLSurfaceViewDetached();
    }

    public MapboxGLSurfaceView(Context context) {
        super(context);
    }

    public MapboxGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (this.detachedListener != null) {
            this.detachedListener.onGLSurfaceViewDetached();
        }
        super.onDetachedFromWindow();
    }

    public void setDetachedListener(@NonNull OnGLSurfaceViewDetachedListener detachedListener2) {
        if (this.detachedListener != null) {
            throw new IllegalArgumentException("Detached from window listener has been already set.");
        }
        this.detachedListener = detachedListener2;
    }
}
