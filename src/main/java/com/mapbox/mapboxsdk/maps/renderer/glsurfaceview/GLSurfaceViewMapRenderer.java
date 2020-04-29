package com.mapbox.mapboxsdk.maps.renderer.glsurfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.maps.renderer.MapRenderer;
import com.mapbox.mapboxsdk.maps.renderer.egl.EGLConfigChooser;
import com.mapbox.mapboxsdk.maps.renderer.glsurfaceview.MapboxGLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceViewMapRenderer extends MapRenderer implements GLSurfaceView.Renderer {
    @NonNull
    private final MapboxGLSurfaceView glSurfaceView;

    public GLSurfaceViewMapRenderer(Context context, MapboxGLSurfaceView glSurfaceView2, String localIdeographFontFamily) {
        super(context, localIdeographFontFamily);
        this.glSurfaceView = glSurfaceView2;
        glSurfaceView2.setEGLContextClientVersion(2);
        glSurfaceView2.setEGLConfigChooser(new EGLConfigChooser());
        glSurfaceView2.setRenderer(this);
        glSurfaceView2.setRenderMode(0);
        glSurfaceView2.setPreserveEGLContextOnPause(true);
        glSurfaceView2.setDetachedListener(new MapboxGLSurfaceView.OnGLSurfaceViewDetachedListener() {
            /* class com.mapbox.mapboxsdk.maps.renderer.glsurfaceview.GLSurfaceViewMapRenderer.AnonymousClass1 */

            public void onGLSurfaceViewDetached() {
                GLSurfaceViewMapRenderer.this.nativeReset();
            }
        });
    }

    public void onStop() {
        this.glSurfaceView.onPause();
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onStart() {
        this.glSurfaceView.onResume();
    }

    public void onResume() {
        super.onResume();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
    }

    /* access modifiers changed from: protected */
    public void onSurfaceDestroyed() {
        super.onSurfaceDestroyed();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
    }

    public void requestRender() {
        this.glSurfaceView.requestRender();
    }

    public void queueEvent(Runnable runnable) {
        this.glSurfaceView.queueEvent(runnable);
    }
}
