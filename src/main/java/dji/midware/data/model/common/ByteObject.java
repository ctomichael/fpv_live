package dji.midware.data.model.common;

import android.support.v4.util.Pools;

public class ByteObject {
    private static final int INIT_SIZE = 1024;
    private static final Pools.SynchronizedPool<ByteObject> sPool = new Pools.SynchronizedPool<>(1024);
    private byte[] bytes;
    private int validLen;

    public byte[] getBytes() {
        return this.bytes;
    }

    private ByteObject(int size) {
        this.bytes = new byte[size];
        this.validLen = size;
    }

    public byte[] getBytesWithValidLen() {
        if (this.bytes.length == this.validLen) {
            return this.bytes;
        }
        byte[] result = new byte[this.validLen];
        System.arraycopy(this.bytes, 0, result, 0, this.validLen);
        return result;
    }

    private int getSize() {
        if (this.bytes != null) {
            return this.bytes.length;
        }
        return 0;
    }

    private void setValidLen(int len) {
        this.validLen = len;
    }

    public int getValidLen() {
        return this.validLen;
    }

    private void expendSize(int size) {
        this.bytes = new byte[size];
    }

    public static ByteObject obtain(int byteSize, String logMsg) {
        ByteObject byteObject = sPool.acquire();
        if (byteObject == null) {
            return new ByteObject(byteSize);
        }
        if (byteSize <= byteObject.getSize()) {
            byteObject.setValidLen(byteSize);
            return byteObject;
        }
        byteObject.expendSize(byteSize);
        byteObject.setValidLen(byteSize);
        return byteObject;
    }

    public void recycle() {
        sPool.release(this);
    }
}
