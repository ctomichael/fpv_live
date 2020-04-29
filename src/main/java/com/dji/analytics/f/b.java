package com.dji.analytics.f;

import android.content.Context;
import com.dji.analytics.DJIA;
import java.io.FileWriter;
import java.io.IOException;

/* compiled from: DebugUtils */
public class b {
    FileWriter a;
    private Context b;

    /* compiled from: DebugUtils */
    private static final class a {
        public static b a = new b();
    }

    public static b a() {
        return a.a;
    }

    private b() {
        this.b = null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0076, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0077, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void a(android.content.Context r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            android.content.Context r0 = r5.b     // Catch:{ all -> 0x0024 }
            if (r0 == 0) goto L_0x0007
        L_0x0005:
            monitor-exit(r5)
            return
        L_0x0007:
            com.dji.analytics.c.b r0 = com.dji.analytics.DJIA.log     // Catch:{ all -> 0x0024 }
            java.lang.String r1 = com.dji.analytics.DJIA.LOG_TAG     // Catch:{ all -> 0x0024 }
            java.lang.String r2 = "DebugUtils is start init."
            r0.a(r1, r2)     // Catch:{ all -> 0x0024 }
            java.io.File r0 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ all -> 0x0024 }
            boolean r1 = r0.exists()     // Catch:{ all -> 0x0024 }
            if (r1 != 0) goto L_0x0027
            java.io.FileNotFoundException r0 = new java.io.FileNotFoundException     // Catch:{ all -> 0x0024 }
            java.lang.String r1 = "can not open storage."
            r0.<init>(r1)     // Catch:{ all -> 0x0024 }
            throw r0     // Catch:{ all -> 0x0024 }
        L_0x0024:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        L_0x0027:
            com.dji.analytics.c.b r1 = com.dji.analytics.DJIA.log     // Catch:{ all -> 0x0024 }
            java.lang.String r2 = com.dji.analytics.DJIA.LOG_TAG     // Catch:{ all -> 0x0024 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0024 }
            r3.<init>()     // Catch:{ all -> 0x0024 }
            java.lang.String r4 = "getExternalStorageDirectory = "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x0024 }
            java.lang.String r4 = r0.getAbsolutePath()     // Catch:{ all -> 0x0024 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x0024 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0024 }
            r1.a(r2, r3)     // Catch:{ all -> 0x0024 }
            java.io.File r1 = new java.io.File     // Catch:{ all -> 0x0024 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0024 }
            r2.<init>()     // Catch:{ all -> 0x0024 }
            java.lang.String r0 = r0.getPath()     // Catch:{ all -> 0x0024 }
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ all -> 0x0024 }
            java.lang.String r2 = "/djia.test.data.txt"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x0024 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0024 }
            r1.<init>(r0)     // Catch:{ all -> 0x0024 }
            boolean r0 = r1.exists()     // Catch:{ all -> 0x0024 }
            if (r0 != 0) goto L_0x006c
            r1.createNewFile()     // Catch:{ all -> 0x0024 }
        L_0x006c:
            java.io.FileWriter r0 = new java.io.FileWriter     // Catch:{ IOException -> 0x0076 }
            r0.<init>(r1)     // Catch:{ IOException -> 0x0076 }
            r5.a = r0     // Catch:{ IOException -> 0x0076 }
        L_0x0073:
            r5.b = r6     // Catch:{ all -> 0x0024 }
            goto L_0x0005
        L_0x0076:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0024 }
            goto L_0x0073
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.analytics.f.b.a(android.content.Context):void");
    }

    public synchronized void a(String str) {
        if (this.a != null) {
            try {
                this.a.write(System.currentTimeMillis() + ":" + str + "\n");
                this.a.flush();
                DJIA.log.a(DJIA.LOG_TAG, " write content = " + str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }
}
