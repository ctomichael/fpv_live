package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataEyeGetHandGestureEnabled extends DataBase implements DJIDataSyncListener {
    private static DataEyeGetHandGestureEnabled instance;

    public static synchronized DataEyeGetHandGestureEnabled getInstance() {
        DataEyeGetHandGestureEnabled dataEyeGetHandGestureEnabled;
        synchronized (DataEyeGetHandGestureEnabled.class) {
            if (instance == null) {
                instance = new DataEyeGetHandGestureEnabled();
            }
            dataEyeGetHandGestureEnabled = instance;
        }
        return dataEyeGetHandGestureEnabled;
    }

    public boolean getEnabled() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.GetHandGestureEnabled.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}
