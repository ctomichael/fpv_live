package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresPermission;
import com.dji.permission.Permission;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

/* compiled from: GpsLocationManager */
public class cw {
    private a a;
    private a b;

    /* compiled from: GpsLocationManager */
    private static class a implements LocationListener {
        /* access modifiers changed from: private */
        public cz a;
        /* access modifiers changed from: private */
        public String b;
        private Context c;
        private C0010a d = new C0010a(this);
        /* access modifiers changed from: private */
        public final List<cx> e = new ArrayList();
        /* access modifiers changed from: private */
        public long f = LongCompanionObject.MAX_VALUE;
        /* access modifiers changed from: private */
        public float g = Float.MAX_VALUE;
        private Location h;

        /* renamed from: com.amap.openapi.cw$a$a  reason: collision with other inner class name */
        /* compiled from: GpsLocationManager */
        private class C0010a extends BroadcastReceiver {
            private LocationListener b;

            public C0010a(LocationListener locationListener) {
                this.b = locationListener;
            }

            public void onReceive(Context context, Intent intent) {
                if (cr.a(context).a("gps")) {
                    synchronized (a.this.e) {
                        if (a.this.e.size() > 0) {
                            a.this.a.a(this.b);
                            a.this.a.a(a.this.b, a.this.f, a.this.g, this.b, Looper.getMainLooper());
                        }
                    }
                }
            }
        }

        a(String str, cz czVar, Context context) {
            this.a = czVar;
            this.b = str;
            this.c = context;
        }

