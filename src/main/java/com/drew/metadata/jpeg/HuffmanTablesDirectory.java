package com.drew.metadata.jpeg;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.MetadataException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

public class HuffmanTablesDirectory extends Directory {
    public static final int TAG_NUMBER_OF_TABLES = 1;
    protected static final byte[] TYPICAL_CHROMINANCE_AC_LENGTHS = {0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119};
    protected static final byte[] TYPICAL_CHROMINANCE_AC_VALUES = {0, 1, 2, 3, 17, 4, 5, 33, TarHeader.LF_LINK, 6, 18, 65, 81, 7, 97, 113, 19, 34, TarHeader.LF_SYMLINK, -127, 8, 20, 66, -111, -95, -79, MessagePack.Code.NEVER_USED, 9, 35, TarHeader.LF_CHR, 82, -16, 21, 98, 114, MessagePack.Code.INT16, 10, 22, 36, TarHeader.LF_BLK, -31, 37, -15, 23, 24, 25, 26, 38, 39, 40, 41, 42, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, MessagePack.Code.FALSE, MessagePack.Code.TRUE, MessagePack.Code.BIN8, MessagePack.Code.BIN16, MessagePack.Code.BIN32, MessagePack.Code.EXT8, MessagePack.Code.EXT16, MessagePack.Code.EXT32, MessagePack.Code.FLOAT32, MessagePack.Code.INT32, MessagePack.Code.INT64, MessagePack.Code.FIXEXT1, MessagePack.Code.FIXEXT2, MessagePack.Code.FIXEXT4, MessagePack.Code.FIXEXT8, MessagePack.Code.FIXEXT16, MessagePack.Code.STR8, MessagePack.Code.STR16, -30, -29, -28, -27, -26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7, -6};
    protected static final byte[] TYPICAL_CHROMINANCE_DC_LENGTHS = {0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0};
    protected static final byte[] TYPICAL_CHROMINANCE_DC_VALUES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    protected static final byte[] TYPICAL_LUMINANCE_AC_LENGTHS = {0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125};
    protected static final byte[] TYPICAL_LUMINANCE_AC_VALUES = {1, 2, 3, 0, 4, 17, 5, 18, 33, TarHeader.LF_LINK, 65, 6, 19, 81, 97, 7, 34, 113, 20, TarHeader.LF_SYMLINK, -127, -111, -95, 8, 35, 66, -79, MessagePack.Code.NEVER_USED, 21, 82, MessagePack.Code.INT16, -16, 36, TarHeader.LF_CHR, 98, 114, -126, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, MessagePack.Code.FALSE, MessagePack.Code.TRUE, MessagePack.Code.BIN8, MessagePack.Code.BIN16, MessagePack.Code.BIN32, MessagePack.Code.EXT8, MessagePack.Code.EXT16, MessagePack.Code.EXT32, MessagePack.Code.FLOAT32, MessagePack.Code.INT32, MessagePack.Code.INT64, MessagePack.Code.FIXEXT1, MessagePack.Code.FIXEXT2, MessagePack.Code.FIXEXT4, MessagePack.Code.FIXEXT8, MessagePack.Code.FIXEXT16, MessagePack.Code.STR8, MessagePack.Code.STR16, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6};
    protected static final byte[] TYPICAL_LUMINANCE_DC_LENGTHS = {0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0};
    protected static final byte[] TYPICAL_LUMINANCE_DC_VALUES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();
    @NotNull
    protected final List<HuffmanTable> tables = new ArrayList(4);

    static {
        _tagNameMap.put(1, "Number of Tables");
    }

    public HuffmanTablesDirectory() {
        setDescriptor(new HuffmanTablesDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Huffman";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }

    @NotNull
    public HuffmanTable getTable(int tableNumber) {
        return this.tables.get(tableNumber);
    }

    public int getNumberOfTables() throws MetadataException {
        return getInt(1);
    }

    /* access modifiers changed from: protected */
    @NotNull
    public List<HuffmanTable> getTables() {
        return this.tables;
    }

    public boolean isTypical() {
        if (this.tables.size() == 0) {
            return false;
        }
        for (HuffmanTable table : this.tables) {
            if (!table.isTypical()) {
                return false;
            }
        }
        return true;
    }

    public boolean isOptimized() {
        return !isTypical();
    }

    public static class HuffmanTable {
        private final byte[] lengthBytes;
        private final HuffmanTableClass tableClass;
        private final int tableDestinationId;
        private final int tableLength;
        private final byte[] valueBytes;

        public HuffmanTable(@NotNull HuffmanTableClass tableClass2, int tableDestinationId2, @NotNull byte[] lBytes, @NotNull byte[] vBytes) {
            this.tableClass = tableClass2;
            this.tableDestinationId = tableDestinationId2;
            this.lengthBytes = lBytes;
            this.valueBytes = vBytes;
            this.tableLength = vBytes.length + 17;
        }

        public int getTableLength() {
            return this.tableLength;
        }

        public HuffmanTableClass getTableClass() {
            return this.tableClass;
        }

        public int getTableDestinationId() {
            return this.tableDestinationId;
        }

        public byte[] getLengthBytes() {
            if (this.lengthBytes == null) {
                return null;
            }
            byte[] result = new byte[this.lengthBytes.length];
            System.arraycopy(this.lengthBytes, 0, result, 0, this.lengthBytes.length);
            return result;
        }

        public byte[] getValueBytes() {
            if (this.valueBytes == null) {
                return null;
            }
            byte[] result = new byte[this.valueBytes.length];
            System.arraycopy(this.valueBytes, 0, result, 0, this.valueBytes.length);
            return result;
        }

        public boolean isTypical() {
            if (this.tableClass == HuffmanTableClass.DC) {
                if ((!Arrays.equals(this.lengthBytes, HuffmanTablesDirectory.TYPICAL_LUMINANCE_DC_LENGTHS) || !Arrays.equals(this.valueBytes, HuffmanTablesDirectory.TYPICAL_LUMINANCE_DC_VALUES)) && (!Arrays.equals(this.lengthBytes, HuffmanTablesDirectory.TYPICAL_CHROMINANCE_DC_LENGTHS) || !Arrays.equals(this.valueBytes, HuffmanTablesDirectory.TYPICAL_CHROMINANCE_DC_VALUES))) {
                    return false;
                }
                return true;
            } else if (this.tableClass != HuffmanTableClass.AC) {
                return false;
            } else {
                if ((!Arrays.equals(this.lengthBytes, HuffmanTablesDirectory.TYPICAL_LUMINANCE_AC_LENGTHS) || !Arrays.equals(this.valueBytes, HuffmanTablesDirectory.TYPICAL_LUMINANCE_AC_VALUES)) && (!Arrays.equals(this.lengthBytes, HuffmanTablesDirectory.TYPICAL_CHROMINANCE_AC_LENGTHS) || !Arrays.equals(this.valueBytes, HuffmanTablesDirectory.TYPICAL_CHROMINANCE_AC_VALUES))) {
                    return false;
                }
                return true;
            }
        }

        public boolean isOptimized() {
            return !isTypical();
        }

        public enum HuffmanTableClass {
            DC,
            AC,
            UNKNOWN;

            public static HuffmanTableClass typeOf(int value) {
                switch (value) {
                    case 0:
                        return DC;
                    case 1:
                        return AC;
                    default:
                        return UNKNOWN;
                }
            }
        }
    }
}
