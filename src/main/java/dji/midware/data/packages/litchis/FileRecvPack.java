package dji.midware.data.packages.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class FileRecvPack extends FilePack {
    public int dataLen;

    public FileRecvPack(byte[] buffer) {
        if (buffer != null && buffer.length >= 10) {
            this.buffer = buffer;
            this.version = buffer[0] >> 6;
            this.length = buffer[0] & 63;
            int it2 = 0 + 1;
            this.cmdId = buffer[it2] >> 5;
            this.cmdType = buffer[it2] & 31;
            int it3 = it2 + 1;
            short flg_len = BytesUtil.getShort(buffer, it3);
            this.isLastFlag = flg_len >> 12;
            this.totalLength = flg_len & 4095;
            int it4 = it3 + 2;
            this.sessionId = BytesUtil.getUShort(buffer, it4);
            int it5 = it4 + 2;
            this.seq = BytesUtil.getInt(buffer, it5);
            int it6 = it5 + 4;
            this.dataLen = buffer.length - this.length;
            if (this.dataLen > 0) {
                this.data = new byte[this.dataLen];
                System.arraycopy(buffer, this.length, this.data, 0, this.dataLen);
            }
        }
    }

    public String toString() {
        return BytesUtil.byte2hex(this.buffer);
    }
}
