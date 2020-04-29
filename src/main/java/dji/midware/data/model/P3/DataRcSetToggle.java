package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcSetToggle extends DataBase implements DJIDataSyncListener {
    private static DataRcSetToggle instance = null;
    private boolean isOpen;

    public static synchronized DataRcSetToggle getInstance() {
        DataRcSetToggle dataRcSetToggle;
        synchronized (DataRcSetToggle.class) {
            if (instance == null) {
                instance = new DataRcSetToggle();
            }
            dataRcSetToggle = instance;
        }
        return dataRcSetToggle;
    }

    public DataRcSetToggle setIsOpen(boolean isOpen2) {
        this.isOpen = isOpen2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 1;
        this._sendData = new byte[1];
        byte[] bArr = this._sendData;
        if (!this.isOpen) {
            i = 0;
        }
        bArr[0] = (byte) i;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetToggle.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
