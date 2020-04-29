package dji.midware.usb.P3;

import dji.midware.data.model.P3.CameraVideoStreamInfo;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataDm368CameraVideoStreams;
import dji.midware.data.model.P3.DataDm368PushLiveVideoStatus;
import dji.midware.data.model.P3.DataModule4GVideoCamera;
import dji.midware.util.DoubleCameraSupportUtil;

public class SdrLteVideoController {
    private boolean mIsLteDebug = false;
    private volatile boolean mIsLteVideoReady = false;
    private volatile DataCameraGetPushStateInfo.CameraType mLteVideoCamera;
    private volatile DataCameraGetPushStateInfo.CameraType mMainVideoCamera;
    private volatile DataCameraGetPushStateInfo.CameraType mSecondaryVideoCamera;

    public static SdrLteVideoController getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static SdrLteVideoController INSTANCE = new SdrLteVideoController();

        private Holder() {
        }
    }

    public void updateCameraStreams(CameraVideoStreamInfo cam1, CameraVideoStreamInfo cam2, CameraVideoStreamInfo fpv) {
        this.mMainVideoCamera = null;
        this.mSecondaryVideoCamera = null;
        this.mLteVideoCamera = null;
        updateStreamDataType(cam1);
        updateStreamDataType(cam2);
        updateStreamDataType(fpv);
    }

    public void updateCameraStreams(DataDm368CameraVideoStreams videoStreams) {
        updateCameraStreams(videoStreams.getMainCameraVideoStream(), videoStreams.getSecondaryCameraVideoStream(), videoStreams.getFpvCameraVideoStream());
    }

    public void updateCameraStreams(DataModule4GVideoCamera videoStreams) {
        updateCameraStreams(videoStreams.getMainCameraVideoStream(), videoStreams.getSecondaryCameraVideoStream(), videoStreams.getFpvCameraVideoStream());
    }

    public void updateCameraStreams(DataDm368PushLiveVideoStatus videoStatus) {
        updateCameraStreams(videoStatus.getMainCameraVideoStream(), videoStatus.getSecondaryCameraVideoStream(), videoStatus.getFpvCameraVideoStream());
    }

    private void updateStreamDataType(CameraVideoStreamInfo streamInfo) {
        if (streamInfo != null) {
            DataCameraGetPushStateInfo.CameraType mLastLteCamera = this.mLteVideoCamera;
            switch (streamInfo.getDataType()) {
                case CameraVideoStreamInfo.DATA_TYPE_SDR_22346:
                    this.mMainVideoCamera = streamInfo.getCameraType();
                    break;
                case CameraVideoStreamInfo.DATA_TYPE_SDR_22350:
                case CameraVideoStreamInfo.DATA_TYPE_SDR_FPV:
                    this.mSecondaryVideoCamera = streamInfo.getCameraType();
                    break;
                case CameraVideoStreamInfo.DATA_TYPE_LTE_VIDEO:
                    this.mLteVideoCamera = streamInfo.getCameraType();
                    break;
            }
            if (streamInfo.isOpenLte()) {
                this.mLteVideoCamera = streamInfo.getCameraType();
            }
            if (mLastLteCamera != this.mLteVideoCamera) {
                this.mIsLteVideoReady = false;
            }
        }
    }

    public DataCameraGetPushStateInfo.CameraType getMainVideoCamera() {
        if (this.mMainVideoCamera != null) {
            return this.mMainVideoCamera;
        }
        return DataCameraGetPushStateInfo.CameraType.OTHER;
    }

    public DataCameraGetPushStateInfo.CameraType getSecondaryVideoCamera() {
        if (this.mSecondaryVideoCamera != null) {
            return this.mSecondaryVideoCamera;
        }
        return DataCameraGetPushStateInfo.CameraType.OTHER;
    }

    public DataCameraGetPushStateInfo.CameraType getLteVideoCamera() {
        if (this.mLteVideoCamera != null) {
            return this.mLteVideoCamera;
        }
        return DataCameraGetPushStateInfo.CameraType.OTHER;
    }

    public boolean isPrimaryVideoFromPrimaryCamera() {
        if (getMainVideoCamera() == DataCameraGetPushStateInfo.getInstance().getCameraType(0)) {
            return true;
        }
        return false;
    }

    public boolean isSecondaryVideoFromSecondaryCamera() {
        if (!DoubleCameraSupportUtil.SupportDoubleCamera) {
            return false;
        }
        DataCameraGetPushStateInfo.CameraType fpvCamera = getSecondaryVideoCamera();
        DataCameraGetPushStateInfo.CameraType secondaryCamera = DataCameraGetPushStateInfo.getInstance().getCameraType(2);
        if (fpvCamera == secondaryCamera || this.mLteVideoCamera == secondaryCamera) {
            return true;
        }
        return false;
    }

    public boolean isMainVideoOpenLte() {
        return (this.mLteVideoCamera != this.mMainVideoCamera || this.mLteVideoCamera == null || this.mLteVideoCamera == DataCameraGetPushStateInfo.CameraType.OTHER) ? false : true;
    }

    public boolean isSecondaryVideoOpenLte() {
        return (this.mLteVideoCamera != this.mSecondaryVideoCamera || this.mLteVideoCamera == null || this.mLteVideoCamera == DataCameraGetPushStateInfo.CameraType.OTHER) ? false : true;
    }

    public boolean isOpenLteVideoAndReady() {
        return (isSecondaryVideoOpenLte() || isMainVideoOpenLte()) && isLteVideoReady();
    }

    public void closeLteVideo() {
        this.mLteVideoCamera = null;
        this.mIsLteVideoReady = false;
    }

    public void setLteVideoReady(boolean isReady) {
        this.mIsLteVideoReady = isReady;
    }

    public boolean isLteVideoReady() {
        return this.mIsLteVideoReady;
    }

    public void setLteDebug(boolean lteDebug) {
        this.mIsLteDebug = lteDebug;
    }

    public boolean isLteDebug() {
        return this.mIsLteDebug;
    }
}
