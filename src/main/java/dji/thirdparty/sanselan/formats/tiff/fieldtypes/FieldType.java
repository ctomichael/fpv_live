package dji.thirdparty.sanselan.formats.tiff.fieldtypes;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.common.BinaryFileFunctions;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;

public abstract class FieldType extends BinaryFileFunctions implements TiffConstants {
    public final int length;
    public final String name;
    public final int type;

    public abstract Object getSimpleValue(TiffField tiffField) throws ImageReadException;

    public abstract byte[] writeData(Object obj, int i) throws ImageWriteException;

    public FieldType(int type2, int length2, String name2) {
        this.type = type2;
        this.length = length2;
        this.name = name2;
    }

    public boolean isLocalValue(TiffField entry) {
        return this.length > 0 && this.length * entry.length <= 4;
    }

    public int getBytesLength(TiffField entry) throws ImageReadException {
        if (this.length >= 1) {
            return this.length * entry.length;
        }
        throw new ImageReadException("Unknown field type");
    }

    public static final byte[] getStubLocalValue() {
        return new byte[4];
    }

    public final byte[] getStubValue(int count) {
        return new byte[(this.length * count)];
    }

    public String getDisplayValue(TiffField entry) throws ImageReadException {
        Object o = getSimpleValue(entry);
        if (o == null) {
            return "NULL";
        }
        return o.toString();
    }

    public final byte[] getRawBytes(TiffField entry) {
        if (!isLocalValue(entry)) {
            return entry.oversizeValue;
        }
        int rawLength = this.length * entry.length;
        byte[] result = new byte[rawLength];
        System.arraycopy(entry.valueOffsetBytes, 0, result, 0, rawLength);
        return result;
    }

    public String toString() {
        return IMemberProtocol.STRING_SEPERATOR_LEFT + getClass().getName() + ". type: " + this.type + ", name: " + this.name + ", length: " + this.length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }
}
