package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdDm368;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataDm368SetForesightShowed extends DataBase implements DJIDataSyncListener {
    private static DataDm368SetForesightShowed instance = null;
    private int mIsOpen = 0;

    public static synchronized DataDm368SetForesightShowed getInstance() {
        DataDm368SetForesightShowed dataDm368SetForesightShowed;
        synchronized (DataDm368SetForesightShowed.class) {
            if (instance == null) {
                instance = new DataDm368SetForesightShowed();
            }
            dataDm368SetForesightShowed = instance;
        }
        return dataDm368SetForesightShowed;
    }

    public DataDm368SetForesightShowed setIsOpen(boolean isOpen) {
        if (isOpen) {
            this.mIsOpen = 1;
        } else {
            this.mIsOpen = 0;
        }
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DM368.value();
        pack.receiverId = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.SetForesightShowed.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mIsOpen;
    }
}
