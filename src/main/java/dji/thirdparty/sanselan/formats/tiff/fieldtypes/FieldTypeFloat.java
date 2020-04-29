package dji.thirdparty.sanselan.formats.tiff.fieldtypes;

import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.util.Debug;

public class FieldTypeFloat extends FieldType {
    public FieldTypeFloat() {
        super(11, 4, "Float");
    }

    public Object getSimpleValue(TiffField entry) {
        if (entry.length == 1) {
            return new Float(convertByteArrayToFloat(this.name + " (" + entry.tagInfo.name + ")", entry.valueOffsetBytes, entry.byteOrder));
        }
        return convertByteArrayToFloatArray(this.name + " (" + entry.tagInfo.name + ")", getRawBytes(entry), 0, entry.length, entry.byteOrder);
    }

    public byte[] writeData(Object o, int byteOrder) throws ImageWriteException {
        if (o instanceof Float) {
            return convertFloatToByteArray(((Float) o).floatValue(), byteOrder);
        }
        if (o instanceof float[]) {
            return convertFloatArrayToByteArray((float[]) o, byteOrder);
        }
        if (o instanceof Float[]) {
            Float[] numbers = (Float[]) o;
            float[] values = new float[numbers.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = numbers[i].floatValue();
            }
            return convertFloatArrayToByteArray(values, byteOrder);
        }
        throw new ImageWriteException("Invalid data: " + o + " (" + Debug.getType(o) + ")");
    }
}
