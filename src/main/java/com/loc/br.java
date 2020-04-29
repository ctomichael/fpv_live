package com.loc;

import android.content.Context;
import android.text.TextUtils;
import dji.logic.album.manager.litchis.DJIFileStreamLoader;
import dji.utils.TimeUtils;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;

/* compiled from: StatisticsManager */
public class br {
    /* access modifiers changed from: private */
    public static WeakReference<bl> a;

    public static void a(final Context context) {
        aq.d().submit(new Runnable() {
            /* class com.loc.br.AnonymousClass2 */

            public final void run() {
                try {
                    bl a2 = bs.a(br.a);
                    bs.a(context, a2, ao.h, 1000, DJIFileStreamLoader.bufferSize, "2");
                    if (a2.g == null) {
                        a2.g = new bt(new bx(context, new bu(new by(new ca()))));
                    }
                    a2.h = TimeUtils.TIMECONSTANT_HOUR;
                    if (TextUtils.isEmpty(a2.i)) {
                        a2.i = "cKey";
                    }
                    if (a2.f == null) {
                        a2.f = new ce(context, a2.h, a2.i, new cb(30, a2.a, new cg(context)));
                    }
                    bm.a(a2);
                } catch (Throwable th) {
                    aq.b(th, "stm", "usd");
                }
            }
        });
    }

    static /* synthetic */ void a(Context context, byte[] bArr) throws IOException {
        bl a2 = bs.a(a);
        bs.a(context, a2, ao.h, 1000, DJIFileStreamLoader.bufferSize, "2");
        if (a2.e == null) {
            a2.e = new aj();
        }
        try {
            bm.a(Integer.toString(new Random().nextInt(100)) + Long.toString(System.nanoTime()), bArr, a2);
        } catch (Throwable th) {
            aq.b(th, "stm", "wts");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0007, code lost:
        if (r3.size() == 0) goto L_0x0009;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void a(final java.util.List<com.loc.bq> r3, final android.content.Context r4) {
        /*
            java.lang.Class<com.loc.br> r1 = com.loc.br.class
            monitor-enter(r1)
            int r0 = r3.size()     // Catch:{ Throwable -> 0x000b }
            if (r0 != 0) goto L_0x000c
        L_0x0009:
            monitor-exit(r1)
            return
        L_0x000b:
            r0 = move-exception
        L_0x000c:
            java.util.concurrent.ExecutorService r0 = com.loc.aq.d()     // Catch:{ all -> 0x0019 }
            com.loc.br$1 r2 = new com.loc.br$1     // Catch:{ all -> 0x0019 }
            r2.<init>(r3, r4)     // Catch:{ all -> 0x0019 }
            r0.submit(r2)     // Catch:{ all -> 0x0019 }
            goto L_0x0009
        L_0x0019:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.br.a(java.util.List, android.content.Context):void");
    }
}
