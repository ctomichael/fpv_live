package dji.midware.util;

import android.support.annotation.IntRange;
import dji.fieldAnnotation.EXClassNullAway;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Formatter;
import kotlin.jvm.internal.LongCompanionObject;

@EXClassNullAway
public class BytesUtil {
    private static final int[] INTERVAL = {48, 57, 65, 90, 97, 122};
    public static byte[] getLongBuf;
    public static int getLongCurIndex = 0;

    public static int getBitsFromByte(int byteData, int offset, int length) {
        if (length <= 0 || length > 8) {
            return -1;
        }
        int mask = 1;
        for (int i = 0; i < length; i++) {
            mask *= 2;
        }
        return (byteData >> offset) & (mask - 1);
    }

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

    public static byte getByte(byte[] bytes, int offset, int length) {
        int bytesLen;
        if (bytes != null && (bytesLen = bytes.length) != 0 && offset >= 0 && bytesLen > offset) {
            return (byte) (bytes[offset] & 255);
        }
        return 0;
    }

    public static short getShort(byte[] bytes, int offset, int length) {
        int bytesLen;
        short value = 0;
        if (bytes != null && (bytesLen = bytes.length) != 0 && offset >= 0 && bytesLen > offset) {
            if (length > bytesLen - offset) {
                length = bytesLen - offset;
            }
            value = 0;
            for (int i = (length + offset) - 1; i >= offset; i--) {
                value = (short) ((value << 8) | (bytes[i] & 255));
            }
        }
        return value;
    }

    public static int getInt(byte[] bytes, int offset, int length) {
        int bytesLen;
        int value = 0;
        if (bytes != null && (bytesLen = bytes.length) != 0 && offset >= 0 && bytesLen > offset) {
            if (length > bytesLen - offset) {
                length = bytesLen - offset;
            }
            value = 0;
            for (int i = (length + offset) - 1; i >= offset; i--) {
                value = (value << 8) | (bytes[i] & 255);
            }
        }
        return value;
    }

    public static long getLong(byte[] bytes, int offset, int length) {
        int bytesLen;
        long value = 0;
        if (bytes != null && (bytesLen = bytes.length) != 0 && offset >= 0 && bytesLen > offset) {
            if (length > bytesLen - offset) {
                length = bytesLen - offset;
            }
            value = 0;
            for (int i = (length + offset) - 1; i >= offset; i--) {
                value = (value << 8) | ((long) (bytes[i] & 255));
            }
        }
        return value;
    }

    public static float getFloat(byte[] bytes, int offset, int length) {
        return Float.intBitsToFloat(getInt(bytes, offset, length));
    }

    public static double getDouble(byte[] bytes, int offset, int length) {
        return Double.longBitsToDouble(getLong(bytes, offset, length));
    }

