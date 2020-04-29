package dji.thirdparty.sanselan.common;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.sanselan.ImageReadException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class BinaryInputStream extends InputStream implements BinaryConstants {
    private int byteOrder = 77;
    protected boolean debug = false;
    private final InputStream is;

    public final void setDebug(boolean b) {
        this.debug = b;
    }

    public final boolean getDebug() {
        return this.debug;
    }

    public BinaryInputStream(byte[] bytes, int byteOrder2) {
        this.byteOrder = byteOrder2;
        this.is = new ByteArrayInputStream(bytes);
    }

    public BinaryInputStream(InputStream is2, int byteOrder2) {
        this.byteOrder = byteOrder2;
        this.is = is2;
    }

    public BinaryInputStream(InputStream is2) {
        this.is = is2;
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

    public int read() throws IOException {
        return this.is.read();
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String name, byte[] bytes) {
        return convertByteArrayToInt(name, bytes, this.byteOrder);
    }

    public final int convertByteArrayToShort(String name, byte[] bytes) {
        return convertByteArrayToShort(name, bytes, this.byteOrder);
    }

    public final int convertByteArrayToShort(String name, int start, byte[] bytes) {
        return convertByteArrayToShort(name, start, bytes, this.byteOrder);
    }

    public final int read4Bytes(String name, String exception) throws ImageReadException, IOException {
        return read4Bytes(name, exception, this.byteOrder);
    }

    public final int read3Bytes(String name, String exception) throws ImageReadException, IOException {
        return read3Bytes(name, exception, this.byteOrder);
    }

    public final int read2Bytes(String name, String exception) throws ImageReadException, IOException {
        return read2Bytes(name, exception, this.byteOrder);
    }

    /* access modifiers changed from: protected */
    public final void readRandomBytes() throws ImageReadException, IOException {
        for (int counter = 0; counter < 100; counter++) {
            readByte("" + counter, "Random Data");
        }
    }

    public final void debugNumber(String msg, int data) {
        debugNumber(msg, data, 1);
    }

    public final void debugNumber(String msg, int data, int bytes) {
        System.out.print(msg + ": " + data + " (");
        int byteData = data;
        for (int i = 0; i < bytes; i++) {
            if (i > 0) {
                System.out.print(",");
            }
            int singleByte = byteData & 255;
            System.out.print(((char) singleByte) + " [" + singleByte + IMemberProtocol.STRING_SEPERATOR_RIGHT);
            byteData >>= 8;
        }
        System.out.println(") [0x" + Integer.toHexString(data) + ", " + Integer.toBinaryString(data) + IMemberProtocol.STRING_SEPERATOR_RIGHT);
    }

    public final void readAndVerifyBytes(byte[] expected, String exception) throws ImageReadException, IOException {
        for (int i = 0; i < expected.length; i++) {
            int data = this.is.read();
            byte b = (byte) (data & 255);
            if (data < 0 || b != expected[i]) {
                System.out.println("i: " + i);
                debugByteArray("expected", expected);
                debugNumber("data[" + i + IMemberProtocol.STRING_SEPERATOR_RIGHT, b);
                throw new ImageReadException(exception);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void readAndVerifyBytes(String name, byte[] expected, String exception) throws ImageReadException, IOException {
        byte[] bytes = readByteArray(name, expected.length, exception);
        for (int i = 0; i < expected.length; i++) {
            if (bytes[i] != expected[i]) {
                System.out.println("i: " + i);
                debugNumber("bytes[" + i + IMemberProtocol.STRING_SEPERATOR_RIGHT, bytes[i]);
                debugNumber("expected[" + i + IMemberProtocol.STRING_SEPERATOR_RIGHT, expected[i]);
                throw new ImageReadException(exception);
            }
        }
    }

    public final void skipBytes(int length, String exception) throws IOException {
        long total = 0;
        while (((long) length) != total) {
            long skipped = this.is.skip(((long) length) - total);
            if (skipped < 1) {
                throw new IOException(exception + " (" + skipped + ")");
            }
            total += skipped;
        }
    }

    /* access modifiers changed from: protected */
    public final void scanForByte(byte value) throws IOException {
        int b;
        int count = 0;
        int i = 0;
        while (count < 3 && (b = this.is.read()) >= 0) {
            if ((b & 255) == value) {
                System.out.println("\t" + i + ": match.");
                count++;
            }
            i++;
        }
    }

    public final byte readByte(String name, String exception) throws IOException {
        int result = this.is.read();
        if (result < 0) {
            System.out.println(name + ": " + result);
            throw new IOException(exception);
        }
        if (this.debug) {
            debugNumber(name, result);
        }
        return (byte) (result & 255);
    }

    /* access modifiers changed from: protected */
    public final RationalNumber[] convertByteArrayToRationalArray(String name, byte[] bytes, int start, int length, int byteOrder2) {
        int expectedLength = start + (length * 8);
        if (bytes.length < expectedLength) {
            System.out.println(name + ": expected length: " + expectedLength + ", actual length: " + bytes.length);
            return null;
        }
        RationalNumber[] result = new RationalNumber[length];
        for (int i = 0; i < length; i++) {
            result[i] = convertByteArrayToRational(name, bytes, (i * 8) + start, byteOrder2);
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public final RationalNumber convertByteArrayToRational(String name, byte[] bytes, int byteOrder2) {
        return convertByteArrayToRational(name, bytes, 0, byteOrder2);
    }

    /* access modifiers changed from: protected */
    public final RationalNumber convertByteArrayToRational(String name, byte[] bytes, int start, int byteOrder2) {
        return new RationalNumber(convertByteArrayToInt(name, bytes, start + 0, 4, byteOrder2), convertByteArrayToInt(name, bytes, start + 4, 4, byteOrder2));
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String name, byte[] bytes, int byteOrder2) {
        return convertByteArrayToInt(name, bytes, 0, 4, byteOrder2);
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String name, byte[] bytes, int start, int length, int byteOrder2) {
        int result;
        byte byte0 = bytes[start + 0];
        byte byte1 = bytes[start + 1];
        byte byte2 = bytes[start + 2];
        byte byte3 = 0;
        if (length == 4) {
            byte3 = bytes[start + 3];
        }
        if (byteOrder2 == 77) {
            result = ((byte0 & 255) << 24) + ((byte1 & 255) << Tnaf.POW_2_WIDTH) + ((byte2 & 255) << 8) + ((byte3 & 255) << 0);
        } else {
            result = ((byte3 & 255) << 24) + ((byte2 & 255) << Tnaf.POW_2_WIDTH) + ((byte1 & 255) << 8) + ((byte0 & 255) << 0);
        }
        if (this.debug) {
            debugNumber(name, result, 4);
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public final int[] convertByteArrayToIntArray(String name, byte[] bytes, int start, int length, int byteOrder2) {
        int expectedLength = start + (length * 4);
        if (bytes.length < expectedLength) {
            System.out.println(name + ": expected length: " + expectedLength + ", actual length: " + bytes.length);
            return null;
        }
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = convertByteArrayToInt(name, bytes, start + (i * 4), 4, byteOrder2);
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToShort(String name, byte[] bytes, int byteOrder2) {
        return convertByteArrayToShort(name, 0, bytes, byteOrder2);
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToShort(String name, int start, byte[] bytes, int byteOrder2) {
        int result;
        byte byte0 = bytes[start + 0];
        byte byte1 = bytes[start + 1];
        if (byteOrder2 == 77) {
            result = ((byte0 & 255) << 8) + ((byte1 & 255) << 0);
        } else {
            result = ((byte1 & 255) << 8) + ((byte0 & 255) << 0);
        }
        if (this.debug) {
            debugNumber(name, result, 2);
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public final int[] convertByteArrayToShortArray(String name, byte[] bytes, int start, int length, int byteOrder2) {
        int expectedLength = start + (length * 2);
        if (bytes.length < expectedLength) {
            System.out.println(name + ": expected length: " + expectedLength + ", actual length: " + bytes.length);
            return null;
        }
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = convertByteArrayToShort(name, (i * 2) + start, bytes, byteOrder2);
        }
        return result;
    }

    public final byte[] readByteArray(String name, int length, String exception) throws ImageReadException, IOException {
        byte[] result = new byte[length];
        int read = 0;
        while (read < length) {
            int count = this.is.read(result, read, length - read);
            if (count < 1) {
                throw new IOException(exception);
            }
            read += count;
        }
        if (this.debug) {
            int i = 0;
            while (i < length && i < 150) {
                debugNumber(name + " (" + i + ")", result[i] & 255);
                i++;
            }
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public final void debugByteArray(String name, byte[] bytes) {
        System.out.println(name + ": " + bytes.length);
        int i = 0;
        while (i < bytes.length && i < 50) {
            debugNumber(name + " (" + i + ")", bytes[i]);
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public final void debugNumberArray(String name, int[] numbers, int length) {
        System.out.println(name + ": " + numbers.length);
        int i = 0;
        while (i < numbers.length && i < 50) {
            debugNumber(name + " (" + i + ")", numbers[i], length);
            i++;
        }
    }

    public final byte[] readBytearray(String name, byte[] bytes, int start, int count) {
        if (bytes.length < start + count) {
            return null;
        }
        byte[] result = new byte[count];
        System.arraycopy(bytes, start, result, 0, count);
        if (!this.debug) {
            return result;
        }
        debugByteArray(name, result);
        return result;
    }

    public final byte[] readByteArray(int length, String error) throws ImageReadException, IOException {
        return readByteArray(length, error, false, true);
    }

    public final byte[] readByteArray(int length, String error, boolean verbose, boolean strict) throws ImageReadException, IOException {
        byte[] bytes = new byte[length];
        int total = 0;
        while (true) {
            int read = read(bytes, total, length - total);
            if (read <= 0) {
                break;
            }
            total += read;
        }
        if (total >= length) {
            return bytes;
        }
        if (strict) {
            throw new ImageReadException(error);
        }
        if (verbose) {
            System.out.println(error);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public final byte[] getBytearrayTail(String name, byte[] bytes, int count) {
        return readBytearray(name, bytes, count, bytes.length - count);
    }

    /* access modifiers changed from: protected */
    public final byte[] getBytearrayHead(String name, byte[] bytes, int count) {
        return readBytearray(name, bytes, 0, bytes.length - count);
    }

    public final boolean compareByteArrays(byte[] a, int aStart, byte[] b, int bStart, int length) {
        if (a.length < aStart + length || b.length < bStart + length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[aStart + i] != b[bStart + i]) {
                debugNumber("a[" + (aStart + i) + IMemberProtocol.STRING_SEPERATOR_RIGHT, a[aStart + i]);
                debugNumber("b[" + (bStart + i) + IMemberProtocol.STRING_SEPERATOR_RIGHT, b[bStart + i]);
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final int read4Bytes(String name, String exception, int byteOrder2) throws ImageReadException, IOException {
        byte[] bytes = new byte[4];
        int read = 0;
        while (read < 4) {
            int count = this.is.read(bytes, read, 4 - read);
            if (count < 1) {
                throw new IOException(exception);
            }
            read += count;
        }
        return convertByteArrayToInt(name, bytes, byteOrder2);
    }

    /* access modifiers changed from: protected */
    public final int read3Bytes(String name, String exception, int byteOrder2) throws ImageReadException, IOException {
        byte[] bytes = new byte[3];
        int read = 0;
        while (read < 3) {
            int count = this.is.read(bytes, read, 3 - read);
            if (count < 1) {
                throw new IOException(exception);
            }
            read += count;
        }
        return convertByteArrayToInt(name, bytes, 0, 3, byteOrder2);
    }

    /* access modifiers changed from: protected */
    public final int read2Bytes(String name, String exception, int byteOrder2) throws ImageReadException, IOException {
        byte[] bytes = new byte[2];
        int read = 0;
        while (read < 2) {
            int count = this.is.read(bytes, read, 2 - read);
            if (count < 1) {
                throw new IOException(exception);
            }
            read += count;
        }
        return convertByteArrayToShort(name, bytes, byteOrder2);
    }

    public final int read1ByteInteger(String exception) throws ImageReadException, IOException {
        int byte0 = this.is.read();
        if (byte0 >= 0) {
            return byte0 & 255;
        }
        throw new ImageReadException(exception);
    }

    public final int read2ByteInteger(String exception) throws ImageReadException, IOException {
        int byte0 = this.is.read();
        int byte1 = this.is.read();
        if (byte0 < 0 || byte1 < 0) {
            throw new ImageReadException(exception);
        } else if (this.byteOrder == 77) {
            return ((byte0 & 255) << 8) + ((byte1 & 255) << 0);
        } else {
            return ((byte1 & 255) << 8) + ((byte0 & 255) << 0);
        }
    }

    public final int read4ByteInteger(String exception) throws ImageReadException, IOException {
        int byte0 = this.is.read();
        int byte1 = this.is.read();
        int byte2 = this.is.read();
        int byte3 = this.is.read();
        if (byte0 < 0 || byte1 < 0 || byte2 < 0 || byte3 < 0) {
            throw new ImageReadException(exception);
        } else if (this.byteOrder == 77) {
            return ((byte0 & 255) << 24) + ((byte1 & 255) << 16) + ((byte2 & 255) << 8) + ((byte3 & 255) << 0);
        } else {
            return ((byte3 & 255) << 24) + ((byte2 & 255) << 16) + ((byte1 & 255) << 8) + ((byte0 & 255) << 0);
        }
    }

    /* access modifiers changed from: protected */
    public final void printCharQuad(String msg, int i) {
        System.out.println(msg + ": '" + ((char) ((i >> 24) & 255)) + ((char) ((i >> 16) & 255)) + ((char) ((i >> 8) & 255)) + ((char) ((i >> 0) & 255)) + "'");
    }

    /* access modifiers changed from: protected */
    public final void printByteBits(String msg, byte i) {
        System.out.println(msg + ": '" + Integer.toBinaryString(i & 255));
    }

    protected static final int CharsToQuad(char c1, char c2, char c3, char c4) {
        return ((c1 & 255) << 24) | ((c2 & 255) << 16) | ((c3 & 255) << 8) | ((c4 & 255) << 0);
    }

    public final int findNull(byte[] src) {
        return findNull(src, 0);
    }

    public final int findNull(byte[] src, int start) {
        for (int i = start; i < src.length; i++) {
            if (src[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public final byte[] getRAFBytes(RandomAccessFile raf, long pos, int length, String exception) throws IOException {
        byte[] result = new byte[length];
        if (this.debug) {
            System.out.println("getRAFBytes pos: " + pos);
            System.out.println("getRAFBytes length: " + length);
        }
        raf.seek(pos);
        int read = 0;
        while (read < length) {
            int count = raf.read(result, read, length - read);
            if (count < 1) {
                throw new IOException(exception);
            }
            read += count;
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public void skipBytes(int length) throws IOException {
        skipBytes(length, "Couldn't skip bytes");
    }
}
