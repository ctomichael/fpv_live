package com.dji.cmd.v1.base;

public class ProtocolBytesUtil {
    private static int[] MASK;

    static {
        MASK = null;
        MASK = new int[33];
        for (int i = 1; i <= 32; i++) {
            MASK[i] = (MASK[i - 1] << 1) | 1;
        }
    }

    public static boolean setValue(byte value, byte[] data, int offset, int len) {
        if (!isValid(data, offset, len) || len != 1) {
            return false;
        }
        data[offset] = value;
        return true;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(int, byte[], int, int):boolean
     arg types: [short, byte[], int, int]
     candidates:
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(byte, byte[], int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(double, byte[], int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(float, byte[], int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(long, byte[], int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(short, byte[], int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(int, byte[], int, int):boolean */
    public static boolean setValue(short value, byte[] data, int offset, int len) {
        if (len <= 2) {
            return setValue((int) value, data, offset, len);
        }
        return false;
    }

    public static boolean setValue(int value, byte[] data, int offset, int len) {
        if (!isValid(data, offset, len) || len > 4) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            data[i + offset] = (byte) ((value >> (i * 8)) & 255);
        }
        return true;
    }

    public static boolean setValue(long value, byte[] data, int offset, int len) {
        if (!isValid(data, offset, len) || len > 8) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            data[i + offset] = (byte) ((int) ((value >> (i * 8)) & 255));
        }
        return true;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(int, byte[], int, int, int, int):boolean
     arg types: [byte, byte[], int, int, int, int]
     candidates:
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(byte, byte[], int, int, int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(long, byte[], int, int, int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(short, byte[], int, int, int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(int, byte[], int, int, int, int):boolean */
    public static boolean setValue(byte value, byte[] data, int offset, int len, int bitOffset, int bitLen) {
        if (bitLen > 0) {
            return setValue((int) value, data, offset, len, bitOffset, bitLen);
        }
        return setValue(value, data, offset, len);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(int, byte[], int, int, int, int):boolean
     arg types: [short, byte[], int, int, int, int]
     candidates:
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(byte, byte[], int, int, int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(long, byte[], int, int, int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(short, byte[], int, int, int, int):boolean
      com.dji.cmd.v1.base.ProtocolBytesUtil.setValue(int, byte[], int, int, int, int):boolean */
    public static boolean setValue(short value, byte[] data, int offset, int len, int bitOffset, int bitLen) {
        if (bitLen > 0) {
            return setValue((int) value, data, offset, len, bitOffset, bitLen);
        }
        return setValue(value, data, offset, len);
    }

    public static boolean setValue(int value, byte[] data, int offset, int len, int bitOffset, int bitLen) {
        if (bitLen <= 0) {
            return setValue(value, data, offset, len);
        }
        setValue(((MASK[bitLen] << bitOffset) & getInt(data, offset, len)) | (value << bitOffset), data, offset, 4);
        return true;
    }

    public static boolean setValue(long value, byte[] data, int offset, int len, int bitOffset, int bitLen) {
        if (bitLen <= 0) {
            return setValue(value, data, offset, len);
        }
        if (value >= 2147483647L || bitLen > 32) {
            return false;
        }
        return setValue((int) value, data, offset, len, bitOffset, bitLen);
    }

    public static boolean setValue(float value, byte[] data, int offset, int len) {
        return setValue(Float.floatToIntBits(value), data, offset, len);
    }

    public static boolean setValue(double value, byte[] data, int offset, int len) {
        return setValue(Double.doubleToLongBits(value), data, offset, len);
    }

    public static byte getByte(byte[] data, int offset, int len, int bitOffset, int bitLen) {
        if (bitLen > 0) {
            return (byte) getInt(data, offset, len, bitOffset, bitLen);
        }
        return getByte(data, offset);
    }

    public static short getShort(byte[] data, int offset, int len, int bitOffset, int bitLen) {
        if (bitLen > 0) {
            return (short) getInt(data, offset, len, bitOffset, bitLen);
        }
        return getShort(data, offset, len);
    }

    public static int getInt(byte[] data, int offset, int len, int bitOffset, int bitLen) {
        if (bitLen <= 0) {
            return getInt(data, offset, len);
        }
        if (bitLen > 32 || bitOffset + bitLen > 32) {
            return 0;
        }
        return (getInt(data, offset, 4) >> bitOffset) & MASK[bitLen];
    }

    public static long getLong(byte[] data, int offset, int len, int bitOffset, int bitLen) {
        if (bitLen > 0) {
            return (long) getInt(data, offset, len, bitOffset, bitLen);
        }
        return getLong(data, offset, len);
    }

    public static byte getByte(byte[] data, int offset) {
        if (isValid(data, offset, 1)) {
            return data[offset];
        }
        return 0;
    }

    public static short getShort(byte[] data, int offset, int len) {
        if (!isValid(data, offset, len) || len > 2) {
            return 0;
        }
        return (short) getInt(data, offset, len);
    }

    public static int getInt(byte[] data, int offset, int len) {
        int value = 0;
        if (isValid(data, offset, len) && len <= 4) {
            for (int i = (len + offset) - 1; i >= offset; i--) {
                value = (value << 8) | (data[i] & 255);
            }
        }
        return value;
    }

    public static long getLong(byte[] data, int offset, int len) {
        long value = 0;
        if (isValid(data, offset, len) && len <= 8) {
            for (int i = (len + offset) - 1; i >= offset; i--) {
                value = (value << 8) | ((long) (data[i] & 255));
            }
        }
        return value;
    }

    private static boolean isValid(byte[] data, int offset, int len) {
        return data != null && offset + len <= data.length;
    }

    public static float getFloat(byte[] data, int offset, int length) {
        return Float.intBitsToFloat(getInt(data, offset, length));
    }

    public static double getDouble(byte[] data, int offset, int length) {
        return Double.longBitsToDouble(getLong(data, offset, length));
    }

    public static byte getByte(byte[] data, int offset, int length) {
        return data[offset];
    }

    public static byte[] getBytes(long data) {
        return new byte[]{(byte) ((int) (data & 255)), (byte) ((int) ((data >> 8) & 255)), (byte) ((int) ((data >> 16) & 255)), (byte) ((int) ((data >> 24) & 255)), (byte) ((int) ((data >> 32) & 255)), (byte) ((int) ((data >> 40) & 255)), (byte) ((int) ((data >> 48) & 255)), (byte) ((int) ((data >> 56) & 255))};
    }

    public static byte[] getBytes(float data) {
        return getBytes((long) Float.floatToIntBits(data));
    }

    public static byte[] getBytes(byte[] data, int offset, long length) {
        return getBytes(data, offset, (int) length);
    }

    public static byte[] getBytes(byte[] data, int offset, int length) {
        byte[] tmpArrRes = new byte[length];
        System.arraycopy(data, offset, tmpArrRes, 0, length);
        return tmpArrRes;
    }

    public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }
}
