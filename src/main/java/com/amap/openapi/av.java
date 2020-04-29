package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.amap.location.collection.CollectionConfig;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.network.HttpRequest;
import com.amap.location.common.network.HttpResponse;
import com.amap.location.common.network.IHttpClient;
import com.amap.openapi.bj;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.IoUtils;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Executor;

/* compiled from: UploadManager */
public class av {
    /* access modifiers changed from: private */
    public Context a;
    private Handler b;
    /* access modifiers changed from: private */
    public CollectionConfig c;
    private SharedPreferences d;
    /* access modifiers changed from: private */
    public ConnectivityManager e;
    private BroadcastReceiver f;
    /* access modifiers changed from: private */
    public t g;
    /* access modifiers changed from: private */
    public k h;
    /* access modifiers changed from: private */
    public IHttpClient i;
    private bj j = new bj();
    private a k = new a();
    private Looper l;

    /* compiled from: UploadManager */
    private class a implements bj.a {
        private boolean b;

        private a() {
            this.b = true;
        }

        public Object a(long j) {
            return this.b ? av.this.a(true, 10000, j) : av.this.a(false, 10000, j);
        }

        public void a() {
        }

        public void a(int i) {
            ALLog.trace("@_3_3_@", "@_3_3_2_@" + i);
        }

        public void a(int i, Object obj) {
            av.this.a(i, obj);
        }

        public boolean a(Object obj) {
            au auVar = (au) obj;
            byte[] a2 = av.this.h.a(av.this.a, av.this.c, auVar);
            if (a2 == null) {
                return false;
            }
            byte[] bArr = null;
            try {
                boolean z = auVar.b.get(0).b() == 0;
                HashMap hashMap = new HashMap();
                hashMap.put("Content-Type", "application/octet-stream");
                HttpRequest httpRequest = new HttpRequest();
                httpRequest.headers = hashMap;
                httpRequest.body = a2;
                if (z) {
                    httpRequest.url = CollectionConfig.sUseTestNet ? "http://aps.testing.amap.com/collection/collectData?src=baseCol&ver=v74&" : "http://cgicol.amap.com/collection/collectData?src=baseCol&ver=v74&";
                } else {
                    httpRequest.url = CollectionConfig.sUseTestNet ? "http://aps.testing.amap.com/collection/collectData?src=extCol&ver=v74&" : "http://cgicol.amap.com/collection/collectData?src=extCol&ver=v74&";
                }
                HttpResponse post = av.this.i.post(httpRequest);
                if (post != null && post.statusCode == 200) {
                    bArr = post.body;
                }
                boolean z2 = bArr != null && "true".equals(new String(bArr, "UTF-8"));
                try {
                    ALLog.trace("@_3_3_@", "@_3_3_1_@" + (bArr != null ? new String(bArr, "UTF-8") : "null"));
                    return z2;
                } catch (Exception e) {
                    return z2;
                }
            } catch (Exception e2) {
                return false;
            }
        }

        public void b() {
        }

        public void b(Object obj) {
            av.this.a((au) obj);
            this.b = !this.b;
        }

        public boolean b(int i) {
            if (i == 1) {
                return true;
            }
            if (i == 0) {
                return av.this.c.getUploadConfig().isNonWifiUploadEnabled();
            }
            return false;
        }

        public long c() {
            int i = IoUtils.DEFAULT_IMAGE_TOTAL_SIZE;
            if (!this.b) {
                int d = av.this.g.d();
                if (d <= 0) {
                    this.b = true;
                } else if (d < 512000 && av.this.g.c() > 512000) {
                    this.b = true;
                }
            } else {
                int c = av.this.g.c();
                if (c <= 0) {
                    this.b = false;
                } else if (c < 512000 && av.this.g.d() > 512000) {
                    this.b = false;
                }
            }
            int c2 = this.b ? av.this.g.c() : av.this.g.d();
            if (c2 <= 4000) {
                i = c2;
            }
            return (long) i;
        }

