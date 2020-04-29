package dji.sdksharedlib.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@EXClassNullAway
public class DateUtils {
    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private static Date string2Date(String str, String pattern) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateStringFromLong(long seconds, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(1000 * seconds));
    }

    public static String getSystemTime() {
        return new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date());
    }

    public static String getSystemTimeOnlyYMD() {
        return new SimpleDateFormat(DJILogUtils.FORMAT_1).format(new Date());
    }

    public static Date string2Date(String str) {
        return string2Date(str, DJILogUtils.FORMAT_1);
    }

    public static Date string2DateTime(String str) {
        return string2Date(str, DJILogUtils.FORMAT_2);
    }

    public static int getUTCOffset() {
        return TimeZone.getDefault().getOffset(new Date().getTime()) / 360000;
    }
}
