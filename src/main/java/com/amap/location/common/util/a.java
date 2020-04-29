package com.amap.location.common.util;

import android.text.TextUtils;
import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import javax.crypto.spec.IvParameterSpec;

/* compiled from: AESUtil */
public class a {
    public static final byte[] a = {0, 1, 1, 2, 3, 5, 8, Draft_75.CR, 8, 7, 6, 5, 4, 3, 2, 1};
    static char[] b = new char[66];
    static char[] c = new char[66];
    public static final IvParameterSpec d = new IvParameterSpec(a);

    static {
        "9O7M5 K3I1G:ZiXgVedcRQEu.CsrzyxwklP8N6L4J2H0jYhWfUTS,bavDtBAqponmF".getChars(0, "9O7M5 K3I1G:ZiXgVedcRQEu.CsrzyxwklP8N6L4J2H0jYhWfUTS,bavDtBAqponmF".length(), b, 0);
        "F0n2p4A6t8v1b3T5M7hY lEwRczrsC:ZijmHoJqLB,NDGaISK.UfWO9ukQxydeVgXP".getChars(0, "F0n2p4A6t8v1b3T5M7hY lEwRczrsC:ZijmHoJqLB,NDGaISK.UfWO9ukQxydeVgXP".length(), c, 0);
    }

    private static int a(char[] cArr, char c2) {
        for (int i = 0; i < cArr.length; i++) {
            if (c2 == cArr[i]) {
                return i;
            }
        }
        return -1;
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            int a2 = a(b, charAt);
            if (a2 >= 0) {
                sb.append(c[a2]);
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    public static String b(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            int a2 = a(c, charAt);
            if (a2 >= 0) {
                sb.append(b[a2]);
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }
}
