package dji.thirdparty.sanselan.util;

import dji.component.accountcenter.IMemberProtocol;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class Debug {
    private static long counter = 0;
    public static String newline = "\r\n";

    public static void debug(String message) {
        System.out.println(message);
    }

    public static void debug(Object o) {
        System.out.println(o == null ? "null" : o.toString());
    }

    public static String getDebug(String message) {
        return message;
    }

    public static void debug() {
        newline();
    }

    public static void newline() {
        System.out.print(newline);
    }

    public static String getDebug(String message, int value) {
        return getDebug(message + ": " + value);
    }

    public static String getDebug(String message, double value) {
        return getDebug(message + ": " + value);
    }

    public static String getDebug(String message, String value) {
        return getDebug(message + " " + value);
    }

    public static String getDebug(String message, long value) {
        return getDebug(message + " " + Long.toString(value));
    }

    public static String getDebug(String message, int[] v) {
        StringBuffer result = new StringBuffer();
        if (v == null) {
            result.append(message + " (" + ((Object) null) + ")" + newline);
        } else {
            result.append(message + " (" + v.length + ")" + newline);
            for (int i = 0; i < v.length; i++) {
                result.append("\t" + v[i] + newline);
            }
            result.append(newline);
        }
        return result.toString();
    }

    public static String getDebug(String message, byte[] v) {
        return getDebug(message, v, 250);
    }

    public static String getDebug(String message, byte[] v, int max) {
        char c;
        StringBuffer result = new StringBuffer();
        if (v == null) {
            result.append(message + " (" + ((Object) null) + ")" + newline);
        } else {
            result.append(message + " (" + v.length + ")" + newline);
            int i = 0;
            while (i < max && i < v.length) {
                int b = v[i] & 255;
                if (b == 0 || b == 10 || b == 11 || b == 13) {
                    c = ' ';
                } else {
                    c = (char) b;
                }
                result.append("\t" + i + ": " + b + " (" + c + ", 0x" + Integer.toHexString(b) + ")" + newline);
                i++;
            }
            if (v.length > max) {
                result.append("\t..." + newline);
            }
            result.append(newline);
        }
        return result.toString();
    }

    public static String getDebug(String message, char[] v) {
        StringBuffer result = new StringBuffer();
        if (v == null) {
            result.append(getDebug(message + " (" + ((Object) null) + ")") + newline);
        } else {
            result.append(getDebug(message + " (" + v.length + ")") + newline);
            for (int i = 0; i < v.length; i++) {
                result.append(getDebug("\t" + v[i] + " (" + ((int) (v[i] & 255))) + ")" + newline);
            }
            result.append(newline);
        }
        return result.toString();
    }

    public static String getDebug(String message, List v) {
        StringBuffer result = new StringBuffer();
        StringBuilder append = new StringBuilder().append(" [");
        long j = counter;
        counter = 1 + j;
        String suffix = append.append(j).append(IMemberProtocol.STRING_SEPERATOR_RIGHT).toString();
        result.append(getDebug(message + " (" + v.size() + ")" + suffix) + newline);
        for (int i = 0; i < v.size(); i++) {
            result.append(getDebug("\t" + v.get(i).toString() + suffix) + newline);
        }
        result.append(newline);
        return result.toString();
    }

    public static void debug(String message, Map map) {
        debug(getDebug(message, map));
    }

    public static String getDebug(String message, Map map) {
        StringBuffer result = new StringBuffer();
        if (map == null) {
            return getDebug(message + " map: " + ((Object) null));
        }
        ArrayList keys = new ArrayList(map.keySet());
        result.append(getDebug(message + " map: " + keys.size()) + newline);
        for (int i = 0; i < keys.size(); i++) {
            Object key = keys.get(i);
            result.append(getDebug("\t" + i + ": '" + key + "' -> '" + map.get(key) + "'") + newline);
        }
        result.append(newline);
        return result.toString();
    }

    public static boolean compare(String prefix, Map a, Map b) {
        return compare(prefix, a, b, null, null);
    }

    private static void log(StringBuffer buffer, String s) {
        debug(s);
        if (buffer != null) {
            buffer.append(s + newline);
        }
    }

    public static boolean compare(String prefix, Map a, Map b, ArrayList ignore, StringBuffer buffer) {
        if (a == null && b == null) {
            log(buffer, prefix + " both maps null");
            return true;
        } else if (a == null) {
            log(buffer, prefix + " map a: null, map b: map");
            return false;
        } else if (b == null) {
            log(buffer, prefix + " map a: map, map b: null");
            return false;
        } else {
            ArrayList keys_a = new ArrayList(a.keySet());
            ArrayList keys_b = new ArrayList(b.keySet());
            if (ignore != null) {
                keys_a.removeAll(ignore);
                keys_b.removeAll(ignore);
            }
            boolean result = true;
            for (int i = 0; i < keys_a.size(); i++) {
                Object key = keys_a.get(i);
                if (!keys_b.contains(key)) {
                    log(buffer, prefix + "b is missing key '" + key + "' from a");
                    result = false;
                } else {
                    keys_b.remove(key);
                    Object value_a = a.get(key);
                    Object value_b = b.get(key);
                    if (!value_a.equals(value_b)) {
                        log(buffer, prefix + "key(" + key + ") value a: " + value_a + ") !=  b: " + value_b + ")");
                        result = false;
                    }
                }
            }
            for (int i2 = 0; i2 < keys_b.size(); i2++) {
                log(buffer, prefix + "a is missing key '" + keys_b.get(i2) + "' from b");
                result = false;
            }
            if (!result) {
                return result;
            }
            log(buffer, prefix + "a is the same as  b");
            return result;
        }
    }

    private static final String byteQuadToString(int bytequad) {
        byte b1 = (byte) ((bytequad >> 24) & 255);
        byte b2 = (byte) ((bytequad >> 16) & 255);
        byte b3 = (byte) ((bytequad >> 8) & 255);
        byte b4 = (byte) ((bytequad >> 0) & 255);
        StringBuffer fStringBuffer = new StringBuffer();
        fStringBuffer.append(new String(new char[]{(char) b1, (char) b2, (char) b3, (char) b4}));
        fStringBuffer.append(" bytequad: " + bytequad);
        fStringBuffer.append(" b1: " + ((int) b1));
        fStringBuffer.append(" b2: " + ((int) b2));
        fStringBuffer.append(" b3: " + ((int) b3));
        fStringBuffer.append(" b4: " + ((int) b4));
        return fStringBuffer.toString();
    }

    public static String getDebug(String message, boolean value) {
        return getDebug(message + " " + (value ? "true" : "false"));
    }

    public static String getDebug(String message, File file) {
        String path;
        StringBuilder append = new StringBuilder().append(message).append(": ");
        if (file == null) {
            path = "null";
        } else {
            path = file.getPath();
        }
        return getDebug(append.append(path).toString());
    }

    public static String getDebug(String message, Date value) {
        return getDebug(message, value == null ? "null" : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(value));
    }

    public static String getDebug(String message, Calendar value) {
        return getDebug(message, value == null ? "null" : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(value.getTime()));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, char[]):void
     arg types: [java.lang.String, java.lang.Object]
     candidates:
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Class, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, double):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, long):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.io.File):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.String):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Calendar):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Date):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.List):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Map):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, boolean):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, byte[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Throwable, int):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, char[]):void */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, byte[]):void
     arg types: [java.lang.String, java.lang.Object]
     candidates:
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Class, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, double):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, long):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.io.File):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.String):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Calendar):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Date):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.List):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Map):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, boolean):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, char[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Throwable, int):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, byte[]):void */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int[]):void
     arg types: [java.lang.String, java.lang.Object]
     candidates:
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Class, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, double):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, long):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.io.File):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.String):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Calendar):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Date):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.List):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Map):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, boolean):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, byte[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, char[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Throwable, int):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int[]):void */
    public static void debug(String message, Object value) {
        if (value == null) {
            debug(message, "null");
        } else if (value instanceof char[]) {
            debug(message, (char[]) ((char[]) value));
        } else if (value instanceof byte[]) {
            debug(message, (byte[]) ((byte[]) value));
        } else if (value instanceof int[]) {
            debug(message, (int[]) ((int[]) value));
        } else if (value instanceof String) {
            debug(message, (String) value);
        } else if (value instanceof List) {
            debug(message, (List) value);
        } else if (value instanceof Map) {
            debug(message, (Map) value);
        } else if (value instanceof File) {
            debug(message, (File) value);
        } else if (value instanceof Date) {
            debug(message, (Date) value);
        } else if (value instanceof Calendar) {
            debug(message, (Calendar) value);
        } else {
            debug(message, value.toString());
        }
    }

    public static void debug(String message, Object[] value) {
        if (value == null) {
            debug(message, "null");
        }
        debug(message, value.length);
        int i = 0;
        while (i < value.length && i < 10) {
            debug("\t" + i, value[i]);
            i++;
        }
        if (value.length > 10) {
            debug("\t...");
        }
        debug();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, byte[]):java.lang.String
     arg types: [java.lang.String, java.lang.Object]
     candidates:
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.Class, java.lang.Throwable):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, double):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, int):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, long):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.io.File):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.Object):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.String):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.Throwable):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.Calendar):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.Date):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.List):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.Map):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, boolean):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, char[]):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, int[]):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.Object[]):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.Throwable, int):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, byte[]):java.lang.String */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, char[]):java.lang.String
     arg types: [java.lang.String, java.lang.Object]
     candidates:
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.Class, java.lang.Throwable):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, double):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, int):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, long):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.io.File):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.Object):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.String):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.Throwable):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.Calendar):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.Date):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.List):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.Map):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, boolean):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, byte[]):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, int[]):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.Object[]):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.Throwable, int):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, char[]):java.lang.String */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, int[]):java.lang.String
     arg types: [java.lang.String, java.lang.Object]
     candidates:
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.Class, java.lang.Throwable):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, double):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, int):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, long):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.io.File):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.Object):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.String):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.Throwable):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.Calendar):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.Date):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.List):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.util.Map):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, boolean):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, byte[]):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, char[]):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, java.lang.Object[]):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.Throwable, int):java.lang.String
      dji.thirdparty.sanselan.util.Debug.getDebug(java.lang.String, int[]):java.lang.String */
    public static String getDebug(String message, Object value) {
        if (value == null) {
            return getDebug(message, "null");
        }
        if (value instanceof Calendar) {
            return getDebug(message, (Calendar) value);
        }
        if (value instanceof Date) {
            return getDebug(message, (Date) value);
        }
        if (value instanceof File) {
            return getDebug(message, (File) value);
        }
        if (value instanceof Map) {
            return getDebug(message, (Map) value);
        }
        if (value instanceof Map) {
            return getDebug(message, (Map) value);
        }
        if (value instanceof String) {
            return getDebug(message, (String) value);
        }
        if (value instanceof byte[]) {
            return getDebug(message, (byte[]) ((byte[]) value));
        }
        if (value instanceof char[]) {
            return getDebug(message, (char[]) ((char[]) value));
        }
        if (value instanceof int[]) {
            return getDebug(message, (int[]) ((int[]) value));
        }
        if (value instanceof List) {
            return getDebug(message, (List) value);
        }
        return getDebug(message, value.toString());
    }

    public static String getType(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Object[]) {
            return "[Object[]: " + ((Object[]) value).length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        if (value instanceof char[]) {
            return "[char[]: " + ((char[]) value).length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        if (value instanceof byte[]) {
            return "[byte[]: " + ((byte[]) value).length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        if (value instanceof short[]) {
            return "[short[]: " + ((short[]) value).length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        if (value instanceof int[]) {
            return "[int[]: " + ((int[]) value).length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        if (value instanceof long[]) {
            return "[long[]: " + ((long[]) value).length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        if (value instanceof float[]) {
            return "[float[]: " + ((float[]) value).length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        if (value instanceof double[]) {
            return "[double[]: " + ((double[]) value).length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        if (value instanceof boolean[]) {
            return "[boolean[]: " + ((boolean[]) value).length + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        return value.getClass().getName();
    }

    public static boolean isArray(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Object[]) {
            return true;
        }
        if (value instanceof char[]) {
            return true;
        }
        if (value instanceof byte[]) {
            return true;
        }
        if (value instanceof short[]) {
            return true;
        }
        if (value instanceof int[]) {
            return true;
        }
        if (value instanceof long[]) {
            return true;
        }
        if (value instanceof float[]) {
            return true;
        }
        if (value instanceof double[]) {
            return true;
        }
        if (value instanceof boolean[]) {
            return true;
        }
        return false;
    }

    public static String getDebug(String message, Object[] value) {
        StringBuffer result = new StringBuffer();
        if (value == null) {
            result.append(getDebug(message, "null") + newline);
        }
        result.append(getDebug(message, value.length));
        int i = 0;
        while (i < value.length && i < 10) {
            result.append(getDebug("\t" + i, value[i]) + newline);
            i++;
        }
        if (value.length > 10) {
            result.append(getDebug("\t...") + newline);
        }
        result.append(newline);
        return result.toString();
    }

    public static String getDebug(Class fClass, Throwable e) {
        return getDebug(fClass == null ? "[Unknown]" : fClass.getName(), e);
    }

    public static void debug(Class fClass, Throwable e) {
        debug(fClass.getName(), e);
    }

    public static void debug(String message, boolean value) {
        debug(message + " " + (value ? "true" : "false"));
    }

    public static void debug(String message, byte[] v) {
        debug(getDebug(message, v));
    }

    public static void debug(String message, char[] v) {
        debug(getDebug(message, v));
    }

    public static void debug(String message, Calendar value) {
        debug(message, value == null ? "null" : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(value.getTime()));
    }

    public static void debug(String message, Date value) {
        debug(message, value == null ? "null" : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(value));
    }

    public static void debug(String message, double value) {
        debug(message + ": " + value);
    }

    public static void debug(String message, File file) {
        debug(message + ": " + (file == null ? "null" : file.getPath()));
    }

    public static void debug(String message, int value) {
        debug(message + ": " + value);
    }

    public static void debug(String message, int[] v) {
        debug(getDebug(message, v));
    }

    public static void debug(String message, byte[] v, int max) {
        debug(getDebug(message, v, max));
    }

    public static void debug(String message, List v) {
        StringBuilder append = new StringBuilder().append(" [");
        long j = counter;
        counter = 1 + j;
        String suffix = append.append(j).append(IMemberProtocol.STRING_SEPERATOR_RIGHT).toString();
        debug(message + " (" + v.size() + ")" + suffix);
        for (int i = 0; i < v.size(); i++) {
            debug("\t" + v.get(i).toString() + suffix);
        }
        debug();
    }

    public static void debug(String message, long value) {
        debug(message + " " + Long.toString(value));
    }

    public static void debug(String message, String value) {
        debug(message + " " + value);
    }

    public static void debug(String message, Throwable e) {
        debug(getDebug(message, e));
    }

    public static void debug(Throwable e) {
        debug(getDebug(e));
    }

    public static void debug(Throwable e, int value) {
        debug(getDebug(e, value));
    }

    public static void dumpStack() {
        debug(getStackTrace(new Exception("Stack trace"), -1, 1));
    }

    public static void dumpStack(int limit) {
        debug(getStackTrace(new Exception("Stack trace"), limit, 1));
    }

    public static String getDebug(String message, Throwable e) {
        return message + newline + getDebug(e);
    }

    public static String getDebug(Throwable e) {
        return getDebug(e, -1);
    }

    public static String getDebug(Throwable e, int max) {
        String str;
        String localizedMessage;
        SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:SSS");
        StringBuffer result = new StringBuffer();
        String datetime = timestamp.format(new Date()).toLowerCase();
        result.append(newline);
        StringBuilder append = new StringBuilder().append("Throwable: ");
        if (e == null) {
            str = "";
        } else {
            str = "(" + e.getClass().getName() + ")";
        }
        result.append(append.append(str).append(":").append(datetime).append(newline).toString());
        StringBuilder append2 = new StringBuilder().append("Throwable: ");
        if (e == null) {
            localizedMessage = "null";
        } else {
            localizedMessage = e.getLocalizedMessage();
        }
        result.append(append2.append(localizedMessage).append(newline).toString());
        result.append(newline);
        result.append(getStackTrace(e, max));
        result.append("Caught here:" + newline);
        result.append(getStackTrace(new Exception(), max, 1));
        result.append(newline);
        return result.toString();
    }

    public static String getStackTrace(Throwable e) {
        return getStackTrace(e, -1);
    }

    public static String getStackTrace(Throwable e, int limit) {
        return getStackTrace(e, limit, 0);
    }

    public static String getStackTrace(Throwable e, int limit, int skip) {
        StringBuffer result = new StringBuffer();
        if (e != null) {
            StackTraceElement[] stes = e.getStackTrace();
            if (stes != null) {
                int i = skip;
                while (i < stes.length && (limit < 0 || i < limit)) {
                    StackTraceElement ste = stes[i];
                    result.append("\tat " + ste.getClassName() + "." + ste.getMethodName() + "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")" + newline);
                    i++;
                }
                if (limit >= 0 && stes.length > limit) {
                    result.append("\t..." + newline);
                }
            }
            result.append(newline);
        }
        return result.toString();
    }

    public static void debugByteQuad(String message, int i) {
        int red = (i >> 16) & 255;
        int green = (i >> 8) & 255;
        int blue = (i >> 0) & 255;
        System.out.println(message + ": alpha: " + ((i >> 24) & 255) + ", red: " + red + ", green: " + green + ", blue: " + blue);
    }

    public static void debugIPQuad(String message, int i) {
        int b2 = (i >> 16) & 255;
        int b3 = (i >> 8) & 255;
        int b4 = (i >> 0) & 255;
        System.out.println(message + ": b1: " + ((i >> 24) & 255) + ", b2: " + b2 + ", b3: " + b3 + ", b4: " + b4);
    }

    public static void debugIPQuad(String message, byte[] bytes) {
        System.out.print(message + ": ");
        if (bytes == null) {
            System.out.print("null");
        } else {
            for (int i = 0; i < bytes.length; i++) {
                if (i > 0) {
                    System.out.print(".");
                }
                System.out.print((int) (bytes[i] & 255));
            }
        }
        System.out.println();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object):void
     arg types: [java.lang.String, boolean[]]
     candidates:
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Class, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, double):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, long):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.io.File):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.String):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Calendar):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Date):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.List):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Map):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, boolean):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, byte[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, char[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Throwable, int):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object):void */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int):void
     arg types: [java.lang.String, byte]
     candidates:
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Class, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, double):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, long):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.io.File):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.String):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Throwable):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Calendar):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Date):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.List):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.util.Map):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, boolean):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, byte[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, char[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, java.lang.Object[]):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.Throwable, int):void
      dji.thirdparty.sanselan.util.Debug.debug(java.lang.String, int):void */
    public static void dump(String prefix, Object value) {
        if (value == null) {
            debug(prefix, "null");
        } else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            debug(prefix, array);
            for (int i = 0; i < array.length; i++) {
                dump(prefix + "\t" + i + ": ", array[i]);
            }
        } else if (value instanceof int[]) {
            int[] array2 = (int[]) value;
            debug(prefix, array2);
            for (int i2 = 0; i2 < array2.length; i2++) {
                debug(prefix + "\t" + i2 + ": ", array2[i2]);
            }
        } else if (value instanceof char[]) {
            debug(prefix, IMemberProtocol.STRING_SEPERATOR_LEFT + new String((char[]) value) + IMemberProtocol.STRING_SEPERATOR_RIGHT);
        } else if (value instanceof long[]) {
            long[] array3 = (long[]) value;
            debug(prefix, array3);
            for (int i3 = 0; i3 < array3.length; i3++) {
                debug(prefix + "\t" + i3 + ": ", array3[i3]);
            }
        } else if (value instanceof boolean[]) {
            boolean[] array4 = (boolean[]) value;
            debug(prefix, (Object) array4);
            for (int i4 = 0; i4 < array4.length; i4++) {
                debug(prefix + "\t" + i4 + ": ", array4[i4]);
            }
        } else if (value instanceof byte[]) {
            byte[] array5 = (byte[]) value;
            debug(prefix, array5);
            for (int i5 = 0; i5 < array5.length; i5++) {
                debug(prefix + "\t" + i5 + ": ", (int) array5[i5]);
            }
        } else if (value instanceof float[]) {
            float[] array6 = (float[]) value;
            debug(prefix, array6);
            for (int i6 = 0; i6 < array6.length; i6++) {
                debug(prefix + "\t" + i6 + ": ", (double) array6[i6]);
            }
        } else if (value instanceof byte[]) {
            double[] array7 = (double[]) value;
            debug(prefix, array7);
            for (int i7 = 0; i7 < array7.length; i7++) {
                debug(prefix + "\t" + i7 + ": ", array7[i7]);
            }
        } else if (value instanceof List) {
            List list = (List) value;
            debug(prefix, "list");
            for (int i8 = 0; i8 < list.size(); i8++) {
                dump(prefix + "\tlist: " + i8 + ": ", list.get(i8));
            }
        } else if (value instanceof Map) {
            Map map = (Map) value;
            debug(prefix, "map");
            ArrayList keys = new ArrayList(map.keySet());
            Collections.sort(keys);
            for (int i9 = 0; i9 < keys.size(); i9++) {
                Object key = keys.get(i9);
                dump(prefix + "\tmap: " + key + " -> ", map.get(key));
            }
        } else {
            debug(prefix, value.toString());
            debug(prefix + "\t", value.getClass().getName());
        }
    }

    public static final void purgeMemory() {
        try {
            System.runFinalization();
            Thread.sleep(50);
            System.gc();
            Thread.sleep(50);
        } catch (Throwable e) {
            debug(e);
        }
    }
}
