package com.loc;

import android.content.Context;

/* compiled from: AdiuManager */
public class c {
    private static c a;
    private final Context b;
    private final String c = i.a("amap_device_adiu");
    private String d;

    private c(Context context) {
        this.b = context.getApplicationContext();
    }

    public static c a(Context context) {
        if (a == null) {
            synchronized (c.class) {
                if (a == null) {
                    a = new c(context);
                }
            }
        }
        return a;
    }

    public static String b() {
        return g.a();
    }

    public final void a(String str) {
        d.a(this.b).a(this.c);
        d.a(this.b).b(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x008a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized boolean a() {
        /*
            r6 = this;
            r5 = 2
            r2 = 0
            r1 = 1
            monitor-enter(r6)
            java.lang.String r0 = r6.d     // Catch:{ all -> 0x0091 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x0091 }
            if (r0 == 0) goto L_0x0016
            java.lang.String r0 = com.loc.g.a()     // Catch:{ all -> 0x0091 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x0091 }
            if (r0 != 0) goto L_0x0019
        L_0x0016:
            r0 = r1
        L_0x0017:
            monitor-exit(r6)
            return r0
        L_0x0019:
            android.content.Context r0 = r6.b     // Catch:{ all -> 0x0091 }
            com.loc.d r0 = com.loc.d.a(r0)     // Catch:{ all -> 0x0091 }
            java.lang.String r3 = r6.c     // Catch:{ all -> 0x0091 }
            r0.a(r3)     // Catch:{ all -> 0x0091 }
            android.content.Context r0 = r6.b     // Catch:{ all -> 0x0091 }
            com.loc.d r0 = com.loc.d.a(r0)     // Catch:{ all -> 0x0091 }
            java.util.List r3 = r0.a()     // Catch:{ all -> 0x0091 }
            if (r3 == 0) goto L_0x008f
            int r0 = r3.size()     // Catch:{ all -> 0x0091 }
            if (r0 <= 0) goto L_0x008f
            r0 = 0
            java.lang.Object r0 = r3.get(r0)     // Catch:{ all -> 0x0091 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0091 }
            boolean r4 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x0091 }
            if (r4 != 0) goto L_0x008f
            r6.d = r0     // Catch:{ all -> 0x0091 }
            java.lang.String r0 = r6.d     // Catch:{ all -> 0x0091 }
            com.loc.g.a(r0)     // Catch:{ all -> 0x0091 }
            java.lang.String r2 = ""
            int r0 = r3.size()     // Catch:{ all -> 0x0091 }
            if (r0 <= r1) goto L_0x0061
            r0 = 1
            java.lang.Object r0 = r3.get(r0)     // Catch:{ all -> 0x0091 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0091 }
            boolean r4 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x0091 }
            if (r4 != 0) goto L_0x0061
            r2 = r0
        L_0x0061:
            int r0 = r3.size()     // Catch:{ all -> 0x0091 }
            if (r0 <= r5) goto L_0x0094
            r0 = 2
            java.lang.Object r0 = r3.get(r0)     // Catch:{ all -> 0x0091 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0091 }
            boolean r3 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x0091 }
            if (r3 != 0) goto L_0x0094
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0091 }
            java.lang.String r3 = ":"
            r2.<init>(r3)     // Catch:{ all -> 0x0091 }
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ all -> 0x0091 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0091 }
        L_0x0084:
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x0091 }
            if (r2 != 0) goto L_0x008d
            com.loc.g.b(r0)     // Catch:{ all -> 0x0091 }
        L_0x008d:
            r0 = r1
            goto L_0x0017
        L_0x008f:
            r0 = r2
            goto L_0x0017
        L_0x0091:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        L_0x0094:
            r0 = r2
            goto L_0x0084
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.c.a():boolean");
    }
}
