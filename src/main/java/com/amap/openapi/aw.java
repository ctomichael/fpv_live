package com.amap.openapi;

import android.util.Base64;
import com.amap.location.security.Core;

/* compiled from: AESUtil */
public class aw {
    private static byte[] a() {
        String[] split = new StringBuffer("16,16,18,77,15,911,121,77,121,911,38,77,911,99,86,67,611,96,48,77,84,911,38,67,021,301,86,67,611,98,48,77,511,77,48,97,511,58,48,97,511,84,501,87,511,96,48,77,221,911,38,77,121,37,86,67,25,301,86,67,021,96,86,67,021,701,86,67,35,56,86,67,611,37,221,87").reverse().toString().split(",");
        byte[] bArr = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            bArr[i] = Byte.parseByte(split[i]);
        }
        String[] split2 = new StringBuffer(new String(Base64.decode(bArr, 2))).reverse().toString().split(",");
        byte[] bArr2 = new byte[split2.length];
        for (int i2 = 0; i2 < split2.length; i2++) {
            bArr2[i2] = Byte.parseByte(split2[i2]);
        }
        return bArr2;
    }

    public static byte[] a(String str) throws Exception {
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        return b(stringBuffer.toString());
    }

    public static byte[] a(String str, byte[] bArr) throws Exception {
        return Core.cole(bArr, a(str), a());
    }

    /* JADX INFO: additional move instructions added (1) to help type inference */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] b(@android.support.annotation.NonNull java.lang.String r6) throws java.security.NoSuchAlgorithmException {
        /*
            r5 = 16
            java.lang.String r0 = "SHA1"
            java.security.MessageDigest r0 = java.security.MessageDigest.getInstance(r0)
            byte[] r1 = r6.getBytes()
            r0.update(r1)
            byte[] r2 = r0.digest()
            java.lang.StringBuffer r3 = new java.lang.StringBuffer
            java.lang.String r0 = ""
            r3.<init>(r0)
            r0 = 0
        L_0x001d:
            int r1 = r2.length
            if (r0 >= r1) goto L_0x0038
            byte r1 = r2[r0]
            if (r1 >= 0) goto L_0x0026
            int r1 = r1 + 256
        L_0x0026:
            if (r1 >= r5) goto L_0x002e
            java.lang.String r4 = "0"
            r3.append(r4)
        L_0x002e:
            java.lang.String r1 = java.lang.Integer.toHexString(r1)
            r3.append(r1)
            int r0 = r0 + 1
            goto L_0x001d
        L_0x0038:
            int r0 = r3.length()
            if (r0 <= r5) goto L_0x0041
            r3.setLength(r5)
        L_0x0041:
            java.lang.String r0 = r3.toString()
            byte[] r0 = r0.getBytes()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.aw.b(java.lang.String):byte[]");
    }
}
