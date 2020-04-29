package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.util.f;
import com.amap.location.offline.IOfflineCloudConfig;
import com.amap.location.offline.OfflineConfig;
import com.amap.openapi.cd;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: OfflineDownloader */
public class cc {
    /* access modifiers changed from: private */
    public Context a;
    private OfflineConfig b;
    /* access modifiers changed from: private */
    public IOfflineCloudConfig c;
    /* access modifiers changed from: private */
    public a d;
    /* access modifiers changed from: private */
    public ReentrantReadWriteLock e = new ReentrantReadWriteLock();
    private cd f;
    /* access modifiers changed from: private */
    public long g;
    private BroadcastReceiver h = new BroadcastReceiver() {
        /* class com.amap.openapi.cc.AnonymousClass2 */

        public void onReceive(Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) && SystemClock.elapsedRealtime() - cc.this.g >= 10000) {
                cc.this.e.readLock().lock();
                try {
                    if (cc.this.d != null) {
                        cc.this.d.removeMessages(1);
                        cc.this.d.sendEmptyMessage(1);
                    }
                } finally {
                    cc.this.e.readLock().unlock();
                }
            }
        }
    };

    /* compiled from: OfflineDownloader */
    private class a extends Handler {
        a(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            ALLog.trace("@_18_5_@", "@_18_5_1_@" + message.what);
            try {
                switch (message.what) {
                    case 0:
                        if (cc.this.e()) {
                            cl.a().a(cc.this.a);
                            return;
                        }
                        return;
                    case 1:
                        cc.this.c();
                        return;
                    case 2:
                        cc.this.d();
                        return;
                    case 3:
                        if (cc.this.c.clearAll() && cp.a(cc.this.a, cc.this.c.getConfigTime())) {
                            by.a(cc.this.a).c();
                        }
                        by.a(cc.this.a).a();
                        by.a(cc.this.a).b();
                        return;
                    case 4:
                        cc.this.g();
                        getLooper().quit();
                        return;
                    default:
                        return;
                }
            } catch (Throwable th) {
            }
        }
    }

    /* compiled from: OfflineDownloader */
    private class b extends HandlerThread {
        public b(String str, int i) {
            super(str, i);
        }

        /* access modifiers changed from: protected */
        public void onLooperPrepared() {
            a unused = cc.this.d = new a(Looper.myLooper());
            synchronized (this) {
                notify();
            }
            long unused2 = cc.this.g = SystemClock.elapsedRealtime();
            cc.this.f();
            cc.this.e.readLock().lock();
            try {
                if (cc.this.d != null) {
                    cc.this.d.removeMessages(0);
                    cc.this.d.sendEmptyMessageDelayed(0, 10000);
                    cc.this.d.removeMessages(1);
                    cc.this.d.sendEmptyMessageDelayed(1, 10000);
                    cc.this.d.removeMessages(3);
                    cc.this.d.sendEmptyMessageDelayed(3, 15000);
                }
            } finally {
                cc.this.e.readLock().unlock();
            }
        }
    }

    public cc(@NonNull Context context, @NonNull OfflineConfig offlineConfig, @NonNull IOfflineCloudConfig iOfflineCloudConfig) {
        this.a = context;
        this.b = offlineConfig;
        this.c = iOfflineCloudConfig;
        this.f = new cd(this.a, this.b, this.c, new cd.a() {
            /* class com.amap.openapi.cc.AnonymousClass1 */

            public void a() {
                cc.this.e.readLock().lock();
                try {
                    if (cc.this.d != null) {
                        cc.this.d.removeMessages(2);
                        cc.this.d.sendMessage(cc.this.d.obtainMessage(2));
                    }
                } finally {
                    cc.this.e.readLock().unlock();
                }
            }
        });
    }

    private boolean a(int i) {
        return this.c.getNeedFirstDownload() && i == 1 && !cp.a(this.a);
    }

    private boolean b(int i) {
        boolean z = false;
        if (i == 1) {
            return cp.d(this.a, this.c.getMaxRequestTimes());
        }
        if (i != 0) {
            return false;
        }
        if (cp.d(this.a, this.c.getMaxRequestTimes()) && cp.e(this.a, this.c.getMaxNonWifiRequestTimes())) {
            z = true;
        }
        if (!z) {
            return z;
        }
        com.amap.location.offline.upload.a.a(100052);
        return z;
    }

    /* access modifiers changed from: private */
    public void c() {
        if (!this.f.a() && e()) {
            int a2 = f.a(this.a);
            if (a(a2)) {
                this.f.a((byte) 0, a2);
            } else if (b(a2)) {
                this.f.a((byte) 1, a2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void d() {
        int a2 = f.a(this.a);
        if (!this.f.a() && e() && b(a2)) {
            this.f.a((byte) 1, a2);
        }
    }

    /* access modifiers changed from: private */
    public boolean e() {
        return this.b != null && this.b.locEnable && this.c != null && this.c.isEnable();
    }

    /* access modifiers changed from: private */
    public void f() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.a.registerReceiver(this.h, intentFilter, null, this.d);
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private */
    public void g() {
        try {
            this.a.unregisterReceiver(this.h);
        } catch (Exception e2) {
        }
    }

    public void a() {
        b bVar = new b("OfflineDownloader", 10);
        bVar.start();
        synchronized (bVar) {
            ALLog.i("@_18_5_@", "offline-thread:before-start");
            while (this.d == null) {
                try {
                    bVar.wait();
                } catch (InterruptedException e2) {
                }
            }
            ALLog.i("@_18_5_@", "offline-thread:after-start");
        }
    }

    public void a(@NonNull OfflineConfig offlineConfig) {
        this.b = offlineConfig;
        this.f.a(offlineConfig);
    }

    public void b() {
        this.e.writeLock().lock();
        try {
            a aVar = this.d;
            this.d = null;
            if (aVar != null) {
                aVar.removeCallbacksAndMessages(null);
                aVar.sendEmptyMessage(4);
            }
        } finally {
            this.e.writeLock().unlock();
        }
    }
}
