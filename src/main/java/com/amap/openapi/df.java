package com.amap.openapi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import java.util.List;

/* compiled from: AmapWifiManager */
public class df {
    private static volatile df c;
    private di a;
    private dh b;
    private long d;

    /* compiled from: AmapWifiManager */
    public interface a {
        void a();
    }

    private df(Context context) {
        this.a = dg.a(context);
        this.b = new dh(context, this.a);
    }

    public static df a(@NonNull Context context) {
        if (c == null) {
            synchronized (df.class) {
                if (c == null) {
                    c = new df(context.getApplicationContext());
                }
            }
        }
        return c;
    }

    public boolean a() {
        boolean z = false;
        if (cq.a <= 0 || SystemClock.elapsedRealtime() - this.d >= cq.a) {
            try {
                if ("true".equals(String.valueOf(de.a(this.a, "startScanActive", new Object[0])))) {
                    z = true;
                }
            } catch (Exception e) {
            }
            if (!z) {
                z = this.a.b();
            }
            this.d = SystemClock.elapsedRealtime();
        }
        return z;
    }

    @Nullable
    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public List<ScanResult> b() {
        return this.a.a();
    }

    public boolean c() {
        return this.a.c();
    }
}
