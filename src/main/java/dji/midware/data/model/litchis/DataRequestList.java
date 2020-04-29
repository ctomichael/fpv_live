package dji.midware.data.model.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.packages.litchis.FileSendPack;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DataRequestList extends DataAppRequest {
    private static DataRequestList instance = null;
    private int index;
    private int num;
    private DataCameraSetStorageInfo.Storage storage = DataCameraSetStorageInfo.Storage.INNER_STORAGE;
    private DataConfig.SubType subType;

    public static synchronized DataRequestList getInstance() {
        DataRequestList dataRequestList;
        synchronized (DataRequestList.class) {
            if (instance == null) {
                instance = new DataRequestList();
            }
            dataRequestList = instance;
        }
        return dataRequestList;
    }

    public DataRequestList setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataRequestList setStorage(DataCameraSetStorageInfo.Storage storage2) {
        this.storage = storage2;
        return this;
    }

    public DataRequestList setNum(int num2) {
        this.num = num2;
        return this;
    }

    public DataRequestList setSubType(DataConfig.SubType subType2) {
        this.subType = subType2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        FileSendPack sendPack = new FileSendPack();
        sendPack.data = new byte[7];
        byte[] indexBytes = BytesUtil.getBytes(this.index);
        indexBytes[3] = (byte) (indexBytes[3] | (BytesUtil.getByte(this.storage.value()) << 6));
        byte[] numBytes = BytesUtil.getUnsignedBytes(this.num);
        System.arraycopy(indexBytes, 0, sendPack.data, 0, indexBytes.length);
        System.arraycopy(numBytes, 0, sendPack.data, 4, numBytes.length);
        sendPack.data[6] = (byte) this.subType.value();
        sendPack.cmdId = DataConfig.CmdId.List.value();
        sendPack.cmdType = DataConfig.CmdType.REQUEST.value();
        sendPack.doPack();
        this._sendData = sendPack.buffer;
    }
}
