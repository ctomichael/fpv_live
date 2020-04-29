package com.amap.openapi;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.amap.location.collection.CollectionConfig;
import com.amap.openapi.bh;
import java.util.List;

/* compiled from: GpsWifiWrapper */
public class bg {
    /* access modifiers changed from: private */
    public bh a;
    private LocationListener b;
    private boolean c;
    private cr d;
    private cu e;
    private final Object f = new Object();
    /* access modifiers changed from: private */
    public Context g;
    private Looper h;
    /* access modifiers changed from: private */
    public bf i;
    /* access modifiers changed from: private */
    public boolean j;

    public bg(Context context, @NonNull CollectionConfig.FpsCollectorConfig fpsCollectorConfig, @NonNull bf bfVar, @NonNull Looper looper) {
        this.g = context;
        this.h = looper;
        this.d = cr.a(context);
        this.i = bfVar;
        this.a = new bh(context, fpsCollectorConfig, looper);
        this.b = new LocationListener() {
            /* class com.amap.openapi.bg.AnonymousClass1 */

            public void onLocationChanged(Location location) {
                if (bg.this.j) {
                    try {
                        if (ba.a(location) && !ba.a(bg.this.g, location)) {
                            bg.this.b();
                            if (bg.this.i != null) {
                                bh.a f = bg.this.a.f();
                                bg.this.i.a(location, f.a, f.b, System.currentTimeMillis());
                            }
                        }
                    } catch (Throwable th) {
                    }
                }
            }

            public void onProviderDisabled(String str) {
            }

            public void onProviderEnabled(String str) {
            }

            public void onStatusChanged(String str, int i, Bundle bundle) {
            }
        };
        this.e = new cu() {
            /* class com.amap.openapi.bg.AnonymousClass2 */

            public void a() {
            }

            public void a(int i) {
            }

            public void a(int i, int i2, float f, List<ct> list) {
                bg.this.a(i);
            }

            public void b() {
            }
        };
    }

    /* access modifiers changed from: private */
    public void a(int i2) {
        boolean z = i2 < 4;
        if (this.c != z) {
            this.c = z;
            if (z) {
                this.a.d();
            } else {
                this.a.c();
            }
        }
    }

    public void a() {
        synchronized (this.f) {
            this.j = false;
            try {
                this.d.a(this.b);
                this.d.a(this.e);
            } catch (Throwable th) {
            }
        }
    }

    public void a(String str, long j2, float f2) {
        synchronized (this.f) {
            this.j = true;
            try {
                List<String> a2 = this.d.a();
                if (a2.contains("gps") || a2.contains("passive")) {
                    this.d.a(str, j2, 0.0f, this.b, this.h);
                    this.d.a(this.e, this.h);
                }
            } catch (Throwable th) {
            }
        }
    }

    public void b() {
        if (!this.a.e()) {
            this.a.a();
        }
    }

    public void c() {
        if (this.a.e()) {
            this.a.b();
        }
    }
}
