package com.squareup.wire;

public final class Wire {
    private Wire() {
    }

    public static <T> T get(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
