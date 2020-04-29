package dji.midware.data.model.P3;

import dji.midware.data.model.P3.DataCameraGetPushStateInfo;

public class CameraVideoStreamInfo {
    public static final int DATA_TYPE_LTE_VIDEO = 22352;
    public static final int DATA_TYPE_SDR_22346 = 22346;
    public static final int DATA_TYPE_SDR_22350 = 22350;
    public static final int DATA_TYPE_SDR_FPV = 22351;
    private boolean isOpenLte;
    private boolean isOpenSDR;
    private DataCameraGetPushStateInfo.CameraType mCameraType;
    private int mSdrDataType;

    public CameraVideoStreamInfo(DataCameraGetPushStateInfo.CameraType cameraType) {
        this.mCameraType = cameraType;
    }

    public CameraVideoStreamInfo create(byte data) {
        boolean z = true;
        int sdrStatus = data & 15;
        this.isOpenSDR = true;
        switch (sdrStatus) {
            case 1:
                this.mSdrDataType = DATA_TYPE_SDR_22346;
                break;
            case 2:
                this.mSdrDataType = DATA_TYPE_SDR_22350;
                break;
            case 3:
                this.mSdrDataType = DATA_TYPE_SDR_FPV;
                break;
            default:
                this.isOpenSDR = false;
                break;
        }
        if ((data & Tnaf.POW_2_WIDTH) != 16) {
            z = false;
        }
        this.isOpenLte = z;
        return this;
    }

    public boolean isOpenSDR() {
        return this.isOpenSDR;
    }

    public boolean isOpenLte() {
        return this.isOpenLte;
    }

    public int getDataType() {
        if (isOpenSDR()) {
            return this.mSdrDataType;
        }
        if (isOpenLte()) {
            return DATA_TYPE_LTE_VIDEO;
        }
        return 0;
    }

    public DataCameraGetPushStateInfo.CameraType getCameraType() {
        return this.mCameraType;
    }
}
