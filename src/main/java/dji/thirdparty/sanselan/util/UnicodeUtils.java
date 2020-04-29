package dji.thirdparty.sanselan.util;

import dji.thirdparty.sanselan.common.BinaryConstants;
import java.io.UnsupportedEncodingException;
import org.bouncycastle.i18n.LocalizedMessage;

public abstract class UnicodeUtils implements BinaryConstants {
    public static final int CHAR_ENCODING_CODE_AMBIGUOUS = -1;
    public static final int CHAR_ENCODING_CODE_ISO_8859_1 = 0;
    public static final int CHAR_ENCODING_CODE_UTF_16_BIG_ENDIAN_NO_BOM = 3;
    public static final int CHAR_ENCODING_CODE_UTF_16_BIG_ENDIAN_WITH_BOM = 1;
    public static final int CHAR_ENCODING_CODE_UTF_16_LITTLE_ENDIAN_NO_BOM = 4;
    public static final int CHAR_ENCODING_CODE_UTF_16_LITTLE_ENDIAN_WITH_BOM = 2;
    public static final int CHAR_ENCODING_CODE_UTF_8 = 5;

    /* access modifiers changed from: protected */
    public abstract int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException;

    private UnicodeUtils() {
    }

    public static class UnicodeException extends Exception {
        public UnicodeException(String message) {
            super(message);
        }
    }

