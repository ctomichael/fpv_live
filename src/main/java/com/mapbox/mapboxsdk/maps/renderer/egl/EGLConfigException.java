package com.mapbox.mapboxsdk.maps.renderer.egl;

public class EGLConfigException extends RuntimeException {
    public EGLConfigException() {
    }

    public EGLConfigException(String message) {
        super(message);
    }

    public EGLConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public EGLConfigException(Throwable cause) {
        super(cause);
    }
}