    public static short getShort(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public static int getInt(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static long getLong(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 8)).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public static float getFloat(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 4)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public static double getDouble(byte[] bytes) {
        return ByteBuffer.wrap(fillBytes(bytes, 8)).order(ByteOrder.LITTLE_ENDIAN).getDouble();
    }

    public static short getShort(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 2), start, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public static int getInt(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 4), start, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static long getLong(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 8), start, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public static long getUnsignedLong(byte[] bytes, int start, ByteOrder byteOrder) {
        if (bytes == null || start >= bytes.length) {
            return -1;
        }
        long rst = 0;
        int maxLen = Math.min(bytes.length - start, 8);
        for (int i = 0; i < maxLen; i++) {
            int curVal = (byteOrder == ByteOrder.BIG_ENDIAN ? bytes[i + start] : bytes[((maxLen - i) + start) - 1]) & 255;
            if (i == 0 && bytes.length - start >= 8 && curVal > 127) {
                return LongCompanionObject.MAX_VALUE;
            }
            rst = (rst << 8) + ((long) curVal);
        }
        return rst;
    }

    public static long getUnsignedLong(byte[] bytes, ByteOrder byteOrder) {
        return getUnsignedLong(bytes, 0, byteOrder);
    }

    public static float getFloat(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 4), start, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public static double getDouble(byte[] bytes, int start) {
        return ByteBuffer.wrap(fillBytes(bytes, 8), start, 8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
    }

    public static int getUShort(byte[] buffer, int start) {
        byte[] bf = new byte[2];
        System.arraycopy(buffer, start, bf, 0, 2);
        return ByteBuffer.wrap(fillBytes(bf, 4), 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static long getUInt(byte[] buffer, int start) {
        byte[] bf = new byte[4];
        System.arraycopy(buffer, start, bf, 0, 4);
        return ByteBuffer.wrap(fillBytes(bf, 8), 0, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();
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
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        int i = start;
        while (true) {
            if (i >= length || i >= bytes.length) {
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
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        int i = start;
        while (true) {
            if (i >= length || i >= bytes.length) {
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
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        int i = offset;
        while (true) {
            if (i >= offset + length || i >= bytes.length) {
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
        if (bytes == null) {
            return "";
        }
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
        if (bytes == null) {
            return "";
        }
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
        if (buffer == null) {
            return "";
        }
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
        if (buffer == null) {
            return h;
        }
        for (byte b : buffer) {
            String temp = Integer.toHexString(b & 255);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }
        return h;
    }

    public static String byteObjects2Hex(Object[] buffer) {
        String h = "";
        if (buffer == null) {
            return h;
        }
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] instanceof Byte) {
                String temp = Integer.toHexString(((Byte) buffer[i]).byteValue() & 255);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                h = h + " " + temp;
            }
        }
        return h;
    }

    public static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        try {
            int length = bytes.length;
            for (int i = 0; i < length; i++) {
                formatter.format("%02x", Byte.valueOf(bytes[i]));
            }
            return formatter.toString();
        } finally {
            formatter.close();
        }
    }

    public static String byte2hex(byte[] buffer, int start, int length) {
        String h = "";
        if (buffer == null) {
            return h;
        }
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
        if (buffer == null) {
            return "";
        }
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

    public static String byte2hex(int buffer) {
        String h = Integer.toHexString(buffer & 255);
        if (h.length() == 1) {
            return "0" + h;
        }
        return h;
    }

    public static String getBinaryStrFromByteArr(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
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

    public static long calcPerByteSum(byte[] bytes, int start, int length) {
        long rst = 0;
        int lastIndex = start + length;
        for (int i = start; i < lastIndex; i++) {
            rst += (long) (bytes[i] & 255);
        }
        return rst;
    }

    public static long getLongFromBytes(byte... bytes) {
        int len = Math.min(8, bytes.length);
        long rst = 0;
        for (int i = 0; i < len; i++) {
            rst += (long) ((bytes[i] & 255) << (i * 8));
        }
        return rst;
    }

    public static long getLongFromBytes(byte[] buf, int... indexes) {
        if (getLongBuf == null) {
            getLongBuf = new byte[8];
        }
        Arrays.fill(getLongBuf, (byte) 0);
        getLongCurIndex = 0;
        for (int index : indexes) {
            byte[] bArr = getLongBuf;
            int i = getLongCurIndex;
            getLongCurIndex = i + 1;
            bArr[i] = buf[index];
        }
        return getLongFromBytes(getLongBuf);
    }

    public static int getIntFromBytes(byte[] buf, int... indexes) {
        return (int) (getLongFromBytes(buf, indexes) & -1);
    }

    public static int convertTwoBytesToSignedInt(byte b1, byte b2) {
        return (b2 << 8) | (b1 & 255);
    }

    public static int convertFourBytesToSignedInt(byte b1, byte b2, byte b3, byte b4) {
        return (b4 << 24) + (b3 << Tnaf.POW_2_WIDTH) + (b2 << 8) + b1;
    }

    public static int kmp(byte[] buf, byte[] dest, int from, int to, int[] next) {
        int i = from;
        int j = 0;
        while (i != to) {
            if (i >= buf.length) {
                i = 0;
            }
            while (j > 0 && buf[i] != dest[j]) {
                j = next[j - 1];
            }
            if (buf[i] == dest[j]) {
                j++;
            }
            if (j == dest.length) {
                int rst = (i - j) + 1;
                if (rst < 0) {
                    return rst + buf.length;
                }
                return rst;
            }
            i++;
        }
        return -1;
    }

    public static int[] kmpNext(byte[] dest) {
        int[] next = new int[dest.length];
        next[0] = 0;
        int i = 1;
        int j = 0;
        while (i < dest.length) {
            while (j > 0 && dest[j] != dest[i]) {
                j = next[j - 1];
            }
            if (dest[i] == dest[j]) {
                j++;
            }
            next[i] = j;
            i++;
        }
        return next;
    }

    public static int setBitValue(int source, @IntRange(from = 0, to = 31) int pos, byte value) {
        int mask = 1 << pos;
        if (value > 0) {
            return source | mask;
        }
        return source & (mask ^ -1);
    }
}
