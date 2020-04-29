package dji.midware.parser.plugins;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.IOException;
import java.io.OutputStream;

@EXClassNullAway
public class DJIParserIOStream extends OutputStream {
    protected byte[] buf;
    protected int count;
    private byte[] mCopybuffer;
    private byte[] mTempbuffer;
    private byte[] mZerobuffer;
    private String name;
    protected int offset;

    public DJIParserIOStream() {
        this.name = "default";
        this.buf = new byte[1024];
        this.mTempbuffer = new byte[1024];
        this.mZerobuffer = new byte[1024];
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public DJIParserIOStream(int size) {
        this.name = "default";
        if (size >= 0) {
            this.buf = new byte[size];
            this.mTempbuffer = new byte[size];
            this.mZerobuffer = new byte[size];
            return;
        }
        throw new IllegalArgumentException(this.name + " size < 0");
    }

    public synchronized int available() {
        return this.count;
    }

    public synchronized int read() {
        byte b;
        if (this.count <= 0) {
            throw new IllegalArgumentException(this.name + " no byte");
        }
        b = this.buf[0];
        this.offset++;
        return b;
    }

    public synchronized int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        checkOffsetAndCount(buffer.length, byteOffset, byteCount);
        if (byteCount > this.count) {
            byteCount = this.count;
        }
        System.arraycopy(this.buf, this.offset, buffer, byteOffset, byteCount);
        resetOffset(byteCount);
        notifyAll();
        return byteCount;
    }

    /* access modifiers changed from: protected */
    public synchronized void resetOffset(int expend) {
        if (this.count < expend) {
            throw new IllegalArgumentException(this.name + " count < expend");
        }
        this.count -= expend;
        System.arraycopy(this.mZerobuffer, 0, this.mTempbuffer, 0, this.count);
        System.arraycopy(this.buf, this.offset + expend, this.mTempbuffer, 0, this.count);
        this.mCopybuffer = this.buf;
        this.buf = this.mTempbuffer;
        this.mTempbuffer = this.mCopybuffer;
        this.offset = 0;
    }

    public synchronized void write(byte[] buffer, int offset2, int len) {
        checkOffsetAndCount(buffer.length, offset2, len);
        if (len != 0) {
            if (this.count + len > this.buf.length) {
                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.count + len > this.buf.length) {
                    throw new ArrayIndexOutOfBoundsException(this.name + " length=" + this.buf.length + "; regionLength=" + (this.count + len));
                }
            }
            System.arraycopy(buffer, offset2, this.buf, this.offset + this.count, len);
            this.count += len;
        }
    }

    public void write(int oneByte) throws IOException {
    }

    public synchronized void skip(int len) {
        if (len > this.count) {
            throw new IllegalArgumentException("skip len>count");
        }
        this.offset += len;
    }

    public synchronized void clear() {
        this.offset = 0;
        this.count = 0;
    }

    public void checkOffsetAndCount(int arrayLength, int offset2, int count2) {
        if ((offset2 | count2) < 0 || offset2 > arrayLength || arrayLength - offset2 < count2) {
            throw new ArrayIndexOutOfBoundsException(this.name + "length=" + arrayLength + "; regionStart=" + offset2 + "; regionLength=" + count2);
        }
    }
}
