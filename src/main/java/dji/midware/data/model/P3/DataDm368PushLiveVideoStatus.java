package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;

public class DataDm368PushLiveVideoStatus extends DataBase {
    public static DataDm368PushLiveVideoStatus getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataDm368PushLiveVideoStatus INSTANCE = new DataDm368PushLiveVideoStatus();

        private Holder() {
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public CameraVideoStreamInfo getMainCameraVideoStream() {
        return new DataDm368CameraVideoStreamGetter(this._recData).getMainCameraVideoStream();
    }

    public CameraVideoStreamInfo getSecondaryCameraVideoStream() {
        return new DataDm368CameraVideoStreamGetter(this._recData).getSecondaryCameraVideoStream();
    }

    public CameraVideoStreamInfo getFpvCameraVideoStream() {
        return new DataDm368CameraVideoStreamGetter(this._recData).getFpvCameraVideoStream();
    }

    public int getMainBandwidth() {
        return ((Short) get(6, 1, Short.class)).shortValue() & 15;
    }

    public int getSecondaryBandwidth() {
        return ((Short) get(6, 1, Short.class)).shortValue() >> 4;
    }

    public boolean isActiveCameraEnable() {
        return ((Integer) get(7, 1, Integer.class)).intValue() == 1;
    }

    public DataCameraGetPushStateInfo.CameraType getActiveCamera() {
        return DataCameraGetPushStateInfo.CameraType.find(((Integer) get(8, 1, Integer.class)).intValue());
    }

    public int getLteVideoDataRate() {
        return ((Integer) get(9, 1, Integer.class)).intValue();
    }
}
