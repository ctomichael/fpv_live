package dji.thirdparty.sanselan.formats.tiff.fieldtypes;

import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.util.Debug;

public class FieldTypeDouble extends FieldType {
    public FieldTypeDouble() {
        super(12, 8, "Double");
    }

    public Object getSimpleValue(TiffField entry) {
        return "?";
    }

    public byte[] writeData(Object o, int byteOrder) throws ImageWriteException {
        if (o instanceof Double) {
            return convertDoubleToByteArray(((Double) o).doubleValue(), byteOrder);
        }
        if (o instanceof double[]) {
            return convertDoubleArrayToByteArray((double[]) o, byteOrder);
        }
        if (o instanceof Double[]) {
            Double[] numbers = (Double[]) o;
            double[] values = new double[numbers.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = numbers[i].doubleValue();
            }
            return convertDoubleArrayToByteArray(values, byteOrder);
        }
        throw new ImageWriteException("Invalid data: " + o + " (" + Debug.getType(o) + ")");
    }
}
