package dji.thirdparty.sanselan.common;

import java.io.IOException;
import java.io.OutputStream;

public class MyByteArrayOutputStream extends OutputStream {
    private final byte[] bytes;
    private int count = 0;

    public MyByteArrayOutputStream(int length) {
        this.bytes = new byte[length];
    }

    public void write(int value) throws IOException {
        if (this.count >= this.bytes.length) {
            throw new IOException("Write exceeded expected length (" + this.count + ", " + this.bytes.length + ")");
        }
        this.bytes[this.count] = (byte) value;
        this.count++;
    }

    public byte[] toByteArray() {
        if (this.count >= this.bytes.length) {
            return this.bytes;
        }
        byte[] result = new byte[this.count];
        System.arraycopy(this.bytes, 0, result, 0, this.count);
        return result;
    }

    public int getBytesWritten() {
        return this.count;
    }
}
