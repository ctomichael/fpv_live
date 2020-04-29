package com.loc;

import dji.pilot.publics.util.DJITimeUtils;
import java.util.Calendar;
import java.util.Date;

/* compiled from: DateUtil */
public final class et {
    private static long a(long j) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(j));
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        return instance.getTimeInMillis();
    }

    public static long a(long j, long j2, int i) {
        if (i <= 0) {
            return j;
        }
        try {
            if (Math.abs(j - j2) <= ((long) i) * 31536000000L) {
                return j;
            }
            long a = (j - a(j)) + a(j2);
            long abs = Math.abs(a - j2);
            Calendar instance = Calendar.getInstance();
            instance.setTime(new Date(a));
            int i2 = instance.get(11);
            if (i2 == 23 && abs >= 82800000) {
                a -= DJITimeUtils.MILLIS_IN_DAY;
            }
            if (i2 == 0 && abs >= 82800000) {
                a += DJITimeUtils.MILLIS_IN_DAY;
            }
            return a;
        } catch (Throwable th) {
            return j;
        }
    }
}
