package dji.midware.data.model.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.packages.litchis.FileSendPack;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DataRequestFile extends DataAppRequest {
    private static DataRequestFile instance = null;
    private boolean doNotChangeSessionId;
    private int index;
    private int num;
    private long offset;
    private long size;
    private int subIndex;
    private DataConfig.SubType subType;

    public static synchronized DataRequestFile getInstance() {
        DataRequestFile dataRequestFile;
        synchronized (DataRequestFile.class) {
            if (instance == null) {
                instance = new DataRequestFile();
            }
            dataRequestFile = instance;
        }
        return dataRequestFile;
    }

    public DataRequestFile setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataRequestFile setNum(int num2) {
        this.num = num2;
        return this;
    }

    public DataRequestFile setSubType(DataConfig.SubType subType2) {
        this.subType = subType2;
        return this;
    }

    public DataRequestFile setSubIndex(int subIndex2) {
        this.subIndex = subIndex2;
        return this;
    }

    public DataRequestFile setOffset(long offset2) {
        this.offset = offset2;
        return this;
    }

    public DataRequestFile setSize(long size2) {
        this.size = size2;
        return this;
    }

    public DataRequestFile setDoNotChangeSessionId() {
        this.doNotChangeSessionId = true;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        FileSendPack sendPack = new FileSendPack();
        sendPack.data = new byte[16];
        byte[] indexBytes = BytesUtil.getBytes(this.index);
        byte[] numBytes = BytesUtil.getUnsignedBytes(this.num);
        byte[] offsetBytes = BytesUtil.getUnsignedBytes(this.offset);
        byte[] sizeBytes = BytesUtil.getUnsignedBytes(this.size);
        System.arraycopy(indexBytes, 0, sendPack.data, 0, indexBytes.length);
        System.arraycopy(numBytes, 0, sendPack.data, 4, numBytes.length);
        sendPack.data[6] = (byte) this.subType.value();
        sendPack.data[7] = (byte) this.subIndex;
        System.arraycopy(offsetBytes, 0, sendPack.data, 8, offsetBytes.length);
        System.arraycopy(sizeBytes, 0, sendPack.data, 12, sizeBytes.length);
        sendPack.cmdId = DataConfig.CmdId.File.value();
        sendPack.cmdType = DataConfig.CmdType.REQUEST.value();
        if (this.doNotChangeSessionId) {
            sendPack.doPack(false);
            this.doNotChangeSessionId = false;
        } else {
            sendPack.doPack(true);
        }
        this._sendData = sendPack.buffer;
    }
}
