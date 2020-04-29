package com.loc;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import java.lang.ref.WeakReference;

/* compiled from: OfflineLocManager */
public class bp {
    static int a = 1000;
    static boolean b = false;
    static int c = 20;
    /* access modifiers changed from: private */
    public static WeakReference<bl> d;
    /* access modifiers changed from: private */
    public static int e = 10;

    public static synchronized void a(int i, boolean z, int i2) {
        synchronized (bp.class) {
            a = i;
            b = z;
            if (i2 < 10 || i2 > 100) {
                i2 = 20;
            }
            c = i2;
            if (i2 / 5 > e) {
                e = c / 5;
            }
        }
    }

    public static void a(final Context context) {
        aq.d().submit(new Runnable() {
            /* class com.loc.bp.AnonymousClass2 */

            public final void run() {
                try {
                    bl a2 = bs.a(bp.d);
                    bs.a(context, a2, ao.i, bp.a, 2097152, "6");
                    a2.h = 14400000;
                    if (a2.g == null) {
                        ag agVar = new ag(new ai(new ak()));
                        a2.g = new bw(new bv(context, new ca(), agVar, new String(al.a(10)), u.f(context), x.u(context), x.l(context), x.g(context), x.a(), Build.MANUFACTURER, Build.DEVICE, x.x(context), u.c(context), Build.MODEL, u.d(context), u.b(context)));
                    }
                    if (TextUtils.isEmpty(a2.i)) {
                        a2.i = "fKey";
                    }
                    a2.f = new ce(context, a2.h, a2.i, new cc(context, bp.b, bp.e * 1024, bp.c * 1024));
                    bm.a(a2);
                } catch (Throwable th) {
                    aq.b(th, "ofm", "uold");
                }
            }
        });
    }

    public static synchronized void a(final bo boVar, final Context context) {
        synchronized (bp.class) {
            aq.d().submit(new Runnable() {
                /* class com.loc.bp.AnonymousClass1 */

                public final void run() {
                    try {
                        synchronized (bp.class) {
                            String l = Long.toString(System.currentTimeMillis());
                            bl a2 = bs.a(bp.d);
                            bs.a(context, a2, ao.i, bp.a, 2097152, "6");
                            if (a2.e == null) {
                                a2.e = new ag(new ai(new ak(new ai())));
                            }
                            bm.a(l, boVar.a(), a2);
                        }
                    } catch (Throwable th) {
                        aq.b(th, "ofm", "aple");
                    }
                }
            });
        }
    }
}
