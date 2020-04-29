package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdGimbal;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataGimbalGetSerialParams extends DataBase implements DJIDataSyncListener {
    private static DataGimbalGetSerialParams instance = null;

    public static synchronized DataGimbalGetSerialParams getInstance() {
        DataGimbalGetSerialParams dataGimbalGetSerialParams;
        synchronized (DataGimbalGetSerialParams.class) {
            if (instance == null) {
                instance = new DataGimbalGetSerialParams();
            }
            dataGimbalGetSerialParams = instance;
        }
        return dataGimbalGetSerialParams;
    }

    public String getGimbalSerial() {
        if (this._recData == null || this._recData.length <= 2) {
            return "";
        }
        return get(2, this._recData.length - 2);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = 0;
        this._sendData[1] = 2;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.GetSerialParams.value();
        start(pack, callBack);
    }
}
