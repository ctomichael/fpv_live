package com.amap.openapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.util.d;
import com.amap.location.security.Core;
import com.loc.fc;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: LocationCloudScheduler */
public class c {
    private d a;
    private Context b;
    /* access modifiers changed from: private */
    public Handler c;
    /* access modifiers changed from: private */
    public a d;
    /* access modifiers changed from: private */
    public ReentrantReadWriteLock e = new ReentrantReadWriteLock();
    private final List<f> f = new ArrayList();
    private a g;
    /* access modifiers changed from: private */
    public boolean h;
    private Runnable i = new Runnable() {
        /* class com.amap.openapi.c.AnonymousClass3 */

        public void run() {
            c.this.e();
        }
    };

    /* compiled from: LocationCloudScheduler */
    private final class a extends HandlerThread {
        protected volatile boolean a;

        public a(String str, int i) {
            super(str, i);
        }

        /* access modifiers changed from: protected */
        public final void onLooperPrepared() {
            c.this.e.writeLock().lock();
            try {
                if (this.a) {
                    Looper looper = getLooper();
                    if (looper != null) {
                        looper.quit();
                    }
                    return;
                }
                Handler unused = c.this.c = new Handler(Looper.myLooper());
                c.this.e.writeLock().unlock();
                try {
                    c.this.b();
                    c.this.c();
                } catch (Throwable th) {
                }
            } finally {
                c.this.e.writeLock().unlock();
            }
        }
    }

    protected c() {
    }

    private void a(a aVar) {
        synchronized (this.f) {
            for (int i2 = 0; i2 < this.f.size(); i2++) {
                this.f.get(i2).a(aVar);
            }
        }
    }

    private void a(String str) {
        SharedPreferences sharedPreferences = this.b.getSharedPreferences("LocationCloudConfig", 0);
        a aVar = new a();
        if (aVar.a(str)) {
            long currentTimeMillis = System.currentTimeMillis();
            sharedPreferences.edit().putString("command", str).putLong("lasttime", currentTimeMillis).commit();
            aVar.d = currentTimeMillis;
            this.d = aVar;
            a(aVar);
            this.e.readLock().lock();
            if (this.c != null) {
                this.c.postDelayed(this.i, this.d.a);
            }
            this.e.readLock().unlock();
            ALLog.trace("@_2_1_@", "@_2_1_8_@");
            return;
        }
        h();
        ALLog.trace("@_2_1_@", "@_2_1_9_@");
    }

    private void a(byte[] bArr) {
        String b2 = b(bArr);
        if (b2 != null) {
            a(b2);
            return;
        }
        ALLog.trace("@_2_1_@", "@_2_1_7_@");
        h();
    }

