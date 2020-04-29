package dji.midware.media.newframing;

import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;

public class SurfaceManager {
    @Deprecated
    public static SurfaceInterface createInstance(Class<?> cls) {
        return new GLYUVSurface();
    }

    public static SurfaceInterface createInstance() {
        return new GLYUVSurface();
    }
}
