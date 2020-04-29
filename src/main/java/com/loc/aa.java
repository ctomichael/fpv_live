package com.loc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* compiled from: MD5 */
public final class aa {
    /* JADX WARNING: Removed duplicated region for block: B:45:0x007e A[SYNTHETIC, Splitter:B:45:0x007e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(java.lang.String r6) {
        /*
            r0 = 0
            r1 = 0
            boolean r2 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x0098, all -> 0x007a }
            if (r2 == 0) goto L_0x000e
            if (r0 == 0) goto L_0x000d
            r1.close()     // Catch:{ IOException -> 0x008d }
        L_0x000d:
            return r0
        L_0x000e:
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0098, all -> 0x007a }
            r3.<init>(r6)     // Catch:{ Throwable -> 0x0098, all -> 0x007a }
            boolean r2 = r3.isFile()     // Catch:{ Throwable -> 0x0098, all -> 0x007a }
            if (r2 == 0) goto L_0x001f
            boolean r2 = r3.exists()     // Catch:{ Throwable -> 0x0098, all -> 0x007a }
            if (r2 != 0) goto L_0x0030
        L_0x001f:
            if (r0 == 0) goto L_0x000d
            r1.close()     // Catch:{ IOException -> 0x0025 }
            goto L_0x000d
        L_0x0025:
            r1 = move-exception
            java.lang.String r2 = "MD5"
            java.lang.String r3 = "gfm"
        L_0x002c:
            com.loc.an.a(r1, r2, r3)
            goto L_0x000d
        L_0x0030:
            r1 = 2048(0x800, float:2.87E-42)
            byte[] r1 = new byte[r1]     // Catch:{ Throwable -> 0x0098, all -> 0x007a }
            java.lang.String r2 = "MD5"
            java.security.MessageDigest r4 = java.security.MessageDigest.getInstance(r2)     // Catch:{ Throwable -> 0x0098, all -> 0x007a }
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0098, all -> 0x007a }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x0098, all -> 0x007a }
        L_0x0040:
            int r3 = r2.read(r1)     // Catch:{ Throwable -> 0x004c }
            r5 = -1
            if (r3 == r5) goto L_0x0064
            r5 = 0
            r4.update(r1, r5, r3)     // Catch:{ Throwable -> 0x004c }
            goto L_0x0040
        L_0x004c:
            r1 = move-exception
        L_0x004d:
            java.lang.String r3 = "MD5"
            java.lang.String r4 = "gfm"
            com.loc.an.a(r1, r3, r4)     // Catch:{ all -> 0x0095 }
            if (r2 == 0) goto L_0x000d
            r2.close()     // Catch:{ IOException -> 0x005c }
            goto L_0x000d
        L_0x005c:
            r1 = move-exception
            java.lang.String r2 = "MD5"
            java.lang.String r3 = "gfm"
            goto L_0x002c
        L_0x0064:
            byte[] r1 = r4.digest()     // Catch:{ Throwable -> 0x004c }
            java.lang.String r0 = com.loc.ad.e(r1)     // Catch:{ Throwable -> 0x004c }
            if (r2 == 0) goto L_0x000d
            r2.close()     // Catch:{ IOException -> 0x0072 }
            goto L_0x000d
        L_0x0072:
            r1 = move-exception
            java.lang.String r2 = "MD5"
            java.lang.String r3 = "gfm"
            goto L_0x002c
        L_0x007a:
            r1 = move-exception
            r2 = r0
        L_0x007c:
            if (r2 == 0) goto L_0x0081
            r2.close()     // Catch:{ IOException -> 0x0082 }
        L_0x0081:
            throw r1
        L_0x0082:
            r0 = move-exception
            java.lang.String r2 = "MD5"
            java.lang.String r3 = "gfm"
            com.loc.an.a(r0, r2, r3)
            goto L_0x0081
        L_0x008d:
            r1 = move-exception
            java.lang.String r2 = "MD5"
            java.lang.String r3 = "gfm"
            goto L_0x002c
        L_0x0095:
            r0 = move-exception
            r1 = r0
            goto L_0x007c
        L_0x0098:
            r1 = move-exception
            r2 = r0
            goto L_0x004d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.aa.a(java.lang.String):java.lang.String");
    }

    public static String a(byte[] bArr) {
        return ad.e(a(bArr, "MD5"));
    }

    public static byte[] a(byte[] bArr, String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(str);
            instance.update(bArr);
            return instance.digest();
        } catch (Throwable th) {
            an.a(th, "MD5", "gmb");
            return null;
        }
    }

    public static String b(String str) {
        if (str == null) {
            return null;
        }
        return ad.e(d(str));
    }

    public static String c(String str) {
        return ad.f(e(str));
    }

    private static byte[] d(String str) {
        try {
            return f(str);
        } catch (Throwable th) {
            an.a(th, "MD5", "gmb");
            return new byte[0];
        }
    }

    private static byte[] e(String str) {
        try {
            return f(str);
        } catch (Throwable th) {
            th.printStackTrace();
            return new byte[0];
        }
    }

    private static byte[] f(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(ad.a(str));
        return instance.digest();
    }
}
