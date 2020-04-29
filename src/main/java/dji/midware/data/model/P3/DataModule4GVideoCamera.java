package dji.midware.data.model.P3;

import android.support.annotation.IntRange;
import dji.midware.data.config.P3.CmdIdModule4G;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataModule4GVideoCamera extends DataBase implements DJIDataSyncListener {
    private boolean isOpen;
    private int mBitRate;
    private DataCameraGetPushStateInfo.CameraType mCameraType;

    public static DataModule4GVideoCamera getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataModule4GVideoCamera INSTANCE = new DataModule4GVideoCamera();

        private Holder() {
        }
    }

    public DataModule4GVideoCamera open4GStream(boolean open) {
        this.isOpen = open;
        return this;
    }

    public DataModule4GVideoCamera setCamera(DataCameraGetPushStateInfo.CameraType cameraType) {
        this.mCameraType = cameraType;
        return this;
    }

    public DataModule4GVideoCamera setBitrate(@IntRange(from = 0, to = 255) int bitRate) {
        this.mBitRate = bitRate;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.isOpen) {
            this._sendData = new byte[2];
            this._sendData[0] = (byte) this.mCameraType.value();
            this._sendData[1] = (byte) this.mBitRate;
            return;
        }
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mCameraType.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DM368.value();
        pack.receiverId = 6;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.Module4G.value();
        if (this.isOpen) {
            pack.cmdId = CmdIdModule4G.CmdIdType.Open4GVideoCamera.value();
        } else {
            pack.cmdId = CmdIdModule4G.CmdIdType.Close4GVideoCamera.value();
        }
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
