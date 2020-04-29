package com.mapbox.mapboxsdk;

import dji.log.DJILogConstant;

public class MapStrictMode {
    private static volatile boolean strictModeEnabled;

    public static synchronized void setStrictModeEnabled(boolean strictModeEnabled2) {
        synchronized (MapStrictMode.class) {
            strictModeEnabled = strictModeEnabled2;
        }
    }

    public static void strictModeViolation(String message) {
        if (strictModeEnabled) {
            throw new MapStrictModeException(message);
        }
    }

    public static void strictModeViolation(String message, Throwable throwable) {
        if (strictModeEnabled) {
            throw new MapStrictModeException(String.format("%s - %s", message, throwable));
        }
    }

    public static void strictModeViolation(Throwable throwable) {
        if (strictModeEnabled) {
            throw new MapStrictModeException(String.format(DJILogConstant.FORMAT_CONSOLE, throwable));
        }
    }
}
