package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.amap.location.collection.CollectionConfig;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: WifiScanner */
public class bh {
    private Context a;
    private Looper b;
    /* access modifiers changed from: private */
    public df c;
    /* access modifiers changed from: private */
    public ReentrantReadWriteLock d;
    private boolean e = false;
    private BroadcastReceiver f;
    /* access modifiers changed from: private */
    public Handler g = null;
    /* access modifiers changed from: private */
    public boolean h = true;
    private boolean i = true;
    /* access modifiers changed from: private */
    public int j = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
    private CollectionConfig.FpsCollectorConfig k;
    /* access modifiers changed from: private */
    public final Object l = new Object();
    /* access modifiers changed from: private */
    public List<ScanResult> m = new ArrayList();
    /* access modifiers changed from: private */
    public long n = 0;
    private Comparator<ScanResult> o = new Comparator<ScanResult>() {
        /* class com.amap.openapi.bh.AnonymousClass2 */

        /* renamed from: a */
        public int compare(ScanResult scanResult, ScanResult scanResult2) {
            int compareTo = scanResult.BSSID.compareTo(scanResult.BSSID);
            if (compareTo > 0) {
                return 1;
            }
            return compareTo == 0 ? 0 : -1;
        }
    };

    /* compiled from: WifiScanner */
    public static class a {
        public List<ScanResult> a = new ArrayList();
        public long b;
    }

    /* compiled from: WifiScanner */
    private final class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void handleMessage(android.os.Message r3) {
            /*
                r2 = this;
                int r0 = r3.what     // Catch:{ Throwable -> 0x0011 }
                switch(r0) {
                    case 0: goto L_0x0006;
                    case 1: goto L_0x0013;
                    case 2: goto L_0x005a;
                    default: goto L_0x0005;
                }     // Catch:{ Throwable -> 0x0011 }
            L_0x0005:
                return
            L_0x0006:
                com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ Throwable -> 0x0011 }
                r0.g()     // Catch:{ Throwable -> 0x0011 }
                com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ Throwable -> 0x0011 }
                r0.i()     // Catch:{ Throwable -> 0x0011 }
                goto L_0x0005
            L_0x0011:
                r0 = move-exception
                goto L_0x0005
            L_0x0013:
                com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ Throwable -> 0x0011 }
                r0.h()     // Catch:{ Throwable -> 0x0011 }
                com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ Throwable -> 0x0011 }
                java.util.concurrent.locks.ReentrantReadWriteLock r0 = r0.d     // Catch:{ Throwable -> 0x0011 }
                java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = r0.writeLock()     // Catch:{ Throwable -> 0x0011 }
                r0.lock()     // Catch:{ Throwable -> 0x0011 }
                com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ all -> 0x004b }
                android.os.Handler r0 = r0.g     // Catch:{ all -> 0x004b }
                if (r0 == 0) goto L_0x003d
                com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ all -> 0x004b }
                android.os.Handler r0 = r0.g     // Catch:{ all -> 0x004b }
                r1 = 0
                r0.removeCallbacksAndMessages(r1)     // Catch:{ all -> 0x004b }
                com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ all -> 0x004b }
                r1 = 0
                android.os.Handler unused = r0.g = r1     // Catch:{ all -> 0x004b }
            L_0x003d:
                com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ Throwable -> 0x0011 }
                java.util.concurrent.locks.ReentrantReadWriteLock r0 = r0.d     // Catch:{ Throwable -> 0x0011 }
                java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = r0.writeLock()     // Catch:{ Throwable -> 0x0011 }
                r0.unlock()     // Catch:{ Throwable -> 0x0011 }
                goto L_0x0005
            L_0x004b:
                r0 = move-exception
                com.amap.openapi.bh r1 = com.amap.openapi.bh.this     // Catch:{ Throwable -> 0x0011 }
                java.util.concurrent.locks.ReentrantReadWriteLock r1 = r1.d     // Catch:{ Throwable -> 0x0011 }
                java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r1 = r1.writeLock()     // Catch:{ Throwable -> 0x0011 }
                r1.unlock()     // Catch:{ Throwable -> 0x0011 }
                throw r0     // Catch:{ Throwable -> 0x0011 }
            L_0x005a:
                com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ Throwable -> 0x0011 }
                r0.i()     // Catch:{ Throwable -> 0x0011 }
                goto L_0x0005
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.bh.b.handleMessage(android.os.Message):void");
        }
    }

    public bh(Context context, CollectionConfig.FpsCollectorConfig fpsCollectorConfig, Looper looper) {
        this.a = context;
        this.h = fpsCollectorConfig.isScanWifiAllowed();
        this.j = fpsCollectorConfig.getScanWifiInterval();
        this.i = fpsCollectorConfig.isScanActiveAllowed();
        this.k = fpsCollectorConfig;
        this.b = looper;
        this.d = new ReentrantReadWriteLock();
        this.c = df.a(this.a);
    }

    private void a(BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver != null && this.a != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
            try {
                this.a.registerReceiver(broadcastReceiver, intentFilter);
            } catch (Throwable th) {
            }
        }
    }

    private void b(BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver != null && this.a != null) {
            try {
                this.a.unregisterReceiver(broadcastReceiver);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void g() {
        this.f = new BroadcastReceiver() {
            /* class com.amap.openapi.bh.AnonymousClass1 */

