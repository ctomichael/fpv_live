package dji.midware.media.mp4;

import dji.fieldAnnotation.EXClassNullAway;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

@EXClassNullAway
public class MP4BytesUtil {
    private static final int[] INTERVAL = {48, 57, 65, 90, 97, 122};

    public static byte getUnsignedBytes(short data) {
        return (byte) (data & 255);
    }

    public static byte[] getBytes(short data) {
        return new byte[]{(byte) (data & 255), (byte) ((65280 & data) >> 8)};
    }

    public static byte[] getBytes(char data) {
        return new byte[]{(byte) data, (byte) (data >> 8)};
    }

    public static byte[] getBytes(int data) {
        return new byte[]{(byte) (data & 255), (byte) ((65280 & data) >> 8), (byte) ((16711680 & data) >> 16), (byte) ((-16777216 & data) >> 24)};
    }

    public static byte[] getUnsignedBytes(int data) {
        return new byte[]{(byte) (data & 255), (byte) ((65280 & data) >> 8)};
    }

    public static byte getByte(int data) {
        return (byte) (data & 255);
    }

    public static byte[] getBytes(long data) {
        return new byte[]{(byte) ((int) (data & 255)), (byte) ((int) ((data >> 8) & 255)), (byte) ((int) ((data >> 16) & 255)), (byte) ((int) ((data >> 24) & 255)), (byte) ((int) ((data >> 32) & 255)), (byte) ((int) ((data >> 40) & 255)), (byte) ((int) ((data >> 48) & 255)), (byte) ((int) ((data >> 56) & 255))};
    }

    public static byte[] getUnsignedBytes(long data) {
        return new byte[]{(byte) ((int) (data & 255)), (byte) ((int) ((data >> 8) & 255)), (byte) ((int) ((data >> 16) & 255)), (byte) ((int) ((data >> 24) & 255))};
    }

    public static byte[] getBytes(float data) {
        return getBytes(Float.floatToIntBits(data));
    }

    public static byte[] getUnsignedBytes(float data) {
        return getUnsignedBytes(Float.floatToIntBits(data));
    }

    public static byte[] getBytes(double data) {
        return getBytes(Double.doubleToLongBits(data));
    }

    public static byte[] getUnsignedBytes(double data) {
        return getUnsignedBytes(Double.doubleToLongBits(data));
    }

    private static byte[] getBytes(String data, String charsetName) {
        return data.getBytes(Charset.forName(charsetName));
    }

    public static byte[] getBytes(String data) {
        return getBytes(data, "GBK");
    }

    public static byte[] getBytesUTF8(String data) {
        return getBytes(data, "UTF-8");
    }

    public static short getInt(byte b) {
        return (short) (b & 255);
    }

    public static short getSignedInt(byte b) {
        return (short) b;
    }

    public static int getInt(short b) {
        return 65535 & b;
    }

    public static int getSignedInt(short b) {
        return b;
    }

