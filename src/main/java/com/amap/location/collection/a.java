package com.amap.location.collection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.common.util.f;
import com.amap.openapi.as;
import com.amap.openapi.at;
import com.amap.openapi.au;
import com.amap.openapi.av;
import com.amap.openapi.az;
import com.amap.openapi.bf;
import com.amap.openapi.bg;
import com.amap.openapi.k;
import com.amap.openapi.m;
import com.amap.openapi.n;
import com.amap.openapi.t;
import java.util.List;

/* compiled from: CollectionManager */
public class a {
    /* access modifiers changed from: private */
    public Context a;
    /* access modifiers changed from: private */
    public CollectionConfig b;
    /* access modifiers changed from: private */
    public IHttpClient c;
    /* access modifiers changed from: private */
    public t d;
    /* access modifiers changed from: private */
    public av e;
    private m f;
    private n g;
    /* access modifiers changed from: private */
    public HandlerThread h;
    /* access modifiers changed from: private */
    public volatile b i;
    /* access modifiers changed from: private */
    public Looper j;
    /* access modifiers changed from: private */
    public boolean k = false;
    /* access modifiers changed from: private */
    public final Object l = new Object();
    /* access modifiers changed from: private */
    public C0007a m;
    private k n;
    private bg o;
    private bf p;

    /* renamed from: com.amap.location.collection.a$a  reason: collision with other inner class name */
    /* compiled from: CollectionManager */
    private class C0007a extends BroadcastReceiver {
        private C0007a() {
        }

        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (action != null) {
                    char c = 65535;
                    switch (action.hashCode()) {
                        case -2128145023:
                            if (action.equals("android.intent.action.SCREEN_OFF")) {
                                c = 0;
                                break;
                            }
                            break;
                        case -1454123155:
                            if (action.equals("android.intent.action.SCREEN_ON")) {
                                c = 1;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            if (a.this.b.isStopCollectionWhenScreenOff()) {
                                a.this.f();
                                return;
                            }
                            return;
                        case 1:
                            if (a.this.b.isStopCollectionWhenScreenOff()) {
                                a.this.e();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                }
            } catch (Throwable th) {
            }
        }
    }

    /* compiled from: CollectionManager */
    class b extends Handler {
        b(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    if (a.this.m != null) {
                        try {
                            a.this.a.unregisterReceiver(a.this.m);
                            C0007a unused = a.this.m = (C0007a) null;
                        } catch (Throwable th) {
                        }
                    }
                    a.this.f();
                    removeCallbacksAndMessages(null);
                    a.this.e.b();
                    a.this.d.b();
                    post(new Runnable() {
                        /* class com.amap.location.collection.a.b.AnonymousClass1 */

                        public void run() {
                            try {
                                a.this.h.quit();
                            } catch (Throwable th) {
                            }
                        }
                    });
                    return;
                default:
                    return;
            }
        }
    }

    public a(Context context, CollectionConfig collectionConfig, IHttpClient iHttpClient) {
        this.a = context;
        this.b = collectionConfig;
        this.c = iHttpClient;
    }

    /* access modifiers changed from: private */
    public void a(Location location, List<ScanResult> list, long j2, long j3) {
        try {
            g();
            if (this.b.getFpsCollectorConfig().isEnabled()) {
                this.f.a(location, list, j2, j3);
            }
            if (this.b.getTrackCollectorConfig().isEnabled()) {
                this.g.a(location);
            }
        } catch (Throwable th) {
        }
    }

