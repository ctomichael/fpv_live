package dji.midware.data.packages.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.queue.P3.PackUtil;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class FileSendPack extends FilePack {
    public void doPack() {
        doPack(true);
    }

    public void doPack(boolean changeSessionId) {
        this.length = this.data == null ? 10 : this.data.length + 10;
        byte[] box_head = new byte[this.length];
        box_head[0] = (byte) ((this.version << 6) | 10);
        int it2 = 0 + 1;
        box_head[it2] = (byte) ((this.cmdId << 5) | this.cmdType);
        byte[] lenBytes = BytesUtil.getBytes((short) this.length);
        System.arraycopy(lenBytes, 0, box_head, it2 + 1, lenBytes.length);
        int it3 = lenBytes.length + 2;
        byte[] sessionBytes = BytesUtil.getUnsignedBytes(getSessionID(changeSessionId));
        System.arraycopy(sessionBytes, 0, box_head, it3, sessionBytes.length);
        int it4 = it3 + sessionBytes.length;
        byte[] seqs = BytesUtil.getBytes(0);
        System.arraycopy(seqs, 0, box_head, it4, seqs.length);
        int it5 = it4 + seqs.length;
        if (this.data != null) {
            System.arraycopy(this.data, 0, box_head, it5, this.data.length);
            int it6 = it5 + this.data.length;
        }
        this.buffer = box_head;
    }

    private int getSessionID(boolean changeSessionId) {
        if (!changeSessionId) {
            return PackUtil.sessionId();
        }
        return PackUtil.getSessionId();
    }

    public void doPackFixSession() {
        doPack(false);
    }

    public String toString() {
        return BytesUtil.byte2hex(this.buffer);
    }
}