    private String b(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            byte[] xxt = Core.xxt(d.b(bArr), -1);
            if (xxt == null) {
                return null;
            }
            String intern = new String(xxt, "utf-8").intern();
            ALLog.trace("@_2_1_@", "@_2_1_10_@" + intern);
            if (e.a(intern)) {
                return intern;
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void b() {
        SharedPreferences sharedPreferences = this.b.getSharedPreferences("LocationCloudConfig", 0);
        String string = sharedPreferences.getString("command", "");
        long j = sharedPreferences.getLong("lasttime", 0);
        if (!TextUtils.isEmpty(string) && e.a(string)) {
            a aVar = new a();
            if (aVar.a(string)) {
                aVar.d = j;
                this.d = aVar;
                a(aVar);
                ALLog.trace("@_2_1_@", "@_2_1_3_@");
                return;
            }
        }
        ALLog.trace("@_2_1_@", "@_2_1_4_@");
        g();
    }

    /* access modifiers changed from: private */
    public void c() {
        this.e.readLock().lock();
        try {
            if (this.c != null) {
                if (d()) {
                    this.c.post(this.i);
                } else {
                    this.c.postDelayed(this.i, this.d.a);
                }
            }
        } finally {
            this.e.readLock().unlock();
        }
    }

    private boolean d() {
        if (this.d == null) {
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis() - this.d.d;
        return currentTimeMillis >= this.d.a || currentTimeMillis < 0;
    }

    /* access modifiers changed from: private */
    public void e() {
        ALLog.trace("@_2_1_@", "@_2_1_5_@");
        byte[] f2 = f();
        if (f2 != null) {
            a(e.a(d.a ? "http://aps.testing.amap.com/conf/r?type=3&mid=300&sver=140" : "http://control.aps.amap.com/conf/r?type=3&mid=300&sver=140", f2, this.a));
            return;
        }
        ALLog.trace("@_2_1_@", "@_2_1_6_@");
        h();
    }

    private byte[] f() {
        try {
            fc fcVar = new fc();
            int a2 = fcVar.a(this.b.getPackageName());
            int a3 = fcVar.a(this.a.b());
            int a4 = fcVar.a(com.amap.location.common.a.c(this.b));
            String e2 = this.a.e();
            if (TextUtils.isEmpty(e2)) {
                e2 = com.amap.location.common.a.b(this.b);
            }
            int a5 = fcVar.a(e2);
            int a6 = fcVar.a(com.amap.location.common.a.a(this.b));
            int a7 = fcVar.a(com.amap.location.common.a.d(this.b));
            int a8 = fcVar.a(com.amap.location.common.a.c());
            int a9 = fcVar.a(com.amap.location.common.a.b());
            int a10 = fcVar.a(this.a.d());
            int a11 = fcVar.a(this.a.c());
            bl.a(fcVar);
            bl.a(fcVar, this.a.a());
            bl.a(fcVar, a2);
            bl.b(fcVar, a3);
            bl.b(fcVar, (byte) com.amap.location.common.a.d());
            bl.c(fcVar, a4);
            bl.d(fcVar, a5);
            bl.e(fcVar, a6);
            bl.f(fcVar, a7);
            bl.a(fcVar, com.amap.location.common.a.e(this.b));
            bl.g(fcVar, a8);
            bl.h(fcVar, a9);
            bl.i(fcVar, a10);
            bl.j(fcVar, a11);
            fcVar.h(bl.b(fcVar));
            return Core.xxt(fcVar.f(), 1);
        } catch (Error | Exception e3) {
            return null;
        }
    }

    private void g() {
        this.h = true;
        synchronized (this.f) {
            for (int i2 = 0; i2 < this.f.size(); i2++) {
                this.f.get(i2).a();
            }
        }
    }

    private void h() {
        this.e.readLock().lock();
        try {
            if (this.c != null) {
                this.c.postDelayed(this.i, 3600000);
            }
        } finally {
            this.e.readLock().unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void a() {
        ALLog.trace("@_2_1_@", "@_2_1_2_@");
        if (this.g != null) {
            this.g.a = true;
        }
        this.e.writeLock().lock();
        final Handler handler = this.c;
        this.c = null;
        this.e.writeLock().unlock();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler.post(new Runnable() {
                /* class com.amap.openapi.c.AnonymousClass2 */

                public void run() {
                    Looper looper = handler.getLooper();
                    if (looper != null) {
                        looper.quit();
                    }
                }
            });
        }
        synchronized (this.f) {
            this.f.clear();
        }
    }

    /* access modifiers changed from: protected */
    public void a(Context context, d dVar) {
        ALLog.trace("@_2_1_@", "@_2_1_1_@");
        this.b = context;
        this.a = dVar;
        this.g = new a("LocationCloudScheduler", 10);
        this.g.a = false;
        this.g.start();
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(final com.amap.openapi.f r4) {
        /*
            r3 = this;
            if (r4 == 0) goto L_0x000e
            java.util.List<com.amap.openapi.f> r1 = r3.f
            monitor-enter(r1)
            java.util.List<com.amap.openapi.f> r0 = r3.f     // Catch:{ all -> 0x0034 }
            boolean r0 = r0.contains(r4)     // Catch:{ all -> 0x0034 }
            if (r0 == 0) goto L_0x000f
            monitor-exit(r1)     // Catch:{ all -> 0x0034 }
        L_0x000e:
            return
        L_0x000f:
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = r3.e     // Catch:{ all -> 0x0034 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r0.readLock()     // Catch:{ all -> 0x0034 }
            r0.lock()     // Catch:{ all -> 0x0034 }
            android.os.Handler r0 = r3.c     // Catch:{ all -> 0x0037 }
            if (r0 == 0) goto L_0x0024
            com.amap.openapi.c$1 r2 = new com.amap.openapi.c$1     // Catch:{ all -> 0x0037 }
            r2.<init>(r4)     // Catch:{ all -> 0x0037 }
            r0.post(r2)     // Catch:{ all -> 0x0037 }
        L_0x0024:
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = r3.e     // Catch:{ all -> 0x0034 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r0.readLock()     // Catch:{ all -> 0x0034 }
            r0.unlock()     // Catch:{ all -> 0x0034 }
            java.util.List<com.amap.openapi.f> r0 = r3.f     // Catch:{ all -> 0x0034 }
            r0.add(r4)     // Catch:{ all -> 0x0034 }
            monitor-exit(r1)     // Catch:{ all -> 0x0034 }
            goto L_0x000e
        L_0x0034:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0034 }
            throw r0
        L_0x0037:
            r0 = move-exception
            java.util.concurrent.locks.ReentrantReadWriteLock r2 = r3.e     // Catch:{ all -> 0x0034 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r2 = r2.readLock()     // Catch:{ all -> 0x0034 }
            r2.unlock()     // Catch:{ all -> 0x0034 }
            throw r0     // Catch:{ all -> 0x0034 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.c.a(com.amap.openapi.f):void");
    }

    /* access modifiers changed from: protected */
    public void b(f fVar) {
        if (fVar != null) {
            synchronized (this.f) {
                if (this.f.contains(fVar)) {
                    this.f.remove(fVar);
                }
            }
        }
    }
}
