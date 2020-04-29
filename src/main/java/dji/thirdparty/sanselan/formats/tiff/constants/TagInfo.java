package dji.thirdparty.sanselan.formats.tiff.constants;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.common.BinaryFileFunctions;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffDirectoryConstants;
import dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType;
import dji.thirdparty.sanselan.util.Debug;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.bouncycastle.i18n.LocalizedMessage;

public class TagInfo implements TiffDirectoryConstants, TiffFieldTypeConstants {
    protected static final int LENGTH_UNKNOWN = -1;
    public final FieldType[] dataTypes;
    public final TiffDirectoryConstants.ExifDirectoryType directoryType;
    public final int length;
    public final String name;
    public final int tag;

    public TagInfo(String name2, int tag2, FieldType dataType, int length2, TiffDirectoryConstants.ExifDirectoryType exifDirectory) {
        this(name2, tag2, new FieldType[]{dataType}, length2, exifDirectory);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TagInfo(java.lang.String r7, int r8, dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType r9, int r10) {
        /*
            r6 = this;
            r0 = 1
            dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType[] r3 = new dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType[r0]
            r0 = 0
            r3[r0] = r9
            dji.thirdparty.sanselan.formats.tiff.constants.TiffDirectoryConstants$ExifDirectoryType r5 = dji.thirdparty.sanselan.formats.tiff.constants.TagInfo.EXIF_DIRECTORY_UNKNOWN
            r0 = r6
            r1 = r7
            r2 = r8
            r4 = r10
            r0.<init>(r1, r2, r3, r4, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.sanselan.formats.tiff.constants.TagInfo.<init>(java.lang.String, int, dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType, int):void");
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TagInfo(java.lang.String r7, int r8, dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType r9, java.lang.String r10) {
        /*
            r6 = this;
            r0 = 1
            dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType[] r3 = new dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType[r0]
            r0 = 0
            r3[r0] = r9
            r4 = -1
            dji.thirdparty.sanselan.formats.tiff.constants.TiffDirectoryConstants$ExifDirectoryType r5 = dji.thirdparty.sanselan.formats.tiff.constants.TagInfo.EXIF_DIRECTORY_UNKNOWN
            r0 = r6
            r1 = r7
            r2 = r8
            r0.<init>(r1, r2, r3, r4, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.sanselan.formats.tiff.constants.TagInfo.<init>(java.lang.String, int, dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType, java.lang.String):void");
    }

    public TagInfo(String name2, int tag2, FieldType[] dataTypes2, String lengthDescription) {
        this(name2, tag2, dataTypes2, -1, EXIF_DIRECTORY_UNKNOWN);
    }

    public TagInfo(String name2, int tag2, FieldType dataType) {
        this(name2, tag2, dataType, -1, EXIF_DIRECTORY_UNKNOWN);
    }

    public TagInfo(String name2, int tag2, FieldType[] dataTypes2, int length2, String lengthDescription) {
        this(name2, tag2, dataTypes2, length2, EXIF_DIRECTORY_UNKNOWN);
    }

    public TagInfo(String name2, int tag2, FieldType[] dataTypes2, int length2, TiffDirectoryConstants.ExifDirectoryType exifDirectory) {
        this.name = name2;
        this.tag = tag2;
        this.dataTypes = dataTypes2;
        this.length = length2;
        this.directoryType = exifDirectory;
    }

    public Object getValue(TiffField entry) throws ImageReadException {
        return entry.fieldType.getSimpleValue(entry);
    }

    public byte[] encodeValue(FieldType fieldType, Object value, int byteOrder) throws ImageWriteException {
        return fieldType.writeData(value, byteOrder);
    }

    public String getDescription() {
        return this.tag + " (0x" + Integer.toHexString(this.tag) + ": " + this.name + "): ";
    }

    public String toString() {
        return "[TagInfo. tag: " + this.tag + " (0x" + Integer.toHexString(this.tag) + ", name: " + this.name + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    public boolean isDate() {
        return false;
    }

    public boolean isOffset() {
        return false;
    }

    public boolean isText() {
        return false;
    }

    public boolean isUnknown() {
        return false;
    }

    public static class Offset extends TagInfo {
        public Offset(String name, int tag, FieldType[] dataTypes, int length, TiffDirectoryConstants.ExifDirectoryType exifDirectory) {
            super(name, tag, dataTypes, length, exifDirectory);
        }

        public Offset(String name, int tag, FieldType dataType, int length, TiffDirectoryConstants.ExifDirectoryType exifDirectory) {
            super(name, tag, dataType, length, exifDirectory);
        }

        public Offset(String name, int tag, FieldType dataType, int length) {
            super(name, tag, dataType, length);
        }

        public boolean isOffset() {
            return true;
        }
    }

    public static class Date extends TagInfo {
        private static final DateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        private static final DateFormat DATE_FORMAT_2 = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");

        public Date(String name, int tag, FieldType dataType, int length) {
            super(name, tag, dataType, length);
        }

        public synchronized Object getValue(TiffField entry) throws ImageReadException {
            Object obj;
            Object o = entry.fieldType.getSimpleValue(entry);
            String s = (String) o;
            try {
                obj = DATE_FORMAT_1.parse(s);
            } catch (Exception e) {
                try {
                    obj = DATE_FORMAT_2.parse(s);
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                    obj = o;
                }
            }
            return obj;
        }

        public byte[] encodeValue(FieldType fieldType, Object value, int byteOrder) throws ImageWriteException {
            throw new ImageWriteException("date encode value: " + value + " (" + Debug.getType(value) + ")");
        }

        public String toString() {
            return "[TagInfo. tag: " + this.tag + ", name: " + this.name + " (data)]";
        }

        public boolean isDate() {
            return true;
        }
    }

    public static final class Text extends TagInfo {
        private static final TextEncoding[] TEXT_ENCODINGS = {TEXT_ENCODING_ASCII, TEXT_ENCODING_JIS, TEXT_ENCODING_UNICODE, TEXT_ENCODING_UNDEFINED};
        private static final TextEncoding TEXT_ENCODING_ASCII = new TextEncoding(new byte[]{65, 83, 67, 73, 73, 0, 0, 0}, "US-ASCII");
        private static final TextEncoding TEXT_ENCODING_JIS = new TextEncoding(new byte[]{74, 73, 83, 0, 0, 0, 0, 0}, "JIS");
        private static final TextEncoding TEXT_ENCODING_UNDEFINED = new TextEncoding(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}, LocalizedMessage.DEFAULT_ENCODING);
        private static final TextEncoding TEXT_ENCODING_UNICODE = new TextEncoding(new byte[]{85, 78, 73, 67, 79, 68, 69, 0}, "UTF-8");

        public Text(String name, int tag, FieldType dataType, int length, TiffDirectoryConstants.ExifDirectoryType exifDirectory) {
            super(name, tag, dataType, length, exifDirectory);
        }

        public Text(String name, int tag, FieldType[] dataTypes, int length, TiffDirectoryConstants.ExifDirectoryType exifDirectory) {
            super(name, tag, dataTypes, length, exifDirectory);
        }

        public boolean isText() {
            return true;
        }

        private static final class TextEncoding {
            public final String encodingName;
            public final byte[] prefix;

            public TextEncoding(byte[] prefix2, String encodingName2) {
                this.prefix = prefix2;
                this.encodingName = encodingName2;
            }
        }

        public byte[] encodeValue(FieldType fieldType, Object value, int byteOrder) throws ImageWriteException {
            if (!(value instanceof String)) {
                throw new ImageWriteException("Text value not String: " + value + " (" + Debug.getType(value) + ")");
            }
            String s = (String) value;
            try {
                byte[] asciiBytes = s.getBytes(TEXT_ENCODING_ASCII.encodingName);
                if (new String(asciiBytes, TEXT_ENCODING_ASCII.encodingName).equals(s)) {
                    byte[] result = new byte[(asciiBytes.length + TEXT_ENCODING_ASCII.prefix.length)];
                    System.arraycopy(TEXT_ENCODING_ASCII.prefix, 0, result, 0, TEXT_ENCODING_ASCII.prefix.length);
                    System.arraycopy(asciiBytes, 0, result, TEXT_ENCODING_ASCII.prefix.length, asciiBytes.length);
                    return result;
                }
                byte[] unicodeBytes = s.getBytes(TEXT_ENCODING_UNICODE.encodingName);
                byte[] result2 = new byte[(unicodeBytes.length + TEXT_ENCODING_UNICODE.prefix.length)];
                System.arraycopy(TEXT_ENCODING_UNICODE.prefix, 0, result2, 0, TEXT_ENCODING_UNICODE.prefix.length);
                System.arraycopy(unicodeBytes, 0, result2, TEXT_ENCODING_UNICODE.prefix.length, unicodeBytes.length);
                return result2;
            } catch (UnsupportedEncodingException e) {
                throw new ImageWriteException(e.getMessage(), e);
            }
        }

        public Object getValue(TiffField entry) throws ImageReadException {
            if (entry.type == FIELD_TYPE_ASCII.type) {
                return FIELD_TYPE_ASCII.getSimpleValue(entry);
            }
            if (entry.type == FIELD_TYPE_UNDEFINED.type || entry.type == FIELD_TYPE_BYTE.type) {
                byte[] bytes = entry.fieldType.getRawBytes(entry);
                if (bytes.length < 8) {
                    try {
                        return new String(bytes, "US-ASCII");
                    } catch (UnsupportedEncodingException e) {
                        throw new ImageReadException("Text field missing encoding prefix.");
                    }
                } else {
                    int i = 0;
                    while (i < TEXT_ENCODINGS.length) {
                        TextEncoding encoding = TEXT_ENCODINGS[i];
                        if (BinaryFileFunctions.compareBytes(bytes, 0, encoding.prefix, 0, encoding.prefix.length)) {
                            try {
                                return new String(bytes, encoding.prefix.length, bytes.length - encoding.prefix.length, encoding.encodingName);
                            } catch (UnsupportedEncodingException e2) {
                                throw new ImageReadException(e2.getMessage(), e2);
                            }
                        } else {
                            i++;
                        }
                    }
                    try {
                        return new String(bytes, "US-ASCII");
                    } catch (UnsupportedEncodingException e3) {
                        throw new ImageReadException("Unknown text encoding prefix.");
                    }
                }
            } else {
                Debug.debug("entry.type", entry.type);
                Debug.debug("entry.directoryType", entry.directoryType);
                Debug.debug("entry.type", entry.getDescriptionWithoutValue());
                Debug.debug("entry.type", entry.fieldType);
                throw new ImageReadException("Text field not encoded as bytes.");
            }
        }
    }

    public static final class Unknown extends TagInfo {
        public Unknown(String name, int tag, FieldType[] dataTypes, int length, TiffDirectoryConstants.ExifDirectoryType exifDirectory) {
            super(name, tag, dataTypes, length, exifDirectory);
        }

        public boolean isUnknown() {
            return true;
        }

        public byte[] encodeValue(FieldType fieldType, Object value, int byteOrder) throws ImageWriteException {
            return TagInfo.super.encodeValue(fieldType, value, byteOrder);
        }

        public Object getValue(TiffField entry) throws ImageReadException {
            return TagInfo.super.getValue(entry);
        }
    }
}
