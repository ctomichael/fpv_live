package com.amap.openapi;

import android.location.GpsStatus;
import android.location.OnNmeaMessageListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: NmeaManager */
public class cy {
    private final List<a> a = new CopyOnWriteArrayList();
    private cz b;
    private OnNmeaMessageListener c;
    private GpsStatus.NmeaListener d;

    /* compiled from: NmeaManager */
    private static class a {
        cs a;
        private Handler b;

        /* renamed from: com.amap.openapi.cy$a$a  reason: collision with other inner class name */
        /* compiled from: NmeaManager */
        private static class C0011a extends Handler {
            private cs a;

            C0011a(cs csVar, Looper looper) {
                super(looper);
                this.a = csVar;
            }

            public void handleMessage(Message message) {
                Bundle data = message.getData();
                this.a.a(data.getLong("timestamp"), data.getString("nmea"));
            }
        }

        a(cs csVar, Looper looper) {
            this.a = csVar;
            this.b = new C0011a(this.a, looper == null ? Looper.getMainLooper() : looper);
        }

        /* access modifiers changed from: package-private */
        public void a(long j, String str) {
            Message obtainMessage = this.b.obtainMessage();
            obtainMessage.getData().putLong("timestamp", j);
            obtainMessage.getData().putString("nmea", str);
            obtainMessage.sendToTarget();
        }

        /* access modifiers changed from: package-private */
        public boolean a(cs csVar, Looper looper) {
            if (looper == null) {
                looper = Looper.getMainLooper();
            }
            return this.a == csVar && this.b.getLooper() == looper;
        }
    }

    public cy(cz czVar) {
        this.b = czVar;
        if (Build.VERSION.SDK_INT >= 24) {
            this.c = new OnNmeaMessageListener() {
                /* class com.amap.openapi.cy.AnonymousClass1 */

                public void onNmeaMessage(String str, long j) {
                    cy.this.a(j, str);
                }
            };
        } else {
            this.d = new GpsStatus.NmeaListener() {
                /* class com.amap.openapi.cy.AnonymousClass2 */

                public void onNmeaReceived(long j, String str) {
                    cy.this.a(j, str);
                }
            };
        }
    }

    private a b(cs csVar) {
        for (a aVar : this.a) {
            if (aVar.a == csVar) {
                return aVar;
            }
        }
        return null;
    }

    public void a(long j, String str) {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a(j, str);
            }
        }
    }

    public void a(cs csVar) {
        if (csVar != null) {
            synchronized (this.a) {
                a b2 = b(csVar);
                if (b2 != null) {
                    this.a.remove(b2);
                    if (this.a.size() == 0) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            if (this.c != null) {
                                this.b.a(this.c);
                            }
                        } else if (this.d != null) {
                            this.b.a(this.d);
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        return r0;
     */
    @android.support.annotation.RequiresPermission(com.dji.permission.Permission.ACCESS_FINE_LOCATION)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a(com.amap.openapi.cs r6, android.os.Looper r7) {
        /*
            r5 = this;
            r1 = 1
            r0 = 0
            if (r6 != 0) goto L_0x0005
        L_0x0004:
            return r0
        L_0x0005:
            java.util.List<com.amap.openapi.cy$a> r2 = r5.a
            monitor-enter(r2)
            com.amap.openapi.cy$a r3 = r5.b(r6)     // Catch:{ all -> 0x0014 }
            if (r3 == 0) goto L_0x0017
            boolean r0 = r3.a(r6, r7)     // Catch:{ all -> 0x0014 }
            monitor-exit(r2)     // Catch:{ all -> 0x0014 }
            goto L_0x0004
        L_0x0014:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0014 }
            throw r0
        L_0x0017:
            com.amap.openapi.cy$a r3 = new com.amap.openapi.cy$a     // Catch:{ all -> 0x0014 }
            r3.<init>(r6, r7)     // Catch:{ all -> 0x0014 }
            java.util.List<com.amap.openapi.cy$a> r4 = r5.a     // Catch:{ all -> 0x0014 }
            r4.add(r3)     // Catch:{ all -> 0x0014 }
            java.util.List<com.amap.openapi.cy$a> r4 = r5.a     // Catch:{ all -> 0x0014 }
            int r4 = r4.size()     // Catch:{ all -> 0x0014 }
            if (r4 != r1) goto L_0x0051
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0014 }
            r4 = 24
            if (r1 < r4) goto L_0x0044
            android.location.OnNmeaMessageListener r1 = r5.c     // Catch:{ all -> 0x0014 }
            if (r1 == 0) goto L_0x003b
            com.amap.openapi.cz r0 = r5.b     // Catch:{ all -> 0x0014 }
            android.location.OnNmeaMessageListener r1 = r5.c     // Catch:{ all -> 0x0014 }
            boolean r0 = r0.a(r1, r7)     // Catch:{ all -> 0x0014 }
        L_0x003b:
            if (r0 != 0) goto L_0x0042
            java.util.List<com.amap.openapi.cy$a> r1 = r5.a     // Catch:{ all -> 0x0014 }
            r1.remove(r3)     // Catch:{ all -> 0x0014 }
        L_0x0042:
            monitor-exit(r2)     // Catch:{ all -> 0x0014 }
            goto L_0x0004
        L_0x0044:
            android.location.GpsStatus$NmeaListener r1 = r5.d     // Catch:{ all -> 0x0014 }
            if (r1 == 0) goto L_0x003b
            com.amap.openapi.cz r0 = r5.b     // Catch:{ all -> 0x0014 }
            android.location.GpsStatus$NmeaListener r1 = r5.d     // Catch:{ all -> 0x0014 }
            boolean r0 = r0.a(r1, r7)     // Catch:{ all -> 0x0014 }
            goto L_0x003b
        L_0x0051:
            monitor-exit(r2)     // Catch:{ all -> 0x0014 }
            r0 = r1
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.cy.a(com.amap.openapi.cs, android.os.Looper):boolean");
    }
}
