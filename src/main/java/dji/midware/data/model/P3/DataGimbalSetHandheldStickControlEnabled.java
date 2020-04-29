package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdGimbal;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataGimbalSetHandheldStickControlEnabled extends DataBase implements DJIDataSyncListener {
    private static DataGimbalSetHandheldStickControlEnabled instance = null;
    private boolean isHandheldStickControlEnabled = false;

    public static synchronized DataGimbalSetHandheldStickControlEnabled getInstance() {
        DataGimbalSetHandheldStickControlEnabled dataGimbalSetHandheldStickControlEnabled;
        synchronized (DataGimbalSetHandheldStickControlEnabled.class) {
            if (instance == null) {
                instance = new DataGimbalSetHandheldStickControlEnabled();
            }
            dataGimbalSetHandheldStickControlEnabled = instance;
        }
        return dataGimbalSetHandheldStickControlEnabled;
    }

    public DataGimbalSetHandheldStickControlEnabled setHandheldStickControlEnabled(boolean enabled) {
        this.isHandheldStickControlEnabled = enabled;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 1;
        this._sendData = new byte[1];
        byte[] bArr = this._sendData;
        if (!this.isHandheldStickControlEnabled) {
            i = 0;
        }
        bArr[0] = (byte) i;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.SetHandheldStickControlEnabled.value();
        start(pack, callBack);
    }
}