            /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
                return;
             */
            /* JADX WARNING: Removed duplicated region for block: B:37:0x00aa A[Catch:{ all -> 0x00b5, SecurityException -> 0x00aa, Throwable -> 0x00b3 }, ExcHandler: SecurityException (e java.lang.SecurityException), Splitter:B:22:0x0061] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onReceive(android.content.Context r5, android.content.Intent r6) {
                /*
                    r4 = this;
                    r0 = 1
                    if (r6 == 0) goto L_0x00a6
                    java.lang.String r1 = r6.getAction()     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    if (r1 == 0) goto L_0x00a6
                    java.lang.String r1 = "android.net.wifi.SCAN_RESULTS"
                    java.lang.String r2 = r6.getAction()     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    boolean r1 = r1.equals(r2)     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    if (r1 == 0) goto L_0x00a6
                    com.amap.openapi.bh r1 = com.amap.openapi.bh.this     // Catch:{ Throwable -> 0x00c4, SecurityException -> 0x00aa }
                    boolean r1 = r1.j()     // Catch:{ Throwable -> 0x00c4, SecurityException -> 0x00aa }
                    if (r1 == 0) goto L_0x0030
                    android.os.Bundle r1 = r6.getExtras()     // Catch:{ Throwable -> 0x00c4, SecurityException -> 0x00aa }
                    if (r1 == 0) goto L_0x0030
                    android.os.Bundle r1 = r6.getExtras()     // Catch:{ Throwable -> 0x00c4, SecurityException -> 0x00aa }
                    java.lang.String r2 = "resultsUpdated"
                    r3 = 1
                    boolean r0 = r1.getBoolean(r2, r3)     // Catch:{ Throwable -> 0x00c4, SecurityException -> 0x00aa }
                L_0x0030:
                    r1 = 100067(0x186e3, float:1.40224E-40)
                    com.amap.openapi.dl.a(r1)     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    if (r0 == 0) goto L_0x00ac
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    java.lang.Object r1 = r0.l     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    monitor-enter(r1)     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ all -> 0x00a7 }
                    long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00a7 }
                    long unused = r0.n = r2     // Catch:{ all -> 0x00a7 }
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ all -> 0x00a7 }
                    com.amap.openapi.bh r2 = com.amap.openapi.bh.this     // Catch:{ all -> 0x00a7 }
                    com.amap.openapi.df r2 = r2.c     // Catch:{ all -> 0x00a7 }
                    java.util.List r2 = r2.b()     // Catch:{ all -> 0x00a7 }
                    java.util.List unused = r0.m = r2     // Catch:{ all -> 0x00a7 }
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ all -> 0x00a7 }
                    java.util.List r0 = r0.m     // Catch:{ all -> 0x00a7 }
                    com.amap.openapi.as.b(r0)     // Catch:{ all -> 0x00a7 }
                    monitor-exit(r1)     // Catch:{ all -> 0x00a7 }
                L_0x0061:
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    boolean r0 = r0.h     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    if (r0 == 0) goto L_0x00a6
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    java.util.concurrent.locks.ReentrantReadWriteLock r0 = r0.d     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r0.readLock()     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    r0.lock()     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ all -> 0x00b5 }
                    android.os.Handler r0 = r0.g     // Catch:{ all -> 0x00b5 }
                    if (r0 == 0) goto L_0x0099
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ all -> 0x00b5 }
                    android.os.Handler r0 = r0.g     // Catch:{ all -> 0x00b5 }
                    r1 = 2
                    r0.removeMessages(r1)     // Catch:{ all -> 0x00b5 }
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ all -> 0x00b5 }
                    android.os.Handler r0 = r0.g     // Catch:{ all -> 0x00b5 }
                    r1 = 2
                    com.amap.openapi.bh r2 = com.amap.openapi.bh.this     // Catch:{ all -> 0x00b5 }
                    int r2 = r2.j     // Catch:{ all -> 0x00b5 }
                    long r2 = (long) r2     // Catch:{ all -> 0x00b5 }
                    r0.sendEmptyMessageDelayed(r1, r2)     // Catch:{ all -> 0x00b5 }
                L_0x0099:
                    com.amap.openapi.bh r0 = com.amap.openapi.bh.this     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    java.util.concurrent.locks.ReentrantReadWriteLock r0 = r0.d     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r0.readLock()     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    r0.unlock()     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                L_0x00a6:
                    return
                L_0x00a7:
                    r0 = move-exception
                    monitor-exit(r1)     // Catch:{ all -> 0x00a7 }
                    throw r0     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                L_0x00aa:
                    r0 = move-exception
                    goto L_0x00a6
                L_0x00ac:
                    r0 = 100068(0x186e4, float:1.40225E-40)
                    com.amap.openapi.dl.a(r0)     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    goto L_0x0061
                L_0x00b3:
                    r0 = move-exception
                    goto L_0x00a6
                L_0x00b5:
                    r0 = move-exception
                    com.amap.openapi.bh r1 = com.amap.openapi.bh.this     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    java.util.concurrent.locks.ReentrantReadWriteLock r1 = r1.d     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r1 = r1.readLock()     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    r1.unlock()     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                    throw r0     // Catch:{ SecurityException -> 0x00aa, Throwable -> 0x00b3 }
                L_0x00c4:
                    r1 = move-exception
                    goto L_0x0030
                */
                throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.bh.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        a(this.f);
    }