        public long c(int i) {
            return av.this.a(i);
        }

        public int d() {
            return av.this.c.getUploadConfig().getMaxUploadFailCount();
        }

        public long d(int i) {
            return i == 1 ? 512000 : 51200;
        }

        public long e() {
            return 300000;
        }

        public int f() {
            return BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        }

        public void g() {
        }

        public Executor h() {
            return null;
        }
    }

    public av(Context context, Looper looper, t tVar, IHttpClient iHttpClient, CollectionConfig collectionConfig) {
        this.a = context;
        this.l = looper;
        this.g = tVar;
        this.i = iHttpClient;
        this.c = collectionConfig;
        this.e = (ConnectivityManager) context.getSystemService("connectivity");
        this.h = new k();
        this.d = context.getSharedPreferences("AMAP_LOCATION_COLLECTOR", 0);
        if (!e()) {
            f();
        }
    }

    private void a(String str, int i2) {
        SharedPreferences.Editor edit = this.d.edit();
        edit.putInt(str, i2);
        if (Build.VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
    }

    private void c() {
        this.f = new BroadcastReceiver() {
            /* class com.amap.openapi.av.AnonymousClass1 */

            public void onReceive(Context context, Intent intent) {
                boolean z = true;
                try {
                    NetworkInfo activeNetworkInfo = av.this.e.getActiveNetworkInfo();
                    if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                        z = false;
                    }
                } catch (Throwable th) {
                }
                try {
                    if (!isInitialStickyBroadcast() && z) {
                        av.this.d();
                    }
                } catch (Throwable th2) {
                }
            }
        };
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.a.registerReceiver(this.f, intentFilter, null, this.b);
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private */
    public void d() {
        try {
            if (g()) {
                this.j.a(500);
            }
        } catch (Throwable th) {
        }
    }

    private boolean e() {
        return Calendar.getInstance().get(6) == this.d.getInt("today_value", 0);
    }

    private void f() {
        a("today_value", Calendar.getInstance().get(6));
        a("uploaded_wifi_size", 0);
        a("uploaded_gprs_size", 0);
    }

    private boolean g() {
        return be.a(this.a) || this.c.getUploadConfig().isNonWifiUploadEnabled();
    }

    public synchronized long a(int i2) {
        long j2;
        int i3 = 0;
        synchronized (this) {
            if (i2 == 1) {
                if (!e()) {
                    f();
                }
                i3 = this.c.getUploadConfig().getMaxWifiUploadSizePerDay() - this.d.getInt("uploaded_wifi_size", 0);
            } else if (i2 == 0) {
                if (!e()) {
                    f();
                }
                i3 = this.c.getUploadConfig().getMaxMobileUploadSizePerDay() - this.d.getInt("uploaded_gprs_size", 0);
            }
            j2 = (long) i3;
        }
        return j2;
    }

    public synchronized au a(boolean z, int i2, long j2) {
        return this.g.a(z, i2, j2);
    }

    public void a() {
        this.b = new Handler(this.l);
        this.j.a(this.a, this.k, this.l);
        c();
        d();
    }

    public synchronized void a(int i2, Object obj) {
        if (obj != null) {
            if (!e()) {
                f();
            }
            au auVar = (au) obj;
            if (i2 == 1) {
                a("uploaded_wifi_size", this.d.getInt("uploaded_wifi_size", 0) + auVar.c);
            } else if (i2 == 0) {
                a("uploaded_gprs_size", this.d.getInt("uploaded_gprs_size", 0) + auVar.c);
            }
        }
    }

    public synchronized void a(au auVar) {
        if (auVar != null) {
            this.g.a(auVar);
        }
    }

    public void b() {
        try {
            this.j.a();
            if (this.f != null) {
                this.a.unregisterReceiver(this.f);
                this.f = null;
            }
        } catch (Throwable th) {
        }
        this.b.removeCallbacksAndMessages(null);
        this.b = null;
    }
}