        private void a() {
            float f2;
            long j = LongCompanionObject.MAX_VALUE;
            float f3 = Float.MAX_VALUE;
            if (this.e.isEmpty()) {
                this.a.a(this);
                this.h = null;
                this.f = LongCompanionObject.MAX_VALUE;
                this.g = Float.MAX_VALUE;
                return;
            }
            Iterator<cx> it2 = this.e.iterator();
            while (true) {
                f2 = f3;
                if (!it2.hasNext()) {
                    break;
                }
                cx next = it2.next();
                j = Math.min(j, next.b);
                f3 = Math.min(f2, next.c);
            }
            if (this.f != j || this.g != f2) {
                this.f = j;
                this.g = f2;
                this.a.a(this);
                this.a.a(this.b, this.f, this.g, this, Looper.getMainLooper());
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void a(long r8, float r10, android.location.LocationListener r11, android.os.Looper r12) {
            /*
                r7 = this;
                java.util.List<com.amap.openapi.cx> r6 = r7.e
                monitor-enter(r6)
                java.util.List<com.amap.openapi.cx> r0 = r7.e     // Catch:{ all -> 0x0058 }
                java.util.Iterator r1 = r0.iterator()     // Catch:{ all -> 0x0058 }
            L_0x0009:
                boolean r0 = r1.hasNext()     // Catch:{ all -> 0x0058 }
                if (r0 == 0) goto L_0x002e
                java.lang.Object r0 = r1.next()     // Catch:{ all -> 0x0058 }
                com.amap.openapi.cx r0 = (com.amap.openapi.cx) r0     // Catch:{ all -> 0x0058 }
                android.location.LocationListener r2 = r0.a     // Catch:{ all -> 0x0058 }
                if (r2 != r11) goto L_0x0009
                long r2 = r0.b     // Catch:{ all -> 0x0058 }
                int r1 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                if (r1 != 0) goto L_0x0025
                float r1 = r0.c     // Catch:{ all -> 0x0058 }
                int r1 = (r1 > r10 ? 1 : (r1 == r10 ? 0 : -1))
                if (r1 == 0) goto L_0x002c
            L_0x0025:
                r0.b = r8     // Catch:{ all -> 0x0058 }
                r0.c = r10     // Catch:{ all -> 0x0058 }
                r7.a()     // Catch:{ all -> 0x0058 }
            L_0x002c:
                monitor-exit(r6)     // Catch:{ all -> 0x0058 }
            L_0x002d:
                return
            L_0x002e:
                java.util.List<com.amap.openapi.cx> r0 = r7.e     // Catch:{ all -> 0x0058 }
                int r0 = r0.size()     // Catch:{ all -> 0x0058 }
                if (r0 != 0) goto L_0x0045
                android.content.Context r0 = r7.c     // Catch:{ Exception -> 0x005b }
                com.amap.openapi.cw$a$a r1 = r7.d     // Catch:{ Exception -> 0x005b }
                android.content.IntentFilter r2 = new android.content.IntentFilter     // Catch:{ Exception -> 0x005b }
                java.lang.String r3 = "android.location.PROVIDERS_CHANGED"
                r2.<init>(r3)     // Catch:{ Exception -> 0x005b }
                r0.registerReceiver(r1, r2)     // Catch:{ Exception -> 0x005b }
            L_0x0045:
                com.amap.openapi.cx r0 = new com.amap.openapi.cx     // Catch:{ all -> 0x0058 }
                r1 = r11
                r2 = r8
                r4 = r10
                r5 = r12
                r0.<init>(r1, r2, r4, r5)     // Catch:{ all -> 0x0058 }
                java.util.List<com.amap.openapi.cx> r1 = r7.e     // Catch:{ all -> 0x0058 }
                r1.add(r0)     // Catch:{ all -> 0x0058 }
                r7.a()     // Catch:{ all -> 0x0058 }
                monitor-exit(r6)     // Catch:{ all -> 0x0058 }
                goto L_0x002d
            L_0x0058:
                r0 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0058 }
                throw r0
            L_0x005b:
                r0 = move-exception
                goto L_0x0045
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.cw.a.a(long, float, android.location.LocationListener, android.os.Looper):void");
        }

        /* access modifiers changed from: package-private */
        public void a(LocationListener locationListener) {
            boolean z;
            synchronized (this.e) {
                Iterator<cx> it2 = this.e.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        z = false;
                        break;
                    }
                    cx next = it2.next();
                    if (next.a == locationListener) {
                        this.e.remove(next);
                        a();
                        z = true;
                        break;
                    }
                }
                if (this.e.size() == 0 && z) {
                    try {
                        this.c.unregisterReceiver(this.d);
                    } catch (Exception e2) {
                    }
                }
            }
        }

        public void onLocationChanged(Location location) {
            if (location != null) {
                float abs = this.h == null ? Float.MAX_VALUE : Math.abs(location.distanceTo(this.h));
                synchronized (this.e) {
                    for (cx cxVar : this.e) {
                        cxVar.a(location, abs);
                    }
                }
                this.h = location;
            }
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.amap.openapi.cx.a(java.lang.String, boolean):void
         arg types: [java.lang.String, int]
         candidates:
          com.amap.openapi.cx.a(android.location.Location, float):void
          com.amap.openapi.cx.a(java.lang.String, boolean):void */
        public void onProviderDisabled(String str) {
            synchronized (this.e) {
                for (cx cxVar : this.e) {
                    cxVar.a(str, false);
                }
            }
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.amap.openapi.cx.a(java.lang.String, boolean):void
         arg types: [java.lang.String, int]
         candidates:
          com.amap.openapi.cx.a(android.location.Location, float):void
          com.amap.openapi.cx.a(java.lang.String, boolean):void */
        public void onProviderEnabled(String str) {
            synchronized (this.e) {
                for (cx cxVar : this.e) {
                    cxVar.a(str, true);
                }
            }
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            synchronized (this.e) {
                for (cx cxVar : this.e) {
                    cxVar.a(str, i, bundle);
                }
            }
        }
    }

    public cw(cz czVar, Context context) {
        this.a = new a("gps", czVar, context);
        this.b = new a("passive", czVar, context);
    }

    public void a(LocationListener locationListener) {
        if (locationListener != null) {
            this.a.a(locationListener);
            this.b.a(locationListener);
        }
    }

    @RequiresPermission(Permission.ACCESS_FINE_LOCATION)
    public void a(String str, long j, float f, LocationListener locationListener, Looper looper) {
        if (locationListener != null) {
            a aVar = null;
            if ("gps".equals(str)) {
                aVar = this.a;
            } else if ("passive".equals(str)) {
                aVar = this.b;
            }
            if (aVar != null) {
                aVar.a(j, f, locationListener, looper);
            }
        }
    }
}