    public static final boolean isValidISO_8859_1(String s) {
        try {
            return s.equals(new String(s.getBytes(LocalizedMessage.DEFAULT_ENCODING), LocalizedMessage.DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error parsing string.", e);
        }
    }

    private static int findFirstDoubleByteTerminator(byte[] bytes, int index) {
        for (int i = index; i < bytes.length - 1; i += 2) {
            int c1 = bytes[index] & 255;
            int c2 = bytes[index + 1] & 255;
            if (c1 == 0 && c2 == 0) {
                return i;
            }
        }
        return -1;
    }

    public final int findEndWithTerminator(byte[] bytes, int index) throws UnicodeException {
        return findEnd(bytes, index, true);
    }

    public final int findEndWithoutTerminator(byte[] bytes, int index) throws UnicodeException {
        return findEnd(bytes, index, false);
    }

    public static UnicodeUtils getInstance(int charEncodingCode) throws UnicodeException {
        switch (charEncodingCode) {
            case 0:
                return new UnicodeMetricsASCII();
            case 1:
            case 2:
                return new UnicodeMetricsUTF16WithBOM();
            case 3:
                return new UnicodeMetricsUTF16NoBOM(77);
            case 4:
                return new UnicodeMetricsUTF16NoBOM(73);
            case 5:
                return new UnicodeMetricsUTF8();
            default:
                throw new UnicodeException("Unknown char encoding code: " + charEncodingCode);
        }
    }

    private static class UnicodeMetricsASCII extends UnicodeUtils {
        private UnicodeMetricsASCII() {
            super();
        }

        public int findEnd(byte[] bytes, int index, boolean includeTerminator) throws UnicodeException {
            int i = index;
            while (i < bytes.length) {
                if (bytes[i] != 0) {
                    i++;
                } else if (includeTerminator) {
                    return i + 1;
                } else {
                    return i;
                }
            }
            return bytes.length;
        }
    }

    private static class UnicodeMetricsUTF8 extends UnicodeUtils {
        private UnicodeMetricsUTF8() {
            super();
        }

        public int findEnd(byte[] bytes, int index, boolean includeTerminator) throws UnicodeException {
            while (index != bytes.length) {
                if (index > bytes.length) {
                    throw new UnicodeException("Terminator not found.");
                }
                int index2 = index + 1;
                int c1 = bytes[index] & 255;
                if (c1 == 0) {
                    return includeTerminator ? index2 : index2 - 1;
                } else if (c1 <= 127) {
                    index = index2;
                } else if (c1 <= 223) {
                    if (index2 >= bytes.length) {
                        throw new UnicodeException("Invalid unicode.");
                    }
                    index = index2 + 1;
                    int c2 = bytes[index2] & 255;
                    if (c2 < 128 || c2 > 191) {
                        throw new UnicodeException("Invalid code point.");
                    }
                } else if (c1 <= 239) {
                    if (index2 >= bytes.length - 1) {
                        throw new UnicodeException("Invalid unicode.");
                    }
                    int index3 = index2 + 1;
                    int c22 = bytes[index2] & 255;
                    if (c22 < 128 || c22 > 191) {
                        throw new UnicodeException("Invalid code point.");
                    }
                    int index4 = index3 + 1;
                    int c3 = bytes[index3] & 255;
                    if (c3 < 128 || c3 > 191) {
                        throw new UnicodeException("Invalid code point.");
                    }
                    index = index4;
                } else if (c1 > 244) {
                    throw new UnicodeException("Invalid code point.");
                } else if (index2 >= bytes.length - 2) {
                    throw new UnicodeException("Invalid unicode.");
                } else {
                    int index5 = index2 + 1;
                    int c23 = bytes[index2] & 255;
                    if (c23 < 128 || c23 > 191) {
                        throw new UnicodeException("Invalid code point.");
                    }
                    int index6 = index5 + 1;
                    int c32 = bytes[index5] & 255;
                    if (c32 < 128 || c32 > 191) {
                        throw new UnicodeException("Invalid code point.");
                    }
                    index = index6 + 1;
                    int c4 = bytes[index6] & 255;
                    if (c4 < 128 || c4 > 191) {
                        throw new UnicodeException("Invalid code point.");
                    }
                }
            }
            return bytes.length;
        }
    }

    private static abstract class UnicodeMetricsUTF16 extends UnicodeUtils {
        protected static final int BYTE_ORDER_BIG_ENDIAN = 0;
        protected static final int BYTE_ORDER_LITTLE_ENDIAN = 1;
        protected int byteOrder = 0;

        public UnicodeMetricsUTF16(int byteOrder2) {
            super();
            this.byteOrder = byteOrder2;
        }

        public boolean isValid(byte[] bytes, int index, boolean mayHaveTerminator, boolean mustHaveTerminator) throws UnicodeException {
            int msb1;
            int msb2;
            while (index != bytes.length) {
                if (index >= bytes.length - 1) {
                    return false;
                }
                int index2 = index + 1;
                int c1 = bytes[index] & 255;
                index = index2 + 1;
                int c2 = bytes[index2] & 255;
                if (this.byteOrder == 0) {
                    msb1 = c1;
                } else {
                    msb1 = c2;
                }
                if (c1 == 0 && c2 == 0) {
                    return mayHaveTerminator;
                }
                if (msb1 >= 216) {
                    if (msb1 >= 220 || index >= bytes.length - 1) {
                        return false;
                    }
                    int index3 = index + 1;
                    int c3 = bytes[index] & 255;
                    index = index3 + 1;
                    int c4 = bytes[index3] & 255;
                    if (this.byteOrder == 0) {
                        msb2 = c3;
                    } else {
                        msb2 = c4;
                    }
                    if (msb2 < 220) {
                        return false;
                    }
                }
            }
            if (!mustHaveTerminator) {
                return true;
            }
            return false;
        }

        public int findEnd(byte[] bytes, int index, boolean includeTerminator) throws UnicodeException {
            int msb1;
            int msb2;
            while (index != bytes.length) {
                if (index > bytes.length - 1) {
                    throw new UnicodeException("Terminator not found.");
                }
                int index2 = index + 1;
                int c1 = bytes[index] & 255;
                index = index2 + 1;
                int c2 = bytes[index2] & 255;
                if (this.byteOrder == 0) {
                    msb1 = c1;
                } else {
                    msb1 = c2;
                }
                if (c1 == 0 && c2 == 0) {
                    return includeTerminator ? index : index - 2;
                }
                if (msb1 >= 216) {
                    if (index > bytes.length - 1) {
                        throw new UnicodeException("Terminator not found.");
                    }
                    int index3 = index + 1;
                    int c3 = bytes[index] & 255;
                    index = index3 + 1;
                    int c4 = bytes[index3] & 255;
                    if (this.byteOrder == 0) {
                        msb2 = c3;
                    } else {
                        msb2 = c4;
                    }
                    if (msb2 < 220) {
                        throw new UnicodeException("Invalid code point.");
                    }
                }
            }
            return bytes.length;
        }
    }

    private static class UnicodeMetricsUTF16NoBOM extends UnicodeMetricsUTF16 {
        public UnicodeMetricsUTF16NoBOM(int byteOrder) {
            super(byteOrder);
        }
    }

    private static class UnicodeMetricsUTF16WithBOM extends UnicodeMetricsUTF16 {
        public UnicodeMetricsUTF16WithBOM() {
            super(0);
        }

        public int findEnd(byte[] bytes, int index, boolean includeTerminator) throws UnicodeException {
            if (index >= bytes.length - 1) {
                throw new UnicodeException("Missing BOM.");
            }
            int index2 = index + 1;
            int c1 = bytes[index] & 255;
            int index3 = index2 + 1;
            int c2 = bytes[index2] & 255;
            if (c1 == 255 && c2 == 254) {
                this.byteOrder = 1;
            } else if (c1 == 254 && c2 == 255) {
                this.byteOrder = 0;
            } else {
                throw new UnicodeException("Invalid byte order mark.");
            }
            return super.findEnd(bytes, index3, includeTerminator);
        }
    }
}
