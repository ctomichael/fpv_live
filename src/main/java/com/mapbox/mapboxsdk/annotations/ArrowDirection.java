package com.mapbox.mapboxsdk.annotations;

@Deprecated
class ArrowDirection {
    static final int BOTTOM = 3;
    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int TOP = 2;
    private final int value;

    ArrowDirection(int value2) {
        this.value = value2;
    }

    public int getValue() {
        return this.value;
    }
}
