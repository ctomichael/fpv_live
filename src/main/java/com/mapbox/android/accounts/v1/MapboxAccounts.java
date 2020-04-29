package com.mapbox.android.accounts.v1;

import android.support.annotation.Keep;
import java.security.SecureRandom;
import java.util.UUID;

public class MapboxAccounts {
    @Keep
    public static final String SKU_ID_MAPS_MAUS = "00";
    @Keep
    public static final String SKU_ID_NAVIGATION_MAUS = "02";
    @Keep
    public static final String SKU_ID_NAVIGATION_TRIPS = "03";

    private static String a(long j) {
        String l = Long.toString(j, 36);
        if (l.length() > 8) {
            return l.substring(l.length() - 8);
        }
        if (l.length() >= 8) {
            return l;
        }
        return String.format("%1$8s", l).replace(" ", "0");
    }

    private static String a(CharSequence charSequence, Object[] objArr) {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Object obj : objArr) {
            if (z) {
                z = false;
            } else {
                sb.append(charSequence);
            }
            sb.append(obj);
        }
        return sb.toString();
    }

    @Keep
    public static long getNow() {
        return System.currentTimeMillis();
    }

    @Keep
    public static String obtainEndUserId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Keep
    public static String obtainMapsSkuUserToken(String str) {
        return a("", new String[]{"1", "00", a(getNow()), str});
    }

    @Keep
    public static String obtainNavigationSkuSessionToken() {
        String[] strArr = new String[3];
        strArr[0] = "1";
        strArr[1] = SKU_ID_NAVIGATION_TRIPS;
        char[] charArray = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] cArr = new char[10];
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 10; i++) {
            cArr[i] = charArray[secureRandom.nextInt(charArray.length)];
        }
        strArr[2] = new String(cArr);
        return a("", strArr);
    }

    @Keep
    public static String obtainNavigationSkuUserToken(String str) {
        return a("", new String[]{"1", "02", a(getNow()), str});
    }
}
