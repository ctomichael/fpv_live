package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import com.amap.location.common.log.ALLog;
import com.amap.openapi.df;
import java.util.List;

/* compiled from: SystemWifiProvider */
public class dj implements di {
    private WifiManager a;

    public dj(Context context) {
        this.a = (WifiManager) context.getSystemService("wifi");
    }

    @Nullable
    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public List<ScanResult> a() {
        try {
            if (this.a == null) {
                return null;
            }
            return this.a.getScanResults();
        } catch (SecurityException e) {
            ALLog.trace("@_24_3_@", "@_24_3_1_@");
            return null;
        }
    }

    public void a(Context context, final df.a aVar) {
        try {
            context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
                /* class com.amap.openapi.dj.AnonymousClass1 */
                df.a a = aVar;

                public void onReceive(Context context, Intent intent) {
                    if (intent != null && "android.net.wifi.SCAN_RESULTS".equals(intent.getAction()) && this.a != null) {
                        this.a.a();
                    }
                }
            }, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
        } catch (Throwable th) {
        }
    }

    @RequiresPermission("android.permission.CHANGE_WIFI_STATE")
    public boolean b() {
        try {
            return this.a != null && this.a.startScan();
        } catch (SecurityException e) {
            ALLog.trace("@_24_3_@", "@_24_3_2_@");
            return false;
        } catch (Exception e2) {
            ALLog.trace("@_24_3_@", "@_24_3_3_@" + e2.toString());
            return false;
        }
    }

    public boolean c() {
        try {
            if (this.a == null) {
                return false;
            }
            return this.a.isWifiEnabled();
        } catch (Exception e) {
            ALLog.trace("@_24_3_@", "@_24_3_9_@", e);
            return false;
        }
    }
}
