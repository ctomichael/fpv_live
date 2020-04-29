package com.amap.location.common.model;

import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.SystemClock;
import com.amap.location.common.util.f;
import dji.component.accountcenter.IMemberProtocol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class WifiStatus {
    public WiFi mainWifi;
    public long updateTime;
    private List<WiFi> wifiList = Collections.emptyList();

    public WifiStatus() {
    }

    public WifiStatus(long j) {
        this.updateTime = j;
    }

    public WifiStatus(long j, List<ScanResult> list) {
        this.updateTime = j;
        this.wifiList = scanResultList2WifiList(list);
    }

    public WifiStatus(long j, List<WiFi> list, int i) {
        this.updateTime = j;
        this.wifiList = list;
    }

    private String toStr(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("wifiStatus:[");
        sb.append("updateTime=" + this.updateTime + ",");
        if (this.mainWifi != null) {
            sb.append("mainWifi:[" + this.mainWifi.toString() + "],");
        } else {
            sb.append("mainWifi:[null],");
        }
        if (this.wifiList != null) {
            ArrayList arrayList = new ArrayList();
            if (this.wifiList.size() <= 5) {
                arrayList.addAll(this.wifiList);
                sb.append("wifiList=" + this.wifiList.toString());
            } else if (z) {
                arrayList.addAll(this.wifiList.subList(0, 5));
                sb.append("wifiList=" + arrayList.toString());
            } else {
                arrayList.addAll(this.wifiList);
                sb.append("wifiList=" + this.wifiList.toString());
            }
        } else {
            sb.append("wifiList=0");
        }
        sb.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return sb.toString();
    }

    public WifiStatus clone() {
        WifiStatus wifiStatus = new WifiStatus(this.updateTime);
        if (this.mainWifi != null) {
            wifiStatus.mainWifi = this.mainWifi.clone();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.wifiList);
        wifiStatus.wifiList = arrayList;
        return wifiStatus;
    }

    public final WiFi getWiFi(int i) {
        return this.wifiList.get(i);
    }

    public List<WiFi> getWifiList() {
        return this.wifiList;
    }

    public final int numWiFis() {
        return this.wifiList.size();
    }

    public List<WiFi> scanResultList2WifiList(List<ScanResult> list) {
        ArrayList arrayList = new ArrayList();
        Iterator<ScanResult> it2 = list.iterator();
        if (Build.VERSION.SDK_INT >= 17) {
            while (it2.hasNext()) {
                ScanResult next = it2.next();
                if (next != null) {
                    arrayList.add(new WiFi(f.a(next.BSSID), next.SSID, next.level, next.frequency, next.timestamp / 1000));
                }
            }
        } else {
            while (it2.hasNext()) {
                ScanResult next2 = it2.next();
                if (next2 != null) {
                    arrayList.add(new WiFi(f.a(next2.BSSID), next2.SSID, next2.level, next2.frequency, SystemClock.elapsedRealtime()));
                }
            }
        }
        return arrayList;
    }

    public void setWifiList(List<WiFi> list) {
        this.wifiList = list;
    }

    public String toString() {
        return toStr(false);
    }

    public String toStringSimple() {
        return toStr(true);
    }
}
