package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;

@EXClassNullAway
public class RcvBufferBean {
    private static RcvBufferBean instance = null;
    private static int mlen = 204800;
    private byte[] fullBuffer;
    private int index = 0;
    private int len = 0;
    private int relLen = 0;
    private boolean result = true;
    private int targetIndex = 0;
    private byte[] tmpfullBuffer;

    public static synchronized RcvBufferBean getInstance() {
        RcvBufferBean rcvBufferBean;
        synchronized (RcvBufferBean.class) {
            if (instance == null) {
                instance = new RcvBufferBean();
                instance.init(mlen);
            }
            rcvBufferBean = instance;
        }
        return rcvBufferBean;
    }

    public void init(int len2) {
        this.len = len2;
        this.fullBuffer = new byte[len2];
        this.tmpfullBuffer = new byte[len2];
    }

    public synchronized int getIndex() {
        return this.index;
    }

    public synchronized int getLen() {
        return this.relLen;
    }

    public synchronized boolean put(byte[] buffer) {
        return put(buffer, 0, buffer.length);
    }

    public synchronized boolean put(byte[] buffer, int count) {
        return put(buffer, 0, count);
    }

    public synchronized boolean put(byte[] buffer, int offset, int count) {
        boolean z = false;
        synchronized (this) {
            this.result = true;
            this.targetIndex = this.index + this.relLen + count;
            if (this.targetIndex + 2048 > this.len) {
                System.arraycopy(this.fullBuffer, this.index, this.tmpfullBuffer, 0, this.relLen);
                System.arraycopy(this.tmpfullBuffer, 0, this.fullBuffer, 0, this.relLen);
                this.index = 0;
                this.targetIndex = this.relLen + count;
            }
            if (this.targetIndex > this.len) {
                clear();
                DJILogHelper.getInstance().LOGD("", "buffer 长度太大了 并且不能够重置 " + System.currentTimeMillis(), false, false);
            } else {
                System.arraycopy(buffer, offset, this.fullBuffer, this.index + this.relLen, count);
                this.relLen += count;
                z = this.result;
            }
        }
        return z;
    }

    public synchronized byte[] get() {
        return this.fullBuffer;
    }

    public synchronized byte get(int index2) {
        return this.fullBuffer[this.index + index2];
    }

    public synchronized boolean get(byte[] buffer, int index2, int len2) {
        boolean z = false;
        synchronized (this) {
            if (index2 + len2 <= this.relLen) {
                System.arraycopy(this.fullBuffer, this.index + index2, buffer, 0, len2);
                z = true;
            }
        }
        return z;
    }

    public synchronized void remove(int len2) {
        if (this.relLen != 0) {
            this.index += len2;
            this.relLen -= len2;
        }
    }

    public synchronized void clear() {
        this.index = 0;
        this.relLen = 0;
    }
}
