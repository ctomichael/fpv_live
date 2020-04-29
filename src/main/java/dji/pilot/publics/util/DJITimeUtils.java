package dji.pilot.publics.util;

import dji.fieldAnnotation.EXClassNullAway;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@EXClassNullAway
public class DJITimeUtils {
    public static final long MILLIS_IN_DAY = 86400000;
    public static final int SECONDS_IN_DAY = 86400;

    public static boolean isSameDayOfMillis(long ms1, long ms2) {
        long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY && interval > -86400000 && toDay(ms1) == toDay(ms2);
    }

    private static long toDay(long millis) {
        return (((long) TimeZone.getDefault().getOffset(millis)) + millis) / MILLIS_IN_DAY;
    }

    public static String formatTime(long time, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(time));
    }
}
