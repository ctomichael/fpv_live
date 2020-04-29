package com.dji.video.framing.internal.opengl;

import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;
import java.util.Hashtable;
import java.util.Set;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class GLContextMgr extends GLContextMgrBase {
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    public static final String TAG = "GLContextMgr17";
    private EGLConfig[] configs;
    private Hashtable<String, ExtraSurfaceInfo> extraSurfaceInfoMap = new Hashtable<>();
    private Hashtable<String, Long> lastExtraDrawTimeMap = new Hashtable<>();
    private EGL10 mEgl;
    private EGLContext mEglContext = EGL10.EGL_NO_CONTEXT;
    private EGLDisplay mEglDisplay = EGL10.EGL_NO_DISPLAY;
    private EGLSurface mEglSurfaceDraw = EGL10.EGL_NO_SURFACE;
    private EGLSurface mEglSurfaceRead = EGL10.EGL_NO_SURFACE;

    public static class ExtraSurfaceInfo {
        public int drawInterval = 1;
        public int height;
        public EGLSurface surfaceDraw = EGL10.EGL_NO_SURFACE;
        public EGLSurface surfaceRead = EGL10.EGL_NO_SURFACE;
        public int width;

        public ExtraSurfaceInfo(EGLSurface surfaceRead2, EGLSurface surfaceDraw2, int width2, int height2, int drawInterval2) {
            this.surfaceRead = surfaceRead2;
            this.surfaceDraw = surfaceDraw2;
            this.width = width2;
            this.height = height2;
            this.drawInterval = drawInterval2;
        }
    }

    public GLContextMgr() {
        this.hasInit = true;
        this.mEgl = (EGL10) EGLContext.getEGL();
        initDisplay();
        initConfig();
        Log.i(TAG, "OpenGL init Surface/pBuffer/Context successful!");
    }

    /* access modifiers changed from: protected */
    public void initConfig() {
        this.configs = new EGLConfig[1];
        if (!this.mEgl.eglChooseConfig(this.mEglDisplay, new int[]{12320, 32, 12321, 8, 12322, 8, 12323, 8, 12324, 8, 12352, 4, 12339, 4, 12344}, this.configs, 1, new int[1])) {
            throw new RuntimeException("eglChooseConfig failed : " + GLUtils.getEGLErrorString(this.mEgl.eglGetError()));
        }
        checkGlError("initConfig");
    }

    public EGLContext getEglContext() {
        return this.mEglContext;
    }

    private EGLConfig getConfig(boolean with_depth_buffer, boolean isRecordable) {
        int[] attribList = {12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344};
        int offset = 10;
        if (with_depth_buffer) {
            int offset2 = 10 + 1;
            attribList[10] = 12325;
            offset = offset2 + 1;
            attribList[offset2] = 16;
        }
        int offset3 = offset;
        if (isRecordable && Build.VERSION.SDK_INT >= 18) {
            int offset4 = offset3 + 1;
            attribList[offset3] = EGL_RECORDABLE_ANDROID;
            offset3 = offset4 + 1;
            attribList[offset4] = 1;
        }
        int offset5 = offset3;
        for (int i = attribList.length - 1; i >= offset5; i--) {
            attribList[i] = 12344;
        }
        EGLConfig[] configs2 = new EGLConfig[1];
        if (this.mEgl.eglChooseConfig(this.mEglDisplay, attribList, configs2, 1, new int[1])) {
            return configs2[0];
        }
        Log.w(TAG, "unable to find RGBA8888 /  EGLConfig");
        return null;
    }

    public void createContext(EGLContext sharedContext) {
        this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, getConfig(false, true), sharedContext, new int[]{12440, 2, 12344});
        if (this.mEglContext.equals(EGL10.EGL_NO_CONTEXT)) {
            Log.e(TAG, "create context returns EGL_NO_CONTEXT");
        }
        checkGlError("initContext");
    }

    public void createContext() {
        this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, this.configs[0], EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
        if (this.mEglContext.equals(EGL10.EGL_NO_CONTEXT)) {
            Log.e(TAG, "create context returns EGL_NO_CONTEXT");
        }
        checkGlError("initContext");
    }

    /* access modifiers changed from: protected */
    public void checkGlError(String op) {
        while (true) {
            int error = GLES20.glGetError();
            if (error != 0) {
                Log.e(TAG, op + ": glError " + error);
            } else {
                return;
            }
        }
    }

    public void bindSurface(Object displaySurfaceTexture) {
        if (displaySurfaceTexture == null) {
            EGLSurface eglGetCurrentSurface = this.mEgl.eglGetCurrentSurface(12377);
            this.mEglSurfaceDraw = eglGetCurrentSurface;
            this.mEglSurfaceRead = eglGetCurrentSurface;
            if (this.mEglSurfaceRead.equals(EGL10.EGL_NO_SURFACE)) {
            }
            return;
        }
        EGLSurface eglCreateWindowSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.configs[0], displaySurfaceTexture, null);
        this.mEglSurfaceDraw = eglCreateWindowSurface;
        this.mEglSurfaceRead = eglCreateWindowSurface;
        checkGlError("bind surface");
        if (this.mEglSurfaceRead.equals(EGL10.EGL_NO_SURFACE)) {
            int error = this.mEgl.eglGetError();
            if (error == 12299) {
                throw new RuntimeException("create pBuffer returned EGL_BAD_NATIVE_WINDOW.");
            }
            throw new RuntimeException("create pBuffer failed : " + GLUtils.getEGLErrorString(error));
        }
    }

    public boolean bindExtraSurface(String surfaceName, Object secondarySurface, int width, int height, int drawInterval) {
        if (surfaceName == null || "".equals(surfaceName)) {
            return false;
        }
        if (secondarySurface == null || width <= 0 || height <= 0) {
            destroyExtraSurfaces(surfaceName);
        } else {
            if (hasBindSecSurface(surfaceName)) {
                destroyExtraSurfaces(surfaceName);
            }
            EGLSurface eglSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.configs[0], secondarySurface, null);
            this.extraSurfaceInfoMap.put(surfaceName, new ExtraSurfaceInfo(eglSurface, eglSurface, width, height, drawInterval));
            checkGlError("bind surface");
            if (eglSurface.equals(EGL10.EGL_NO_SURFACE)) {
                if (this.mEgl.eglGetError() == 12299) {
                }
                return false;
            }
        }
        return true;
    }

    public boolean hasBindSecSurface(String surfaceName) {
        return this.extraSurfaceInfoMap.get(surfaceName) != null && !EGL10.EGL_NO_SURFACE.equals(this.extraSurfaceInfoMap.get(surfaceName).surfaceDraw);
    }

    public int getSecSurfaceNum() {
        return this.extraSurfaceInfoMap.size();
    }

    public Set<String> getExtraSurfaceKeys() {
        return this.extraSurfaceInfoMap.keySet();
    }

    public ExtraSurfaceInfo getExtraSurfaceInfo(String key) {
        return this.extraSurfaceInfoMap.get(key);
    }

    public long getLastExtraDrawTime(String key) {
        Long time = this.lastExtraDrawTimeMap.get(key);
        if (time != null) {
            return time.longValue();
        }
        return -1;
    }

    public void resetSurface() {
        this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        if (!(this.mEglSurfaceRead == null || this.mEglSurfaceRead == EGL10.EGL_NO_SURFACE)) {
            this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurfaceRead);
            checkGlError("resetSurface eglDestroySurface");
        }
        if (!(this.mEglSurfaceDraw == null || this.mEglSurfaceDraw == this.mEglSurfaceRead || this.mEglSurfaceDraw == EGL10.EGL_NO_SURFACE)) {
            this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurfaceDraw);
            checkGlError("resetSurface eglDestroySurface");
        }
        this.mEglSurfaceRead = EGL10.EGL_NO_SURFACE;
        this.mEglSurfaceDraw = EGL10.EGL_NO_SURFACE;
    }

    /* access modifiers changed from: protected */
    public void initDisplay() {
        this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (this.mEglDisplay.equals(EGL10.EGL_NO_DISPLAY)) {
            throw new RuntimeException("eglGetdisplay failed : " + GLUtils.getEGLErrorString(this.mEgl.eglGetError()));
        }
        if (!this.mEgl.eglInitialize(this.mEglDisplay, new int[2])) {
            throw new RuntimeException("eglInitialize failed : " + GLUtils.getEGLErrorString(this.mEgl.eglGetError()));
        }
        checkGlError("initDisplay");
    }

    public void detachFromThread() {
        if (this.mEgl != null && this.mEglDisplay != null && !this.mEglDisplay.equals(EGL10.EGL_NO_DISPLAY)) {
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        }
    }

    public void attachToThread() {
        if (this.mEgl != null && this.mEglDisplay != null && !this.mEglDisplay.equals(EGL10.EGL_NO_DISPLAY)) {
            this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurfaceDraw, this.mEglSurfaceRead, this.mEglContext);
            checkGlError("make Current this context");
        }
    }

    public void attachSecondaryToThread(String surfaceName) {
        if (this.mEgl != null && this.mEglDisplay != null && !this.mEglDisplay.equals(EGL10.EGL_NO_DISPLAY)) {
            this.mEgl.eglMakeCurrent(this.mEglDisplay, this.extraSurfaceInfoMap.get(surfaceName).surfaceDraw, this.extraSurfaceInfoMap.get(surfaceName).surfaceRead, this.mEglContext);
            checkGlError("make Current this context");
        }
    }

    public void loadFromThread() {
        this.mEglSurfaceRead = this.mEgl.eglGetCurrentSurface(12378);
        this.mEglSurfaceDraw = this.mEgl.eglGetCurrentSurface(12377);
        this.mEglContext = this.mEgl.eglGetCurrentContext();
        this.mEglDisplay = this.mEgl.eglGetCurrentDisplay();
    }

    public void destroySurfaces() {
        if (!(this.mEglSurfaceDraw == null || this.mEglSurfaceDraw == EGL10.EGL_NO_SURFACE)) {
            this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurfaceDraw);
            checkGlError("destroy surfaceDraw");
        }
        if (!(this.mEglSurfaceRead == null || this.mEglSurfaceRead == EGL10.EGL_NO_SURFACE || this.mEglSurfaceRead.equals(this.mEglSurfaceDraw))) {
            this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurfaceRead);
            checkGlError("destroy surfaceRead");
        }
        this.mEglSurfaceDraw = EGL10.EGL_NO_SURFACE;
        this.mEglSurfaceRead = EGL10.EGL_NO_SURFACE;
        for (String str : this.extraSurfaceInfoMap.keySet()) {
            destroyExtraSurfaces(str, false);
        }
        this.extraSurfaceInfoMap.clear();
    }

    public void destroyExtraSurfaces(String surfaceName, boolean needRemoveKey) {
        ExtraSurfaceInfo surfaceInfo = this.extraSurfaceInfoMap.get(surfaceName);
        if (surfaceInfo != null) {
            if (!(surfaceInfo.surfaceDraw == null || surfaceInfo.surfaceDraw == EGL10.EGL_NO_SURFACE)) {
                this.mEgl.eglDestroySurface(this.mEglDisplay, surfaceInfo.surfaceDraw);
                checkGlError("destroy surfaceDraw");
            }
            if (!(surfaceInfo.surfaceRead == null || surfaceInfo.surfaceRead == EGL10.EGL_NO_SURFACE || surfaceInfo.surfaceRead.equals(this.mEglSurfaceDraw))) {
                this.mEgl.eglDestroySurface(this.mEglDisplay, surfaceInfo.surfaceRead);
                checkGlError("destroy surfaceRead");
            }
            surfaceInfo.surfaceDraw = EGL10.EGL_NO_SURFACE;
            surfaceInfo.surfaceRead = EGL10.EGL_NO_SURFACE;
        }
        if (needRemoveKey) {
            this.extraSurfaceInfoMap.remove(surfaceName);
        }
    }

    public void destroyExtraSurfaces(String surfaceName) {
        destroyExtraSurfaces(surfaceName, true);
    }

    public synchronized void destroyAll() {
        if (this.hasInit) {
            this.hasInit = false;
            if (!(this.mEgl == null || this.mEglDisplay == null || this.mEglDisplay == EGL10.EGL_NO_DISPLAY)) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                destroySurfaces();
                if (!(this.mEglContext == null || this.mEglContext == EGL10.EGL_NO_CONTEXT)) {
                    this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
                    checkGlError("destroy context");
                    this.mEglContext = EGL10.EGL_NO_CONTEXT;
                }
                if (!(this.mEglDisplay == null || this.mEglDisplay == EGL10.EGL_NO_DISPLAY)) {
                    this.mEgl.eglTerminate(this.mEglDisplay);
                    checkGlError("destroy display");
                    this.mEglDisplay = EGL10.EGL_NO_DISPLAY;
                }
            }
        }
    }

    public void swapBuffers() {
        if (this.hasInit) {
            this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurfaceDraw);
            checkGlError("swapBuffers");
        }
    }

    public void swapExtraBuffers(String surfaceName) {
        if (this.hasInit) {
            this.mEgl.eglSwapBuffers(this.mEglDisplay, this.extraSurfaceInfoMap.get(surfaceName).surfaceDraw);
            checkGlError("swapBuffers");
            this.lastExtraDrawTimeMap.put(surfaceName, new Long(System.currentTimeMillis()));
        }
    }

    public boolean existContext() {
        boolean _display;
        boolean _context;
        if (this.mEglDisplay == null || this.mEglDisplay.equals(EGL10.EGL_NO_DISPLAY)) {
            _display = false;
        } else {
            _display = true;
        }
        if (this.mEglContext == null || this.mEglContext.equals(EGL10.EGL_NO_CONTEXT)) {
            _context = false;
        } else {
            _context = true;
        }
        if (!_display || !_context) {
            return false;
        }
        return true;
    }

    public int getSurfaceWidth() {
        int[] re = new int[1];
        this.mEgl.eglQuerySurface(this.mEglDisplay, this.mEglSurfaceDraw, 12375, re);
        checkGlError("read surface width");
        return re[0];
    }

    public int getSurfaceHeight() {
        int[] re = new int[1];
        this.mEgl.eglQuerySurface(this.mEglDisplay, this.mEglSurfaceDraw, 12374, re);
        checkGlError("read surface height");
        return re[0];
    }

    public void setPresentationTime(long timeUs) {
    }

    public String getTAG() {
        return TAG;
    }
}
