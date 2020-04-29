package dji.thirdparty.sanselan.common.mylzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyLZWCompressor {
    private final int byteOrder;
    private final int clearCode;
    private int codeSize;
    private int codes;
    private final boolean earlyLimit;
    private final int eoiCode;
    private final int initialCodeSize;
    private final Listener listener;
    private final Map map;

    public interface Listener {
        void clearCode(int i);

        void dataCode(int i);

        void eoiCode(int i);

        void init(int i, int i2);
    }

    public MyLZWCompressor(int initialCodeSize2, int byteOrder2, boolean earlyLimit2) {
        this(initialCodeSize2, byteOrder2, earlyLimit2, null);
    }

    public MyLZWCompressor(int initialCodeSize2, int byteOrder2, boolean earlyLimit2, Listener listener2) {
        this.codes = -1;
        this.map = new HashMap();
        this.listener = listener2;
        this.byteOrder = byteOrder2;
        this.earlyLimit = earlyLimit2;
        this.initialCodeSize = initialCodeSize2;
        this.clearCode = 1 << initialCodeSize2;
        this.eoiCode = this.clearCode + 1;
        if (listener2 != null) {
            listener2.init(this.clearCode, this.eoiCode);
        }
        InitializeStringTable();
    }

    private final void InitializeStringTable() {
        this.codeSize = this.initialCodeSize;
        int intial_entries_count = (1 << this.codeSize) + 2;
        this.map.clear();
        this.codes = 0;
        while (this.codes < intial_entries_count) {
            if (!(this.codes == this.clearCode || this.codes == this.eoiCode)) {
                this.map.put(arrayToKey((byte) this.codes), new Integer(this.codes));
            }
            this.codes++;
        }
    }

    private final void clearTable() {
        InitializeStringTable();
        incrementCodeSize();
    }

    private final void incrementCodeSize() {
        if (this.codeSize != 12) {
            this.codeSize++;
        }
    }

    private final Object arrayToKey(byte b) {
        return arrayToKey(new byte[]{b}, 0, 1);
    }

    private static final class ByteArray {
        private final byte[] bytes;
        private final int hash;
        private final int length;
        private final int start;

        public ByteArray(byte[] bytes2) {
            this(bytes2, 0, bytes2.length);
        }

        public ByteArray(byte[] bytes2, int start2, int length2) {
            this.bytes = bytes2;
            this.start = start2;
            this.length = length2;
            int tempHash = length2;
            for (int i = 0; i < length2; i++) {
                tempHash = (((tempHash << 8) + tempHash) ^ (bytes2[i + start2] & 255)) ^ i;
            }
            this.hash = tempHash;
        }

        public final int hashCode() {
            return this.hash;
        }

        public final boolean equals(Object o) {
            ByteArray other = (ByteArray) o;
            if (other.hash != this.hash || other.length != this.length) {
                return false;
            }
            for (int i = 0; i < this.length; i++) {
                if (other.bytes[other.start + i] != this.bytes[this.start + i]) {
                    return false;
                }
            }
            return true;
        }
    }

    private final Object arrayToKey(byte[] bytes, int start, int length) {
        return new ByteArray(bytes, start, length);
    }

    private final void writeDataCode(MyBitOutputStream bos, int code) throws IOException {
        if (this.listener != null) {
            this.listener.dataCode(code);
        }
        writeCode(bos, code);
    }

    private final void writeClearCode(MyBitOutputStream bos) throws IOException {
        if (this.listener != null) {
            this.listener.dataCode(this.clearCode);
        }
        writeCode(bos, this.clearCode);
    }

    private final void writeEoiCode(MyBitOutputStream bos) throws IOException {
        if (this.listener != null) {
            this.listener.eoiCode(this.eoiCode);
        }
        writeCode(bos, this.eoiCode);
    }

    private final void writeCode(MyBitOutputStream bos, int code) throws IOException {
        bos.writeBits(code, this.codeSize);
    }

    private final boolean isInTable(byte[] bytes, int start, int length) {
        return this.map.containsKey(arrayToKey(bytes, start, length));
    }

    private final int codeFromString(byte[] bytes, int start, int length) throws IOException {
        Object o = this.map.get(arrayToKey(bytes, start, length));
        if (o != null) {
            return ((Integer) o).intValue();
        }
        throw new IOException("CodeFromString");
    }

    private final boolean addTableEntry(MyBitOutputStream bos, byte[] bytes, int start, int length) throws IOException {
        return addTableEntry(bos, arrayToKey(bytes, start, length));
    }

    private final boolean addTableEntry(MyBitOutputStream bos, Object key) throws IOException {
        boolean cleared = false;
        int limit = 1 << this.codeSize;
        if (this.earlyLimit) {
            limit--;
        }
        if (this.codes == limit) {
            if (this.codeSize < 12) {
                incrementCodeSize();
            } else {
                writeClearCode(bos);
                clearTable();
                cleared = true;
            }
        }
        if (!cleared) {
            this.map.put(key, new Integer(this.codes));
            this.codes++;
        }
        return cleared;
    }

    public byte[] compress(byte[] bytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
        MyBitOutputStream bos = new MyBitOutputStream(baos, this.byteOrder);
        InitializeStringTable();
        clearTable();
        writeClearCode(bos);
        int w_start = 0;
        int w_length = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (isInTable(bytes, w_start, w_length + 1)) {
                w_length++;
            } else {
                writeDataCode(bos, codeFromString(bytes, w_start, w_length));
                boolean cleared = addTableEntry(bos, bytes, w_start, w_length + 1);
                w_start = i;
                w_length = 1;
            }
        }
        writeDataCode(bos, codeFromString(bytes, w_start, w_length));
        writeEoiCode(bos);
        bos.flushCache();
        return baos.toByteArray();
    }
}
