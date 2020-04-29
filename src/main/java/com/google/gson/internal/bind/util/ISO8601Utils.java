package com.google.gson.internal.bind.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;

public class ISO8601Utils {
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);
    private static final String UTC_ID = "UTC";

    public static String format(Date date) {
        return format(date, false, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean millis) {
        return format(date, millis, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean millis, TimeZone tz) {
        int length;
        Calendar calendar = new GregorianCalendar(tz, Locale.US);
        calendar.setTime(date);
        int capacity = "yyyy-MM-ddThh:mm:ss".length() + (millis ? ".sss".length() : 0);
        if (tz.getRawOffset() == 0) {
            length = "Z".length();
        } else {
            length = "+hh:mm".length();
        }
        StringBuilder formatted = new StringBuilder(capacity + length);
        padInt(formatted, calendar.get(1), "yyyy".length());
        formatted.append('-');
        padInt(formatted, calendar.get(2) + 1, "MM".length());
        formatted.append('-');
        padInt(formatted, calendar.get(5), "dd".length());
        formatted.append('T');
        padInt(formatted, calendar.get(11), "hh".length());
        formatted.append(':');
        padInt(formatted, calendar.get(12), "mm".length());
        formatted.append(':');
        padInt(formatted, calendar.get(13), "ss".length());
        if (millis) {
            formatted.append('.');
            padInt(formatted, calendar.get(14), "sss".length());
        }
        int offset = tz.getOffset(calendar.getTimeInMillis());
        if (offset != 0) {
            int hours = Math.abs((offset / 60000) / 60);
            int minutes = Math.abs((offset / 60000) % 60);
            formatted.append(offset < 0 ? '-' : '+');
            padInt(formatted, hours, "hh".length());
            formatted.append(':');
            padInt(formatted, minutes, "mm".length());
        } else {
            formatted.append((char) Matrix.MATRIX_TYPE_ZERO);
        }
        return formatted.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0151  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0326  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Date parse(java.lang.String r32, java.text.ParsePosition r33) throws java.text.ParseException {
        /*
            r11 = 0
            int r20 = r33.getIndex()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            int r21 = r20 + 4
            r0 = r32
            r1 = r20
            r2 = r21
            int r28 = parseInt(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 45
            r0 = r32
            r1 = r21
            r2 = r29
            boolean r29 = checkOffset(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r29 == 0) goto L_0x0023
            int r20 = r21 + 1
            r21 = r20
        L_0x0023:
            int r20 = r21 + 2
            r0 = r32
            r1 = r21
            r2 = r20
            int r18 = parseInt(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 45
            r0 = r32
            r1 = r20
            r2 = r29
            boolean r29 = checkOffset(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r29 == 0) goto L_0x034e
            int r20 = r20 + 1
            r21 = r20
        L_0x0041:
            int r20 = r21 + 2
            r0 = r32
            r1 = r21
            r2 = r20
            int r7 = parseInt(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r14 = 0
            r17 = 0
            r23 = 0
            r16 = 0
            r29 = 84
            r0 = r32
            r1 = r20
            r2 = r29
            boolean r13 = checkOffset(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r13 != 0) goto L_0x0083
            int r29 = r32.length()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r29
            r1 = r20
            if (r0 > r1) goto L_0x0083
            java.util.GregorianCalendar r5 = new java.util.GregorianCalendar     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            int r29 = r18 + -1
            r0 = r28
            r1 = r29
            r5.<init>(r0, r1, r7)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r33
            r1 = r20
            r0.setIndex(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.util.Date r29 = r5.getTime()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
        L_0x0082:
            return r29
        L_0x0083:
            if (r13 == 0) goto L_0x013a
            int r20 = r20 + 1
            int r21 = r20 + 2
            r0 = r32
            r1 = r20
            r2 = r21
            int r14 = parseInt(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 58
            r0 = r32
            r1 = r21
            r2 = r29
            boolean r29 = checkOffset(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r29 == 0) goto L_0x00a5
            int r20 = r21 + 1
            r21 = r20
        L_0x00a5:
            int r20 = r21 + 2
            r0 = r32
            r1 = r21
            r2 = r20
            int r17 = parseInt(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 58
            r0 = r32
            r1 = r20
            r2 = r29
            boolean r29 = checkOffset(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r29 == 0) goto L_0x034a
            int r20 = r20 + 1
            r21 = r20
        L_0x00c3:
            int r29 = r32.length()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r29
            r1 = r21
            if (r0 <= r1) goto L_0x0346
            r0 = r32
            r1 = r21
            char r4 = r0.charAt(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 90
            r0 = r29
            if (r4 == r0) goto L_0x0346
            r29 = 43
            r0 = r29
            if (r4 == r0) goto L_0x0346
            r29 = 45
            r0 = r29
            if (r4 == r0) goto L_0x0346
            int r20 = r21 + 2
            r0 = r32
            r1 = r21
            r2 = r20
            int r23 = parseInt(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 59
            r0 = r23
            r1 = r29
            if (r0 <= r1) goto L_0x0105
            r29 = 63
            r0 = r23
            r1 = r29
            if (r0 >= r1) goto L_0x0105
            r23 = 59
        L_0x0105:
            r29 = 46
            r0 = r32
            r1 = r20
            r2 = r29
            boolean r29 = checkOffset(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r29 == 0) goto L_0x013a
            int r20 = r20 + 1
            int r29 = r20 + 1
            r0 = r32
            r1 = r29
            int r9 = indexOfNonDigit(r0, r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            int r29 = r20 + 3
            r0 = r29
            int r22 = java.lang.Math.min(r9, r0)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r32
            r1 = r20
            r2 = r22
            int r12 = parseInt(r0, r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            int r29 = r22 - r20
            switch(r29) {
                case 1: goto L_0x01bb;
                case 2: goto L_0x01b7;
                default: goto L_0x0136;
            }     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
        L_0x0136:
            r16 = r12
        L_0x0138:
            r20 = r9
        L_0x013a:
            int r29 = r32.length()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r29
            r1 = r20
            if (r0 > r1) goto L_0x01bf
            java.lang.IllegalArgumentException r29 = new java.lang.IllegalArgumentException     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r30 = "No time zone indicator"
            r29.<init>(r30)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            throw r29     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
        L_0x014d:
            r8 = move-exception
            r11 = r8
        L_0x014f:
            if (r32 != 0) goto L_0x0326
            r15 = 0
        L_0x0152:
            java.lang.String r19 = r11.getMessage()
            if (r19 == 0) goto L_0x015e
            boolean r29 = r19.isEmpty()
            if (r29 == 0) goto L_0x0181
        L_0x015e:
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            r29.<init>()
            java.lang.String r30 = "("
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.Class r30 = r11.getClass()
            java.lang.String r30 = r30.getName()
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r30 = ")"
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r19 = r29.toString()
        L_0x0181:
            java.text.ParseException r10 = new java.text.ParseException
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            r29.<init>()
            java.lang.String r30 = "Failed to parse date ["
            java.lang.StringBuilder r29 = r29.append(r30)
            r0 = r29
            java.lang.StringBuilder r29 = r0.append(r15)
            java.lang.String r30 = "]: "
            java.lang.StringBuilder r29 = r29.append(r30)
            r0 = r29
            r1 = r19
            java.lang.StringBuilder r29 = r0.append(r1)
            java.lang.String r29 = r29.toString()
            int r30 = r33.getIndex()
            r0 = r29
            r1 = r30
            r10.<init>(r0, r1)
            r10.initCause(r11)
            throw r10
        L_0x01b7:
            int r16 = r12 * 10
            goto L_0x0138
        L_0x01bb:
            int r16 = r12 * 100
            goto L_0x0138
        L_0x01bf:
            r24 = 0
            r0 = r32
            r1 = r20
            char r26 = r0.charAt(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 90
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x022d
            java.util.TimeZone r24 = com.google.gson.internal.bind.util.ISO8601Utils.TIMEZONE_UTC     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            int r20 = r20 + 1
        L_0x01d5:
            java.util.GregorianCalendar r5 = new java.util.GregorianCalendar     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r24
            r5.<init>(r0)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 0
            r0 = r29
            r5.setLenient(r0)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 1
            r0 = r29
            r1 = r28
            r5.set(r0, r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 2
            int r30 = r18 + -1
            r0 = r29
            r1 = r30
            r5.set(r0, r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 5
            r0 = r29
            r5.set(r0, r7)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 11
            r0 = r29
            r5.set(r0, r14)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 12
            r0 = r29
            r1 = r17
            r5.set(r0, r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 13
            r0 = r29
            r1 = r23
            r5.set(r0, r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29 = 14
            r0 = r29
            r1 = r16
            r5.set(r0, r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r33
            r1 = r20
            r0.setIndex(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.util.Date r29 = r5.getTime()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            goto L_0x0082
        L_0x022d:
            r29 = 43
            r0 = r26
            r1 = r29
            if (r0 == r1) goto L_0x023d
            r29 = 45
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x02fd
        L_0x023d:
            r0 = r32
            r1 = r20
            java.lang.String r27 = r0.substring(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            int r29 = r27.length()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r30 = 5
            r0 = r29
            r1 = r30
            if (r0 < r1) goto L_0x0275
        L_0x0251:
            int r29 = r27.length()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            int r20 = r20 + r29
            java.lang.String r29 = "+0000"
            r0 = r29
            r1 = r27
            boolean r29 = r0.equals(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r29 != 0) goto L_0x0271
            java.lang.String r29 = "+00:00"
            r0 = r29
            r1 = r27
            boolean r29 = r0.equals(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r29 == 0) goto L_0x028e
        L_0x0271:
            java.util.TimeZone r24 = com.google.gson.internal.bind.util.ISO8601Utils.TIMEZONE_UTC     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            goto L_0x01d5
        L_0x0275:
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r29
            r1 = r27
            java.lang.StringBuilder r29 = r0.append(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r30 = "00"
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r27 = r29.toString()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            goto L_0x0251
        L_0x028e:
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r30 = "GMT"
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r29
            r1 = r27
            java.lang.StringBuilder r29 = r0.append(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r25 = r29.toString()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.util.TimeZone r24 = java.util.TimeZone.getTimeZone(r25)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r3 = r24.getID()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r25
            boolean r29 = r3.equals(r0)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r29 != 0) goto L_0x01d5
            java.lang.String r29 = ":"
            java.lang.String r30 = ""
            r0 = r29
            r1 = r30
            java.lang.String r6 = r3.replace(r0, r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r25
            boolean r29 = r6.equals(r0)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            if (r29 != 0) goto L_0x01d5
            java.lang.IndexOutOfBoundsException r29 = new java.lang.IndexOutOfBoundsException     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.StringBuilder r30 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r30.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r31 = "Mismatching time zone indicator: "
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r30
            r1 = r25
            java.lang.StringBuilder r30 = r0.append(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r31 = " given, resolves to "
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r31 = r24.getID()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r30 = r30.toString()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29.<init>(r30)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            throw r29     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
        L_0x02f9:
            r8 = move-exception
            r11 = r8
            goto L_0x014f
        L_0x02fd:
            java.lang.IndexOutOfBoundsException r29 = new java.lang.IndexOutOfBoundsException     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.StringBuilder r30 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r30.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r31 = "Invalid time zone indicator '"
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r0 = r30
            r1 = r26
            java.lang.StringBuilder r30 = r0.append(r1)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r31 = "'"
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            java.lang.String r30 = r30.toString()     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            r29.<init>(r30)     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
            throw r29     // Catch:{ IndexOutOfBoundsException -> 0x014d, NumberFormatException -> 0x02f9, IllegalArgumentException -> 0x0322 }
        L_0x0322:
            r8 = move-exception
            r11 = r8
            goto L_0x014f
        L_0x0326:
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            r29.<init>()
            r30 = 34
            java.lang.StringBuilder r29 = r29.append(r30)
            r0 = r29
            r1 = r32
            java.lang.StringBuilder r29 = r0.append(r1)
            java.lang.String r30 = "'"
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r15 = r29.toString()
            goto L_0x0152
        L_0x0346:
            r20 = r21
            goto L_0x013a
        L_0x034a:
            r21 = r20
            goto L_0x00c3
        L_0x034e:
            r21 = r20
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.util.ISO8601Utils.parse(java.lang.String, java.text.ParsePosition):java.util.Date");
    }

    private static boolean checkOffset(String value, int offset, char expected) {
        return offset < value.length() && value.charAt(offset) == expected;
    }

    private static int parseInt(String value, int beginIndex, int endIndex) throws NumberFormatException {
        int i;
        if (beginIndex < 0 || endIndex > value.length() || beginIndex > endIndex) {
            throw new NumberFormatException(value);
        }
        int i2 = beginIndex;
        int result = 0;
        if (i2 < endIndex) {
            i = i2 + 1;
            int digit = Character.digit(value.charAt(i2), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + value.substring(beginIndex, endIndex));
            }
            result = -digit;
        } else {
            i = i2;
        }
        while (i < endIndex) {
            int i3 = i + 1;
            int digit2 = Character.digit(value.charAt(i), 10);
            if (digit2 < 0) {
                throw new NumberFormatException("Invalid number: " + value.substring(beginIndex, endIndex));
            }
            result = (result * 10) - digit2;
            i = i3;
        }
        return -result;
    }

    private static void padInt(StringBuilder buffer, int value, int length) {
        String strValue = Integer.toString(value);
        for (int i = length - strValue.length(); i > 0; i--) {
            buffer.append('0');
        }
        buffer.append(strValue);
    }

    private static int indexOfNonDigit(String string, int offset) {
        int i = offset;
        while (i < string.length()) {
            char c = string.charAt(i);
            if (c < '0' || c > '9') {
                return i;
            }
            i++;
        }
        return string.length();
    }
}
