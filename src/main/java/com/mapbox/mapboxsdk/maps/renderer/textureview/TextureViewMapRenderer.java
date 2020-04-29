package com.mapbox.mapboxsdk.maps.renderer.textureview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.TextureView;
import com.mapbox.mapboxsdk.maps.renderer.MapRenderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TextureViewMapRenderer extends MapRenderer {
    private TextureViewRenderThread renderThread;
    private boolean translucentSurface;

    public TextureViewMapRenderer(@NonNull Context context, @NonNull TextureView textureView, String localIdeographFontFamily, boolean translucentSurface2) {
        super(context, localIdeographFontFamily);
        this.translucentSurface = translucentSurface2;
        this.renderThread = new TextureViewRenderThread(textureView, this);
        this.renderThread.start();
    }

    /* access modifiers changed from: protected */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
    }

    /* access modifiers changed from: protected */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    /* access modifiers changed from: protected */
    public void onSurfaceDestroyed() {
        super.onSurfaceDestroyed();
    }

    /* access modifiers changed from: protected */
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
    }

    public void requestRender() {
        this.renderThread.requestRender();
    }

    public void queueEvent(Runnable runnable) {
        this.renderThread.queueEvent(runnable);
    }

    public void onStop() {
        this.renderThread.onPause();
    }

    public void onStart() {
        this.renderThread.onResume();
    }

    public void onDestroy() {
        this.renderThread.onDestroy();
    }

    public boolean isTranslucentSurface() {
        return this.translucentSurface;
    }
}
