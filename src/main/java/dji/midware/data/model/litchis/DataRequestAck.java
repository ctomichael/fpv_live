package dji.midware.data.model.litchis;

import android.util.Pair;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.packages.litchis.FileSendPack;
import dji.midware.util.BytesUtil;
import java.util.List;

@EXClassNullAway
public class DataRequestAck extends DataAppRequest {
    protected DataConfig.CmdId cmdId;
    protected int num;
    private List<Pair<Integer, Integer>> ranges;
    protected int seq;

    public static synchronized DataRequestAck newInstance() {
        DataRequestAck dataRequestAck;
        synchronized (DataRequestAck.class) {
            dataRequestAck = new DataRequestAck();
        }
        return dataRequestAck;
    }

    public DataRequestAck setCmdId(DataConfig.CmdId cmdId2) {
        this.cmdId = cmdId2;
        return this;
    }

    public DataRequestAck setSeq(int seq2) {
        this.seq = seq2;
        return this;
    }

    public DataRequestAck setMissNum(int num2) {
        this.num = num2;
        return this;
    }

    public DataRequestAck setMissRanges(List<Pair<Integer, Integer>> ranges2) {
        this.ranges = ranges2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        FileSendPack sendPack = new FileSendPack();
        sendPack.cmdId = this.cmdId.value();
        sendPack.cmdType = DataConfig.CmdType.ACK.value();
        sendPack.data = new byte[((this.num * 8) + 5)];
        System.arraycopy(BytesUtil.getBytes(this.seq), 0, sendPack.data, 0, 4);
        sendPack.data[4] = (byte) this.num;
        if (this.num > 0) {
            if (this.ranges == null) {
                byte[] missSeqBytes = BytesUtil.getBytes(this.seq);
                System.arraycopy(missSeqBytes, 0, sendPack.data, 5, missSeqBytes.length);
                byte[] missCntBytes = BytesUtil.getBytes(-1);
                System.arraycopy(missCntBytes, 0, sendPack.data, 9, missCntBytes.length);
            } else {
                for (int i = 0; i < this.ranges.size(); i++) {
                    Pair<Integer, Integer> range = this.ranges.get(i);
                    byte[] missSeqBytes2 = BytesUtil.getBytes(((Integer) range.first).intValue());
                    byte[] missCntBytes2 = BytesUtil.getBytes(((Integer) range.second).intValue());
                    System.arraycopy(missSeqBytes2, 0, sendPack.data, (i * 8) + 5, missSeqBytes2.length);
                    System.arraycopy(missCntBytes2, 0, sendPack.data, (i * 8) + 5 + 4, missCntBytes2.length);
                }
                this.ranges.clear();
                this.ranges = null;
            }
        }
        sendPack.doPackFixSession();
        this._sendData = sendPack.buffer;
    }
}
