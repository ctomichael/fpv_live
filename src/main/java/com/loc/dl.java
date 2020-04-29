package com.loc;

import java.util.concurrent.atomic.AtomicBoolean;

final class dl {
    private static boolean a = false;
    private static AtomicBoolean b = new AtomicBoolean(false);

    /* JADX WARNING: Removed duplicated region for block: B:59:0x01b0  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x01b5 A[SYNTHETIC, Splitter:B:61:0x01b5] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x01ba A[Catch:{ IOException -> 0x01e3 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void a(android.content.Context r9) {
        /*
            r3 = 0
            r1 = 1
            r8 = 0
            java.util.concurrent.atomic.AtomicBoolean r0 = com.loc.dl.b
            boolean r0 = r0.compareAndSet(r8, r1)
            if (r0 == 0) goto L_0x0156
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            r1 = 14
            if (r0 < r1) goto L_0x0017
            r0 = 40965(0xa005, float:5.7404E-41)
            android.net.TrafficStats.setThreadStatsTag(r0)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
        L_0x0017:
            com.loc.cx r0 = com.loc.cy.a(r9)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            if (r0 == 0) goto L_0x0027
            java.lang.String r1 = r0.e()     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            boolean r1 = com.loc.dw.a(r1)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            if (r1 == 0) goto L_0x0157
        L_0x0027:
            java.lang.String r0 = "ffffffffffffffffffffffff"
        L_0x002a:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r2 = "stat: "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.StringBuilder r1 = r1.append(r0)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            com.loc.dk.a(r1)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r2 = "HTTPDNS-"
            r1.<init>(r2)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r1 = r0.toString()     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r2 = "23356390Raw"
            r0.<init>(r2)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r2 = com.loc.dn.c(r1)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r4 = "16594f72217bece5a457b4803a48f2da"
            r2.<init>(r4)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r0 = com.loc.dn.c(r0)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r2 = "16594f72217bece5a457b4803a48f2da"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r0 = com.loc.dn.d(r0)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r4 = "http://adash.man.aliyuncs.com:80/man/api?ak=23356390&s="
            r2.<init>(r4)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.net.URL r2 = new java.net.URL     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            r2.<init>(r0)     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.net.URLConnection r0 = r2.openConnection()     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            r2 = 1
            r0.setDoOutput(r2)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            r2 = 0
            r0.setUseCaches(r2)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r4 = "==="
            r2.<init>(r4)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r4 = "==="
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r4 = "Content-Type"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r6 = "multipart/form-data; boundary="
            r5.<init>(r6)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.StringBuilder r5 = r5.append(r2)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            r0.setRequestProperty(r4, r5)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r5 = "--"
            r4.<init>(r5)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.StringBuilder r4 = r4.append(r2)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r5 = "\r\nContent-Disposition: form-data; name=\"Raw\"\r\nContent-Type: text/plain; charset=UTF-8\r\n\r\n"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.StringBuilder r1 = r4.append(r1)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r4 = "\r\n--"
            java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r2 = "--\r\n"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            java.io.OutputStream r2 = r0.getOutputStream()     // Catch:{ Throwable -> 0x0201, all -> 0x01ee }
            byte[] r1 = r1.getBytes()     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            r2.write(r1)     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            int r1 = r0.getResponseCode()     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            r4 = 200(0xc8, float:2.8E-43)
            if (r1 != r4) goto L_0x01c3
            java.io.DataInputStream r4 = new java.io.DataInputStream     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            java.io.InputStream r1 = r0.getInputStream()     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            r4.<init>(r1)     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            r1.<init>()     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r3]     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
        L_0x0126:
            int r5 = r4.read(r3)     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            r6 = -1
            if (r5 == r6) goto L_0x015d
            java.lang.String r6 = new java.lang.String     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            r7 = 0
            r6.<init>(r3, r7, r5)     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            r1.append(r6)     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            goto L_0x0126
        L_0x0137:
            r1 = move-exception
            r5 = r0
        L_0x0139:
            java.lang.String r0 = "MAN report error"
            com.loc.dk.a(r0)     // Catch:{ all -> 0x01f7 }
            com.loc.dk.a(r1)     // Catch:{ all -> 0x01f7 }
            if (r5 == 0) goto L_0x0147
            r5.disconnect()
        L_0x0147:
            if (r2 == 0) goto L_0x014c
            r2.close()     // Catch:{ IOException -> 0x01dd }
        L_0x014c:
            if (r4 == 0) goto L_0x0151
            r4.close()     // Catch:{ IOException -> 0x01dd }
        L_0x0151:
            java.util.concurrent.atomic.AtomicBoolean r0 = com.loc.dl.b
            r0.set(r8)
        L_0x0156:
            return
        L_0x0157:
            java.lang.String r0 = r0.e()     // Catch:{ Throwable -> 0x01fa, all -> 0x01e8 }
            goto L_0x002a
        L_0x015d:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            java.lang.String r5 = "get MAN response: "
            r3.<init>(r5)     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            java.lang.String r5 = r1.toString()     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            com.loc.dk.a(r3)     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x01a7 }
            java.lang.String r1 = r1.toString()     // Catch:{ JSONException -> 0x01a7 }
            r3.<init>(r1)     // Catch:{ JSONException -> 0x01a7 }
            java.lang.String r1 = "success"
            java.lang.Object r1 = r3.get(r1)     // Catch:{ JSONException -> 0x01a7 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ JSONException -> 0x01a7 }
            java.lang.String r3 = "success"
            boolean r1 = r1.equals(r3)     // Catch:{ JSONException -> 0x01a7 }
            if (r1 == 0) goto L_0x0192
            r1 = 1
            com.loc.dl.a = r1     // Catch:{ JSONException -> 0x01a7 }
        L_0x0192:
            if (r0 == 0) goto L_0x0197
            r0.disconnect()
        L_0x0197:
            if (r2 == 0) goto L_0x019c
            r2.close()     // Catch:{ IOException -> 0x01d8 }
        L_0x019c:
            if (r4 == 0) goto L_0x01a1
            r4.close()     // Catch:{ IOException -> 0x01d8 }
        L_0x01a1:
            java.util.concurrent.atomic.AtomicBoolean r0 = com.loc.dl.b
            r0.set(r8)
            goto L_0x0156
        L_0x01a7:
            r1 = move-exception
            com.loc.dk.a(r1)     // Catch:{ Throwable -> 0x0137, all -> 0x01ac }
            goto L_0x0192
        L_0x01ac:
            r1 = move-exception
            r5 = r0
        L_0x01ae:
            if (r5 == 0) goto L_0x01b3
            r5.disconnect()
        L_0x01b3:
            if (r2 == 0) goto L_0x01b8
            r2.close()     // Catch:{ IOException -> 0x01e3 }
        L_0x01b8:
            if (r4 == 0) goto L_0x01bd
            r4.close()     // Catch:{ IOException -> 0x01e3 }
        L_0x01bd:
            java.util.concurrent.atomic.AtomicBoolean r0 = com.loc.dl.b
            r0.set(r8)
            throw r1
        L_0x01c3:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            java.lang.String r5 = "MAN API error: "
            r4.<init>(r5)     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            java.lang.StringBuilder r1 = r4.append(r1)     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            com.loc.dk.a(r1)     // Catch:{ Throwable -> 0x0207, all -> 0x01f3 }
            r4 = r3
            goto L_0x0192
        L_0x01d8:
            r0 = move-exception
            com.loc.dk.a(r0)
            goto L_0x01a1
        L_0x01dd:
            r0 = move-exception
            com.loc.dk.a(r0)
            goto L_0x0151
        L_0x01e3:
            r0 = move-exception
            com.loc.dk.a(r0)
            goto L_0x01bd
        L_0x01e8:
            r0 = move-exception
            r1 = r0
            r2 = r3
            r4 = r3
            r5 = r3
            goto L_0x01ae
        L_0x01ee:
            r1 = move-exception
            r2 = r3
            r4 = r3
            r5 = r0
            goto L_0x01ae
        L_0x01f3:
            r1 = move-exception
            r4 = r3
            r5 = r0
            goto L_0x01ae
        L_0x01f7:
            r0 = move-exception
            r1 = r0
            goto L_0x01ae
        L_0x01fa:
            r0 = move-exception
            r1 = r0
            r2 = r3
            r4 = r3
            r5 = r3
            goto L_0x0139
        L_0x0201:
            r1 = move-exception
            r2 = r3
            r4 = r3
            r5 = r0
            goto L_0x0139
        L_0x0207:
            r1 = move-exception
            r4 = r3
            r5 = r0
            goto L_0x0139
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.dl.a(android.content.Context):void");
    }

    static boolean a() {
        return a;
    }
}
