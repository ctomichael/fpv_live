package com.dji.video.framing.internal.opengl;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLUtils;
import android.util.Log;

@TargetApi(18)
public class GLContextMgr18plus extends GLContextMgrBase {
    public static final String TAG = "GLContextMgr18";
    private EGLConfig[] configs;
    private EGLContext mEglContext = EGL14.EGL_NO_CONTEXT;
    private EGLDisplay mEglDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLSurface mEglSurfaceDraw = EGL14.EGL_NO_SURFACE;
    private EGLSurface mEglSurfaceRead = EGL14.EGL_NO_SURFACE;

    public GLContextMgr18plus() {
        this.hasInit = true;
        initDisplay();
        initConfig();
        Log.i(TAG, "GLContextMgr18 init successful!");
    }

    /* access modifiers changed from: protected */
    public void initConfig() {
        this.configs = new EGLConfig[1];
        if (!EGL14.eglChooseConfig(this.mEglDisplay, new int[]{12320, 32, 12321, 8, 12322, 8, 12323, 8, 12324, 8, 12352, 4, 12339, 4, 12344}, 0, this.configs, 0, 1, new int[1], 0)) {
            throw new RuntimeException("eglChooseConfig failed : " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
        }
        checkGlError("initConfig");
    }

    public void createContext() {
        this.mEglContext = EGL14.eglCreateContext(this.mEglDisplay, this.configs[0], EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
        if (this.mEglContext.equals(EGL14.EGL_NO_CONTEXT)) {
            Log.e(TAG, "create context returns EGL_NO_CONTEXT");
        }
        checkGlError("initContext");
    }

    public void bindSurface(Object displaySurfaceTexture) {
        if (displaySurfaceTexture == null) {
            EGLSurface eglGetCurrentSurface = EGL14.eglGetCurrentSurface(12377);
            this.mEglSurfaceDraw = eglGetCurrentSurface;
            this.mEglSurfaceRead = eglGetCurrentSurface;
            if (this.mEglSurfaceRead.equals(EGL14.EGL_NO_SURFACE)) {
            }
            return;
        }
        EGLSurface eglCreateWindowSurface = EGL14.eglCreateWindowSurface(this.mEglDisplay, this.configs[0], displaySurfaceTexture, new int[]{12344}, 0);
        this.mEglSurfaceDraw = eglCreateWindowSurface;
        this.mEglSurfaceRead = eglCreateWindowSurface;
        checkGlError("bind surface");
        if (this.mEglSurfaceRead.equals(EGL14.EGL_NO_SURFACE)) {
            Log.e(TAG, "create surface returns EGL10.EGL_NO_SURFACE");
            if (EGL14.eglGetError() == 12299) {
                throw new RuntimeException("create pBuffer returned EGL_BAD_NATIVE_WINDOW.");
            }
            throw new RuntimeException("create pBuffer failed : " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
        }
    }

    /* access modifiers changed from: protected */
    public void initDisplay() {
        this.mEglDisplay = EGL14.eglGetDisplay(0);
        if (this.mEglDisplay.equals(EGL14.EGL_NO_DISPLAY)) {
            throw new RuntimeException("eglGetdisplay failed : " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
        }
        if (!EGL14.eglInitialize(this.mEglDisplay, new int[1], 0, new int[1], 0)) {
            throw new RuntimeException("eglInitialize failed : " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
        }
        checkGlError("initDisplay");
    }

    public void detachFromThread() {
        if (this.mEglDisplay != null && !this.mEglDisplay.equals(EGL14.EGL_NO_DISPLAY)) {
            EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        }
    }

    public void attachToThread() {
        if (this.mEglDisplay != null && !this.mEglDisplay.equals(EGL14.EGL_NO_DISPLAY)) {
            EGL14.eglMakeCurrent(this.mEglDisplay, this.mEglSurfaceDraw, this.mEglSurfaceRead, this.mEglContext);
            checkGlError("make Current this context");
        }
    }

    public void loadFromThread() {
        this.mEglSurfaceRead = EGL14.eglGetCurrentSurface(12378);
        this.mEglSurfaceDraw = EGL14.eglGetCurrentSurface(12377);
        this.mEglContext = EGL14.eglGetCurrentContext();
        this.mEglDisplay = EGL14.eglGetCurrentDisplay();
    }

    public void destroySurfaces() {
        if (this.mEglSurfaceDraw != null && !this.mEglSurfaceDraw.equals(EGL14.EGL_NO_SURFACE)) {
            EGL14.eglDestroySurface(this.mEglDisplay, this.mEglSurfaceDraw);
            checkGlError("destroy surfaceDraw");
        }
        if (this.mEglSurfaceRead != null && !this.mEglSurfaceRead.equals(EGL14.EGL_NO_SURFACE) && !this.mEglSurfaceRead.equals(this.mEglSurfaceDraw)) {
            EGL14.eglDestroySurface(this.mEglDisplay, this.mEglSurfaceRead);
            checkGlError("destroy surfaceRead");
        }
        this.mEglSurfaceDraw = EGL14.EGL_NO_SURFACE;
        this.mEglSurfaceRead = EGL14.EGL_NO_SURFACE;
    }

    public synchronized void destroyAll() {
        if (this.hasInit) {
            this.hasInit = false;
            if (!this.mEglDisplay.equals(EGL14.EGL_NO_DISPLAY)) {
                EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
                destroySurfaces();
                if (this.mEglContext != null && !this.mEglContext.equals(EGL14.EGL_NO_CONTEXT)) {
                    EGL14.eglDestroyContext(this.mEglDisplay, this.mEglContext);
                    checkGlError("destroy context");
                    this.mEglContext = EGL14.EGL_NO_CONTEXT;
                }
                if (this.mEglDisplay != null && !this.mEglDisplay.equals(EGL14.EGL_NO_DISPLAY)) {
                    EGL14.eglTerminate(this.mEglDisplay);
                    checkGlError("destroy display");
                    this.mEglDisplay = EGL14.EGL_NO_DISPLAY;
                }
            }
        }
    }

    public void swapBuffers() {
        if (this.hasInit) {
            EGL14.eglSwapBuffers(this.mEglDisplay, this.mEglSurfaceDraw);
            checkGlError("swapBuffers");
        }
    }

    public boolean existContext() {
        boolean _display;
        boolean _context;
        if (this.mEglDisplay == null || this.mEglDisplay.equals(EGL14.EGL_NO_DISPLAY)) {
            _display = false;
        } else {
            _display = true;
        }
        if (this.mEglContext == null || this.mEglContext.equals(EGL14.EGL_NO_CONTEXT)) {
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
        EGL14.eglQuerySurface(this.mEglDisplay, this.mEglSurfaceDraw, 12375, re, 0);
        checkGlError("read surface width");
        return re[0];
    }

    public int getSurfaceHeight() {
        int[] re = new int[1];
        EGL14.eglQuerySurface(this.mEglDisplay, this.mEglSurfaceDraw, 12374, re, 0);
        checkGlError("read surface height");
        return re[0];
    }

    public void setPresentationTime(long timeUs) {
        EGLExt.eglPresentationTimeANDROID(this.mEglDisplay, this.mEglSurfaceDraw, 1000 * timeUs);
    }

    public String getTAG() {
        return TAG;
    }
}
