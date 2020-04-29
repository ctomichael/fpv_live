package com.mapbox.mapboxsdk.maps.renderer.textureview;

import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.view.TextureView;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.renderer.egl.EGLConfigChooser;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

class TextureViewRenderThread extends Thread implements TextureView.SurfaceTextureListener {
    private static final String TAG = "Mbgl-TextureViewRenderThread";
    private boolean destroyContext;
    private boolean destroySurface;
    @NonNull
    private final EGLHolder eglHolder;
    private final ArrayList<Runnable> eventQueue = new ArrayList<>();
    private boolean exited;
    private int height;
    private final Object lock = new Object();
    @NonNull
    private final TextureViewMapRenderer mapRenderer;
    private boolean paused;
    private boolean requestRender;
    private boolean shouldExit;
    private boolean sizeChanged;
    @Nullable
    private SurfaceTexture surface;
    private int width;

    @UiThread
    TextureViewRenderThread(@NonNull TextureView textureView, @NonNull TextureViewMapRenderer mapRenderer2) {
        textureView.setOpaque(!mapRenderer2.isTranslucentSurface());
        textureView.setSurfaceTextureListener(this);
        this.mapRenderer = mapRenderer2;
        this.eglHolder = new EGLHolder(new WeakReference(textureView), mapRenderer2.isTranslucentSurface());
    }

    @UiThread
    public void onSurfaceTextureAvailable(SurfaceTexture surface2, int width2, int height2) {
        synchronized (this.lock) {
            this.surface = surface2;
            this.width = width2;
            this.height = height2;
            this.requestRender = true;
            this.lock.notifyAll();
        }
    }

