package dji.midware.data.packages.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.queue.P3.PackUtil;
import dji.midware.natives.GroudStation;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class SendPack extends Pack {
    public static final int packRepeatTimes = 2;
    public static final int packTimeOut = 1000;
    public PackBufferObject bufferObject;
    public int repeatTimes = 2;
    public int timeOut = 1000;

    public void doPack() {
        int length;
        if (this.data == null) {
            length = 13;
        } else {
            length = this.data.length + 13;
        }
        this.length = length;
        this.bufferObject = PackBufferObject.getPackBufferObject(this.length);
        byte[] box_head = this.bufferObject.getBuffer();
        box_head[0] = this.sof;
        int it2 = 0 + 1;
        byte[] VL = BytesUtil.getBytes((this.version << 10) | this.length);
        box_head[it2] = VL[0];
        int it3 = it2 + 1;
        box_head[it3] = VL[1];
        int it4 = it3 + 1;
        box_head[it4] = GroudStation.native_calcCrc8(BytesUtil.readBytes(box_head, 0, 3));
        int it5 = it4 + 1;
        box_head[it5] = (byte) ((this.senderId << 5) | this.senderType);
        int it6 = it5 + 1;
        box_head[it6] = (byte) ((this.receiverId << 5) | this.receiverType);
        int it7 = it6 + 1;
        this.seq = this.seq == 0 ? PackUtil.getSeq() : this.seq;
        byte[] seqs = BytesUtil.getBytes(this.seq);
        box_head[it7] = seqs[0];
        int it8 = it7 + 1;
        box_head[it8] = seqs[1];
        int it9 = it8 + 1;
        box_head[it9] = (byte) ((this.cmdType << 7) | (this.isNeedAck << 5) | this.encryptType);
        int it10 = it9 + 1;
        box_head[it10] = (byte) this.cmdSet;
        int it11 = it10 + 1;
        box_head[it11] = (byte) this.cmdId;
        int it12 = it11 + 1;
        if (this.data != null) {
            for (byte b : this.data) {
                box_head[it12] = b;
                it12++;
            }
        }
        byte[] crcs = BytesUtil.getBytes(GroudStation.native_calcCrc16(BytesUtil.readBytes(box_head, 0, this.length - 2)));
        box_head[it12] = crcs[0];
        int it13 = it12 + 1;
        box_head[it13] = crcs[1];
        int it14 = it13 + 1;
        this.buffer = box_head;
    }

    public String toString() {
        if (this.buffer != null) {
            return BytesUtil.byte2hex(this.buffer, 0, getLength());
        }
        return "";
    }

    public void reCrc() {
        byte[] crcs = BytesUtil.getBytes(GroudStation.native_calcCrc16(this.buffer, this.length - 2));
        this.buffer[this.length - 2] = crcs[0];
        this.buffer[this.length - 1] = crcs[1];
    }
}
