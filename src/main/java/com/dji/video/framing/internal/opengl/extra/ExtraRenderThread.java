package com.dji.video.framing.internal.opengl.extra;

import com.dji.video.framing.internal.opengl.GLContextMgr;
import com.dji.video.framing.internal.opengl.renderer.GLIdentityRender;
import com.dji.video.framing.internal.opengl.renderer.GLRenderBase;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.microedition.khronos.egl.EGLContext;

public class ExtraRenderThread implements Runnable {
    private static final String TAG = "ExtraRenderThread";
    private GLContextMgr glContextMgr;
    private GLContextMgr glContextMgrPre;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private final Object lock = new Object();
    private GLRenderBase renderer;
    private long secondaryDrawCount = 0;
    private EGLContext sharedContext;
    private float[] stMatrix;
    private ArrayDeque<Integer> texIdQueue = new ArrayDeque<>(5);
    private int textureID = -1;
    private Thread thread;
    private int viewRotateDegree;
    private int viewX;
    private int viewportHeight;
    private int viewportWidth;

    public ExtraRenderThread(EGLContext sharedContext2, GLRenderBase render, int textureID2, float[] stMatrix2, int vpX, int rotateDegree) {
        this.sharedContext = sharedContext2;
        this.renderer = render;
        this.textureID = textureID2;
        this.stMatrix = stMatrix2;
        this.viewRotateDegree = rotateDegree;
        this.viewX = vpX;
        init();
    }

    public void init() {
        this.glContextMgrPre = new GLContextMgr();
        this.glContextMgrPre.loadFromThread();
        this.glContextMgr = new GLContextMgr();
        this.glContextMgr.createContext(this.sharedContext);
        this.glContextMgr.attachToThread();
        if (this.renderer == null) {
            this.renderer = new GLIdentityRender(true);
            this.renderer.init();
        }
        this.glContextMgr.detachFromThread();
        this.glContextMgrPre.attachToThread();
    }

    public void startRunning() {
        this.texIdQueue.clear();
        this.isRunning.set(true);
        this.thread = new Thread(this, TAG);
        this.thread.start();
    }

    public void stopRunning() {
        synchronized (this.lock) {
            this.isRunning.set(false);
            this.lock.notifyAll();
            this.thread = null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        if (r8.thread != null) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0023, code lost:
        if (r8.glContextMgr.getSecSurfaceNum() <= 0) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0025, code lost:
        startRunning();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0033, code lost:
        if (r8.glContextMgr.getSecSurfaceNum() > 0) goto L_0x0028;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003b, code lost:
        if (r8.isRunning.get() == false) goto L_0x0028;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003d, code lost:
        stopRunning();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setAsyncRenderSurface(java.lang.String r9, java.lang.Object r10, int r11, int r12, int r13) {
        /*
            r8 = this;
            r6 = 0
            com.dji.video.framing.internal.opengl.GLContextMgr r0 = r8.glContextMgr
            if (r0 == 0) goto L_0x0041
            javax.microedition.khronos.egl.EGLContext r7 = r8.sharedContext
            monitor-enter(r7)
            com.dji.video.framing.internal.opengl.GLContextMgr r0 = r8.glContextMgr     // Catch:{ all -> 0x002a }
            r1 = r9
            r2 = r10
            r3 = r11
            r4 = r12
            r5 = r13
            boolean r0 = r0.bindExtraSurface(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x002a }
            if (r0 != 0) goto L_0x0018
            monitor-exit(r7)     // Catch:{ all -> 0x002a }
            r0 = r6
        L_0x0017:
            return r0
        L_0x0018:
            monitor-exit(r7)     // Catch:{ all -> 0x002a }
            java.lang.Thread r0 = r8.thread
            if (r0 != 0) goto L_0x002d
            com.dji.video.framing.internal.opengl.GLContextMgr r0 = r8.glContextMgr
            int r0 = r0.getSecSurfaceNum()
            if (r0 <= 0) goto L_0x002d
            r8.startRunning()
        L_0x0028:
            r0 = 1
            goto L_0x0017
        L_0x002a:
            r0 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x002a }
            throw r0
        L_0x002d:
            com.dji.video.framing.internal.opengl.GLContextMgr r0 = r8.glContextMgr
            int r0 = r0.getSecSurfaceNum()
            if (r0 > 0) goto L_0x0028
            java.util.concurrent.atomic.AtomicBoolean r0 = r8.isRunning
            boolean r0 = r0.get()
            if (r0 == 0) goto L_0x0028
            r8.stopRunning()
            goto L_0x0028
        L_0x0041:
            r0 = r6
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.video.framing.internal.opengl.extra.ExtraRenderThread.setAsyncRenderSurface(java.lang.String, java.lang.Object, int, int, int):boolean");
    }

    public void setAsyncRenderInterval(String name, int interval) {
        GLContextMgr.ExtraSurfaceInfo surfaceInfo = this.glContextMgr.getExtraSurfaceInfo(name);
        if (surfaceInfo != null) {
            surfaceInfo.drawInterval = interval;
        }
    }

    public int getAsyncRenderInterval(String name) {
        GLContextMgr.ExtraSurfaceInfo surfaceInfo = this.glContextMgr.getExtraSurfaceInfo(name);
        if (surfaceInfo != null) {
            return surfaceInfo.drawInterval;
        }
        return -1;
    }

    public void run() {
        Integer texIdIns;
        if (this.sharedContext != null) {
            while (this.isRunning.get()) {
                synchronized (this.lock) {
                    if (this.texIdQueue.isEmpty()) {
                        try {
                            this.lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    texIdIns = this.texIdQueue.poll();
                }
                if (texIdIns != null) {
                    synchronized (this.sharedContext) {
                        for (String key : this.glContextMgr.getExtraSurfaceKeys()) {
                            if (this.glContextMgr.hasBindSecSurface(key)) {
                                GLContextMgr.ExtraSurfaceInfo surfaceInfo = this.glContextMgr.getExtraSurfaceInfo(key);
                                if (this.secondaryDrawCount % ((long) surfaceInfo.drawInterval) == 0) {
                                    this.glContextMgr.attachSecondaryToThread(key);
                                    this.renderer.draw(this.textureID, 36197, this.stMatrix, false, (float) this.viewRotateDegree, this.viewX, 0, surfaceInfo.width, surfaceInfo.height);
                                    this.glContextMgr.swapExtraBuffers(key);
                                }
                            }
                        }
                        this.secondaryDrawCount++;
                    }
                }
            }
        }
    }

    public void drawNotify(int texId) {
        if (this.isRunning.get()) {
            synchronized (this.lock) {
                boolean needNotify = this.texIdQueue.isEmpty();
                Integer texIdIns = Integer.valueOf(texId);
                this.texIdQueue.remove(texIdIns);
                if (needNotify && this.texIdQueue.offer(texIdIns)) {
                    this.lock.notifyAll();
                }
            }
        }
    }
}
