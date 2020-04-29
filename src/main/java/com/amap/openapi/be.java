package com.amap.openapi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.amap.location.common.util.f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/* compiled from: WifiUtil */
public class be {
    public static List<ScanResult> a(@NonNull List<ScanResult> list) {
        Collections.sort(list, new Comparator<ScanResult>() {
            /* class com.amap.openapi.be.AnonymousClass1 */

            /* renamed from: a */
            public final int compare(ScanResult scanResult, ScanResult scanResult2) {
                return scanResult2.level - scanResult.level;
            }
        });
        return list;
    }

    public static void a(@NonNull List<aa> list, @Nullable List<ScanResult> list2) {
        list.clear();
        if (list2 != null) {
            List<ScanResult> a = a(b(list2));
            int size = a.size();
            if (size > 40) {
                size = 40;
            }
            for (int i = 0; i < size; i++) {
                ScanResult scanResult = a.get(i);
                if (scanResult != null) {
                    aa aaVar = new aa();
                    aaVar.a = f.a(scanResult.BSSID);
                    aaVar.b = (short) scanResult.level;
                    aaVar.c = scanResult.SSID != null ? scanResult.SSID.substring(0, Math.min(32, scanResult.SSID.length())) : "";
                    aaVar.f = (short) scanResult.frequency;
                    if (Build.VERSION.SDK_INT >= 17) {
                        aaVar.e = scanResult.timestamp / 1000;
                        aaVar.d = (short) ((int) ((SystemClock.elapsedRealtime() - aaVar.e) / 1000));
                        if (aaVar.d < 0) {
                            aaVar.d = 0;
                        }
                    }
                    list.add(aaVar);
                }
            }
        }
    }

    public static boolean a(@NonNull Context context) {
        return f.a(context) == 1;
    }

    public static boolean a(@Nullable WifiManager wifiManager) {
        if (wifiManager != null) {
            try {
                if (wifiManager.isWifiEnabled()) {
                    return true;
                }
                return Build.VERSION.SDK_INT >= 18 && wifiManager.isScanAlwaysAvailable();
            } catch (Exception | SecurityException e) {
            }
        }
    }

    public static boolean a(@Nullable List<ScanResult> list, @Nullable List<ScanResult> list2, double d) {
        List<ScanResult> list3;
        List<ScanResult> list4;
        if (list == null || list2 == null) {
            return false;
        }
        int size = list.size();
        int size2 = list2.size();
        int i = size + size2;
        if (size > size2) {
            list3 = list;
            list4 = list2;
        } else {
            list3 = list2;
            list4 = list;
        }
        HashMap hashMap = new HashMap(list3.size());
        for (ScanResult scanResult : list3) {
            if (scanResult.BSSID != null) {
                hashMap.put(scanResult.BSSID, 1);
            }
        }
        int i2 = 0;
        for (ScanResult scanResult2 : list4) {
            if (!(scanResult2.BSSID == null || ((Integer) hashMap.get(scanResult2.BSSID)) == null)) {
                i2++;
            }
            i2 = i2;
        }
        return ((double) i2) * 2.0d >= ((double) i) * d;
    }

    private static List<ScanResult> b(@NonNull List<ScanResult> list) {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < list.size()) {
                ScanResult scanResult = list.get(i2);
                hashMap.put(Integer.valueOf(scanResult.level), scanResult);
                i = i2 + 1;
            } else {
                arrayList.addAll(hashMap.values());
                return arrayList;
            }
        }
    }
}
