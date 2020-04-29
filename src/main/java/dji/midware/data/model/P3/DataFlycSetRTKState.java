package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataFlycSetRTKState extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetRTKState instance = null;
    private boolean isOpen = false;

    public static synchronized DataFlycSetRTKState getInstance() {
        DataFlycSetRTKState dataFlycSetRTKState;
        synchronized (DataFlycSetRTKState.class) {
            if (instance == null) {
                instance = new DataFlycSetRTKState();
            }
            dataFlycSetRTKState = instance;
        }
        return dataFlycSetRTKState;
    }

    public DataFlycSetRTKState setIsOpen(boolean bl) {
        this.isOpen = bl;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetRTKState.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 0;
        this._sendData = new byte[2];
        this._sendData[0] = 3;
        byte[] bArr = this._sendData;
        if (this.isOpen) {
            i = 1;
        }
        bArr[1] = (byte) i;
    }
}
