package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.Arrays;

@Keep
@EXClassNullAway
public class DataCommonTranslateData extends DataBase implements DJIDataSyncListener {
    private static DataCommonTranslateData instance = null;
    private byte[] mDatas = null;
    private int mEncrypt = 0;
    private int mReceiveId = 0;
    private DeviceType mReceiveType = DeviceType.RC;
    private int mSequence = 0;
    private int mSize = 0;

    public static synchronized DataCommonTranslateData getInstance() {
        DataCommonTranslateData dataCommonTranslateData;
        synchronized (DataCommonTranslateData.class) {
            if (instance == null) {
                instance = new DataCommonTranslateData();
            }
            dataCommonTranslateData = instance;
        }
        return dataCommonTranslateData;
    }

    public DataCommonTranslateData setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DataCommonTranslateData setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public DataCommonTranslateData setSequence(int sequence) {
        this.mSequence = sequence;
        return this;
    }

    public DataCommonTranslateData setData(byte[] data) {
        this.mDatas = data;
        return this;
    }

    public DataCommonTranslateData setData(byte[] data, int size) {
        this.mDatas = data;
        this.mSize = size;
        return this;
    }

    public int getSequence() {
        int sequence = this.mSequence;
        if (this._recData == null) {
        }
        if (this._recData != null && this._recData.length >= 4) {
            return ((Integer) get(0, 4, Integer.class)).intValue();
        }
        if (sequence > 0) {
            return sequence - 1;
        }
        return 0;
    }

    public int getFailSequence() {
        if (this._recData == null || this._recData.length < 4) {
            return 0;
        }
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        if (this._recData == null) {
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this.mSize = this.mSize == 0 ? this.mDatas.length : this.mSize;
        int length = this.mSize + 7;
        if (this._sendData == null || this._sendData.length != length) {
            this._sendData = new byte[length];
        } else {
            Arrays.fill(this._sendData, (byte) 0);
        }
        this._sendData[0] = (byte) this.mEncrypt;
        System.arraycopy(BytesUtil.getBytes(this.mSequence), 0, this._sendData, 1, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mSize), 0, this._sendData, 5, 2);
        System.arraycopy(this.mDatas, 0, this._sendData, 7, this.mSize);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.TranslateData.value();
        pack.repeatTimes = 1;
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.TranslateData.value();
        start(pack);
    }
}
