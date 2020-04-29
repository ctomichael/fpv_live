package org.msgpack.core.buffer;

import java.nio.ByteBuffer;
import org.msgpack.core.Preconditions;

public class MessageBufferU extends MessageBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = (!MessageBufferU.class.desiredAssertionStatus());
    private final ByteBuffer wrap;

    MessageBufferU(byte[] bArr, int i, int i2) {
        super(bArr, i, i2);
        ByteBuffer wrap2 = ByteBuffer.wrap(bArr);
        wrap2.position(i);
        wrap2.limit(i + i2);
        this.wrap = wrap2.slice();
    }

    private MessageBufferU(Object obj, long j, int i, ByteBuffer byteBuffer) {
        super(obj, j, i);
        this.wrap = byteBuffer;
    }

    /* JADX INFO: finally extract failed */
    public MessageBufferU slice(int i, int i2) {
        if (i == 0 && i2 == size()) {
            return this;
        }
        Preconditions.checkArgument(i + i2 <= size());
        try {
            this.wrap.position(i);
            this.wrap.limit(i + i2);
            MessageBufferU messageBufferU = new MessageBufferU(this.base, this.address + ((long) i), i2, this.wrap.slice());
            resetBufferPosition();
            return messageBufferU;
        } catch (Throwable th) {
            resetBufferPosition();
            throw th;
        }
    }

    private void resetBufferPosition() {
        this.wrap.position(0);
        this.wrap.limit(this.size);
    }

    public byte getByte(int i) {
        return this.wrap.get(i);
    }

    public boolean getBoolean(int i) {
        return this.wrap.get(i) != 0;
    }

    public short getShort(int i) {
        return this.wrap.getShort(i);
    }

    public int getInt(int i) {
        return this.wrap.getInt(i);
    }

    public float getFloat(int i) {
        return this.wrap.getFloat(i);
    }

    public long getLong(int i) {
        return this.wrap.getLong(i);
    }

    public double getDouble(int i) {
        return this.wrap.getDouble(i);
    }

    public void getBytes(int i, int i2, ByteBuffer byteBuffer) {
        try {
            this.wrap.position(i);
            this.wrap.limit(i + i2);
            byteBuffer.put(this.wrap);
        } finally {
            resetBufferPosition();
        }
    }

    public void putByte(int i, byte b) {
        this.wrap.put(i, b);
    }

    public void putBoolean(int i, boolean z) {
        this.wrap.put(i, z ? (byte) 1 : 0);
    }

    public void putShort(int i, short s) {
        this.wrap.putShort(i, s);
    }

    public void putInt(int i, int i2) {
        this.wrap.putInt(i, i2);
    }

    public void putFloat(int i, float f) {
        this.wrap.putFloat(i, f);
    }

    public void putLong(int i, long j) {
        this.wrap.putLong(i, j);
    }

    public void putDouble(int i, double d) {
        this.wrap.putDouble(i, d);
    }

    public ByteBuffer sliceAsByteBuffer(int i, int i2) {
        try {
            this.wrap.position(i);
            this.wrap.limit(i + i2);
            return this.wrap.slice();
        } finally {
            resetBufferPosition();
        }
    }

    public ByteBuffer sliceAsByteBuffer() {
        return sliceAsByteBuffer(0, this.size);
    }

    public void getBytes(int i, byte[] bArr, int i2, int i3) {
        try {
            this.wrap.position(i);
            this.wrap.get(bArr, i2, i3);
        } finally {
            resetBufferPosition();
        }
    }

    /* JADX INFO: finally extract failed */
    public void putByteBuffer(int i, ByteBuffer byteBuffer, int i2) {
        if (!$assertionsDisabled && i2 > byteBuffer.remaining()) {
            throw new AssertionError();
        } else if (byteBuffer.hasArray()) {
            putBytes(i, byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), i2);
            byteBuffer.position(byteBuffer.position() + i2);
        } else {
            int limit = byteBuffer.limit();
            try {
                byteBuffer.limit(byteBuffer.position() + i2);
                this.wrap.position(i);
                this.wrap.put(byteBuffer);
                byteBuffer.limit(limit);
            } catch (Throwable th) {
                byteBuffer.limit(limit);
                throw th;
            }
        }
    }

    public void putBytes(int i, byte[] bArr, int i2, int i3) {
        try {
            this.wrap.position(i);
            this.wrap.put(bArr, i2, i3);
        } finally {
            resetBufferPosition();
        }
    }

    public void copyTo(int i, MessageBuffer messageBuffer, int i2, int i3) {
        try {
            this.wrap.position(i);
            messageBuffer.putByteBuffer(i2, this.wrap, i3);
        } finally {
            resetBufferPosition();
        }
    }

    public byte[] toByteArray() {
        byte[] bArr = new byte[size()];
        getBytes(0, bArr, 0, bArr.length);
        return bArr;
    }
}
