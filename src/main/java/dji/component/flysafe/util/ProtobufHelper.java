package dji.component.flysafe.util;

import org.bouncycastle.asn1.cmc.BodyPartID;

public class ProtobufHelper {
    public static int toInt(Integer value) {
        if (value != null) {
            return value.intValue();
        }
        return 0;
    }

    public static boolean toBool(Boolean value) {
        if (value != null) {
            return value.booleanValue();
        }
        return false;
    }

    public static boolean toBool(Boolean value, boolean defaultValue) {
        if (value != null) {
            return value.booleanValue();
        }
        return defaultValue;
    }

    public static float toFloat(Float value) {
        if (value != null) {
            return value.floatValue();
        }
        return 0.0f;
    }

    public static float toFloat(Float value, float defaultValue) {
        if (value != null) {
            return value.floatValue();
        }
        return defaultValue;
    }

    public static double toDouble(Double value) {
        if (value != null) {
            return value.doubleValue();
        }
        return 0.0d;
    }

    public static long toLong(Long value) {
        if (value != null) {
            return value.longValue();
        }
        return 0;
    }

    public static short toShort(Short value) {
        if (value != null) {
            return value.shortValue();
        }
        return 0;
    }

    public static String getString(String value) {
        return value != null ? value : "";
    }

    public static long uint32toLong(int value) {
        return ((long) value) & BodyPartID.bodyIdMax;
    }
}
