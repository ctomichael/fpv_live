package com.loc;

import android.content.Context;
import android.text.TextUtils;

/* compiled from: StatisticsEntity */
public final class bq {
    private Context a;
    private String b;
    private String c;
    private String d;
    private String e;

    public bq(Context context, String str, String str2, String str3) throws t {
        if (TextUtils.isEmpty(str3) || str3.length() > 256) {
            throw new t("无效的参数 - IllegalArgumentException");
        }
        this.a = context.getApplicationContext();
        this.c = str;
        this.d = str2;
        this.b = str3;
    }

    public final void a(String str) throws t {
        if (TextUtils.isEmpty(str) || str.length() > 65536) {
            throw new t("无效的参数 - IllegalArgumentException");
        }
        this.e = str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a1 A[SYNTHETIC, Splitter:B:28:0x00a1] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00ae A[SYNTHETIC, Splitter:B:35:0x00ae] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final byte[] a() {
        /*
            r8 = this;
            r1 = 0
            r3 = 0
            byte[] r0 = new byte[r1]
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x00bb, all -> 0x00aa }
            r2.<init>()     // Catch:{ Throwable -> 0x00bb, all -> 0x00aa }
            java.lang.String r3 = r8.c     // Catch:{ Throwable -> 0x0095 }
            com.loc.ad.a(r2, r3)     // Catch:{ Throwable -> 0x0095 }
            java.lang.String r3 = r8.d     // Catch:{ Throwable -> 0x0095 }
            com.loc.ad.a(r2, r3)     // Catch:{ Throwable -> 0x0095 }
            java.lang.String r3 = r8.b     // Catch:{ Throwable -> 0x0095 }
            com.loc.ad.a(r2, r3)     // Catch:{ Throwable -> 0x0095 }
            android.content.Context r3 = r8.a     // Catch:{ Throwable -> 0x0095 }
            int r3 = com.loc.x.q(r3)     // Catch:{ Throwable -> 0x0095 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Throwable -> 0x0095 }
            com.loc.ad.a(r2, r3)     // Catch:{ Throwable -> 0x0095 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00be }
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            int r1 = (int) r4
        L_0x002d:
            r3 = 4
            byte[] r3 = new byte[r3]     // Catch:{ Throwable -> 0x0095 }
            r4 = 0
            int r5 = r1 >> 24
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x0095 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0095 }
            r4 = 1
            int r5 = r1 >> 16
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x0095 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0095 }
            r4 = 2
            int r5 = r1 >> 8
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x0095 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0095 }
            r4 = 3
            r1 = r1 & 255(0xff, float:3.57E-43)
            byte r1 = (byte) r1     // Catch:{ Throwable -> 0x0095 }
            r3[r4] = r1     // Catch:{ Throwable -> 0x0095 }
            r2.write(r3)     // Catch:{ Throwable -> 0x0095 }
            java.lang.String r1 = r8.e     // Catch:{ Throwable -> 0x0095 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0095 }
            if (r1 == 0) goto L_0x0075
            r1 = 2
            byte[] r1 = new byte[r1]     // Catch:{ Throwable -> 0x0095 }
            r1 = {0, 0} // fill-array     // Catch:{ Throwable -> 0x0095 }
        L_0x005f:
            r2.write(r1)     // Catch:{ Throwable -> 0x0095 }
            java.lang.String r1 = r8.e     // Catch:{ Throwable -> 0x0095 }
            byte[] r1 = com.loc.ad.a(r1)     // Catch:{ Throwable -> 0x0095 }
            r2.write(r1)     // Catch:{ Throwable -> 0x0095 }
            byte[] r0 = r2.toByteArray()     // Catch:{ Throwable -> 0x0095 }
            if (r2 == 0) goto L_0x0074
            r2.close()     // Catch:{ Throwable -> 0x00b7 }
        L_0x0074:
            return r0
        L_0x0075:
            java.lang.String r1 = r8.e     // Catch:{ Throwable -> 0x0095 }
            byte[] r1 = com.loc.ad.a(r1)     // Catch:{ Throwable -> 0x0095 }
            if (r1 != 0) goto L_0x0084
            r1 = 2
            byte[] r1 = new byte[r1]     // Catch:{ Throwable -> 0x0095 }
            r1 = {0, 0} // fill-array     // Catch:{ Throwable -> 0x0095 }
            goto L_0x005f
        L_0x0084:
            int r1 = r1.length     // Catch:{ Throwable -> 0x0095 }
            int r3 = r1 / 256
            byte r3 = (byte) r3     // Catch:{ Throwable -> 0x0095 }
            int r1 = r1 % 256
            byte r4 = (byte) r1     // Catch:{ Throwable -> 0x0095 }
            r1 = 2
            byte[] r1 = new byte[r1]     // Catch:{ Throwable -> 0x0095 }
            r5 = 0
            r1[r5] = r3     // Catch:{ Throwable -> 0x0095 }
            r3 = 1
            r1[r3] = r4     // Catch:{ Throwable -> 0x0095 }
            goto L_0x005f
        L_0x0095:
            r1 = move-exception
        L_0x0096:
            java.lang.String r3 = "se"
            java.lang.String r4 = "tds"
            com.loc.aq.b(r1, r3, r4)     // Catch:{ all -> 0x00b9 }
            if (r2 == 0) goto L_0x0074
            r2.close()     // Catch:{ Throwable -> 0x00a5 }
            goto L_0x0074
        L_0x00a5:
            r1 = move-exception
        L_0x00a6:
            r1.printStackTrace()
            goto L_0x0074
        L_0x00aa:
            r0 = move-exception
            r2 = r3
        L_0x00ac:
            if (r2 == 0) goto L_0x00b1
            r2.close()     // Catch:{ Throwable -> 0x00b2 }
        L_0x00b1:
            throw r0
        L_0x00b2:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00b1
        L_0x00b7:
            r1 = move-exception
            goto L_0x00a6
        L_0x00b9:
            r0 = move-exception
            goto L_0x00ac
        L_0x00bb:
            r1 = move-exception
            r2 = r3
            goto L_0x0096
        L_0x00be:
            r3 = move-exception
            goto L_0x002d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bq.a():byte[]");
    }
}
