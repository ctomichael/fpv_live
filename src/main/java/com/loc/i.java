package com.loc;

import com.dji.api.protocol.IAccountCenterHttpApi;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: SecurityUtil */
public final class i {
    private static byte[] a = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static int[] b = {-1835101520, 1886468213, 614966444, 1545358877, 153741611, 1460081543, -1035306422, -708721439};

    private static int a(int i, int i2) {
        int i3 = 0;
        int i4 = 0;
        while (i3 < i2) {
            i3++;
            i4 = (i4 >> 1) | Integer.MIN_VALUE;
        }
        return ((i & i4) >>> (32 - i2)) | (i << i2);
    }

    public static String a() {
        SecureRandom secureRandom = new SecureRandom();
        try {
            KeyGenerator instance = KeyGenerator.getInstance("AES");
            instance.init(128, secureRandom);
            return f.a(instance.generateKey().getEncoded());
        } catch (Throwable th) {
            return null;
        }
    }

    private static String a(int i) {
        char[] cArr = new char[4];
        for (int i2 = 0; i2 < 4; i2++) {
            cArr[(4 - i2) - 1] = (char) ((i >>> (i2 * 8)) & 255);
            char c = cArr[(4 - i2) - 1];
            String str = " ";
            for (int i3 = 0; i3 < 32; i3++) {
                str = str + (((Integer.MIN_VALUE >>> i3) & c) >>> (31 - i3));
            }
        }
        return new String(cArr);
    }

    public static String a(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            char[] charArray = str.toCharArray();
            byte[] bArr = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                bArr[i] = (byte) charArray[i];
            }
            byte[] digest = instance.digest(bArr);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : digest) {
                byte b3 = b2 & 255;
                if (b3 < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(b3));
            }
            return stringBuffer.toString();
        } catch (Throwable th) {
            return "";
        }
    }

    private static String a(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        if (iArr != null) {
            for (int i = 0; i < iArr.length; i++) {
                sb.append(a(a(b(iArr[i]), i)));
            }
        }
        return sb.toString();
    }

    private static byte[] a(byte[] bArr) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(a);
            SecretKeySpec secretKeySpec = new SecretKeySpec(a(b).getBytes("UTF-8"), "AES");
            Cipher instance = Cipher.getInstance(IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION);
            instance.init(1, secretKeySpec, ivParameterSpec);
            return instance.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int b(int i) {
        int i2 = 1;
        for (int i3 = 0; i3 < 15; i3++) {
            i2 = (i2 << 2) | 1;
        }
        return (((i2 << 1) & i) >>> 1) | ((i2 & i) << 1);
    }

    public static String b(String str) {
        try {
            return f.a(a(str.getBytes("UTF-8")));
        } catch (Throwable th) {
            return null;
        }
    }

    private static byte[] b(byte[] bArr) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(a);
            SecretKeySpec secretKeySpec = new SecretKeySpec(a(b).getBytes("UTF-8"), "AES");
            Cipher instance = Cipher.getInstance(IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION);
            instance.init(2, secretKeySpec, ivParameterSpec);
            return instance.doFinal(bArr);
        } catch (Throwable th) {
            return null;
        }
    }

    public static String c(String str) {
        try {
            return new String(b(f.a(str)), "UTF-8");
        } catch (Throwable th) {
            return null;
        }
    }
}
