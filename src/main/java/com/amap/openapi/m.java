package com.amap.openapi;

import android.content.Context;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.amap.location.collection.CollectionConfig;
import java.util.List;

/* compiled from: FpsCollector */
public class m {
    private static final String a = m.class.getSimpleName();
    private Context b;
    private Handler c;
    private t d;
    private l e;
    private o f;
    private cs g;
    /* access modifiers changed from: private */
    public long h;
    private long i;
    private Location j;
    private h k;
    private v l = new v();

    public m(Context context, t tVar, CollectionConfig.FpsCollectorConfig fpsCollectorConfig, Looper looper) {
        this.b = context;
        this.d = tVar;
        this.c = new Handler(looper);
        this.e = new l(this.b, looper);
        this.f = new o(this.b, looper);
        this.k = new h();
    }

    public void a() {
        this.e.a();
        this.f.a();
        this.g = new cs() {
            /* class com.amap.openapi.m.AnonymousClass1 */

            public void a(long j, String str) {
                long unused = m.this.h = j;
            }
        };
        try {
            cr.a(this.b).a(this.g, this.c.getLooper());
        } catch (Exception | SecurityException e2) {
        }
    }

    public void a(Location location, List<ScanResult> list, long j2, long j3) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.j == null || location.distanceTo(this.j) >= 10.0f) {
            q a2 = this.e.a(location);
            List<aa> a3 = this.f.a(location, list, j2, j3);
            if (!(a2 == null && a3 == null)) {
                ba.a(this.l, location, this.h, j3);
                byte[] a4 = this.k.a(this.b, this.l, a2, this.f.c(), a3);
                if (a4 != null) {
                    this.d.a(0, a4);
                }
            }
            this.j = location;
            this.i = elapsedRealtime;
        }
    }

    public void b() {
        try {
            cr.a(this.b).a(this.g);
        } catch (Exception e2) {
        }
        this.c.removeCallbacksAndMessages(null);
        this.e.b();
        this.f.b();
    }
}
