package dji.midware.data.packages.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class RecvPack extends Pack {
    private static final int MAX_POOL_SIZE = 50;
    protected static final String TAG = RecvPack.class.getSimpleName();
    private static RecvPack sPool;
    private static int sPoolSize = 0;
    private static final Object sPoolSync = new Object();
    public CmdSet cmdSetObj;
    public boolean isNeedCcode = true;
    private RecvPack next;

    public RecvPack(byte[] buffer) {
        setBuffer(buffer);
    }

    private void setBuffer(byte[] buffer) {
        if (buffer != null && buffer.length >= 13) {
            this.buffer = buffer;
            this.sof = buffer[0];
            int it2 = 0 + 1;
            short VL = BytesUtil.getShort(buffer, it2, 2);
            this.version = VL >> 10;
            this.length = VL & 1023;
            int it3 = it2 + 2;
            this.crc8 = buffer[it3];
            int it4 = it3 + 1;
            this.senderId = getInt(it4) >> 5;
            this.senderType = getInt(it4) & 31;
            int it5 = it4 + 1;
            this.receiverId = getInt(it5) >> 5;
            this.receiverType = getInt(it5) & 31;
            int it6 = it5 + 1;
            this.seq = BytesUtil.getInt(buffer, it6, 2);
            int it7 = it6 + 2;
            this.cmdType = getInt(it7) >> 7;
            this.isNeedAck = (getInt(it7) >> 5) & 3;
            this.encryptType = getInt(it7) & 7;
            int it8 = it7 + 1;
            this.cmdSet = getInt(it8);
            int it9 = it8 + 1;
            this.cmdId = getInt(it9);
            int it10 = it9 + 1;
            isNeedCcode();
            if (this.cmdType == 1 && this.isNeedCcode) {
                this.ccode = getInt(it10);
                it10++;
            }
            int dataLen = (buffer.length - it10) - 2;
            if (dataLen > 0) {
                this.data = new byte[dataLen];
                System.arraycopy(buffer, it10, this.data, 0, dataLen);
            }
            this.crc16 = BytesUtil.getInt(buffer, buffer.length - 2, 2);
        }
    }

    private int getInt(int index) {
        return BytesUtil.getInt(this.buffer[index]);
    }

    private void isNeedCcode() {
        try {
            this.cmdSetObj = CmdSet.find(this.cmdSet);
            if (this.cmdSetObj == null || this.cmdSetObj.cmdIdClass() == null) {
                DJILog.save(TAG, "can't find cmdset=" + this.cmdSet);
            } else {
                this.isNeedCcode = this.cmdSetObj.cmdIdClass().isNeedCcode(this.cmdId);
            }
        } catch (Exception e) {
            DJILog.exceptionToString(e);
            DJILog.save(TAG, "cmdset=" + this.cmdSet + ", exption : " + e.getMessage());
        }
    }

    public String toString() {
        return BytesUtil.byte2hex(this.buffer, 0, this.length);
    }

    public static RecvPack obtain(byte[] buffer) {
        return new RecvPack(buffer);
    }

    public void recycle() {
    }

    private void resetData() {
        this.isNeedCcode = true;
        this.buffer = null;
        this.sof = 0;
        this.version = 1;
        this.length = 0;
        this.crc8 = 0;
        this.senderId = 0;
        this.senderType = 0;
        this.receiverId = 0;
        this.receiverType = 0;
        this.seq = 0;
        this.cmdType = 0;
        this.isNeedAck = 0;
        this.encryptType = 0;
        this.cmdSet = 0;
        this.cmdId = 0;
        this.ccode = 0;
        this.data = null;
        this.crc16 = 0;
    }
}
