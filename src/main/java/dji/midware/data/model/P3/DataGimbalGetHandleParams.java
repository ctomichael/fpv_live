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
public class DataGimbalGetHandleParams extends DataBase implements DJIDataSyncListener {
    private static final String TAG = "DataGimbalGetHandleParams";

    public int getDualChannelEnable() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getTiltDirection() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getPanDirection() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getProfile() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getPitchFree() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public boolean getZoom2SpeedEnable() {
        return (((Integer) get(5, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean getRotationFocusEnable() {
        return (((Integer) get(6, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean getCellphoneSensorDisable() {
        return (((Integer) get(7, 1, Integer.class)).intValue() & 1) == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.GetHandleParams.value();
        start(pack, callBack);
    }
}
