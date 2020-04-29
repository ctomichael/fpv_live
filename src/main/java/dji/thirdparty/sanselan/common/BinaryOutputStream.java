package dji.thirdparty.sanselan.common;

import dji.thirdparty.sanselan.ImageWriteException;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryOutputStream extends OutputStream implements BinaryConstants {
    private int byteOrder = 77;
    private int count = 0;
    protected boolean debug = false;
    private final OutputStream os;

    public final void setDebug(boolean b) {
        this.debug = b;
    }

    public final boolean getDebug() {
        return this.debug;
    }

    public BinaryOutputStream(OutputStream os2, int byteOrder2) {
        this.byteOrder = byteOrder2;
        this.os = os2;
    }

    public BinaryOutputStream(OutputStream os2) {
        this.os = os2;
    }

    /* access modifiers changed from: protected */
    public void setByteOrder(int a, int b) throws ImageWriteException, IOException {
        if (a != b) {
            throw new ImageWriteException("Byte Order bytes don't match (" + a + ", " + b + ").");
        } else if (a == 77) {
            this.byteOrder = a;
        } else if (a == 73) {
            this.byteOrder = a;
        } else {
            throw new ImageWriteException("Unknown Byte Order hint: " + a);
        }
    }

    /* access modifiers changed from: protected */
    public void setByteOrder(int byteOrder2) {
        this.byteOrder = byteOrder2;
    }

    public int getByteOrder() {
        return this.byteOrder;
    }

    public void write(int i) throws IOException {
        this.os.write(i);
        this.count++;
    }

    public int getByteCount() {
        return this.count;
    }

    public final void write4Bytes(int value) throws ImageWriteException, IOException {
        writeNBytes(value, 4);
    }

    public final void write3Bytes(int value) throws ImageWriteException, IOException {
        writeNBytes(value, 3);
    }

    public final void write2Bytes(int value) throws ImageWriteException, IOException {
        writeNBytes(value, 2);
    }

    public final void write4ByteInteger(int value) throws ImageWriteException, IOException {
        if (this.byteOrder == 77) {
            write((value >> 24) & 255);
            write((value >> 16) & 255);
            write((value >> 8) & 255);
            write(value & 255);
            return;
        }
        write(value & 255);
        write((value >> 8) & 255);
        write((value >> 16) & 255);
        write((value >> 24) & 255);
    }

    public final void write2ByteInteger(int value) throws ImageWriteException, IOException {
        if (this.byteOrder == 77) {
            write((value >> 8) & 255);
            write(value & 255);
            return;
        }
        write(value & 255);
        write((value >> 8) & 255);
    }

    public final void writeByteArray(byte[] bytes) throws IOException {
        this.os.write(bytes, 0, bytes.length);
        this.count += bytes.length;
    }

    private byte[] convertValueToByteArray(int value, int n) {
        byte[] result = new byte[n];
        if (this.byteOrder == 77) {
            for (int i = 0; i < n; i++) {
                result[i] = (byte) ((value >> (((n - i) - 1) * 8)) & 255);
            }
        } else {
            for (int i2 = 0; i2 < n; i2++) {
                result[i2] = (byte) ((value >> (i2 * 8)) & 255);
            }
        }
        return result;
    }

    private final void writeNBytes(int value, int n) throws ImageWriteException, IOException {
        write(convertValueToByteArray(value, n));
    }
}
