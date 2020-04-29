package com.loc;

import android.content.Context;
import java.util.concurrent.Callable;

final class dq implements Callable {
    private static dg c = dg.a();
    /* access modifiers changed from: private */
    public static Context d;
    private String a;
    private int b = 1;

    dq(String str) {
        this.a = str;
    }

    static void a(Context context) {
        d = context;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00c1 A[Catch:{ all -> 0x0178 }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x013f A[SYNTHETIC, Splitter:B:60:0x013f] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0144 A[Catch:{ IOException -> 0x0164 }] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x014d  */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String[] call() {
        /*
            r6 = this;
            r3 = 0
            boolean r0 = com.loc.dl.a()     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            if (r0 != 0) goto L_0x001c
            java.lang.Thread r0 = new java.lang.Thread     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            com.loc.dr r1 = new com.loc.dr     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            r1.<init>(r6)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            r0.<init>(r1)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            com.loc.dm r1 = new com.loc.dm     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            r1.<init>()     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            r0.setUncaughtExceptionHandler(r1)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            r0.start()     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
        L_0x001c:
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            r1 = 14
            if (r0 < r1) goto L_0x0028
            r0 = 40965(0xa005, float:5.7404E-41)
            android.net.TrafficStats.setThreadStatsTag(r0)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
        L_0x0028:
            java.lang.String r0 = r6.a     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            com.loc.dg.c(r0)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.lang.String r1 = "http://203.107.1.1:80/"
            r0.<init>(r1)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.lang.String r1 = com.loc.dj.a     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.lang.String r1 = "/d?host="
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.lang.String r1 = r6.a     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.net.URL r1 = new java.net.URL     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            r1.<init>(r0)     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.net.URLConnection r0 = r1.openConnection()     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x017b, all -> 0x0169 }
            r1 = 15000(0x3a98, float:2.102E-41)
            r0.setConnectTimeout(r1)     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            r1 = 15000(0x3a98, float:2.102E-41)
            r0.setReadTimeout(r1)     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            int r1 = r0.getResponseCode()     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            r2 = 200(0xc8, float:2.8E-43)
            if (r1 == r2) goto L_0x0094
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            java.lang.String r2 = "response code is "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            int r2 = r0.getResponseCode()     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            java.lang.String r2 = " expect 200"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            com.loc.dk.b(r1)     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            if (r0 == 0) goto L_0x008c
            r0.disconnect()
        L_0x008c:
            java.lang.String r0 = r6.a
            com.loc.dg.d(r0)
            java.lang.String[] r0 = com.loc.dj.b
        L_0x0093:
            return r0
        L_0x0094:
            java.io.InputStream r4 = r0.getInputStream()     // Catch:{ Throwable -> 0x0182, all -> 0x016f }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x0188, all -> 0x0174 }
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x0188, all -> 0x0174 }
            java.lang.String r5 = "UTF-8"
            r1.<init>(r4, r5)     // Catch:{ Throwable -> 0x0188, all -> 0x0174 }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x0188, all -> 0x0174 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            r1.<init>()     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
        L_0x00aa:
            java.lang.String r3 = r2.readLine()     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            if (r3 == 0) goto L_0x00da
            r1.append(r3)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            goto L_0x00aa
        L_0x00b4:
            r1 = move-exception
            r5 = r0
        L_0x00b6:
            com.loc.dk.a(r1)     // Catch:{ all -> 0x0178 }
            int r0 = r6.b     // Catch:{ all -> 0x0178 }
            int r1 = r0 + -1
            r6.b = r1     // Catch:{ all -> 0x0178 }
            if (r0 <= 0) goto L_0x014d
            java.lang.String[] r0 = r6.call()     // Catch:{ all -> 0x0178 }
            if (r5 == 0) goto L_0x00ca
            r5.disconnect()
        L_0x00ca:
            if (r4 == 0) goto L_0x00cf
            r4.close()     // Catch:{ IOException -> 0x00d5 }
        L_0x00cf:
            if (r2 == 0) goto L_0x0093
            r2.close()     // Catch:{ IOException -> 0x00d5 }
            goto L_0x0093
        L_0x00d5:
            r1 = move-exception
            com.loc.dk.a(r1)
            goto L_0x0093
        L_0x00da:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.String r5 = "resolve host: "
            r3.<init>(r5)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.String r5 = r6.a     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.String r5 = ", return: "
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.String r5 = r1.toString()     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            com.loc.dk.a(r3)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            com.loc.dh r3 = new com.loc.dh     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            r3.<init>(r1)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            int r1 = com.loc.dg.b()     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            r5 = 100
            if (r1 >= r5) goto L_0x012d
            java.lang.String r1 = r6.a     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            com.loc.dg.a(r1, r3)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.String r1 = r6.a     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            com.loc.dg.d(r1)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.String[] r1 = r3.a()     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            if (r0 == 0) goto L_0x0122
            r0.disconnect()
        L_0x0122:
            if (r4 == 0) goto L_0x0127
            r4.close()     // Catch:{ IOException -> 0x0148 }
        L_0x0127:
            r2.close()     // Catch:{ IOException -> 0x0148 }
        L_0x012a:
            r0 = r1
            goto L_0x0093
        L_0x012d:
            java.lang.Exception r1 = new java.lang.Exception     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            java.lang.String r3 = "the total number of hosts is exceed 100"
            r1.<init>(r3)     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
            throw r1     // Catch:{ Throwable -> 0x00b4, all -> 0x0136 }
        L_0x0136:
            r1 = move-exception
            r5 = r0
        L_0x0138:
            if (r5 == 0) goto L_0x013d
            r5.disconnect()
        L_0x013d:
            if (r4 == 0) goto L_0x0142
            r4.close()     // Catch:{ IOException -> 0x0164 }
        L_0x0142:
            if (r2 == 0) goto L_0x0147
            r2.close()     // Catch:{ IOException -> 0x0164 }
        L_0x0147:
            throw r1
        L_0x0148:
            r0 = move-exception
            com.loc.dk.a(r0)
            goto L_0x012a
        L_0x014d:
            if (r5 == 0) goto L_0x0152
            r5.disconnect()
        L_0x0152:
            if (r4 == 0) goto L_0x0157
            r4.close()     // Catch:{ IOException -> 0x015e }
        L_0x0157:
            if (r2 == 0) goto L_0x008c
            r2.close()     // Catch:{ IOException -> 0x015e }
            goto L_0x008c
        L_0x015e:
            r0 = move-exception
            com.loc.dk.a(r0)
            goto L_0x008c
        L_0x0164:
            r0 = move-exception
            com.loc.dk.a(r0)
            goto L_0x0147
        L_0x0169:
            r0 = move-exception
            r1 = r0
            r2 = r3
            r4 = r3
            r5 = r3
            goto L_0x0138
        L_0x016f:
            r1 = move-exception
            r2 = r3
            r4 = r3
            r5 = r0
            goto L_0x0138
        L_0x0174:
            r1 = move-exception
            r2 = r3
            r5 = r0
            goto L_0x0138
        L_0x0178:
            r0 = move-exception
            r1 = r0
            goto L_0x0138
        L_0x017b:
            r0 = move-exception
            r1 = r0
            r2 = r3
            r4 = r3
            r5 = r3
            goto L_0x00b6
        L_0x0182:
            r1 = move-exception
            r2 = r3
            r4 = r3
            r5 = r0
            goto L_0x00b6
        L_0x0188:
            r1 = move-exception
            r2 = r3
            r5 = r0
            goto L_0x00b6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.dq.call():java.lang.String[]");
    }
}
