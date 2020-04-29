package com.mapbox.mapboxsdk.storage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Resource {
    public static final int GLYPHS = 4;
    public static final int SOURCE = 2;
    public static final int SPRITE_IMAGE = 5;
    public static final int SPRITE_JSON = 6;
    public static final int STYLE = 1;
    public static final int TILE = 3;
    public static final int UNKNOWN = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Kind {
    }
}
