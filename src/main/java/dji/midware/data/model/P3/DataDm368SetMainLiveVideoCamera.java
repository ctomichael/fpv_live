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

public class DataDm368SetMainLiveVideoCamera extends DataBase implements DJIDataSyncListener {
    private DataCameraGetPushStateInfo.CameraType mCameraType = DataCameraGetPushStateInfo.CameraType.OTHER;
    private DataRcSetMaster.MODE mRcModel;

    public static DataDm368SetMainLiveVideoCamera getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataDm368SetMainLiveVideoCamera INSTANCE = new DataDm368SetMainLiveVideoCamera();

        private Holder() {
        }
    }

    public DataDm368SetMainLiveVideoCamera setCameraType(DataCameraGetPushStateInfo.CameraType cameraType) {
        this.mCameraType = cameraType;
        return this;
    }

    public DataDm368SetMainLiveVideoCamera setRcModel(DataRcSetMaster.MODE model) {
        this.mRcModel = model;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.mCameraType.value();
        byte[] bArr = this._sendData;
        if (this.mRcModel == DataRcSetMaster.MODE.Master) {
            i = 0;
        } else {
            i = 1;
        }
        bArr[1] = (byte) i;
        this._sendData[2] = 0;
        this._sendData[3] = 0;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.SetCurrentCamera.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
