package com.mapbox.android.gestures;

public final class MoveDistancesObject {
    private float currX;
    private float currY;
    private float distanceXSinceLast;
    private float distanceXSinceStart;
    private float distanceYSinceLast;
    private float distanceYSinceStart;
    private final float initialX;
    private final float initialY;
    private float prevX;
    private float prevY;

    public MoveDistancesObject(float initialX2, float initialY2) {
        this.initialX = initialX2;
        this.initialY = initialY2;
    }

    public void addNewPosition(float x, float y) {
        this.prevX = this.currX;
        this.prevY = this.currY;
        this.currX = x;
        this.currY = y;
        this.distanceXSinceLast = this.prevX - this.currX;
        this.distanceYSinceLast = this.prevY - this.currY;
        this.distanceXSinceStart = this.initialX - this.currX;
        this.distanceYSinceStart = this.initialY - this.currY;
    }

    public float getInitialX() {
        return this.initialX;
    }

    public float getInitialY() {
        return this.initialY;
    }

    public float getPreviousX() {
        return this.prevX;
    }

    public float getPreviousY() {
        return this.prevY;
    }

    public float getCurrentX() {
        return this.currX;
    }

    public float getCurrentY() {
        return this.currY;
    }

    public float getDistanceXSinceLast() {
        return this.distanceXSinceLast;
    }

    public float getDistanceYSinceLast() {
        return this.distanceYSinceLast;
    }

    public float getDistanceXSinceStart() {
        return this.distanceXSinceStart;
    }

    public float getDistanceYSinceStart() {
        return this.distanceYSinceStart;
    }
}
