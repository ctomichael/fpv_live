package dji.thirdparty.sanselan.common.mylzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class MyLZWDecompressor {
    private static final int MAX_TABLE_SIZE = 4096;
    private final int byteOrder;
    private final int clearCode;
    private int codeSize;
    private int codes;
    private final int eoiCode;
    private final int initialCodeSize;
    private final Listener listener;
    private final byte[][] table;
    private boolean tiffLZWMode;
    private int written;

    public interface Listener {
        void code(int i);

        void init(int i, int i2);
    }

    public MyLZWDecompressor(int initialCodeSize2, int byteOrder2) {
        this(initialCodeSize2, byteOrder2, null);
    }

    public MyLZWDecompressor(int initialCodeSize2, int byteOrder2, Listener listener2) {
        this.codes = -1;
        this.written = 0;
        this.tiffLZWMode = false;
        this.listener = listener2;
        this.byteOrder = byteOrder2;
        this.initialCodeSize = initialCodeSize2;
        this.table = new byte[4096][];
        this.clearCode = 1 << initialCodeSize2;
        this.eoiCode = this.clearCode + 1;
        if (listener2 != null) {
            listener2.init(this.clearCode, this.eoiCode);
        }
        InitializeTable();
    }

    private final void InitializeTable() {
        this.codeSize = this.initialCodeSize;
        int intial_entries_count = 1 << (this.codeSize + 2);
        for (int i = 0; i < intial_entries_count; i++) {
            this.table[i] = new byte[]{(byte) i};
        }
    }

    private final void clearTable() {
        this.codes = (1 << this.initialCodeSize) + 2;
        this.codeSize = this.initialCodeSize;
        incrementCodeSize();
    }

    private final int getNextCode(MyBitInputStream is) throws IOException {
        int code = is.readBits(this.codeSize);
        if (this.listener != null) {
            this.listener.code(code);
        }
        return code;
    }

    private final byte[] stringFromCode(int code) throws IOException {
        if (code < this.codes && code >= 0) {
            return this.table[code];
        }
        throw new IOException("Bad Code: " + code + " codes: " + this.codes + " code_size: " + this.codeSize + ", table: " + this.table.length);
    }

    private final boolean isInTable(int Code) {
        return Code < this.codes;
    }

    private final byte firstChar(byte[] bytes) {
        return bytes[0];
    }

    private final void addStringToTable(byte[] bytes) throws IOException {
        if (this.codes < (1 << this.codeSize)) {
            this.table[this.codes] = bytes;
            this.codes++;
            checkCodeSize();
            return;
        }
        throw new IOException("AddStringToTable: codes: " + this.codes + " code_size: " + this.codeSize);
    }

    private final byte[] appendBytes(byte[] bytes, byte b) {
        byte[] result = new byte[(bytes.length + 1)];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        result[result.length - 1] = b;
        return result;
    }

    private final void writeToResult(OutputStream os, byte[] bytes) throws IOException {
        os.write(bytes);
        this.written += bytes.length;
    }

    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }

    public byte[] decompress(InputStream is, int expectedLength) throws IOException {
        int code;
        int oldCode = -1;
        MyBitInputStream mbis = new MyBitInputStream(is, this.byteOrder);
        if (this.tiffLZWMode) {
            mbis.setTiffLZWMode();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(expectedLength);
        clearTable();
        do {
            int code2 = getNextCode(mbis);
            if (code2 == this.eoiCode) {
                break;
            } else if (code2 == this.clearCode) {
                clearTable();
                if (this.written >= expectedLength || (code = getNextCode(mbis)) == this.eoiCode) {
                    break;
                }
                writeToResult(baos, stringFromCode(code));
                oldCode = code;
            } else if (isInTable(code2)) {
                writeToResult(baos, stringFromCode(code2));
                addStringToTable(appendBytes(stringFromCode(oldCode), firstChar(stringFromCode(code2))));
                oldCode = code2;
            } else {
                byte[] OutString = appendBytes(stringFromCode(oldCode), firstChar(stringFromCode(oldCode)));
                writeToResult(baos, OutString);
                addStringToTable(OutString);
                oldCode = code2;
            }
        } while (this.written < expectedLength);
        return baos.toByteArray();
    }

    private final void checkCodeSize() {
        int limit = 1 << this.codeSize;
        if (this.tiffLZWMode) {
            limit--;
        }
        if (this.codes == limit) {
            incrementCodeSize();
        }
    }

    private final void incrementCodeSize() {
        if (this.codeSize != 12) {
            this.codeSize++;
        }
    }
}
