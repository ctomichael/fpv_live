package com.loc;

/* compiled from: StringUtils */
public final class dw {
    public static boolean a(String str) {
        return str == null || str.length() <= 0;
    }

    public static int b(String str) {
        int i = 0;
        if (str.length() <= 0) {
            return 0;
        }
        char[] charArray = str.toCharArray();
        int i2 = 0;
        while (true) {
            int i3 = i;
            if (i2 >= charArray.length) {
                return i3;
            }
            i = (i3 * 31) + charArray[i2];
            i2++;
        }
    }
}
