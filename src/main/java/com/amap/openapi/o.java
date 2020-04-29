package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import com.amap.location.common.util.f;
import dji.midware.media.DJIVideoDecoder;
import java.util.ArrayList;
import java.util.List;

/* compiled from: WifiCollector */
public class o {
    private static final String a = o.class.getSimpleName();
    private Context b;
    private Handler c;
    private WifiManager d;
    private BroadcastReceiver e;
    private long f = -1;
    private List<ScanResult> g = new ArrayList();
    private Location h;
    private ArrayList<aa> i = new ArrayList<>();

    public o(Context context, Looper looper) {
        this.b = context;
        this.d = (WifiManager) context.getSystemService("wifi");
        this.c = new Handler(looper);
    }

    /* access modifiers changed from: private */
    public void a(Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            char c2 = 65535;
            switch (action.hashCode()) {
                case -343630553:
                    if (action.equals("android.net.wifi.STATE_CHANGE")) {
                        c2 = 0;
                        break;
                    }
                    break;
            }
            switch (c2) {
                case 0:
                    d();
                    return;
                default:
                    return;
            }
        }
    }

    private boolean a(Location location) {
        float f2 = 10.0f;
        if (location.getSpeed() > 10.0f) {
            f2 = 200.0f;
        } else if (location.getSpeed() > 2.0f) {
            f2 = 50.0f;
        }
        return location.distanceTo(this.h) > f2;
    }

    private boolean a(Location location, long j, long j2) {
        int i2 = 3500;
        if (location.getSpeed() >= 10.0f) {
            i2 = DJIVideoDecoder.connectLosedelay;
        }
        return j > 0 && j2 - j < ((long) i2);
    }

    private boolean b(Location location, List<ScanResult> list, long j, long j2) {
        if (!be.a(this.d) || !a(location, j, j2) || list == null || list.size() <= 0) {
            return false;
        }
        if (this.h == null) {
            return true;
        }
        boolean a2 = a(location);
        if (a2) {
            return a2;
        }
        return !be.a(list, this.g, 0.5d);
    }

    private void d() {
        this.f = -1;
        try {
            WifiInfo connectionInfo = this.d == null ? null : this.d.getConnectionInfo();
            if (connectionInfo != null) {
                this.f = f.a(connectionInfo.getBSSID());
            }
        } catch (Throwable th) {
        }
    }

    public List<aa> a(Location location, List<ScanResult> list, long j, long j2) {
        if (!b(location, list, j, j2)) {
            return null;
        }
        be.a(this.i, list);
        this.g.clear();
        this.g.addAll(list);
        this.h = location;
        return this.i;
    }

    public void a() {
        this.e = new BroadcastReceiver() {
            /* class com.amap.openapi.o.AnonymousClass1 */

            public void onReceive(Context context, Intent intent) {
                try {
                    o.this.a(intent);
                } catch (Throwable th) {
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        try {
            this.b.registerReceiver(this.e, intentFilter, null, this.c);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        d();
    }

    public void b() {
        if (this.e != null) {
            try {
                this.b.unregisterReceiver(this.e);
                this.e = null;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        this.c.removeCallbacksAndMessages(null);
        this.c = null;
    }

    public long c() {
        return this.f;
    }
}
