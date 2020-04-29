package com.dji.analytics.f;

import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: HashUtil */
public class c {
    public static String a(byte[] bArr, String str) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes("UTF-8"), "HmacMD5");
            Mac instance = Mac.getInstance("HmacMD5");
            instance.init(secretKeySpec);
            return a(instance.doFinal(bArr));
        } catch (Exception e) {
            return null;
        }
    }

    public static String a(byte[] bArr) {
        int i;
        byte b;
        if (bArr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b2 : bArr) {
            byte b3 = b2 & 255;
            int i2 = b3 >>> 4;
            byte b4 = b3 & 15;
            if (i2 < 10) {
                i = 48;
            } else {
                i = 87;
            }
            stringBuffer.append((char) (i + i2));
            if (b4 < 10) {
                b = 48;
            } else {
                b = 87;
            }
            stringBuffer.append((char) (b + b4));
        }
        return stringBuffer.toString();
    }

    public static String a() {
        return UUID.randomUUID().toString().replace("-", "d");
    }
}
