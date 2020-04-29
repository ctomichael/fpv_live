package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.transfer.base.ITransferFileObject;
import dji.midware.util.BytesUtil;
import java.util.Arrays;

@Keep
@EXClassNullAway
public class DataCommonTransferFileRequest extends DataCommonTransferFile {
    private byte[] mFileName;
    private int mFileSize = 0;
    private ITransferFileObject.CommonTransferFileType mFileType = ITransferFileObject.CommonTransferFileType.OTHER;
    private byte[] mParameter;

    public static DataCommonTransferFileRequest newInstance(DeviceType receiveType) {
        return newInstance(receiveType, 0);
    }

    public static DataCommonTransferFileRequest newInstance(DeviceType receiveType, int receiverId) {
        DataCommonTransferFileRequest data = new DataCommonTransferFileRequest(ITransferFileObject.TransferCmdType.REQUEST);
        data.setReceiveType(receiveType).setReceiveId(receiverId);
        return data;
    }

    public DataCommonTransferFileRequest setFileName(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            this.mFileName = BytesUtil.getBytesUTF8(fileName + "\u0000");
        }
        return this;
    }

    public DataCommonTransferFileRequest setFileSize(int fileSize) {
        this.mFileSize = fileSize;
        return this;
    }

    public DataCommonTransferFileRequest setFileType(ITransferFileObject.CommonTransferFileType type) {
        this.mFileType = type;
        return this;
    }

    public DataCommonTransferFileRequest setParameter(byte[] parameter) {
        this.mParameter = parameter;
        return this;
    }

    public int getPackageLength() {
        if (this._recData != null) {
            return BytesUtil.getInt(this._recData, 0, 2);
        }
        return -1;
    }

    public int getWindowSize() {
        if (this._recData != null) {
            return BytesUtil.getInt(this._recData, 2, 2);
        }
        return -1;
    }

    public ITransferFileObject.CommonTransferVerifyType getVerifyType() {
        if (this._recData != null) {
            return ITransferFileObject.CommonTransferVerifyType.find(this._recData[4]);
        }
        return ITransferFileObject.CommonTransferVerifyType.OTHER;
    }

    public ITransferFileObject.CommonTransferProtocolType getProtocolType() {
        if (this._recData == null || this._recData.length < 6) {
            return ITransferFileObject.CommonTransferProtocolType.PROTOCOL_1;
        }
        return ITransferFileObject.CommonTransferProtocolType.find(this._recData[5]);
    }

    private DataCommonTransferFileRequest(ITransferFileObject.TransferCmdType type) {
        super(type);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int fileNameLength;
        int parameterLength;
        if (this.mFileName != null) {
            fileNameLength = this.mFileName.length;
        } else {
            fileNameLength = 0;
        }
        if (this.mParameter != null) {
            parameterLength = this.mParameter.length;
        } else {
            parameterLength = 0;
        }
        int length = fileNameLength + 6 + 1 + 1 + parameterLength;
        if (this._sendData == null || this._sendData.length != length) {
            this._sendData = new byte[length];
        } else {
            Arrays.fill(this._sendData, (byte) 0);
        }
        this._sendData[0] = (byte) this.mCmdType.value();
        int index = 0 + 1;
        System.arraycopy(BytesUtil.getBytes(this.mFileSize), 0, this._sendData, index, 4);
        int index2 = index + 4;
        this._sendData[index2] = (byte) fileNameLength;
        int index3 = index2 + 1;
        if (this.mFileName != null) {
            System.arraycopy(this.mFileName, 0, this._sendData, index3, fileNameLength);
            index3 = fileNameLength + 6;
        }
        this._sendData[index3] = (byte) this.mFileType.value();
        int index4 = index3 + 1;
        this._sendData[index4] = (byte) parameterLength;
        int index5 = index4 + 1;
        if (this.mParameter != null) {
            System.arraycopy(this.mParameter, 0, this._sendData, index5, parameterLength);
        }
        DJILogHelper.getInstance().LOGE(this.TAG, BytesUtil.byte2hex(this._sendData));
    }
}
