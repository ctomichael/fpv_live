package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.Keep;

@Keep
public class CannotAddSourceException extends RuntimeException {
    public CannotAddSourceException(String message) {
        super(message);
    }
}
