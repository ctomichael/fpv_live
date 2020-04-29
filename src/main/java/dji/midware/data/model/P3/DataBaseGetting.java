package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataBaseGetting extends DataBase implements DJIDataSyncListener {
    protected int cmdId;
    protected CmdSet cmdSet;
    protected DeviceType receiver;
    protected int value;

    public DataBaseGetting setCmdId(int cmdId2) {
        this.cmdId = cmdId2;
        return this;
    }

    public DataBaseGetting setCmdSet(CmdSet cmdSet2) {
        this.cmdSet = cmdSet2;
        return this;
    }

    public DataBaseGetting setReceiver(DeviceType device) {
        this.receiver = device;
        return this;
    }

    public int getValue() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getValue(int from, int length) {
        if (this._recData == null || from + length > this._recData.length) {
            return -1;
        }
        return ((Integer) get(from, length, Integer.class)).intValue();
    }

    public byte[] getData() {
        return this._recData;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.receiver.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = this.cmdSet.value();
        pack.cmdId = this.cmdId;
        start(pack, callBack);
    }
}
