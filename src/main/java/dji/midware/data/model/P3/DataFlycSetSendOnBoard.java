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
public class DataFlycSetSendOnBoard extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetSendOnBoard instance = null;
    private byte[] data;

    public static synchronized DataFlycSetSendOnBoard getInstance() {
        DataFlycSetSendOnBoard dataFlycSetSendOnBoard;
        synchronized (DataFlycSetSendOnBoard.class) {
            if (instance == null) {
                instance = new DataFlycSetSendOnBoard();
            }
            dataFlycSetSendOnBoard = instance;
        }
        return dataFlycSetSendOnBoard;
    }

    public DataFlycSetSendOnBoard setSendData(byte[] data2) {
        this.data = data2;
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data2) {
        return true;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetSendOnBoard.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = this.data;
    }
}
