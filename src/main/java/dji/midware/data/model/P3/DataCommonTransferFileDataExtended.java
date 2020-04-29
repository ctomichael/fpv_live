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
public class DataCommonTransferFileDataExtended extends DataCommonTransferFile {
    private static DataCommonTransferFileDataExtended mInstance;
    private byte[] mFileData;
    private int mSequence = 0;

    public static synchronized DataCommonTransferFileDataExtended getInstance() {
        DataCommonTransferFileDataExtended dataCommonTransferFileDataExtended;
        synchronized (DataCommonTransferFileDataExtended.class) {
            if (mInstance == null) {
                mInstance = new DataCommonTransferFileDataExtended(ITransferFileObject.TransferCmdType.DATA_EXTENDED);
            }
            dataCommonTransferFileDataExtended = mInstance;
        }
        return dataCommonTransferFileDataExtended;
    }

    public static DataCommonTransferFileDataExtended newInstance(DeviceType receiveType) {
        return newInstance(receiveType, 0);
    }

    public static DataCommonTransferFileDataExtended newInstance(DeviceType receiveType, int receiveId) {
        DataCommonTransferFileDataExtended data = new DataCommonTransferFileDataExtended(ITransferFileObject.TransferCmdType.DATA_EXTENDED);
        data.setReceiveType(receiveType).setReceiveId(receiveId);
        return data;
    }

    protected DataCommonTransferFileDataExtended(ITransferFileObject.TransferCmdType type) {
        super(type);
    }

    public DataCommonTransferFileDataExtended setSequence(int sequence) {
        this.mSequence = sequence;
        return this;
    }

    public DataCommonTransferFileDataExtended setFileData(byte[] data) {
        this.mFileData = data;
        return this;
    }

    public int getSequence() {
        return this.mSequence;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int dataLength;
        if (this.mFileData != null) {
            dataLength = this.mFileData.length;
        } else {
            dataLength = 0;
        }
        int length = dataLength + 5;
        if (this._sendData == null || this._sendData.length != length) {
            this._sendData = new byte[length];
        } else {
            Arrays.fill(this._sendData, (byte) 0);
        }
        this._sendData[0] = (byte) this.mCmdType.value();
        int index = 0 + 1;
        System.arraycopy(BytesUtil.getBytes(this.mSequence), 0, this._sendData, index, 4);
        int index2 = index + 4;
        if (this.mFileData != null) {
            System.arraycopy(this.mFileData, 0, this._sendData, index2, dataLength);
        }
    }

    public int start() {
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
        return startWithSpecialChannel(pack);
    }
}