    @UiThread
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface2, int width2, int height2) {
        synchronized (this.lock) {
            this.width = width2;
            this.height = height2;
            this.sizeChanged = true;
            this.requestRender = true;
            this.lock.notifyAll();
        }
    }

    @UiThread
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface2) {
        synchronized (this.lock) {
            this.surface = null;
            this.destroySurface = true;
            this.requestRender = false;
            this.lock.notifyAll();
        }
        return true;
    }

    @UiThread
    public void onSurfaceTextureUpdated(SurfaceTexture surface2) {
    }

    /* access modifiers changed from: package-private */
    public void requestRender() {
        synchronized (this.lock) {
            this.requestRender = true;
            this.lock.notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    public void queueEvent(@NonNull Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("runnable must not be null");
        }
        synchronized (this.lock) {
            this.eventQueue.add(runnable);
            this.lock.notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    @UiThread
    public void onPause() {
        synchronized (this.lock) {
            this.paused = true;
            this.lock.notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    @UiThread
    public void onResume() {
        synchronized (this.lock) {
            this.paused = false;
            this.lock.notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    @UiThread
    public void onDestroy() {
        synchronized (this.lock) {
            this.shouldExit = true;
            this.lock.notifyAll();
            while (!this.exited) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0100, code lost:
        if (r13.sizeChanged == false) goto L_0x010c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0102, code lost:
        r13.mapRenderer.onSurfaceChanged(r2, r7, r3);
        r13.sizeChanged = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x0114, code lost:
        if (com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread.EGLHolder.access$300(r13.eglHolder) == javax.microedition.khronos.egl.EGL10.EGL_NO_SURFACE) goto L_0x0000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x0116, code lost:
        r13.mapRenderer.onDrawFrame(r2);
        r6 = r13.eglHolder.swap();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x0121, code lost:
        switch(r6) {
            case 12288: goto L_0x0000;
            case 12302: goto L_0x014a;
            default: goto L_0x0124;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0124, code lost:
        com.mapbox.mapboxsdk.log.Logger.w(com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread.TAG, java.lang.String.format("eglSwapBuffer error: %s. Waiting or new surface", java.lang.Integer.valueOf(r6)));
        r9 = r13.lock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x013d, code lost:
        monitor-enter(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:?, code lost:
        r13.surface = null;
        r13.destroySurface = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x0144, code lost:
        monitor-exit(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x014a, code lost:
        com.mapbox.mapboxsdk.log.Logger.w(com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread.TAG, "Context lost. Waiting for re-aquire");
        r9 = r13.lock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x0155, code lost:
        monitor-enter(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:?, code lost:
        r13.surface = null;
        r13.destroySurface = true;
        r13.destroyContext = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x015f, code lost:
        monitor-exit(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0000, code lost:
        continue;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x0000, code lost:
        continue;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0036, code lost:
        if (r1 == null) goto L_0x00b5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r1.run();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        r2 = r13.eglHolder.createGL();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00bb, code lost:
        if (r4 == false) goto L_0x00e9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00bd, code lost:
        r13.eglHolder.prepare();
        r9 = r13.lock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00c4, code lost:
        monitor-enter(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x00cb, code lost:
        if (r13.eglHolder.createSurface() != false) goto L_0x00d6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x00cd, code lost:
        r13.destroySurface = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x00d0, code lost:
        monitor-exit(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:?, code lost:
        monitor-exit(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:?, code lost:
        r13.mapRenderer.onSurfaceCreated(r2, com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread.EGLHolder.access$400(r13.eglHolder));
        r13.mapRenderer.onSurfaceChanged(r2, r7, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x00e9, code lost:
        if (r5 == false) goto L_0x00fe;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x00eb, code lost:
        r9 = r13.lock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x00ed, code lost:
        monitor-enter(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:?, code lost:
        r13.eglHolder.createSurface();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x00f3, code lost:
        monitor-exit(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:?, code lost:
        r13.mapRenderer.onSurfaceChanged(r2, r7, r3);
     */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r13 = this;
        L_0x0000:
            r1 = 0
            r4 = 0
            r5 = 0
            r7 = -1
            r3 = -1
            java.lang.Object r9 = r13.lock     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            monitor-enter(r9)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
        L_0x0008:
            boolean r8 = r13.shouldExit     // Catch:{ all -> 0x005f }
            if (r8 == 0) goto L_0x0022
            monitor-exit(r9)     // Catch:{ all -> 0x005f }
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder
            r8.cleanup()
            java.lang.Object r9 = r13.lock
            monitor-enter(r9)
            r8 = 1
            r13.exited = r8     // Catch:{ all -> 0x001f }
            java.lang.Object r8 = r13.lock     // Catch:{ all -> 0x001f }
            r8.notifyAll()     // Catch:{ all -> 0x001f }
            monitor-exit(r9)     // Catch:{ all -> 0x001f }
        L_0x001e:
            return
        L_0x001f:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x001f }
            throw r8
        L_0x0022:
            java.util.ArrayList<java.lang.Runnable> r8 = r13.eventQueue     // Catch:{ all -> 0x005f }
            boolean r8 = r8.isEmpty()     // Catch:{ all -> 0x005f }
            if (r8 != 0) goto L_0x0052
            java.util.ArrayList<java.lang.Runnable> r8 = r13.eventQueue     // Catch:{ all -> 0x005f }
            r10 = 0
            java.lang.Object r8 = r8.remove(r10)     // Catch:{ all -> 0x005f }
            r0 = r8
            java.lang.Runnable r0 = (java.lang.Runnable) r0     // Catch:{ all -> 0x005f }
            r1 = r0
        L_0x0035:
            monitor-exit(r9)     // Catch:{ all -> 0x005f }
            if (r1 == 0) goto L_0x00b5
            r1.run()     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            goto L_0x0000
        L_0x003c:
            r8 = move-exception
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder
            r8.cleanup()
            java.lang.Object r9 = r13.lock
            monitor-enter(r9)
            r8 = 1
            r13.exited = r8     // Catch:{ all -> 0x004f }
            java.lang.Object r8 = r13.lock     // Catch:{ all -> 0x004f }
            r8.notifyAll()     // Catch:{ all -> 0x004f }
            monitor-exit(r9)     // Catch:{ all -> 0x004f }
            goto L_0x001e
        L_0x004f:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x004f }
            throw r8
        L_0x0052:
            boolean r8 = r13.destroySurface     // Catch:{ all -> 0x005f }
            if (r8 == 0) goto L_0x0075
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ all -> 0x005f }
            r8.destroySurface()     // Catch:{ all -> 0x005f }
            r8 = 0
            r13.destroySurface = r8     // Catch:{ all -> 0x005f }
            goto L_0x0035
        L_0x005f:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x005f }
            throw r8     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
        L_0x0062:
            r8 = move-exception
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r9 = r13.eglHolder
            r9.cleanup()
            java.lang.Object r9 = r13.lock
            monitor-enter(r9)
            r10 = 1
            r13.exited = r10     // Catch:{ all -> 0x0165 }
            java.lang.Object r10 = r13.lock     // Catch:{ all -> 0x0165 }
            r10.notifyAll()     // Catch:{ all -> 0x0165 }
            monitor-exit(r9)     // Catch:{ all -> 0x0165 }
            throw r8
        L_0x0075:
            boolean r8 = r13.destroyContext     // Catch:{ all -> 0x005f }
            if (r8 == 0) goto L_0x0082
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ all -> 0x005f }
            r8.destroyContext()     // Catch:{ all -> 0x005f }
            r8 = 0
            r13.destroyContext = r8     // Catch:{ all -> 0x005f }
            goto L_0x0035
        L_0x0082:
            android.graphics.SurfaceTexture r8 = r13.surface     // Catch:{ all -> 0x005f }
            if (r8 == 0) goto L_0x00ae
            boolean r8 = r13.paused     // Catch:{ all -> 0x005f }
            if (r8 != 0) goto L_0x00ae
            boolean r8 = r13.requestRender     // Catch:{ all -> 0x005f }
            if (r8 == 0) goto L_0x00ae
            int r7 = r13.width     // Catch:{ all -> 0x005f }
            int r3 = r13.height     // Catch:{ all -> 0x005f }
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ all -> 0x005f }
            javax.microedition.khronos.egl.EGLContext r8 = r8.eglContext     // Catch:{ all -> 0x005f }
            javax.microedition.khronos.egl.EGLContext r10 = javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT     // Catch:{ all -> 0x005f }
            if (r8 != r10) goto L_0x009e
            r4 = 1
            goto L_0x0035
        L_0x009e:
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ all -> 0x005f }
            javax.microedition.khronos.egl.EGLSurface r8 = r8.eglSurface     // Catch:{ all -> 0x005f }
            javax.microedition.khronos.egl.EGLSurface r10 = javax.microedition.khronos.egl.EGL10.EGL_NO_SURFACE     // Catch:{ all -> 0x005f }
            if (r8 != r10) goto L_0x00aa
            r5 = 1
            goto L_0x0035
        L_0x00aa:
            r8 = 0
            r13.requestRender = r8     // Catch:{ all -> 0x005f }
            goto L_0x0035
        L_0x00ae:
            java.lang.Object r8 = r13.lock     // Catch:{ all -> 0x005f }
            r8.wait()     // Catch:{ all -> 0x005f }
            goto L_0x0008
        L_0x00b5:
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            javax.microedition.khronos.opengles.GL10 r2 = r8.createGL()     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            if (r4 == 0) goto L_0x00e9
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r8.prepare()     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            java.lang.Object r9 = r13.lock     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            monitor-enter(r9)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ all -> 0x00d3 }
            boolean r8 = r8.createSurface()     // Catch:{ all -> 0x00d3 }
            if (r8 != 0) goto L_0x00d6
            r8 = 1
            r13.destroySurface = r8     // Catch:{ all -> 0x00d3 }
            monitor-exit(r9)     // Catch:{ all -> 0x00d3 }
            goto L_0x0000
        L_0x00d3:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x00d3 }
            throw r8     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
        L_0x00d6:
            monitor-exit(r9)     // Catch:{ all -> 0x00d3 }
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewMapRenderer r8 = r13.mapRenderer     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r9 = r13.eglHolder     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            javax.microedition.khronos.egl.EGLConfig r9 = r9.eglConfig     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r8.onSurfaceCreated(r2, r9)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewMapRenderer r8 = r13.mapRenderer     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r8.onSurfaceChanged(r2, r7, r3)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            goto L_0x0000
        L_0x00e9:
            if (r5 == 0) goto L_0x00fe
            java.lang.Object r9 = r13.lock     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            monitor-enter(r9)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ all -> 0x00fb }
            r8.createSurface()     // Catch:{ all -> 0x00fb }
            monitor-exit(r9)     // Catch:{ all -> 0x00fb }
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewMapRenderer r8 = r13.mapRenderer     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r8.onSurfaceChanged(r2, r7, r3)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            goto L_0x0000
        L_0x00fb:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x00fb }
            throw r8     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
        L_0x00fe:
            boolean r8 = r13.sizeChanged     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            if (r8 == 0) goto L_0x010c
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewMapRenderer r8 = r13.mapRenderer     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r8.onSurfaceChanged(r2, r7, r3)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r8 = 0
            r13.sizeChanged = r8     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            goto L_0x0000
        L_0x010c:
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            javax.microedition.khronos.egl.EGLSurface r8 = r8.eglSurface     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            javax.microedition.khronos.egl.EGLSurface r9 = javax.microedition.khronos.egl.EGL10.EGL_NO_SURFACE     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            if (r8 == r9) goto L_0x0000
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewMapRenderer r8 = r13.mapRenderer     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r8.onDrawFrame(r2)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread$EGLHolder r8 = r13.eglHolder     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            int r6 = r8.swap()     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            switch(r6) {
                case 12288: goto L_0x0000;
                case 12302: goto L_0x014a;
                default: goto L_0x0124;
            }     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
        L_0x0124:
            java.lang.String r8 = "Mbgl-TextureViewRenderThread"
            java.lang.String r9 = "eglSwapBuffer error: %s. Waiting or new surface"
            r10 = 1
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r11 = 0
            java.lang.Integer r12 = java.lang.Integer.valueOf(r6)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r10[r11] = r12     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            java.lang.String r9 = java.lang.String.format(r9, r10)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            com.mapbox.mapboxsdk.log.Logger.w(r8, r9)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            java.lang.Object r9 = r13.lock     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            monitor-enter(r9)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r8 = 0
            r13.surface = r8     // Catch:{ all -> 0x0147 }
            r8 = 1
            r13.destroySurface = r8     // Catch:{ all -> 0x0147 }
            monitor-exit(r9)     // Catch:{ all -> 0x0147 }
            goto L_0x0000
        L_0x0147:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0147 }
            throw r8     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
        L_0x014a:
            java.lang.String r8 = "Mbgl-TextureViewRenderThread"
            java.lang.String r9 = "Context lost. Waiting for re-aquire"
            com.mapbox.mapboxsdk.log.Logger.w(r8, r9)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            java.lang.Object r9 = r13.lock     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            monitor-enter(r9)     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
            r8 = 0
            r13.surface = r8     // Catch:{ all -> 0x0162 }
            r8 = 1
            r13.destroySurface = r8     // Catch:{ all -> 0x0162 }
            r8 = 1
            r13.destroyContext = r8     // Catch:{ all -> 0x0162 }
            monitor-exit(r9)     // Catch:{ all -> 0x0162 }
            goto L_0x0000
        L_0x0162:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0162 }
            throw r8     // Catch:{ InterruptedException -> 0x003c, all -> 0x0062 }
        L_0x0165:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0165 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewRenderThread.run():void");
    }

    private static class EGLHolder {
        private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
        private EGL10 egl;
        /* access modifiers changed from: private */
        @Nullable
        public EGLConfig eglConfig;
        /* access modifiers changed from: private */
        public EGLContext eglContext = EGL10.EGL_NO_CONTEXT;
        private EGLDisplay eglDisplay = EGL10.EGL_NO_DISPLAY;
        /* access modifiers changed from: private */
        public EGLSurface eglSurface = EGL10.EGL_NO_SURFACE;
        private final WeakReference<TextureView> textureViewWeakRef;
        private boolean translucentSurface;

        EGLHolder(WeakReference<TextureView> textureViewWeakRef2, boolean translucentSurface2) {
            this.textureViewWeakRef = textureViewWeakRef2;
            this.translucentSurface = translucentSurface2;
        }

        /* access modifiers changed from: package-private */
        public void prepare() {
            this.egl = (EGL10) EGLContext.getEGL();
            if (this.eglDisplay == EGL10.EGL_NO_DISPLAY) {
                this.eglDisplay = this.egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
                if (this.eglDisplay == EGL10.EGL_NO_DISPLAY) {
                    throw new RuntimeException("eglGetDisplay failed");
                }
                if (!this.egl.eglInitialize(this.eglDisplay, new int[2])) {
                    throw new RuntimeException("eglInitialize failed");
                }
            }
            if (this.textureViewWeakRef == null) {
                this.eglConfig = null;
                this.eglContext = EGL10.EGL_NO_CONTEXT;
            } else if (this.eglContext == EGL10.EGL_NO_CONTEXT) {
                this.eglConfig = new EGLConfigChooser(this.translucentSurface).chooseConfig(this.egl, this.eglDisplay);
                this.eglContext = this.egl.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{EGL_CONTEXT_CLIENT_VERSION, 2, 12344});
            }
            if (this.eglContext == EGL10.EGL_NO_CONTEXT) {
                throw new RuntimeException("createContext");
            }
        }

        /* access modifiers changed from: package-private */
        @NonNull
        public GL10 createGL() {
            return (GL10) this.eglContext.getGL();
        }

        /* access modifiers changed from: package-private */
        public boolean createSurface() {
            destroySurface();
            TextureView view = this.textureViewWeakRef.get();
            if (view != null) {
                this.eglSurface = this.egl.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, view.getSurfaceTexture(), new int[]{12344});
            } else {
                this.eglSurface = EGL10.EGL_NO_SURFACE;
            }
            if (this.eglSurface != null && this.eglSurface != EGL10.EGL_NO_SURFACE) {
                return makeCurrent();
            }
            if (this.egl.eglGetError() != 12299) {
                return false;
            }
            Logger.e(TextureViewRenderThread.TAG, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean makeCurrent() {
            if (this.egl.eglMakeCurrent(this.eglDisplay, this.eglSurface, this.eglSurface, this.eglContext)) {
                return true;
            }
            Logger.w(TextureViewRenderThread.TAG, String.format("eglMakeCurrent: %s", Integer.valueOf(this.egl.eglGetError())));
            return false;
        }

        /* access modifiers changed from: package-private */
        public int swap() {
            if (!this.egl.eglSwapBuffers(this.eglDisplay, this.eglSurface)) {
                return this.egl.eglGetError();
            }
            return 12288;
        }

        /* access modifiers changed from: private */
        public void destroySurface() {
            if (this.eglSurface != EGL10.EGL_NO_SURFACE) {
                if (!this.egl.eglDestroySurface(this.eglDisplay, this.eglSurface)) {
                    Logger.w(TextureViewRenderThread.TAG, String.format("Could not destroy egl surface. Display %s, Surface %s", this.eglDisplay, this.eglSurface));
                }
                this.eglSurface = EGL10.EGL_NO_SURFACE;
            }
        }

        /* access modifiers changed from: private */
        public void destroyContext() {
            if (this.eglContext != EGL10.EGL_NO_CONTEXT) {
                if (!this.egl.eglDestroyContext(this.eglDisplay, this.eglContext)) {
                    Logger.w(TextureViewRenderThread.TAG, String.format("Could not destroy egl context. Display %s, Context %s", this.eglDisplay, this.eglContext));
                }
                this.eglContext = EGL10.EGL_NO_CONTEXT;
            }
        }

        private void terminate() {
            if (this.eglDisplay != EGL10.EGL_NO_DISPLAY) {
                if (!this.egl.eglTerminate(this.eglDisplay)) {
                    Logger.w(TextureViewRenderThread.TAG, String.format("Could not terminate egl. Display %s", this.eglDisplay));
                }
                this.eglDisplay = EGL10.EGL_NO_DISPLAY;
            }
        }

        /* access modifiers changed from: package-private */
        public void cleanup() {
            destroySurface();
            destroyContext();
            terminate();
        }
    }
}
