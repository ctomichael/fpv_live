package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdDm368;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataRcSetMaster;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataDm368CameraVideoStreams extends DataBase implements DJIDataSyncListener {
    private boolean mIsGet;
    private boolean mIsMainOpen;
    private boolean mIsSecondaryOpen;
    private DataCameraGetPushStateInfo.CameraType mMainCameraType;
    private DataRcSetMaster.MODE mRcMode;
    private DataCameraGetPushStateInfo.CameraType mSecondaryCameraType;

    public static DataDm368CameraVideoStreams getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataDm368CameraVideoStreams INSTANCE = new DataDm368CameraVideoStreams();

        private Holder() {
        }
    }

    public DataDm368CameraVideoStreams isGet(boolean isGet) {
        this.mIsGet = isGet;
        return this;
    }

    public DataDm368CameraVideoStreams setRcMode(DataRcSetMaster.MODE mode) {
        this.mRcMode = mode;
        return this;
    }

    public DataDm368CameraVideoStreams setMainCamera(DataCameraGetPushStateInfo.CameraType mainCamera) {
        this.mMainCameraType = mainCamera;
        return this;
    }

    public DataDm368CameraVideoStreams isMainCameraOpen(boolean isOpen) {
        this.mIsMainOpen = isOpen;
        return this;
    }

    public DataDm368CameraVideoStreams isSecondaryCameraOpen(boolean isOpen) {
        this.mIsSecondaryOpen = isOpen;
        return this;
    }

    public DataDm368CameraVideoStreams setSecondaryCamera(DataCameraGetPushStateInfo.CameraType secondaryCamera) {
        this.mSecondaryCameraType = secondaryCamera;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int main;
        int secondary;
        int rcMode;
        int isGet;
        int i = 0;
        this._sendData = new byte[3];
        if (this.mIsMainOpen) {
            main = 1;
        } else {
            main = 0;
        }
        if (this.mIsSecondaryOpen) {
            secondary = 1;
        } else {
            secondary = 0;
        }
        if (this.mRcMode == DataRcSetMaster.MODE.Slave) {
            rcMode = 1;
        } else {
            rcMode = 0;
        }
        if (this.mIsGet) {
            isGet = 0;
        } else {
            isGet = 1;
        }
        this._sendData[0] = (byte) ((secondary << 1) | main | (isGet << 2) | (rcMode << 3));
        this._sendData[1] = (byte) (this.mMainCameraType == null ? 0 : this.mMainCameraType.value());
        byte[] bArr = this._sendData;
        if (this.mSecondaryCameraType != null) {
            i = this.mSecondaryCameraType.value();
        }
        bArr[2] = (byte) i;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DM368.value();
        pack.receiverId = 6;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.SetVideoCameras.value();
        pack.data = getSendData();
        start(pack, callBack);
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
}
