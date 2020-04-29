package dji.common.camera;

public final class CameraPeakThreshold {
    private float highCameraPeakThreshold;
    private float lowCameraPeakThreshold;
    private float normalCameraPeakThreshold;

    public CameraPeakThreshold(float lowCameraPeakThreshold2, float normalCameraPeakThreshold2, float highCameraPeakThreshold2) {
        this.lowCameraPeakThreshold = lowCameraPeakThreshold2;
        this.normalCameraPeakThreshold = normalCameraPeakThreshold2;
        this.highCameraPeakThreshold = highCameraPeakThreshold2;
    }

    public float getLowCameraPeakThreshold() {
        return this.lowCameraPeakThreshold;
    }

    public float getNormalCameraPeakThreshold() {
        return this.normalCameraPeakThreshold;
    }

    public float getHighCameraPeakThreshold() {
        return this.highCameraPeakThreshold;
    }
}
