package com.loc;

import android.content.Context;
import java.lang.ref.WeakReference;

/* compiled from: Utils */
public final class bs {
    public static bl a(WeakReference<bl> weakReference) {
        if (weakReference == null || weakReference.get() == null) {
            weakReference = new WeakReference<>(new bl());
        }
        return weakReference.get();
    }

    public static String a(Context context, ac acVar) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"sim\":\"").append(x.f(context)).append("\",\"sdkversion\":\"").append(acVar.c()).append("\",\"product\":\"").append(acVar.a()).append("\",\"ed\":\"").append(acVar.d()).append("\",\"nt\":\"").append(x.d(context)).append("\",\"np\":\"").append(x.b(context)).append("\",\"mnc\":\"").append(x.c(context)).append("\",\"ant\":\"").append(x.e(context)).append("\"");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    public static void a(Context context, bl blVar, String str, int i, int i2, String str2) {
        blVar.a = ao.c(context, str);
        blVar.d = i;
        blVar.b = (long) i2;
        blVar.c = str2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0060 A[SYNTHETIC, Splitter:B:46:0x0060] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0065 A[SYNTHETIC, Splitter:B:49:0x0065] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static byte[] a(com.loc.bc r6, java.lang.String r7) {
        /*
            r3 = 0
            r1 = 0
            r0 = 0
            byte[] r0 = new byte[r0]
            com.loc.bc$b r4 = r6.a(r7)     // Catch:{ Throwable -> 0x0042, all -> 0x005b }
            if (r4 != 0) goto L_0x0016
            if (r3 == 0) goto L_0x0010
            r1.close()     // Catch:{ Throwable -> 0x0078 }
        L_0x0010:
            if (r4 == 0) goto L_0x0015
            r4.close()     // Catch:{ Throwable -> 0x007d }
        L_0x0015:
            return r0
        L_0x0016:
            java.io.InputStream r2 = r4.a()     // Catch:{ Throwable -> 0x008e, all -> 0x0089 }
            if (r2 != 0) goto L_0x002c
            if (r2 == 0) goto L_0x0021
            r2.close()     // Catch:{ Throwable -> 0x007f }
        L_0x0021:
            if (r4 == 0) goto L_0x0015
            r4.close()     // Catch:{ Throwable -> 0x0027 }
            goto L_0x0015
        L_0x0027:
            r1 = move-exception
        L_0x0028:
            r1.printStackTrace()
            goto L_0x0015
        L_0x002c:
            int r1 = r2.available()     // Catch:{ Throwable -> 0x0091 }
            byte[] r0 = new byte[r1]     // Catch:{ Throwable -> 0x0091 }
            r2.read(r0)     // Catch:{ Throwable -> 0x0091 }
            if (r2 == 0) goto L_0x003a
            r2.close()     // Catch:{ Throwable -> 0x0084 }
        L_0x003a:
            if (r4 == 0) goto L_0x0015
            r4.close()     // Catch:{ Throwable -> 0x0040 }
            goto L_0x0015
        L_0x0040:
            r1 = move-exception
            goto L_0x0028
        L_0x0042:
            r1 = move-exception
            r2 = r3
            r4 = r3
        L_0x0045:
            java.lang.String r3 = "sui"
            java.lang.String r5 = "rdS"
            com.loc.aq.b(r1, r3, r5)     // Catch:{ all -> 0x008c }
            if (r2 == 0) goto L_0x0053
            r2.close()     // Catch:{ Throwable -> 0x0073 }
        L_0x0053:
            if (r4 == 0) goto L_0x0015
            r4.close()     // Catch:{ Throwable -> 0x0059 }
            goto L_0x0015
        L_0x0059:
            r1 = move-exception
            goto L_0x0028
        L_0x005b:
            r0 = move-exception
            r2 = r3
            r4 = r3
        L_0x005e:
            if (r2 == 0) goto L_0x0063
            r2.close()     // Catch:{ Throwable -> 0x0069 }
        L_0x0063:
            if (r4 == 0) goto L_0x0068
            r4.close()     // Catch:{ Throwable -> 0x006e }
        L_0x0068:
            throw r0
        L_0x0069:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0063
        L_0x006e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0068
        L_0x0073:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0053
        L_0x0078:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0010
        L_0x007d:
            r1 = move-exception
            goto L_0x0028
        L_0x007f:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0021
        L_0x0084:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x003a
        L_0x0089:
            r0 = move-exception
            r2 = r3
            goto L_0x005e
        L_0x008c:
            r0 = move-exception
            goto L_0x005e
        L_0x008e:
            r1 = move-exception
            r2 = r3
            goto L_0x0045
        L_0x0091:
            r1 = move-exception
            goto L_0x0045
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bs.a(com.loc.bc, java.lang.String):byte[]");
    }
}