    private boolean d() {
        return this.b.getFpsCollectorConfig().isEnabled() || this.b.getTrackCollectorConfig().isEnabled();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.min(long, long):long}
     arg types: [long, int]
     candidates:
      ClspMth{java.lang.Math.min(double, double):double}
      ClspMth{java.lang.Math.min(float, float):float}
      ClspMth{java.lang.Math.min(int, int):int}
      ClspMth{java.lang.Math.min(long, long):long} */
    /* access modifiers changed from: private */
    public void e() {
        long j2;
        int i2 = 5;
        if (this.p == null) {
            boolean isEnabled = this.b.getFpsCollectorConfig().isEnabled();
            boolean isEnabled2 = this.b.getTrackCollectorConfig().isEnabled();
            long j3 = 0;
            int i3 = 0;
            if (isEnabled) {
                j3 = 1000;
                i3 = 10;
            }
            if (isEnabled2) {
                j2 = isEnabled ? Math.min(j2, 2000L) : 2000;
                if (isEnabled) {
                    i2 = Math.min(i3, 5);
                }
            } else {
                i2 = i3;
            }
            try {
                this.p = new bf() {
                    /* class com.amap.location.collection.a.AnonymousClass2 */

                    public void a(Location location, List<ScanResult> list, long j, long j2) {
                        a.this.a(location, list, j, j2);
                    }
                };
                if (this.o == null) {
                    this.o = new bg(this.a, this.b.getFpsCollectorConfig(), this.p, this.j);
                }
                this.o.a("passive", j2, (float) i2);
            } catch (Throwable th) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void f() {
        try {
            if (this.p != null && this.o != null) {
                this.o.c();
                this.o.a();
                this.p = null;
                h();
                as.a();
            }
        } catch (Throwable th) {
        }
    }

    private void g() {
        if (this.b.getFpsCollectorConfig().isEnabled() && this.f == null) {
            this.f = new m(this.a, this.d, this.b.getFpsCollectorConfig(), this.j);
            this.f.a();
        }
        if (this.b.getTrackCollectorConfig().isEnabled() && this.g == null) {
            this.g = new n(this.a, this.d, this.b.getTrackCollectorConfig(), this.j);
            this.g.a();
        }
    }

    private void h() {
        if (this.f != null) {
            this.f.b();
            this.f = null;
        }
        if (this.g != null) {
            this.g.b();
            this.g = null;
        }
    }

    public void a() {
        if (d()) {
            this.h = new HandlerThread("collection") {
                /* class com.amap.location.collection.a.AnonymousClass1 */

                /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                 method: com.amap.location.collection.a.a(com.amap.location.collection.a, boolean):boolean
                 arg types: [com.amap.location.collection.a, int]
                 candidates:
                  com.amap.location.collection.a.a(com.amap.location.collection.a, android.os.Looper):android.os.Looper
                  com.amap.location.collection.a.a(com.amap.location.collection.a, com.amap.location.collection.a$a):com.amap.location.collection.a$a
                  com.amap.location.collection.a.a(com.amap.location.collection.a, com.amap.location.collection.a$b):com.amap.location.collection.a$b
                  com.amap.location.collection.a.a(com.amap.location.collection.a, com.amap.openapi.av):com.amap.openapi.av
                  com.amap.location.collection.a.a(com.amap.location.collection.a, com.amap.openapi.t):com.amap.openapi.t
                  com.amap.location.collection.a.a(boolean, com.amap.openapi.at):void
                  com.amap.location.collection.a.a(com.amap.location.collection.a, boolean):boolean */
                /* access modifiers changed from: protected */
                public void onLooperPrepared() {
                    try {
                        Looper unused = a.this.j = getLooper();
                        t unused2 = a.this.d = new t(a.this.a, a.this.j);
                        a.this.d.a();
                        av unused3 = a.this.e = new av(a.this.a, a.this.j, a.this.d, a.this.c, a.this.b);
                        a.this.e.a();
                        synchronized (a.this.l) {
                            b unused4 = a.this.i = new b(a.this.j);
                            if (a.this.k) {
                                boolean unused5 = a.this.k = false;
                                a.this.i.obtainMessage(1).sendToTarget();
                            }
                        }
                        if (a.this.b.isStopCollectionWhenScreenOff()) {
                            C0007a unused6 = a.this.m = new C0007a();
                            IntentFilter intentFilter = new IntentFilter();
                            intentFilter.addAction("android.intent.action.SCREEN_ON");
                            intentFilter.addAction("android.intent.action.SCREEN_OFF");
                            try {
                                a.this.a.registerReceiver(a.this.m, intentFilter, null, a.this.i);
                            } catch (Throwable th) {
                            }
                            if (!az.c(a.this.a)) {
                                return;
                            }
                        }
                        a.this.e();
                    } catch (Throwable th2) {
                    }
                }
            };
            this.h.start();
        }
    }

    public void a(boolean z, at atVar) {
        if (atVar != null && this.i != null) {
            try {
                au auVar = (au) atVar.b;
                this.e.a(f.a(this.a), auVar);
                if (z) {
                    this.e.a(auVar);
                }
            } catch (Throwable th) {
            }
        }
    }

    public void b() {
        synchronized (this.l) {
            if (this.i != null) {
                this.i.obtainMessage(1).sendToTarget();
            } else {
                this.k = true;
            }
        }
    }

    public at c() {
        au a2;
        byte[] a3;
        if (this.i != null) {
            try {
                if (this.n == null) {
                    this.n = new k();
                }
                if (this.e.a(f.a(this.a)) > 0 && (a2 = this.e.a(true, 1, PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) != null && a2.b.size() > 0 && (a3 = this.n.a(this.a, this.b, a2)) != null) {
                    at atVar = new at();
                    try {
                        atVar.a = a3;
                        atVar.b = a2;
                        return atVar;
                    } catch (Throwable th) {
                        return atVar;
                    }
                }
            } catch (Throwable th2) {
                return null;
            }
        }
        return null;
    }
}
