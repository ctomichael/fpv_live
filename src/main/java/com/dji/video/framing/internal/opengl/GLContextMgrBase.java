package com.dji.video.framing.internal.opengl;

import android.opengl.GLES20;
import android.util.Log;

public abstract class GLContextMgrBase {
    public final boolean DEBUG = false;
    protected final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    protected final int EGL_OPENGL_ES2_BIT = 4;
    protected boolean hasInit = false;

    public abstract void attachToThread();

    public abstract void bindSurface(Object obj);

    public abstract void createContext();

    public abstract void destroyAll();

    public abstract void destroySurfaces();

    public abstract void detachFromThread();

    public abstract boolean existContext();

    public abstract int getSurfaceHeight();

    public abstract int getSurfaceWidth();

    public abstract String getTAG();

    public abstract void loadFromThread();

    public abstract void setPresentationTime(long j);

    public abstract void swapBuffers();

    public static GLContextMgrBase createGLContextMgrInstance() {
        return new GLContextMgr18plus();
    }

    /* access modifiers changed from: protected */
    public void checkGlError(String op) {
        while (true) {
            int error = GLES20.glGetError();
            if (error != 0) {
                Log.e(getTAG(), op + ": glError " + error);
            } else {
                return;
            }
        }
    }
}
