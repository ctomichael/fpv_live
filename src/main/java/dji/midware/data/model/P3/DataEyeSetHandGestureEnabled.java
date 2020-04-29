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
public class DataEyeSetHandGestureEnabled extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetHandGestureEnabled instance;
    private boolean enabled;

    public DataEyeSetHandGestureEnabled setEnabled(boolean enabled2) {
        this.enabled = enabled2;
        return this;
    }

    public static synchronized DataEyeSetHandGestureEnabled getInstance() {
        DataEyeSetHandGestureEnabled dataEyeSetHandGestureEnabled;
        synchronized (DataEyeSetHandGestureEnabled.class) {
            if (instance == null) {
                instance = new DataEyeSetHandGestureEnabled();
            }
            dataEyeSetHandGestureEnabled = instance;
        }
        return dataEyeSetHandGestureEnabled;
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetHandGestureEnabled.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 1;
        this._sendData = new byte[1];
        byte[] bArr = this._sendData;
        if (!this.enabled) {
            i = 0;
        }
        bArr[0] = (byte) i;
    }
}
