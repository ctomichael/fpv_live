package com.mapbox.android.gestures;

public class MultiFingerDistancesObject {
    private final float currFingersDiffX;
    private final float currFingersDiffXY;
    private final float currFingersDiffY;
    private final float prevFingersDiffX;
    private final float prevFingersDiffXY;
    private final float prevFingersDiffY;

    public MultiFingerDistancesObject(float prevFingersDiffX2, float prevFingersDiffY2, float currFingersDiffX2, float currFingersDiffY2) {
        this.prevFingersDiffX = prevFingersDiffX2;
        this.prevFingersDiffY = prevFingersDiffY2;
        this.currFingersDiffX = currFingersDiffX2;
        this.currFingersDiffY = currFingersDiffY2;
        this.prevFingersDiffXY = (float) Math.sqrt((double) ((prevFingersDiffX2 * prevFingersDiffX2) + (prevFingersDiffY2 * prevFingersDiffY2)));
        this.currFingersDiffXY = (float) Math.sqrt((double) ((currFingersDiffX2 * currFingersDiffX2) + (currFingersDiffY2 * currFingersDiffY2)));
    }

    public float getPrevFingersDiffX() {
        return this.prevFingersDiffX;
    }

    public float getPrevFingersDiffY() {
        return this.prevFingersDiffY;
    }

    public float getCurrFingersDiffX() {
        return this.currFingersDiffX;
    }

    public float getCurrFingersDiffY() {
        return this.currFingersDiffY;
    }

    public float getPrevFingersDiffXY() {
        return this.prevFingersDiffXY;
    }

    public float getCurrFingersDiffXY() {
        return this.currFingersDiffXY;
    }
}
