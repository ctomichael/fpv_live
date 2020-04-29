package dji.thirdparty.sanselan.common.byteSources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteSourceArray extends ByteSource {
    private final byte[] bytes;

    public ByteSourceArray(String filename, byte[] bytes2) {
        super(filename);
        this.bytes = bytes2;
    }

    public ByteSourceArray(byte[] bytes2) {
        super(null);
        this.bytes = bytes2;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    public byte[] getBlock(int start, int length) throws IOException {
        if (start + length > this.bytes.length) {
            throw new IOException("Could not read block (block start: " + start + ", block length: " + length + ", data length: " + this.bytes.length + ").");
        }
        byte[] result = new byte[length];
        System.arraycopy(this.bytes, start, result, 0, length);
        return result;
    }

    public long getLength() {
        return (long) this.bytes.length;
    }

    public byte[] getAll() throws IOException {
        return this.bytes;
    }

    public String getDescription() {
        return this.bytes.length + " byte array";
    }
}
