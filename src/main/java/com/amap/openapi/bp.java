package com.amap.openapi;

import android.content.Context;
import android.support.annotation.NonNull;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.CellStatus;
import com.amap.location.common.model.FPS;
import com.amap.location.common.model.WiFi;
import com.amap.location.common.model.WifiStatus;
import com.amap.location.offline.IOfflineCloudConfig;
import com.amap.location.offline.OfflineConfig;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* compiled from: OfflineCore */
public class bp {
    private Context a;
    private OfflineConfig b;
    private cc c;
    private a d = new a();
    private int e = 0;

    /* compiled from: OfflineCore */
    private static class a implements Comparator<WiFi> {
        private a() {
        }

        /* renamed from: a */
        public int compare(WiFi wiFi, WiFi wiFi2) {
            return wiFi2.rssi - wiFi.rssi;
        }
    }

    public bp(@NonNull Context context, @NonNull OfflineConfig offlineConfig, @NonNull IOfflineCloudConfig iOfflineCloudConfig) {
        this.a = context;
        this.b = offlineConfig;
        this.c = new cc(context, offlineConfig, iOfflineCloudConfig);
    }

    private AmapLoc a(boolean z, AmapLoc amapLoc) {
        if (amapLoc == null) {
            return null;
        }
        if (z && AmapLoc.TYPE_OFFLINE_CELL.equals(amapLoc.getType())) {
            return null;
        }
        if (z) {
            com.amap.location.offline.upload.a.a(100035);
            return amapLoc;
        }
        com.amap.location.offline.upload.a.a(100036);
        return amapLoc;
    }

    private bs a(CellStatus cellStatus) {
        String a2 = cn.a(cellStatus);
        return by.a(this.a).a(a2, cn.a(a2));
    }

    private bu a(WifiStatus wifiStatus) {
        bu buVar = new bu();
        by.a(this.a).a(a(wifiStatus, buVar), buVar);
        return buVar;
    }

    private String a(WifiStatus wifiStatus, bu buVar) {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        if (wifiStatus != null && wifiStatus.numWiFis() > 0) {
            List<WiFi> wifiList = wifiStatus.getWifiList();
            Collections.sort(wifiList, this.d);
            int min = Math.min(wifiList.size(), 30);
            buVar.a = min;
            for (int i = 0; i < min; i++) {
                WiFi wiFi = wifiList.get(i);
                long a2 = cn.a(wiFi.mac);
                if (a2 != -1) {
                    if (z) {
                        z = false;
                    } else {
                        sb.append(',');
                    }
                    sb.append(a2);
                    bt btVar = new bt();
                    btVar.a = a2;
                    btVar.b = wiFi.mac;
                    btVar.c = wiFi.rssi;
                    buVar.b.put(Long.valueOf(a2), btVar);
                }
            }
        }
        return sb.toString();
    }

    private void a(AmapLoc amapLoc, bu buVar, bs bsVar, int i) {
        try {
            if (this.b.mLocateLogRecorder != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(amapLoc.getType()).append("|");
                sb.append(amapLoc.getLon()).append(",").append(amapLoc.getLat()).append(",").append(amapLoc.getAccuracy()).append("|");
                if (amapLoc.getType().equals(AmapLoc.TYPE_OFFLINE_WIFI)) {
                    sb.append((CharSequence) buVar.d).append("@").append((CharSequence) buVar.e).append("@").append(i).append("@").append(buVar.a);
                    if (bsVar != null) {
                        sb.append("@").append(bsVar.c + "," + bsVar.b + "," + bsVar.d);
                    }
                } else {
                    sb.append(bsVar.f).append("@").append(bsVar.c).append(",").append(bsVar.b).append(",").append(bsVar.d);
                }
                this.b.mLocateLogRecorder.onLocateSuccess(sb.toString().getBytes());
            }
        } catch (Exception e2) {
        }
    }

    public AmapLoc a(FPS fps, int i, boolean z) {
        AmapLoc amapLoc;
        boolean z2 = false;
        try {
            bs a2 = a(fps.cellStatus);
            bu a3 = a(fps.wifiStatus);
            if (!z) {
                amapLoc = bq.a(a2, a3, i);
                if (amapLoc != null) {
                    ALLog.trace("@_18_1_@", "@_18_1_1_@" + ALLog.logEncode(a2.toString() + "," + a3.toString() + "," + i));
                } else {
                    ALLog.trace("@_18_1_@", "@_18_1_3_@" + a2.a + "," + a3.a + "," + a3.c);
                }
            } else {
                amapLoc = null;
            }
            br.a(this.a, a2);
            br.a(this.a, a3);
            cl.a().a(this.a, a2);
            this.e++;
            if (this.e > 20) {
                by.a(this.a).b();
                this.e = 0;
            }
            if (i > 0) {
                z2 = true;
            }
            AmapLoc a4 = a(z2, amapLoc);
            if (a4 == null) {
                return a4;
            }
            a(a4, a3, a2, i);
            return a4;
        } catch (Throwable th) {
            return null;
        }
    }

    public void a() {
        this.c.a();
    }

    public void a(FPS fps, AmapLoc amapLoc) {
        bs a2 = a(fps.cellStatus);
        bu a3 = a(fps.wifiStatus);
        if (bq.a(a2, a3, 0) != null) {
            ALLog.trace("@_18_1_@", "@_18_1_2_@" + ALLog.logEncode(a2.toString() + "," + a3.toString() + ",(" + amapLoc.getLat() + "," + amapLoc.getLon() + ")"));
        } else {
            ALLog.trace("@_18_1_@", "@_18_1_4_@" + a2.a + "," + a3.c + "," + a3.c);
        }
        if (amapLoc != null) {
            br.a(this.a, this.b, a2, a3, amapLoc);
        }
    }

    public void a(@NonNull OfflineConfig offlineConfig) {
        this.c.a(offlineConfig);
    }

    public void b() {
        this.c.b();
    }
}
