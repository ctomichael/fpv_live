package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOnBoardSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataOnBoardSdkGetPowerState extends DataBase implements DJIDataSyncListener {
    private static DataOnBoardSdkGetPowerState instance = null;

    public static synchronized DataOnBoardSdkGetPowerState getInstance() {
        DataOnBoardSdkGetPowerState dataOnBoardSdkGetPowerState;
        synchronized (DataOnBoardSdkGetPowerState.class) {
            if (instance == null) {
                instance = new DataOnBoardSdkGetPowerState();
            }
            dataOnBoardSdkGetPowerState = instance;
        }
        return dataOnBoardSdkGetPowerState;
    }

    public boolean isPowerOn() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isOverLoaded() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 2) != 0;
    }

    public int getElectricCurrent() {
        return ((Integer) get(1, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = 1;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverId = 7;
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OnboardSDK.value();
        pack.cmdId = CmdIdOnBoardSDK.CmdIdType.setPowerInf.value();
        start(pack, callBack);
    }
}
