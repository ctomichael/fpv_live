package dji.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.media.session.PlaybackStateCompat;

public class UnitUtils {
    public static final float LENGTH_METRIC2IMPERIAL = 3.28f;
    public static final float LENGTH_METRIC2INCH = 39.4f;
    public static final int MEMORY_SIZE_BYTE = 1;
    public static final int MEMORY_SIZE_GB = 1073741824;
    public static final int MEMORY_SIZE_KB = 1024;
    public static final int MEMORY_SIZE_MB = 1048576;
    public static final float SPEED_METRIC2IMPERIAL = 2.237f;
    public static final float SPEED_MS_TO_KMH = 3.6f;
    public static final float TEMPERATURE_K2C = 273.15f;

    public static String bytes2Bits(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            for (int j = 7; j >= 0; j--) {
                sb.append(((aByte >> j) & 1) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    public static byte[] bits2Bytes(String bits) {
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        if (lenMod != 0) {
            for (int i = lenMod; i < 8; i++) {
                bits = "0" + bits;
            }
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for (int i2 = 0; i2 < byteLen; i2++) {
            for (int j = 0; j < 8; j++) {
                bytes[i2] = (byte) (bytes[i2] << 1);
                bytes[i2] = (byte) (bytes[i2] | (bits.charAt((i2 * 8) + j) - '0'));
            }
        }
        return bytes;
    }

    public static char[] bytes2Chars(byte[] bytes) {
        int len;
        char[] chars = null;
        if (bytes != null && (len = bytes.length) > 0) {
            chars = new char[len];
            for (int i = 0; i < len; i++) {
                chars[i] = (char) (bytes[i] & 255);
            }
        }
        return chars;
    }

    public static byte[] chars2Bytes(char[] chars) {
        if (chars == null || chars.length <= 0) {
            return null;
        }
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }

    public static long memorySize2Byte(long memorySize, int unit) {
        if (memorySize < 0) {
            return -1;
        }
        return ((long) unit) * memorySize;
    }

    public static double byte2MemorySize(long byteSize, int unit) {
        if (byteSize < 0) {
            return -1.0d;
        }
        return ((double) byteSize) / ((double) unit);
    }

    public static String byte2FitMemorySize(long byteSize) {
        if (byteSize < 0) {
            return "shouldn't be less than zero!";
        }
        if (byteSize < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            return String.format("%.3fB", Double.valueOf((double) byteSize));
        } else if (byteSize < 1048576) {
            return String.format("%.3fKB", Double.valueOf(((double) byteSize) / 1024.0d));
        } else if (byteSize < 1073741824) {
            return String.format("%.3fMB", Double.valueOf(((double) byteSize) / 1048576.0d));
        } else {
            return String.format("%.3fGB", Double.valueOf(((double) byteSize) / 1.073741824E9d));
        }
    }

    public static int getDimens(Context context, int dimenId) {
        return (int) context.getResources().getDimension(dimenId);
    }

    public static int dp2px(float dpValue) {
        return (int) ((dpValue * Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dp(float pxValue) {
        return (int) ((pxValue / Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    public static int sp2px(float spValue) {
        return (int) ((spValue * Resources.getSystem().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int px2sp(float pxValue) {
        return (int) ((pxValue / Resources.getSystem().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    private static boolean hasSpace(String string) {
        if (string == null) {
            return true;
        }
        int len = string.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static final float meterPerSecondToKiloMeterPerHour(float speed) {
        return 3.6f * speed;
    }

    public static final float kelvinToCelsius(float kelvin) {
        return kelvin - 273.15f;
    }

    public static final float celsiusToKelvin(float celsius) {
        return 273.15f + celsius;
    }

    public static final float celsiusToFahrenheit(float celsius) {
        return (1.8f * celsius) + 32.0f;
    }

    public static final float fahrenheitToCelsius(float fahrenheit) {
        return (fahrenheit - 32.0f) / 1.8f;
    }

    public static float metricToImperialByLength(float value) {
        return 3.28f * value;
    }

    public static float imperialToMetricByLength(float value) {
        return value / 3.28f;
    }

    public static float metricToImperialBySpeed(float value) {
        return 2.237f * value;
    }

    public static float imperialToMetricBySpeed(float value) {
        return value / 2.237f;
    }
}
