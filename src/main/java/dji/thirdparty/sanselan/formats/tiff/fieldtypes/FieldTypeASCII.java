package dji.thirdparty.sanselan.formats.tiff.fieldtypes;

import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.formats.tiff.TiffField;

public class FieldTypeASCII extends FieldType {
    public FieldTypeASCII(int type, String name) {
        super(type, 1, name);
    }

    public Object getSimpleValue(TiffField entry) {
        byte[] rawBytes = getRawBytes(entry);
        if (rawBytes.length > 0) {
            return new String(rawBytes, 0, rawBytes.length - 1);
        }
        return "";
    }

    public byte[] writeData(Object o, int byteOrder) throws ImageWriteException {
        byte[] originalArray;
        if (o instanceof byte[]) {
            originalArray = (byte[]) o;
        } else if (o instanceof String) {
            originalArray = ((String) o).getBytes();
        } else {
            throw new ImageWriteException("Unknown data type: " + o);
        }
        byte[] retVal = new byte[(originalArray.length + 1)];
        System.arraycopy(originalArray, 0, retVal, 0, originalArray.length);
        return retVal;
    }
}
