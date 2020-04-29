package dji.midware.data.model.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.packages.litchis.FileSendPack;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DataRequestAbort extends DataAppRequest {
    private static DataRequestAbort instance = null;
    private DataConfig.CmdId cmdId;
    private AbortReason reason;

    public static synchronized DataRequestAbort getInstance() {
        DataRequestAbort dataRequestAbort;
        synchronized (DataRequestAbort.class) {
            if (instance == null) {
                instance = new DataRequestAbort();
            }
            dataRequestAbort = instance;
        }
        return dataRequestAbort;
    }

    public DataRequestAbort setCmdId(DataConfig.CmdId cmdId2) {
        this.cmdId = cmdId2;
        return this;
    }

    public DataRequestAbort setReason(AbortReason reason2) {
        this.reason = reason2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        FileSendPack sendPack = new FileSendPack();
        sendPack.cmdId = this.cmdId.value();
        sendPack.cmdType = DataConfig.CmdType.ABORT.value();
        if (this.reason != null) {
            sendPack.data = new byte[4];
            System.arraycopy(BytesUtil.getBytes(this.reason.value()), 0, sendPack.data, 0, 4);
            this.reason = AbortReason.Force;
        }
        sendPack.doPackFixSession();
        this._sendData = sendPack.buffer;
    }

    public enum AbortReason {
        Error(0),
        Force(1),
        SizeErr(2),
        ReadFail(3),
        Seek(4),
        UNDEFINED(100);
        
        private int data;

        private AbortReason(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static AbortReason find(int b) {
            AbortReason result = UNDEFINED;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
