package com.mapbox.mapboxsdk.maps;

import android.support.annotation.Keep;

@Keep
class Image {
    private final byte[] buffer;
    private final int height;
    private final String name;
    private final float pixelRatio;
    private final boolean sdf;
    private final int width;

    public Image(byte[] buffer2, float pixelRatio2, String name2, int width2, int height2, boolean sdf2) {
        this.buffer = buffer2;
        this.pixelRatio = pixelRatio2;
        this.name = name2;
        this.width = width2;
        this.height = height2;
        this.sdf = sdf2;
    }
}