    public static short getShort(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 2)).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public static int getInt(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 4)).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public static long getLong(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 8)).order(ByteOrder.BIG_ENDIAN).getLong();
    }

    public static float getFloat(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 4)).order(ByteOrder.BIG_ENDIAN).getFloat();
    }

    public static double getDouble(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 8)).order(ByteOrder.BIG_ENDIAN).getDouble();
    }

    public static short getShort(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 2), start, 2).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public static int getInt(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 4), start, 4).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public static long getLong(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 8), start, 8).order(ByteOrder.BIG_ENDIAN).getLong();
    }

    public static float getFloat(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 4), start, 4).order(ByteOrder.BIG_ENDIAN).getFloat();
    }

    public static double getDouble(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 8), start, 8).order(ByteOrder.BIG_ENDIAN).getDouble();
    }

    public static int getUShort(byte[] buffer, int start) {
        byte[] bf = new byte[2];
        System.arraycopy(buffer, start, bf, 0, 2);
        return ByteBuffer.wrap(fillBytes(bf, 4), 0, 4).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public static long getUInt(byte[] buffer, int start) {
        byte[] bf = new byte[4];
        System.arraycopy(buffer, start, bf, 0, 4);
        return ByteBuffer.wrap(fillBytes(bf, 8), 0, 8).order(ByteOrder.BIG_ENDIAN).getLong();
    }

    private static byte[] fillBytes(byte[] bytes, int maxLen) {
        int len = maxLen - bytes.length;
        if (len > 0) {
            return arrayComb(bytes, new byte[len]);
        }
        return bytes;
    }

    private static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    private static String getString(byte[] bytes, int start, int length, String charsetName) {
        return new String(bytes, start, length, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes, int start, int length) {
        int i = start;
        while (true) {
            if (i >= length) {
                break;
            } else if (bytes[i] == 0) {
                length = i - start;
                break;
            } else {
                i++;
            }
        }
        return getString(bytes, start, length, "GBK");
    }

    public static String getStringUTF8(byte[] bytes, int start, int length) {
        int i = start;
        while (true) {
            if (i >= length) {
                break;
            } else if (bytes[i] == 0) {
                length = i - start;
                break;
            } else {
                i++;
            }
        }
        return getString(bytes, start, length, "UTF-8");
    }

    public static String getStringUTF8Offset(byte[] bytes, int offset, int length) {
        int i = offset;
        while (true) {
            if (i >= offset + length) {
                break;
            } else if (bytes[i] == 0) {
                length = i - offset;
                break;
            } else {
                i++;
            }
        }
        return new String(bytes, offset, length, Charset.forName("UTF-8"));
    }

    public static String getString(byte[] bytes) {
        int i = 0;
        while (true) {
            if (i >= bytes.length) {
                break;
            } else if (bytes[i] == 0 || bytes[i] == -1) {
                bytes = readBytes(bytes, 0, i);
            } else {
                i++;
            }
        }
        bytes = readBytes(bytes, 0, i);
        return getString(bytes, "GBK");
    }

    public static String getStringUTF8(byte[] bytes) {
        int i = 0;
        while (true) {
            if (i >= bytes.length) {
                break;
            } else if (bytes[i] == 0 || bytes[i] == -1) {
                bytes = readBytes(bytes, 0, i);
            } else {
                i++;
            }
        }
        bytes = readBytes(bytes, 0, i);
        return getString(bytes, "UTF-8");
    }

    public static String byte2hexNoSep(byte[] buffer) {
        String h = "";
        for (byte b : buffer) {
            String temp = Integer.toHexString(b & 255);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + temp;
        }
        return h;
    }

    public static String byte2hex(byte[] buffer) {
        String h = "";
        for (byte b : buffer) {
            String temp = Integer.toHexString(b & 255);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }
        return h;
    }

    public static String byte2hex(byte[] buffer, int start, int length) {
        String h = "";
        for (int i = start; i < start + length; i++) {
            String temp = Integer.toHexString(buffer[i] & 255);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }
        return h;
    }

    public static String byte2hex(byte[] buffer, String sep) {
        String h = "";
        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 255);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            if (i == 0) {
                h = h + temp;
            } else {
                h = h + sep + temp;
            }
        }
        return h;
    }

    public static String byte2hex(byte buffer) {
        String h = Integer.toHexString(buffer & 255);
        if (h.length() == 1) {
            return "0" + h;
        }
        return h;
    }

    public static String getBinaryStrFromByteArr(byte[] bArr) {
        String result = "";
        for (int i = 0; i < bArr.length; i++) {
            result = result + getBinaryStrFromByte(bArr[i]);
        }
        return result;
    }

    public static String getBinaryStrFromByte(byte b) {
        String result = "";
        byte a = b;
        for (int i = 0; i < 8; i++) {
            result = (a % 2) + result;
            a = (byte) (a >> 1);
        }
        return result;
    }

    public static byte[] hex2byte(String str) {
        if (str == null) {
            return null;
        }
        String str2 = str.trim();
        int len = str2.length();
        if (len == 0 || len % 2 == 1) {
            return null;
        }
        byte[] b = new byte[(len / 2)];
        int i = 0;
        while (i < str2.length()) {
            try {
                b[i / 2] = (byte) Integer.decode("0X" + str2.substring(i, i + 2)).intValue();
                i += 2;
            } catch (Exception e) {
                return null;
            }
        }
        return b;
    }

    public static byte[] readBytes(byte[] source, int from, int length) {
        byte[] result = new byte[length];
        System.arraycopy(source, from, result, 0, length);
        return result;
    }

    public static byte[] arrayComb(byte[] prep, byte[] after) {
        byte[] result = new byte[(prep.length + after.length)];
        System.arraycopy(prep, 0, result, 0, prep.length);
        System.arraycopy(after, 0, result, prep.length, after.length);
        return result;
    }

    public static byte[] arrayApend(byte[] prep, byte after) {
        byte[] result = new byte[(prep.length + 1)];
        System.arraycopy(prep, 0, result, 0, prep.length);
        result[prep.length] = after;
        return result;
    }

    public static byte[] arrayRemove(byte[] source, int len) {
        int lenNow = source.length - len;
        byte[] result = new byte[lenNow];
        System.arraycopy(source, len, result, 0, lenNow);
        return result;
    }

    public static byte[] arrayPop(byte[] source, int len) {
        int lenNow = source.length - len;
        byte[] result = new byte[lenNow];
        System.arraycopy(source, 0, result, 0, lenNow);
        return result;
    }

    public static byte[] arraycopy(byte[] from, byte[] to) {
        System.arraycopy(from, 0, to, 0, from.length);
        return to;
    }

    public static byte[] arraycopy(byte[] from, byte[] to, int toPos) {
        System.arraycopy(from, 0, to, toPos, from.length);
        return to;
    }

    public static byte parseBcd(int t) {
        return (byte) (((t / 10) * 16) + (t % 10));
    }

    public static boolean isNumberOrLetter(byte value) {
        return (INTERVAL[0] <= value && value <= INTERVAL[1]) || (INTERVAL[2] <= value && value <= INTERVAL[3]) || (INTERVAL[4] <= value && value <= INTERVAL[5]);
    }
}
