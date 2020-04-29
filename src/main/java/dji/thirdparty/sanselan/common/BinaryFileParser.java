package dji.thirdparty.sanselan.common;

import dji.thirdparty.sanselan.ImageReadException;
import java.io.IOException;
import java.io.InputStream;

public class BinaryFileParser extends BinaryFileFunctions {
    private int byteOrder = 77;

    public BinaryFileParser(int byteOrder2) {
        this.byteOrder = byteOrder2;
    }

    public BinaryFileParser() {
    }

    /* access modifiers changed from: protected */
    public void setByteOrder(int a, int b) throws ImageReadException, IOException {
        if (a != b) {
            throw new ImageReadException("Byte Order bytes don't match (" + a + ", " + b + ").");
        } else if (a == 77) {
            this.byteOrder = a;
        } else if (a == 73) {
            this.byteOrder = a;
        } else {
            throw new ImageReadException("Unknown Byte Order hint: " + a);
        }
    }

    /* access modifiers changed from: protected */
    public void setByteOrder(int byteOrder2) {
        this.byteOrder = byteOrder2;
    }

    /* access modifiers changed from: protected */
    public int getByteOrder() {
        return this.byteOrder;
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String name, int start, byte[] bytes) {
        return convertByteArrayToInt(name, bytes, start, this.byteOrder);
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String name, byte[] bytes) {
        return convertByteArrayToInt(name, bytes, this.byteOrder);
    }

    public final int convertByteArrayToShort(String name, byte[] bytes) throws ImageReadException {
        return convertByteArrayToShort(name, bytes, this.byteOrder);
    }

    public final int convertByteArrayToShort(String name, int start, byte[] bytes) throws ImageReadException {
        return convertByteArrayToShort(name, start, bytes, this.byteOrder);
    }

    public final int read4Bytes(String name, InputStream is, String exception) throws ImageReadException, IOException {
        return read4Bytes(name, is, exception, this.byteOrder);
    }

    public final int read3Bytes(String name, InputStream is, String exception) throws ImageReadException, IOException {
        return read3Bytes(name, is, exception, this.byteOrder);
    }

    public final int read2Bytes(String name, InputStream is, String exception) throws ImageReadException, IOException {
        return read2Bytes(name, is, exception, this.byteOrder);
    }

    public static boolean byteArrayHasPrefix(byte[] bytes, byte[] prefix) {
        if (bytes == null || bytes.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (bytes[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final byte[] int2ToByteArray(int value) {
        return int2ToByteArray(value, this.byteOrder);
    }
}
