package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataSingleSetMainCameraBandwidthPercent extends DataBase implements DJIDataSyncListener {
    private int percent = 5;
    private int receiverType;

    public DataSingleSetMainCameraBandwidthPercent() {
        setReceiverId(7);
        this.receiverType = DeviceType.GIMBAL.value();
    }

    public DataSingleSetMainCameraBandwidthPercent setReceiverType(int deviceType) {
        this.receiverType = deviceType;
        return this;
    }

    public DataSingleSetMainCameraBandwidthPercent setPercent(int percent2) {
        this.percent = percent2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.percent;
        DataCameraGetPushStateInfo info = DataCameraGetPushStateInfo.getInstance();
        if (!info.isGetted() || info.isPushLosed(0) || !info.hasPrimaryCameraRecData()) {
            this._sendData[1] = -1;
        } else {
            this._sendData[1] = (byte) DataCameraGetPushStateInfo.getInstance().getCameraType(0).value();
        }
        if (!info.isGetted() || info.isPushLosed(2) || !info.hasSecondaryCameraRecData()) {
            this._sendData[2] = -1;
        } else {
            this._sendData[2] = (byte) DataCameraGetPushStateInfo.getInstance().getCameraType(2).value();
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.receiverType;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetMainCameraBandwidthPercent.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 4;
        start(pack, callBack);
    }
}
