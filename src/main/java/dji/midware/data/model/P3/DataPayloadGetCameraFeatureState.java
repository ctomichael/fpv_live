package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdPayloadSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataPayloadGetCameraFeatureState extends DataBase implements DJIDataSyncListener {
    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public boolean isDigitalZoomSupported() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isOpticalZoomSupported() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isAdjustableFocalPointSupported() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 4) != 0;
    }

    public boolean isMeteringSupported() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 8) != 0;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.receiverId = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.PayloadSDK.value();
        pack.cmdId = CmdIdPayloadSDK.CmdIdType.getFeatureofCamera.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 6;
        start(pack, callBack);
    }
}
