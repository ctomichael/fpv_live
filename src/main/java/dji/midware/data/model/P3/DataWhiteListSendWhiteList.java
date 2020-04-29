package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdADS_B;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataWhiteListSendWhiteList extends DataBase implements DJIDataSyncListener {
    private static DataWhiteListSendWhiteList instance;
    private int blockNum = -1;
    private byte[] data = null;
    private int index = -1;
    private int mDataLength = 0;

    public static synchronized DataWhiteListSendWhiteList getInstance() {
        DataWhiteListSendWhiteList dataWhiteListSendWhiteList;
        synchronized (DataWhiteListSendWhiteList.class) {
            if (instance == null) {
                instance = new DataWhiteListSendWhiteList();
            }
            dataWhiteListSendWhiteList = instance;
        }
        return dataWhiteListSendWhiteList;
    }

    public DataWhiteListSendWhiteList setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataWhiteListSendWhiteList setBlockNum(int blockNum2) {
        this.blockNum = blockNum2;
        return this;
    }

    public DataWhiteListSendWhiteList setDataLength(int dataLength) {
        this.mDataLength = dataLength;
        return this;
    }

    public DataWhiteListSendWhiteList setData(byte[] data2) {
        this.data = data2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getExpectIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.data != null) {
            this._sendData = new byte[(this.data.length + 3)];
            this._sendData[0] = (byte) this.index;
            this._sendData[1] = (byte) this.blockNum;
            this._sendData[2] = (byte) this.mDataLength;
            System.arraycopy(this.data, 0, this._sendData, 3, this.data.length);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.ADS_B.value();
        pack.cmdId = CmdIdADS_B.CmdIdType.SendWhiteList.value();
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}
