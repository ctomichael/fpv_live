package dji.utils;

import com.mapzen.android.lost.internal.FusionEngine;
import dji.log.DJILogUtils;
import dji.pilot.publics.util.DJITimeUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static final int TIMECONSTANT_DAY = 86400000;
    public static final int TIMECONSTANT_HOUR = 3600000;
    public static final int TIMECONSTANT_MIN = 60000;
    public static final int TIMECONSTANT_MSEC = 1;
    public static final int TIMECONSTANT_SEC = 1000;

    public static boolean isSameDayOfMillis(long ms1, long ms2) {
        if (ms1 == 0 || ms2 == 0) {
            return false;
        }
        long interval = ms1 - ms2;
        if (interval >= DJITimeUtils.MILLIS_IN_DAY || interval <= -86400000 || millis2Date(ms1) != millis2Date(ms2)) {
            return false;
        }
        return true;
    }

    public static String millsSpanToMinSecString(long mills) {
        long mills2 = mills / 1000;
        int min = (int) (mills2 / 60);
        return String.format(Locale.US, "%02d:%02d", Integer.valueOf(min), Integer.valueOf((int) (mills2 % 60)));
    }

    private static SimpleDateFormat getDefaultFormat() {
        return new SimpleDateFormat(DJILogUtils.FORMAT_2, Locale.getDefault());
    }

    public static String millis2String(long millis) {
        return millis2String(millis, getDefaultFormat());
    }

    public static String millis2String(long millis, DateFormat format) {
        if (!(millis == 0 || format == null)) {
            try {
                return format.format(new Date(millis));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static long string2Millis(String time) {
        return string2Millis(time, getDefaultFormat());
    }

    public static long string2Millis(String time, DateFormat format) {
        if (!(time == null || format == null)) {
            try {
                return format.parse(time).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static Date string2Date(String time) {
        return string2Date(time, getDefaultFormat());
    }

    public static Date string2Date(String time, DateFormat format) {
        if (!(time == null || format == null)) {
            try {
                return format.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String date2String(Date date) {
        return date2String(date, getDefaultFormat());
    }

    public static String date2String(Date date, DateFormat format) {
        return format.format(date);
    }

    public static long date2Millis(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return -1;
    }

    public static Date millis2Date(long millis) {
        return new Date(millis);
    }

    public static long getTimeSpan(String time1, String time2, int unit) {
        return getTimeSpan(time1, time2, getDefaultFormat(), unit);
    }

    public static long getTimeSpan(String time1, String time2, DateFormat format, int unit) {
        return millis2TimeSpan(string2Millis(time1, format) - string2Millis(time2, format), unit);
    }

    public static long getTimeSpan(Date date1, Date date2, int unit) {
        return millis2TimeSpan(date2Millis(date1) - date2Millis(date2), unit);
    }

    public static long getTimeSpan(long millis1, long millis2, int unit) {
        return millis2TimeSpan(millis1 - millis2, unit);
    }

    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    public static String getNowString() {
        return millis2String(System.currentTimeMillis(), getDefaultFormat());
    }

    public static String getNowString(DateFormat format) {
        return millis2String(System.currentTimeMillis(), format);
    }

    public static Date getNowDate() {
        return new Date();
    }

    public static long getTimeSpanByNow(String time, int unit) {
        return getTimeSpan(time, getNowString(), getDefaultFormat(), unit);
    }

    public static long getTimeSpanByNow(String time, DateFormat format, int unit) {
        return getTimeSpan(time, getNowString(format), format, unit);
    }

    public static long getTimeSpanByNow(Date date, int unit) {
        return getTimeSpan(date, new Date(), unit);
    }

    public static long getTimeSpanByNow(long millis, int unit) {
        return getTimeSpan(millis, System.currentTimeMillis(), unit);
    }

    private static long getZeroOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(11, 0);
        cal.set(13, 0);
        cal.set(12, 0);
        cal.set(14, 0);
        return cal.getTimeInMillis();
    }

    private static long millis2TimeSpan(long millis, int unit) {
        if (millis == 0 || unit == 0) {
            return -1;
        }
        return millis / ((long) unit);
    }

    public static String formatTimeString(long timeMs) {
        long minute = timeMs / FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS;
        long second = (timeMs % FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS) / 1000;
        return String.format(Locale.ENGLISH, "%d:%02d", Long.valueOf(minute), Long.valueOf(second));
    }
}
