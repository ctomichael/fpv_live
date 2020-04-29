package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.transfer.base.ITransferFileObject;
import dji.midware.util.BytesUtil;
import java.util.Arrays;

@Keep
@EXClassNullAway
public class DataCommonTransferFileVerify extends DataCommonTransferFile {
    private byte[] mVerifyData;
    private ITransferFileObject.CommonTransferVerifyType mVerifyType = ITransferFileObject.CommonTransferVerifyType.OTHER;

    public static DataCommonTransferFileVerify newInstance(DeviceType receiveType) {
        return newInstance(receiveType, 0);
    }

    public static DataCommonTransferFileVerify newInstance(DeviceType receiveType, int receiveId) {
        DataCommonTransferFileVerify verify = new DataCommonTransferFileVerify(ITransferFileObject.TransferCmdType.VERIFY);
        verify.setReceiveType(receiveType).setReceiveId(receiveId);
        return verify;
    }

    public DataCommonTransferFileVerify setVerifyType(ITransferFileObject.CommonTransferVerifyType type) {
        this.mVerifyType = type;
        return this;
    }

    public DataCommonTransferFileVerify setVerifyData(byte[] data) {
        this.mVerifyData = data;
        return this;
    }

    protected DataCommonTransferFileVerify(ITransferFileObject.TransferCmdType type) {
        super(type);
    }

    public int getIndex() {
        if (this._recData == null || this._recData.length <= 3) {
            return 0;
        }
        return BytesUtil.getInt(this._recData, 0, 4);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int dataLength;
        switch (this.mVerifyType) {
            case MD5:
                dataLength = 16;
                break;
            case CRC16:
                dataLength = 2;
                break;
            case CRC32:
                dataLength = 4;
                break;
            default:
                dataLength = 0;
                break;
        }
        int length = dataLength + 1;
        if (this._sendData == null || this._sendData.length != length) {
            this._sendData = new byte[length];
        } else {
            Arrays.fill(this._sendData, (byte) 0);
        }
        this._sendData[0] = (byte) this.mCmdType.value();
        int index = 0 + 1;
        if (this.mVerifyData != null && dataLength != 0 && this.mVerifyData.length == dataLength) {
            System.arraycopy(this.mVerifyData, 0, this._sendData, index, dataLength);
        }
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
        pack.cmdId = CmdIdCommon.CmdIdType.TransferFile.value();
        pack.repeatTimes = 10;
        pack.timeOut = 5000;
        start(pack, callBack);
    }
}
