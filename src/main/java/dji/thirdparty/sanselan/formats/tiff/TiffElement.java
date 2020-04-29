package dji.thirdparty.sanselan.formats.tiff;

import java.util.Comparator;

public abstract class TiffElement {
    public static final Comparator COMPARATOR = new Comparator() {
        /* class dji.thirdparty.sanselan.formats.tiff.TiffElement.AnonymousClass1 */

        public int compare(Object o1, Object o2) {
            return ((TiffElement) o1).offset - ((TiffElement) o2).offset;
        }
    };
    public final int length;
    public final int offset;

    public abstract String getElementDescription(boolean z);

    public TiffElement(int offset2, int length2) {
        this.offset = offset2;
        this.length = length2;
    }

    public String getElementDescription() {
        return getElementDescription(false);
    }

    public static abstract class DataElement extends TiffElement {
        public final byte[] data;

        public DataElement(int offset, int length, byte[] data2) {
            super(offset, length);
            this.data = data2;
        }
    }

    public static final class Stub extends TiffElement {
        public Stub(int offset, int length) {
            super(offset, length);
        }

        public String getElementDescription(boolean verbose) {
            return "Element, offset: " + this.offset + ", length: " + this.length + ", last: " + (this.offset + this.length) + "";
        }
    }
}
