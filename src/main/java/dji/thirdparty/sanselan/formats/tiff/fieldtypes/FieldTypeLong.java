package dji.thirdparty.sanselan.formats.tiff.fieldtypes;

import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.util.Debug;

public class FieldTypeLong extends FieldType {
    public FieldTypeLong(int type, String name) {
        super(type, 4, name);
    }

    public Object getSimpleValue(TiffField entry) {
        if (entry.length == 1) {
            return new Integer(convertByteArrayToInt(this.name + " (" + entry.tagInfo.name + ")", entry.valueOffsetBytes, entry.byteOrder));
        }
        return convertByteArrayToIntArray(this.name + " (" + entry.tagInfo.name + ")", getRawBytes(entry), 0, entry.length, entry.byteOrder);
    }

    public byte[] writeData(Object o, int byteOrder) throws ImageWriteException {
        if (o instanceof Integer) {
            return convertIntArrayToByteArray(new int[]{((Integer) o).intValue()}, byteOrder);
        } else if (o instanceof int[]) {
            return convertIntArrayToByteArray((int[]) o, byteOrder);
        } else {
            if (o instanceof Integer[]) {
                Integer[] numbers = (Integer[]) o;
                int[] values = new int[numbers.length];
                for (int i = 0; i < values.length; i++) {
                    values[i] = numbers[i].intValue();
                }
                return convertIntArrayToByteArray(values, byteOrder);
            }
            throw new ImageWriteException("Invalid data: " + o + " (" + Debug.getType(o) + ")");
        }
    }
}