    /* access modifiers changed from: private */
    public void h() {
        synchronized (this.l) {
            this.n = 0;
            if (this.m != null) {
                this.m.clear();
            }
        }
        if (this.f != null) {
            b(this.f);
        }
    }

    /* access modifiers changed from: private */
    public void i() {
        Object a2;
        boolean z = false;
        if (this.h && this.c != null && this.c.c()) {
            try {
                if (Build.VERSION.SDK_INT < 18 && this.i && (a2 = bc.a(this.c, "startScanActive", new Object[0])) != null && "true".equals(String.valueOf(a2))) {
                    z = true;
                }
            } catch (Exception e2) {
            }
            if (!z) {
                try {
                    this.c.a();
                } catch (Exception e3) {
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean j() {
        CollectionConfig.FpsCollectorConfig fpsCollectorConfig = this.k;
        if (this.k != null) {
            return fpsCollectorConfig.isWifiFilterByUpdated();
        }
        return true;
    }

    public void a() {
        if (!this.e) {
            this.e = true;
            this.d.writeLock().lock();
            try {
                if (this.g == null) {
                    this.g = new b(this.b);
                }
                this.g.sendEmptyMessage(0);
            } finally {
                this.d.writeLock().unlock();
            }
        }
    }

    public void b() {
        if (this.e) {
            this.e = false;
            this.d.readLock().lock();
            try {
                if (this.g != null) {
                    this.g.sendEmptyMessage(1);
                }
            } finally {
                this.d.readLock().unlock();
            }
        }
    }

    public void c() {
        this.d.readLock().lock();
        try {
            if (this.g != null && !this.g.hasMessages(2)) {
                this.g.sendEmptyMessage(2);
            }
        } finally {
            this.d.readLock().unlock();
        }
    }

    public void d() {
        this.d.readLock().lock();
        try {
            if (this.g != null) {
                this.g.removeMessages(2);
            }
        } finally {
            this.d.readLock().unlock();
        }
    }

    public boolean e() {
        return this.e;
    }

    public a f() {
        a aVar = new a();
        synchronized (this.l) {
            if (this.m == null) {
                return aVar;
            }
            for (ScanResult scanResult : this.m) {
                aVar.a.add(scanResult);
            }
            aVar.b = this.n;
            return aVar;
        }
    }
}
