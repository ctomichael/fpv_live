package dji.thirdparty.sanselan.formats.tiff.write;

import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.common.BinaryOutputStream;
import dji.thirdparty.sanselan.formats.tiff.constants.AllTagConstants;
import java.io.IOException;

abstract class TiffOutputItem implements AllTagConstants {
    public static final int UNDEFINED_VALUE = -1;
    private int offset = -1;

    public abstract String getItemDescription();

    public abstract int getItemLength();

    public abstract void writeItem(BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException;

    TiffOutputItem() {
    }

    /* access modifiers changed from: protected */
    public int getOffset() {
        return this.offset;
    }

    /* access modifiers changed from: protected */
    public void setOffset(int offset2) {
        this.offset = offset2;
    }

    public static class Value extends TiffOutputItem {
        private final byte[] bytes;
        private final String name;

        public Value(String name2, byte[] bytes2) {
            this.name = name2;
            this.bytes = bytes2;
        }

        public int getItemLength() {
            return this.bytes.length;
        }

        public String getItemDescription() {
            return this.name;
        }

        public void updateValue(byte[] bytes2) throws ImageWriteException {
            if (this.bytes.length != bytes2.length) {
                throw new ImageWriteException("Updated data size mismatch: " + this.bytes.length + " vs. " + bytes2.length);
            }
            System.arraycopy(bytes2, 0, this.bytes, 0, bytes2.length);
        }

        public void writeItem(BinaryOutputStream bos) throws IOException, ImageWriteException {
            bos.write(this.bytes);
        }
    }
}
