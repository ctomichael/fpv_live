package com.mapbox.mapboxsdk.maps.renderer;

import android.support.annotation.Keep;

public interface MapRendererScheduler {
    @Keep
    void queueEvent(Runnable runnable);

    @Keep
    void requestRender();
}
