package com.loc;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

/* compiled from: ProxyUtil */
public final class ab {
    private static String a() {
        String str;
        try {
            str = Proxy.getDefaultHost();
        } catch (Throwable th) {
            aq.b(th, "pu", "gdh");
            str = null;
        }
        return str == null ? "null" : str;
    }

    public static java.net.Proxy a(Context context) {
        try {
            return Build.VERSION.SDK_INT >= 11 ? a(context, new URI("http://restapi.amap.com")) : b(context);
        } catch (Throwable th) {
            aq.b(th, "pu", "gp");
            return null;
        }
    }

    private static java.net.Proxy a(Context context, URI uri) {
        if (c(context)) {
            try {
                List<java.net.Proxy> select = ProxySelector.getDefault().select(uri);
                if (select == null || select.isEmpty()) {
                    return null;
                }
                java.net.Proxy proxy = select.get(0);
                if (proxy == null || proxy.type() == Proxy.Type.DIRECT) {
                    return null;
                }
                return proxy;
            } catch (Throwable th) {
                aq.b(th, "pu", "gpsc");
            }
        }
        return null;
    }

    private static int b() {
        try {
            return android.net.Proxy.getDefaultPort();
        } catch (Throwable th) {
            aq.b(th, "pu", "gdp");
            return -1;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x006b A[SYNTHETIC, Splitter:B:29:0x006b] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0071 A[SYNTHETIC, Splitter:B:33:0x0071] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x007c A[Catch:{ Throwable -> 0x014e }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00cc A[Catch:{ SecurityException -> 0x00b8, Throwable -> 0x0129, all -> 0x0143, all -> 0x018b }] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0100 A[SYNTHETIC, Splitter:B:75:0x0100] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x013c A[SYNTHETIC, Splitter:B:93:0x013c] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0147 A[SYNTHETIC, Splitter:B:99:0x0147] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.net.Proxy b(android.content.Context r11) {
        /*
            r6 = 80
            r10 = 0
            r9 = 1
            r7 = 0
            r8 = -1
            boolean r0 = c(r11)
            if (r0 == 0) goto L_0x015b
            java.lang.String r0 = "content://telephony/carriers/preferapn"
            android.net.Uri r1 = android.net.Uri.parse(r0)
            android.content.ContentResolver r0 = r11.getContentResolver()
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r2 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ SecurityException -> 0x00b8, Throwable -> 0x0129, all -> 0x0143 }
            if (r2 == 0) goto L_0x01d1
            boolean r0 = r2.moveToFirst()     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            if (r0 == 0) goto L_0x01d1
            java.lang.String r0 = "apn"
            int r0 = r2.getColumnIndex(r0)     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            java.lang.String r0 = r2.getString(r0)     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            if (r0 == 0) goto L_0x003a
            java.util.Locale r1 = java.util.Locale.US     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            java.lang.String r0 = r0.toLowerCase(r1)     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
        L_0x003a:
            if (r0 == 0) goto L_0x0088
            java.lang.String r1 = "ctwap"
            boolean r1 = r0.contains(r1)     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            if (r1 == 0) goto L_0x0088
            java.lang.String r4 = a()     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            int r0 = b()     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            boolean r1 = android.text.TextUtils.isEmpty(r4)     // Catch:{ SecurityException -> 0x01a6, Throwable -> 0x0192 }
            if (r1 != 0) goto L_0x01d8
            java.lang.String r1 = "null"
            boolean r1 = r4.equals(r1)     // Catch:{ SecurityException -> 0x01a6, Throwable -> 0x0192 }
            if (r1 != 0) goto L_0x01d8
            r1 = r9
        L_0x005d:
            if (r1 != 0) goto L_0x01d5
            java.lang.String r1 = "QMTAuMC4wLjIwMA=="
            java.lang.String r1 = com.loc.ad.c(r1)     // Catch:{ SecurityException -> 0x01ab, Throwable -> 0x0196 }
        L_0x0066:
            if (r0 != r8) goto L_0x0069
            r0 = r6
        L_0x0069:
            if (r2 == 0) goto L_0x006e
            r2.close()     // Catch:{ Throwable -> 0x017f }
        L_0x006e:
            r6 = r0
        L_0x006f:
            if (r1 == 0) goto L_0x014b
            int r0 = r1.length()     // Catch:{ Throwable -> 0x014e }
            if (r0 <= 0) goto L_0x014b
            if (r6 == r8) goto L_0x014b
            r0 = r9
        L_0x007a:
            if (r0 == 0) goto L_0x015b
            java.net.Proxy r0 = new java.net.Proxy     // Catch:{ Throwable -> 0x014e }
            java.net.Proxy$Type r2 = java.net.Proxy.Type.HTTP     // Catch:{ Throwable -> 0x014e }
            java.net.InetSocketAddress r1 = java.net.InetSocketAddress.createUnresolved(r1, r6)     // Catch:{ Throwable -> 0x014e }
            r0.<init>(r2, r1)     // Catch:{ Throwable -> 0x014e }
        L_0x0087:
            return r0
        L_0x0088:
            if (r0 == 0) goto L_0x01d1
            java.lang.String r1 = "wap"
            boolean r0 = r0.contains(r1)     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            if (r0 == 0) goto L_0x01d1
            java.lang.String r4 = a()     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            int r3 = b()     // Catch:{ SecurityException -> 0x01a0, Throwable -> 0x018d }
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ SecurityException -> 0x01af, Throwable -> 0x0199 }
            if (r0 != 0) goto L_0x01cd
            java.lang.String r0 = "null"
            boolean r0 = r4.equals(r0)     // Catch:{ SecurityException -> 0x01af, Throwable -> 0x0199 }
            if (r0 != 0) goto L_0x01cd
            r0 = r9
        L_0x00ab:
            if (r0 != 0) goto L_0x01ca
            java.lang.String r0 = "QMTAuMC4wLjE3Mg=="
            java.lang.String r1 = com.loc.ad.c(r0)     // Catch:{ SecurityException -> 0x01b4, Throwable -> 0x019d }
        L_0x00b4:
            if (r3 != r8) goto L_0x01c7
            r0 = r6
            goto L_0x0069
        L_0x00b8:
            r0 = move-exception
            r1 = r0
            r2 = r7
            r3 = r8
            r4 = r7
        L_0x00bd:
            java.lang.String r0 = "pu"
            java.lang.String r5 = "ghp"
            com.loc.aq.b(r1, r0, r5)     // Catch:{ all -> 0x018b }
            java.lang.String r0 = com.loc.x.s(r11)     // Catch:{ all -> 0x018b }
            if (r0 == 0) goto L_0x01bc
            java.util.Locale r1 = java.util.Locale.US     // Catch:{ all -> 0x018b }
            java.lang.String r1 = r0.toLowerCase(r1)     // Catch:{ all -> 0x018b }
            java.lang.String r0 = a()     // Catch:{ all -> 0x018b }
            int r3 = b()     // Catch:{ all -> 0x018b }
            java.lang.String r5 = "ctwap"
            int r5 = r1.indexOf(r5)     // Catch:{ all -> 0x018b }
            if (r5 == r8) goto L_0x0106
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x018b }
            if (r1 != 0) goto L_0x01c3
            java.lang.String r1 = "null"
            boolean r1 = r0.equals(r1)     // Catch:{ all -> 0x018b }
            if (r1 != 0) goto L_0x01c3
            r1 = r9
        L_0x00f3:
            if (r1 != 0) goto L_0x00fc
            java.lang.String r0 = "QMTAuMC4wLjIwMA=="
            java.lang.String r0 = com.loc.ad.c(r0)     // Catch:{ all -> 0x018b }
        L_0x00fc:
            if (r3 != r8) goto L_0x01c0
        L_0x00fe:
            if (r2 == 0) goto L_0x0103
            r2.close()     // Catch:{ Throwable -> 0x0169 }
        L_0x0103:
            r1 = r0
            goto L_0x006f
        L_0x0106:
            java.lang.String r5 = "wap"
            int r1 = r1.indexOf(r5)     // Catch:{ all -> 0x018b }
            if (r1 == r8) goto L_0x01bc
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x018b }
            if (r1 != 0) goto L_0x01b8
            java.lang.String r1 = "null"
            boolean r1 = r0.equals(r1)     // Catch:{ all -> 0x018b }
            if (r1 != 0) goto L_0x01b8
            r1 = r9
        L_0x011f:
            if (r1 != 0) goto L_0x00fe
            java.lang.String r0 = "QMTAuMC4wLjE3Mg=="
            java.lang.String r0 = com.loc.ad.c(r0)     // Catch:{ all -> 0x018b }
            goto L_0x00fe
        L_0x0129:
            r0 = move-exception
            r1 = r0
            r2 = r7
            r3 = r8
            r4 = r7
        L_0x012e:
            java.lang.String r0 = "pu"
            java.lang.String r5 = "gPx1"
            com.loc.aq.b(r1, r0, r5)     // Catch:{ all -> 0x018b }
            r1.printStackTrace()     // Catch:{ all -> 0x018b }
            if (r2 == 0) goto L_0x013f
            r2.close()     // Catch:{ Throwable -> 0x0174 }
        L_0x013f:
            r6 = r3
            r1 = r4
            goto L_0x006f
        L_0x0143:
            r0 = move-exception
            r2 = r7
        L_0x0145:
            if (r2 == 0) goto L_0x014a
            r2.close()     // Catch:{ Throwable -> 0x015e }
        L_0x014a:
            throw r0
        L_0x014b:
            r0 = r10
            goto L_0x007a
        L_0x014e:
            r0 = move-exception
            java.lang.String r1 = "pu"
            java.lang.String r2 = "gp2"
            com.loc.an.a(r0, r1, r2)
            r0.printStackTrace()
        L_0x015b:
            r0 = r7
            goto L_0x0087
        L_0x015e:
            r1 = move-exception
            java.lang.String r2 = "pu"
            java.lang.String r3 = "gPx2"
            com.loc.aq.b(r1, r2, r3)
            goto L_0x014a
        L_0x0169:
            r1 = move-exception
            java.lang.String r2 = "pu"
            java.lang.String r3 = "gPx2"
            com.loc.aq.b(r1, r2, r3)
            goto L_0x0103
        L_0x0174:
            r0 = move-exception
            java.lang.String r1 = "pu"
            java.lang.String r2 = "gPx2"
            com.loc.aq.b(r0, r1, r2)
            goto L_0x013f
        L_0x017f:
            r2 = move-exception
            java.lang.String r3 = "pu"
            java.lang.String r4 = "gPx2"
            com.loc.aq.b(r2, r3, r4)
            goto L_0x006e
        L_0x018b:
            r0 = move-exception
            goto L_0x0145
        L_0x018d:
            r0 = move-exception
            r1 = r0
            r3 = r8
            r4 = r7
            goto L_0x012e
        L_0x0192:
            r1 = move-exception
            r3 = r0
            r4 = r7
            goto L_0x012e
        L_0x0196:
            r1 = move-exception
            r3 = r0
            goto L_0x012e
        L_0x0199:
            r0 = move-exception
            r1 = r0
            r4 = r7
            goto L_0x012e
        L_0x019d:
            r0 = move-exception
            r1 = r0
            goto L_0x012e
        L_0x01a0:
            r0 = move-exception
            r1 = r0
            r3 = r8
            r4 = r7
            goto L_0x00bd
        L_0x01a6:
            r1 = move-exception
            r3 = r0
            r4 = r7
            goto L_0x00bd
        L_0x01ab:
            r1 = move-exception
            r3 = r0
            goto L_0x00bd
        L_0x01af:
            r0 = move-exception
            r1 = r0
            r4 = r7
            goto L_0x00bd
        L_0x01b4:
            r0 = move-exception
            r1 = r0
            goto L_0x00bd
        L_0x01b8:
            r1 = r10
            r0 = r4
            goto L_0x011f
        L_0x01bc:
            r6 = r3
            r0 = r4
            goto L_0x00fe
        L_0x01c0:
            r6 = r3
            goto L_0x00fe
        L_0x01c3:
            r1 = r10
            r0 = r4
            goto L_0x00f3
        L_0x01c7:
            r0 = r3
            goto L_0x0069
        L_0x01ca:
            r1 = r4
            goto L_0x00b4
        L_0x01cd:
            r0 = r10
            r4 = r7
            goto L_0x00ab
        L_0x01d1:
            r0 = r8
            r1 = r7
            goto L_0x0069
        L_0x01d5:
            r1 = r4
            goto L_0x0066
        L_0x01d8:
            r1 = r10
            r4 = r7
            goto L_0x005d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ab.b(android.content.Context):java.net.Proxy");
    }

    private static boolean c(Context context) {
        return x.q(context) == 0;
    }
}
