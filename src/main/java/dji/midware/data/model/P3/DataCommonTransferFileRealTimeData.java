package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.transfer.base.ITransferFileObject;
import dji.midware.util.BytesUtil;
import java.util.Arrays;

@Keep
public class DataCommonTransferFileRealTimeData extends DataCommonTransferFile {
    private static DataCommonTransferFileRealTimeData mInstance;
    protected int isLastSequence;
    protected byte[] mFileData;
    protected int mSequence = 0;
    protected int packSize;

    public static synchronized DataCommonTransferFileRealTimeData getInstance() {
        DataCommonTransferFileRealTimeData dataCommonTransferFileRealTimeData;
        synchronized (DataCommonTransferFileRealTimeData.class) {
            if (mInstance == null) {
                mInstance = new DataCommonTransferFileRealTimeData(ITransferFileObject.TransferCmdType.REAL_TIME_DATA);
            }
            dataCommonTransferFileRealTimeData = mInstance;
        }
        return dataCommonTransferFileRealTimeData;
    }

    public static DataCommonTransferFileRealTimeData newInstance(DeviceType receiveType, int receiveId) {
        DataCommonTransferFileRealTimeData data = new DataCommonTransferFileRealTimeData(ITransferFileObject.TransferCmdType.REAL_TIME_DATA);
        data.setReceiveType(receiveType).setReceiveId(receiveId);
        return data;
    }

    private DataCommonTransferFileRealTimeData(ITransferFileObject.TransferCmdType type) {
        super(type);
    }

    public DataCommonTransferFileRealTimeData setSequence(int sequence) {
        this.mSequence = sequence;
        return this;
    }

    public DataCommonTransferFileRealTimeData setFileData(byte[] data) {
        this.mFileData = data;
        return this;
    }

    public DataCommonTransferFileRealTimeData setLastSequence(Boolean isLastSequence2) {
        this.isLastSequence = isLastSequence2.booleanValue() ? 1 : 0;
        return this;
    }

    public DataCommonTransferFileRealTimeData setPackSize(int packSize2) {
        this.packSize = packSize2;
        return this;
    }

    public int getNextConsequentSequence() {
        if (this._recData != null) {
            return BytesUtil.getInt(this._recData, 4, 4);
        }
        return -1;
    }

    public int getAckSequence() {
        if (this._recData != null) {
            return BytesUtil.getInt(this._recData, 0, 4);
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int dataLength;
        if (this.mFileData != null) {
            dataLength = this.mFileData.length;
        } else {
            dataLength = 0;
        }
        int length = dataLength + 8;
        if (this._sendData == null || this._sendData.length != length) {
            this._sendData = new byte[length];
        } else {
            Arrays.fill(this._sendData, (byte) 0);
        }
        this._sendData[0] = (byte) this.mCmdType.value();
        int index = 0 + 1;
        System.arraycopy(BytesUtil.getBytes(this.mSequence), 0, this._sendData, index, 4);
        int index2 = index + 4;
        this._sendData[index2] = (byte) this.isLastSequence;
        int index3 = index2 + 1;
        System.arraycopy(BytesUtil.getUnsignedBytes(this.packSize), 0, this._sendData, index3, 2);
        int index4 = index3 + 2;
        if (this.mFileData != null) {
            System.arraycopy(this.mFileData, 0, this._sendData, index4, dataLength);
        }
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.TransferFile.value();
        pack.repeatTimes = 0;
        pack.timeOut = 3000;
        start(pack);
    }
}
