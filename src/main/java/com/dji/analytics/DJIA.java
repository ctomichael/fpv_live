package com.dji.analytics;

import android.content.Context;
import com.dji.analytics.c.a;
import com.dji.analytics.c.b;
import com.dji.analytics.d.c;
import com.dji.analytics.f.g;
import java.io.IOException;
import java.util.HashMap;

public class DJIA {
    public static boolean DEV_FLAG = false;
    public static final String LOG_TAG = DJIA.class.getSimpleName();
    private static boolean a = false;
    private static boolean b = false;
    private static final Object c = new Object();
    private static ReportConfig d;
    public static b log = new a();
    public static String mAppId;
    public static String mAppKey;
    public static Context mContext;

    public static ReportConfig getConfig() {
        return d;
    }

    private DJIA() {
    }

    @Deprecated
    public static void setDebug(boolean z) {
        if (z) {
            enableDebug();
        }
    }

    public static void enableDebug() {
        DEV_FLAG = true;
        try {
            com.dji.analytics.f.b.a().a(mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void setCanReport(boolean z) {
        setIsEnableReport(z);
    }

    public static void setIsEnableReport(boolean z) {
        a = z;
        g.a().b(z);
        if (z) {
            logEvent("DJIA_user_opened_log_report");
        } else {
            com.dji.analytics.b.a.a().c();
        }
    }

    public static void setIsEnableCellular(boolean z) {
        d.is4GEnable = z;
    }

    public static void setIsDevelopMode(boolean z) {
        b = z;
    }

    public static void addExtraData(String str, String str2) {
        d.addExtraData(str, str2);
    }

    @Deprecated
    public static boolean getCanReport() {
        return getIsEnableReport();
    }

    public static boolean getIsEnableReport() {
        return a;
    }

    public static String getUUID() {
        if (isInitialised()) {
            return com.dji.analytics.d.a.a().e();
        }
        return null;
    }

    public static void reportBaseInfo() {
        com.dji.analytics.d.a.a().c();
    }

    public static void init(Context context, String str, String str2) {
        init(context, str, str2, new ReportConfig());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        if (r5.length() == 0) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
        if (r7 != null) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
        com.dji.analytics.DJIA.log.b(com.dji.analytics.DJIA.LOG_TAG, "init data is empty.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002e, code lost:
        com.dji.analytics.DJIA.d = r7;
        com.dji.analytics.DJIA.d.setAppId(r5);
        com.dji.analytics.DJIA.d.setAppKey(r6);
        com.dji.analytics.DJIA.mContext = r4.getApplicationContext();
        com.dji.analytics.f.g.a(r4);
        com.dji.analytics.DJIA.a = com.dji.analytics.f.g.a().d();
        com.dji.analytics.b.a.a().a(r4);
        com.dji.analytics.d.a.a().a(r4, r5);
        com.dji.analytics.d.c.a().a(r7);
        com.dji.analytics.d.b.a(r4);
        com.dji.analytics.d.b.b(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006a, code lost:
        if (com.dji.analytics.DJIA.a == false) goto L_0x0073;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006c, code lost:
        com.dji.analytics.d.a.a().c();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0073, code lost:
        com.dji.analytics.DJIA.log.a(com.dji.analytics.DJIA.LOG_TAG, "DJIA initialised.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0014, code lost:
        if (r4 == null) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0016, code lost:
        if (r5 == null) goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void init(android.content.Context r4, java.lang.String r5, java.lang.String r6, com.dji.analytics.ReportConfig r7) {
        /*
            java.lang.Object r1 = com.dji.analytics.DJIA.c
            monitor-enter(r1)
            android.content.Context r0 = com.dji.analytics.DJIA.mContext     // Catch:{ all -> 0x002b }
            if (r0 == 0) goto L_0x0013
            com.dji.analytics.c.b r0 = com.dji.analytics.DJIA.log     // Catch:{ all -> 0x002b }
            java.lang.String r2 = com.dji.analytics.DJIA.LOG_TAG     // Catch:{ all -> 0x002b }
            java.lang.String r3 = "Initialization can not be repeated"
            r0.b(r2, r3)     // Catch:{ all -> 0x002b }
            monitor-exit(r1)     // Catch:{ all -> 0x002b }
        L_0x0012:
            return
        L_0x0013:
            monitor-exit(r1)     // Catch:{ all -> 0x002b }
            if (r4 == 0) goto L_0x0020
            if (r5 == 0) goto L_0x0020
            int r0 = r5.length()
            if (r0 == 0) goto L_0x0020
            if (r7 != 0) goto L_0x002e
        L_0x0020:
            com.dji.analytics.c.b r0 = com.dji.analytics.DJIA.log
            java.lang.String r1 = com.dji.analytics.DJIA.LOG_TAG
            java.lang.String r2 = "init data is empty."
            r0.b(r1, r2)
            goto L_0x0012
        L_0x002b:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x002b }
            throw r0
        L_0x002e:
            com.dji.analytics.DJIA.d = r7
            com.dji.analytics.ReportConfig r0 = com.dji.analytics.DJIA.d
            r0.setAppId(r5)
            com.dji.analytics.ReportConfig r0 = com.dji.analytics.DJIA.d
            r0.setAppKey(r6)
            android.content.Context r0 = r4.getApplicationContext()
            com.dji.analytics.DJIA.mContext = r0
            com.dji.analytics.f.g.a(r4)
            com.dji.analytics.f.g r0 = com.dji.analytics.f.g.a()
            boolean r0 = r0.d()
            com.dji.analytics.DJIA.a = r0
            com.dji.analytics.b.a r0 = com.dji.analytics.b.a.a()
            r0.a(r4)
            com.dji.analytics.d.a r0 = com.dji.analytics.d.a.a()
            r0.a(r4, r5)
            com.dji.analytics.d.c r0 = com.dji.analytics.d.c.a()
            r0.a(r7)
            com.dji.analytics.d.b.a(r4)
            com.dji.analytics.d.b.b(r4)
            boolean r0 = com.dji.analytics.DJIA.a
            if (r0 == 0) goto L_0x0073
            com.dji.analytics.d.a r0 = com.dji.analytics.d.a.a()
            r0.c()
        L_0x0073:
            com.dji.analytics.c.b r0 = com.dji.analytics.DJIA.log
            java.lang.String r1 = com.dji.analytics.DJIA.LOG_TAG
            java.lang.String r2 = "DJIA initialised."
            r0.a(r1, r2)
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.analytics.DJIA.init(android.content.Context, java.lang.String, java.lang.String, com.dji.analytics.ReportConfig):void");
    }

    public static void logEvent(String str) {
        logEvent(str, null, false);
    }

    public static void logEvent(String str, HashMap<String, String> hashMap) {
        logEvent(str, hashMap, false);
    }

    public static void logEvent(String str, HashMap<String, String> hashMap, boolean z) {
        log.a(LOG_TAG, "DJIA logEvent <" + str + ">");
        if (b) {
            log.a(LOG_TAG, "DJIA is in develop mode. :)");
        } else if (a || z) {
            if (mContext == null) {
                log.b(LOG_TAG, "DJIA is not initialized, call init(..) method first.");
                return;
            }
            com.dji.analytics.a.a aVar = new com.dji.analytics.a.a(str, hashMap, z);
            if (DEV_FLAG) {
                log.a(LOG_TAG, "DJIA logEvent <" + str + ">");
                com.dji.analytics.f.b.a().a(aVar.toString() + "\n");
            }
            c.a().a(aVar);
        } else if (DEV_FLAG) {
            log.a(LOG_TAG, "DJIA can not logEvent ");
        }
    }

    public static boolean isInitialised() {
        return d != null;
    }
}
