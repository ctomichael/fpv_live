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
import dji.midware.util.BytesUtil;
import kotlin.jvm.internal.ByteCompanionObject;

@Keep
@EXClassNullAway
public class DataFlycSendAgpsData extends DataBase implements DJIDataSyncListener {
    private static DataFlycSendAgpsData mInstance = null;
    private byte[] mData;
    private byte[] mHeader = new byte[3];

    public static synchronized DataFlycSendAgpsData getInstance() {
        DataFlycSendAgpsData dataFlycSendAgpsData;
        synchronized (DataFlycSendAgpsData.class) {
            if (mInstance == null) {
                mInstance = new DataFlycSendAgpsData();
            }
            dataFlycSendAgpsData = mInstance;
        }
        return dataFlycSendAgpsData;
    }

    public void setAgpsData(byte[] data) {
        this.mData = data;
    }

    public void setHeaderData(short data_index, byte type, byte complete_flag) {
        System.arraycopy(BytesUtil.getBytes(data_index), 0, this.mHeader, 0, 2);
        this.mHeader[2] = (byte) ((complete_flag << 7) | (type & ByteCompanionObject.MAX_VALUE));
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int dataLen = this.mHeader.length + this.mData.length;
        if (this._sendData == null || this._sendData.length != dataLen) {
            this._sendData = new byte[dataLen];
        }
        System.arraycopy(this.mHeader, 0, this._sendData, 0, this.mHeader.length);
        System.arraycopy(this.mData, 0, this._sendData, this.mHeader.length, this.mData.length);
    }

    public Short getSequence() {
        return (Short) get(0, 2, Short.class);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SendAgpsData.value();
        pack.repeatTimes = 5;
        pack.timeOut = 5000;
        start(pack, callBack);
    }
}
