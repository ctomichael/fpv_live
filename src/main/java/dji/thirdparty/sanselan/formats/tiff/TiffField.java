package dji.thirdparty.sanselan.formats.tiff;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.common.byteSources.ByteSource;
import dji.thirdparty.sanselan.formats.tiff.constants.TagInfo;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;
import dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TiffField implements TiffConstants {
    private static final Map ALL_TAG_MAP = makeTagMap(ALL_TAGS, true, "All");
    public static final String Attribute_Tag = "Tag";
    private static final Map EXIF_TAG_MAP = makeTagMap(ALL_EXIF_TAGS, true, "EXIF");
    private static final Map GPS_TAG_MAP = makeTagMap(ALL_GPS_TAGS, false, "GPS");
    private static final Map TIFF_TAG_MAP = makeTagMap(ALL_TIFF_TAGS, false, "TIFF");
    public final int byteOrder;
    public final int directoryType;
    public final FieldType fieldType;
    public final int length;
    public byte[] oversizeValue = null;
    private int sortHint = -1;
    public final int tag;
    public final TagInfo tagInfo;
    public final int type;
    public final int valueOffset;
    public final byte[] valueOffsetBytes;

    public TiffField(int tag2, int directoryType2, int type2, int Length, int ValueOffset, byte[] ValueOffsetBytes, int byteOrder2) {
        this.tag = tag2;
        this.directoryType = directoryType2;
        this.type = type2;
        this.length = Length;
        this.valueOffset = ValueOffset;
        this.valueOffsetBytes = ValueOffsetBytes;
        this.byteOrder = byteOrder2;
        this.fieldType = getFieldType(type2);
        this.tagInfo = getTag(directoryType2, tag2);
    }

    public boolean isLocalValue() {
        return this.fieldType.isLocalValue(this);
    }

    public int getBytesLength() throws ImageReadException {
        return this.fieldType.getBytesLength(this);
    }

    public final class OversizeValueElement extends TiffElement {
        public OversizeValueElement(int offset, int length) {
            super(offset, length);
        }

        public String getElementDescription(boolean verbose) {
            if (verbose) {
                return null;
            }
            return "OversizeValueElement, tag: " + TiffField.this.tagInfo.name + ", fieldType: " + TiffField.this.fieldType.name;
        }
    }

    public TiffElement getOversizeValueElement() {
        if (this.fieldType.isLocalValue(this)) {
            return null;
        }
        return new OversizeValueElement(this.valueOffset, this.oversizeValue.length);
    }

    public void setOversizeValue(byte[] bytes) {
        this.oversizeValue = bytes;
    }

    private static FieldType getFieldType(int value) {
        for (int i = 0; i < FIELD_TYPES.length; i++) {
            FieldType fieldType2 = FIELD_TYPES[i];
            if (fieldType2.type == value) {
                return fieldType2;
            }
        }
        return FIELD_TYPE_UNKNOWN;
    }

    private static TagInfo getTag(int directoryType2, int tag2, List possibleMatches) {
        if (possibleMatches.size() < 1) {
            return null;
        }
        for (int i = 0; i < possibleMatches.size(); i++) {
            TagInfo tagInfo2 = (TagInfo) possibleMatches.get(i);
            if (tagInfo2.directoryType != EXIF_DIRECTORY_UNKNOWN) {
                if (directoryType2 == -2 && tagInfo2.directoryType == EXIF_DIRECTORY_EXIF_IFD) {
                    return tagInfo2;
                }
                if (directoryType2 == -4 && tagInfo2.directoryType == EXIF_DIRECTORY_INTEROP_IFD) {
                    return tagInfo2;
                }
                if (directoryType2 == -3 && tagInfo2.directoryType == EXIF_DIRECTORY_GPS) {
                    return tagInfo2;
                }
                if (directoryType2 == -5 && tagInfo2.directoryType == EXIF_DIRECTORY_MAKER_NOTES) {
                    return tagInfo2;
                }
                if (directoryType2 == 0 && tagInfo2.directoryType == TIFF_DIRECTORY_IFD0) {
                    return tagInfo2;
                }
                if (directoryType2 == 1 && tagInfo2.directoryType == TIFF_DIRECTORY_IFD1) {
                    return tagInfo2;
                }
                if (directoryType2 == 2 && tagInfo2.directoryType == TIFF_DIRECTORY_IFD2) {
                    return tagInfo2;
                }
                if (directoryType2 == 3 && tagInfo2.directoryType == TIFF_DIRECTORY_IFD3) {
                    return tagInfo2;
                }
            }
        }
        for (int i2 = 0; i2 < possibleMatches.size(); i2++) {
            TagInfo tagInfo3 = (TagInfo) possibleMatches.get(i2);
            if (tagInfo3.directoryType != EXIF_DIRECTORY_UNKNOWN) {
                if (directoryType2 >= 0 && tagInfo3.directoryType.isImageDirectory()) {
                    return tagInfo3;
                }
                if (directoryType2 < 0 && !tagInfo3.directoryType.isImageDirectory()) {
                    return tagInfo3;
                }
            }
        }
        for (int i3 = 0; i3 < possibleMatches.size(); i3++) {
            TagInfo tagInfo4 = (TagInfo) possibleMatches.get(i3);
            if (tagInfo4.directoryType == EXIF_DIRECTORY_UNKNOWN) {
                return tagInfo4;
            }
        }
        return TIFF_TAG_UNKNOWN;
    }

    private static TagInfo getTag(int directoryType2, int tag2) {
        List possibleMatches = (List) EXIF_TAG_MAP.get(new Integer(tag2));
        if (possibleMatches == null) {
            return TIFF_TAG_UNKNOWN;
        }
        return getTag(directoryType2, tag2, possibleMatches);
    }

    private int getValueLengthInBytes() {
        return this.fieldType.length * this.length;
    }

    public void fillInValue(ByteSource byteSource) throws ImageReadException, IOException {
        if (!this.fieldType.isLocalValue(this)) {
            setOversizeValue(byteSource.getBlock(this.valueOffset, getValueLengthInBytes()));
        }
    }

    public String getValueDescription() {
        try {
            return getValueDescription(getValue());
        } catch (ImageReadException e) {
            return "Invalid value: " + e.getMessage();
        }
    }

    private String getValueDescription(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return o.toString();
        }
        if (o instanceof String) {
            return "'" + o.toString().trim() + "'";
        }
        if (o instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format((Date) o);
        }
        if (o instanceof Object[]) {
            Object[] objects = (Object[]) o;
            StringBuffer result = new StringBuffer();
            int i = 0;
            while (true) {
                if (i >= objects.length) {
                    break;
                }
                Object object = objects[i];
                if (i > 50) {
                    result.append("... (" + objects.length + ")");
                    break;
                }
                if (i > 0) {
                    result.append(", ");
                }
                result.append("" + object);
                i++;
            }
            return result.toString();
        } else if (o instanceof int[]) {
            int[] values = (int[]) o;
            StringBuffer result2 = new StringBuffer();
            int i2 = 0;
            while (true) {
                if (i2 >= values.length) {
                    break;
                }
                int value = values[i2];
                if (i2 > 50) {
                    result2.append("... (" + values.length + ")");
                    break;
                }
                if (i2 > 0) {
                    result2.append(", ");
                }
                result2.append("" + value);
                i2++;
            }
            return result2.toString();
        } else if (o instanceof long[]) {
            long[] values2 = (long[]) o;
            StringBuffer result3 = new StringBuffer();
            int i3 = 0;
            while (true) {
                if (i3 >= values2.length) {
                    break;
                }
                long value2 = values2[i3];
                if (i3 > 50) {
                    result3.append("... (" + values2.length + ")");
                    break;
                }
                if (i3 > 0) {
                    result3.append(", ");
                }
                result3.append("" + value2);
                i3++;
            }
            return result3.toString();
        } else if (o instanceof double[]) {
            double[] values3 = (double[]) o;
            StringBuffer result4 = new StringBuffer();
            int i4 = 0;
            while (true) {
                if (i4 >= values3.length) {
                    break;
                }
                double value3 = values3[i4];
                if (i4 > 50) {
                    result4.append("... (" + values3.length + ")");
                    break;
                }
                if (i4 > 0) {
                    result4.append(", ");
                }
                result4.append("" + value3);
                i4++;
            }
            return result4.toString();
        } else if (o instanceof byte[]) {
            byte[] values4 = (byte[]) o;
            StringBuffer result5 = new StringBuffer();
            int i5 = 0;
            while (true) {
                if (i5 >= values4.length) {
                    break;
                }
                byte value4 = values4[i5];
                if (i5 > 50) {
                    result5.append("... (" + values4.length + ")");
                    break;
                }
                if (i5 > 0) {
                    result5.append(", ");
                }
                result5.append("" + ((int) value4));
                i5++;
            }
            return result5.toString();
        } else if (o instanceof char[]) {
            char[] values5 = (char[]) o;
            StringBuffer result6 = new StringBuffer();
            int i6 = 0;
            while (true) {
                if (i6 >= values5.length) {
                    break;
                }
                char value5 = values5[i6];
                if (i6 > 50) {
                    result6.append("... (" + values5.length + ")");
                    break;
                }
                if (i6 > 0) {
                    result6.append(", ");
                }
                result6.append("" + value5);
                i6++;
            }
            return result6.toString();
        } else if (!(o instanceof float[])) {
            return "Unknown: " + o.getClass().getName();
        } else {
            float[] values6 = (float[]) o;
            StringBuffer result7 = new StringBuffer();
            int i7 = 0;
            while (true) {
                if (i7 >= values6.length) {
                    break;
                }
                float value6 = values6[i7];
                if (i7 > 50) {
                    result7.append("... (" + values6.length + ")");
                    break;
                }
                if (i7 > 0) {
                    result7.append(", ");
                }
                result7.append("" + value6);
                i7++;
            }
            return result7.toString();
        }
    }

    public void dump() {
        PrintWriter pw = new PrintWriter(System.out);
        dump(pw);
        pw.flush();
    }

    public void dump(PrintWriter pw) {
        dump(pw, null);
    }

    public void dump(PrintWriter pw, String prefix) {
        if (prefix != null) {
            pw.print(prefix + ": ");
        }
        pw.println(toString());
        pw.flush();
    }

    public String getDescriptionWithoutValue() {
        return this.tag + " (0x" + Integer.toHexString(this.tag) + ": " + this.tagInfo.name + "): ";
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this.tag + " (0x" + Integer.toHexString(this.tag) + ": " + this.tagInfo.name + "): ");
        result.append(getValueDescription() + " (" + this.length + " " + this.fieldType.name + ")");
        return result.toString();
    }

    public String getTagName() {
        if (this.tagInfo == TIFF_TAG_UNKNOWN) {
            return this.tagInfo.name + " (0x" + Integer.toHexString(this.tag) + ")";
        }
        return this.tagInfo.name;
    }

    public String getFieldTypeName() {
        return this.fieldType.name;
    }

    public Object getValue() throws ImageReadException {
        return this.tagInfo.getValue(this);
    }

    public String getStringValue() throws ImageReadException {
        Object o = getValue();
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String) o;
        }
        throw new ImageReadException("Expected String value(" + this.tagInfo.getDescription() + "): " + o);
    }

    private static final Map makeTagMap(TagInfo[] tags, boolean ignoreDuplicates, String name) {
        Map map = new Hashtable();
        for (TagInfo tag2 : tags) {
            Integer key = new Integer(tag2.tag);
            List tagList = (List) map.get(key);
            if (tagList == null) {
                tagList = new ArrayList();
                map.put(key, tagList);
            }
            tagList.add(tag2);
        }
        return map;
    }

    public int[] getIntArrayValue() throws ImageReadException {
        Object o = getValue();
        if (o instanceof Number) {
            return new int[]{((Number) o).intValue()};
        } else if (o instanceof Number[]) {
            Number[] numbers = (Number[]) o;
            int[] result = new int[numbers.length];
            for (int i = 0; i < numbers.length; i++) {
                result[i] = numbers[i].intValue();
            }
            return result;
        } else if (o instanceof int[]) {
            int[] numbers2 = (int[]) o;
            int[] result2 = new int[numbers2.length];
            for (int i2 = 0; i2 < numbers2.length; i2++) {
                result2[i2] = numbers2[i2];
            }
            return result2;
        } else {
            throw new ImageReadException("Unknown value: " + o + " for: " + this.tagInfo.getDescription());
        }
    }

    public double[] getDoubleArrayValue() throws ImageReadException {
        Object o = getValue();
        if (o instanceof Number) {
            return new double[]{((Number) o).doubleValue()};
        } else if (o instanceof Number[]) {
            Number[] numbers = (Number[]) o;
            double[] result = new double[numbers.length];
            for (int i = 0; i < numbers.length; i++) {
                result[i] = numbers[i].doubleValue();
            }
            return result;
        } else if (o instanceof int[]) {
            int[] numbers2 = (int[]) o;
            double[] result2 = new double[numbers2.length];
            for (int i2 = 0; i2 < numbers2.length; i2++) {
                result2[i2] = (double) numbers2[i2];
            }
            return result2;
        } else if (o instanceof float[]) {
            float[] numbers3 = (float[]) o;
            double[] result3 = new double[numbers3.length];
            for (int i3 = 0; i3 < numbers3.length; i3++) {
                result3[i3] = (double) numbers3[i3];
            }
            return result3;
        } else if (o instanceof double[]) {
            double[] numbers4 = (double[]) o;
            double[] result4 = new double[numbers4.length];
            for (int i4 = 0; i4 < numbers4.length; i4++) {
                result4[i4] = numbers4[i4];
            }
            return result4;
        } else {
            throw new ImageReadException("Unknown value: " + o + " for: " + this.tagInfo.getDescription());
        }
    }

    public int getIntValueOrArraySum() throws ImageReadException {
        int[] numbers;
        Number[] numbers2;
        Object o = getValue();
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        if (o instanceof Number[]) {
            int sum = 0;
            for (Number number : (Number[]) o) {
                sum += number.intValue();
            }
            return sum;
        } else if (o instanceof int[]) {
            int sum2 = 0;
            for (int i : (int[]) o) {
                sum2 += i;
            }
            return sum2;
        } else {
            throw new ImageReadException("Unknown value: " + o + " for: " + this.tagInfo.getDescription());
        }
    }

    public int getIntValue() throws ImageReadException {
        Object o = getValue();
        if (o != null) {
            return ((Number) o).intValue();
        }
        throw new ImageReadException("Missing value: " + this.tagInfo.getDescription());
    }

    public double getDoubleValue() throws ImageReadException {
        Object o = getValue();
        if (o != null) {
            return ((Number) o).doubleValue();
        }
        throw new ImageReadException("Missing value: " + this.tagInfo.getDescription());
    }

    public byte[] getByteArrayValue() throws ImageReadException {
        return this.fieldType.getRawBytes(this);
    }

    public int getSortHint() {
        return this.sortHint;
    }

    public void setSortHint(int sortHint2) {
        this.sortHint = sortHint2;
    }
}
