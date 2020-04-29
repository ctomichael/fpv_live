package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.transfer.base.ITransferFileObject;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Keep
@EXClassNullAway
public class DataCommonTransferFileData extends DataCommonTransferFile {
    private static DataCommonTransferFileData mInstance;
    protected byte[] mFileData;
    protected int mSequence = 0;

    public static synchronized DataCommonTransferFileData getInstance() {
        DataCommonTransferFileData dataCommonTransferFileData;
        synchronized (DataCommonTransferFileData.class) {
            if (mInstance == null) {
                mInstance = new DataCommonTransferFileData(ITransferFileObject.TransferCmdType.DATA);
            }
            dataCommonTransferFileData = mInstance;
        }
        return dataCommonTransferFileData;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack == null || pack.senderType != DeviceType.OFDM.value()) {
            super.setPushRecPack(pack);
        } else {
            DataCommonTransferFileRealTimeData.getInstance().outerSetPushRecPack(pack);
        }
    }

    public static DataCommonTransferFileData newInstance(DeviceType receiveType) {
        return newInstance(receiveType, 0);
    }

    public static DataCommonTransferFileData newInstance(DeviceType receiveType, int receiveId) {
        DataCommonTransferFileData data = new DataCommonTransferFileData(ITransferFileObject.TransferCmdType.DATA);
        data.setReceiveType(receiveType).setReceiveId(receiveId);
        return data;
    }

    public DataCommonTransferFileData setSequence(int sequence) {
        this.mSequence = sequence;
        return this;
    }

    public DataCommonTransferFileData setFileData(byte[] data) {
        this.mFileData = data;
        return this;
    }

    public int getSequence() {
        return this.mSequence;
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

    public List<Integer> getLostSegment() {
        if (this._recData == null) {
            return null;
        }
        int segment = 0;
        List<Integer> segmentList = new ArrayList<>();
        while (this._recData.length >= (segment + 1) * 8) {
            int sequence = BytesUtil.getInt(this._recData, (segment * 8) + 4, 4);
            try {
                int count = BytesUtil.getInt(this._recData, (segment + 1) * 8, 4);
                for (int i = 0; i < count; i++) {
                    segmentList.add(Integer.valueOf(sequence + i));
                }
                segment++;
            } catch (Exception e) {
                return segmentList;
            }
        }
        return segmentList;
    }

    private DataCommonTransferFileData(ITransferFileObject.TransferCmdType type) {
        super(type);
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
