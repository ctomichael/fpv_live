package com.dji.component.fpv.widget.subline;

public abstract class SubLineTypeInfo {
    private boolean mCanDraw;
    private String mKey;
    private float[] mLinePoints;

    /* access modifiers changed from: package-private */
    public abstract float[] calculatePoint(int i, int i2);

    public SubLineTypeInfo(String key) {
        this.mKey = key;
    }

    public String getKey() {
        return this.mKey;
    }

    public float[] getLinePoints() {
        return this.mLinePoints;
    }

    public boolean isCanDraw() {
        return this.mCanDraw;
    }

    public void setCanDraw(boolean canDraw) {
        this.mCanDraw = canDraw;
    }

    public void refreshLinePoints(int width, int height) {
        this.mLinePoints = calculatePoint(width, height);
    }
}
