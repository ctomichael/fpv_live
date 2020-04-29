package dji.midware.media.newframing;

import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;

final /* synthetic */ class GLYUVSurface$$Lambda$2 implements Runnable {
    private final GLYUVSurface arg$1;
    private final int arg$2;
    private final int arg$3;
    private final SurfaceInterface.BitmapCallback arg$4;

    GLYUVSurface$$Lambda$2(GLYUVSurface gLYUVSurface, int i, int i2, SurfaceInterface.BitmapCallback bitmapCallback) {
        this.arg$1 = gLYUVSurface;
        this.arg$2 = i;
        this.arg$3 = i2;
        this.arg$4 = bitmapCallback;
    }

    public void run() {
        this.arg$1.lambda$getBitmap$2$GLYUVSurface(this.arg$2, this.arg$3, this.arg$4);
    }
}
