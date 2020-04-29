package dji.midware.data.model.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.packages.litchis.FileSendPack;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DataRequestStream extends DataAppRequest {
    private static DataRequestStream instance = null;
    private int index;
    private byte rateCtrl;
    private byte subIndex;
    private DataConfig.SubType subType;
    private long timeLen;
    private long timeOffset;

    public static synchronized DataRequestStream getInstance() {
        DataRequestStream dataRequestStream;
        synchronized (DataRequestStream.class) {
            if (instance == null) {
                instance = new DataRequestStream();
            }
            dataRequestStream = instance;
        }
        return dataRequestStream;
    }

    public DataRequestStream setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataRequestStream setTimeOffset(long timeOffset2) {
        this.timeOffset = timeOffset2;
        return this;
    }

    public DataRequestStream setTimeLen(long timeLen2) {
        this.timeLen = timeLen2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        FileSendPack sendPack = new FileSendPack();
        sendPack.data = new byte[16];
        byte[] indexBytes = BytesUtil.getBytes(this.index);
        byte[] timeOffsetBytes = BytesUtil.getBytes(this.timeOffset);
        byte[] timeLenBytes = BytesUtil.getBytes(this.timeLen);
        System.arraycopy(indexBytes, 0, sendPack.data, 0, 4);
        System.arraycopy(timeOffsetBytes, 0, sendPack.data, 4, 4);
        System.arraycopy(timeLenBytes, 0, sendPack.data, 8, 4);
        sendPack.cmdId = DataConfig.CmdId.Stream.value();
        sendPack.cmdType = DataConfig.CmdType.REQUEST.value();
        sendPack.doPack();
        this._sendData = sendPack.buffer;
    }
}
