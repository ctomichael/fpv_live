package dji.midware.data.model.P3;

import dji.midware.data.model.P3.DataCameraGetPushStateInfo;

public class DataDm368CameraVideoStreamGetter {
    private byte[] _recData;

    public DataDm368CameraVideoStreamGetter(byte[] recData) {
        this._recData = recData;
    }

    public CameraVideoStreamInfo getMainCameraVideoStream() {
        if (this._recData == null || this._recData.length < 2) {
            return null;
        }
        return new CameraVideoStreamInfo(DataCameraGetPushStateInfo.CameraType.find(this._recData[0])).create(this._recData[1]);
    }

    public CameraVideoStreamInfo getSecondaryCameraVideoStream() {
        if (this._recData == null || this._recData.length < 4) {
            return null;
        }
        return new CameraVideoStreamInfo(DataCameraGetPushStateInfo.CameraType.find(this._recData[2])).create(this._recData[3]);
    }

    public CameraVideoStreamInfo getFpvCameraVideoStream() {
        if (this._recData == null || this._recData.length < 6) {
            return null;
        }
        return new CameraVideoStreamInfo(DataCameraGetPushStateInfo.CameraType.find(this._recData[4])).create(this._recData[5]);
    }
}
