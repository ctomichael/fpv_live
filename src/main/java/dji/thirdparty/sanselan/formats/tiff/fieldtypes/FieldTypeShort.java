package dji.thirdparty.sanselan.formats.tiff.fieldtypes;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.util.Debug;

public class FieldTypeShort extends FieldType {
    public FieldTypeShort(int type, String name) {
        super(type, 2, name);
    }

    public Object getSimpleValue(TiffField entry) throws ImageReadException {
        if (entry.length == 1) {
            return new Integer(convertByteArrayToShort(this.name + " (" + entry.tagInfo.name + ")", entry.valueOffsetBytes, entry.byteOrder));
        }
        return convertByteArrayToShortArray(this.name + " (" + entry.tagInfo.name + ")", getRawBytes(entry), 0, entry.length, entry.byteOrder);
    }

    public byte[] writeData(Object o, int byteOrder) throws ImageWriteException {
        if (o instanceof Integer) {
            return convertShortArrayToByteArray(new int[]{((Integer) o).intValue()}, byteOrder);
        } else if (o instanceof int[]) {
            return convertShortArrayToByteArray((int[]) o, byteOrder);
        } else {
            if (o instanceof Integer[]) {
                Integer[] numbers = (Integer[]) o;
                int[] values = new int[numbers.length];
                for (int i = 0; i < values.length; i++) {
                    values[i] = numbers[i].intValue();
                }
                return convertShortArrayToByteArray(values, byteOrder);
            }
            throw new ImageWriteException("Invalid data: " + o + " (" + Debug.getType(o) + ")");
        }
    }
}
