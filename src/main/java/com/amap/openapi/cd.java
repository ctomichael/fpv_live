package com.amap.openapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.network.HttpRequest;
import com.amap.location.common.network.HttpResponse;
import com.amap.location.common.util.d;
import com.amap.location.offline.IOfflineCloudConfig;
import com.amap.location.offline.OfflineConfig;
import com.amap.location.security.Core;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

/* compiled from: OfflineProtocol */
public class cd {
    private Context a;
    private OfflineConfig b;
    private IOfflineCloudConfig c;
    private cg d = new cg();
    private by e;
    private boolean f;
    private a g;

    /* compiled from: OfflineProtocol */
    interface a {
        void a();
    }

    public cd(Context context, OfflineConfig offlineConfig, IOfflineCloudConfig iOfflineCloudConfig, a aVar) {
        this.a = context;
        this.b = offlineConfig;
        this.c = iOfflineCloudConfig;
        this.g = aVar;
    }

    private void a(ce ceVar) {
        this.f = false;
        if (ceVar != null) {
            if (ceVar.a == 1) {
                cp.c(this.a);
            } else if (this.g != null) {
                this.g.a();
            }
        }
    }

    private void a(ce ceVar, HttpResponse httpResponse) {
        if (httpResponse == null) {
            ALLog.trace("@_18_6_@", "@_18_6_12_@");
            a(ceVar);
            return;
        }
        List list = httpResponse.headers.get("code");
        String str = list != null ? (String) list.get(list.size() - 1) : null;
        ALLog.trace("@_18_6_@", "@_18_6_13_@" + str);
        if (ceVar == null) {
            this.f = false;
            ALLog.trace("@_18_6_@", "@_18_6_11_@");
        } else if (!"260".equals(str)) {
            a(ceVar);
        } else {
            if (ceVar.a == 1) {
                cp.c(this.a);
                if (ceVar.b == 0) {
                    cp.d(this.a);
                }
            }
            if (ceVar.a == 0) {
                cp.b(this.a);
            }
            boolean b2 = b(ceVar, httpResponse);
            this.f = false;
            if ((b2 || ceVar.a == 0) && this.g != null) {
                this.g.a();
            }
        }
    }

    private ce b(byte b2, int i) throws Exception {
        byte[] xxt;
        byte[] a2;
        if (this.e == null) {
            this.e = by.a(this.a);
        }
        List<Long> list = null;
        List<String> list2 = null;
        if (b2 == 1) {
            int trainingThreshold = this.c.getTrainingThreshold();
            int maxNumPerRequest = this.c.getMaxNumPerRequest();
            List<Long> b3 = this.e.b(trainingThreshold, maxNumPerRequest);
            int size = b3.size();
            list2 = this.e.a(trainingThreshold, size < maxNumPerRequest ? maxNumPerRequest - size : (maxNumPerRequest * 2) / 10);
            int size2 = list2.size();
            List<Long> subList = (size2 <= 0 || size != maxNumPerRequest) ? b3 : b3.subList(0, maxNumPerRequest - size2);
            if (subList.size() + list2.size() < 5) {
                ALLog.trace("@_18_6_@", "@_18_6_6_@");
                return null;
            }
            ALLog.trace("@_18_6_@", "@_18_6_7_@(" + list2.size() + "," + subList.size() + ")");
            list = subList;
        } else {
            ALLog.trace("@_18_6_@", "@_18_6_8_@");
        }
        ce ceVar = new ce(b2, list, list2);
        ceVar.b = i;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/octet-stream");
        hashMap.put("Accept-Encoding", "gzip");
        hashMap.put("gzipped", "1");
        hashMap.put("v", "1.4.0");
        hashMap.put("et", "110");
        ceVar.e = hashMap;
        byte[] a3 = this.d.a(b2, "1.4.0", this.b.productId, this.b.packageName, this.b.productVersion, (byte) com.amap.location.common.a.d(), this.b.imei, this.b.imsi, this.b.uuid, com.amap.location.common.a.c(this.a), com.amap.location.common.a.e(this.a), com.amap.location.common.a.c(), com.amap.location.common.a.b(), this.b.license, this.b.mapKey, list, list2);
        if (a3 == null || (xxt = Core.xxt(a3, 1)) == null || xxt.length == 0 || (a2 = d.a(xxt)) == null || a2.length == 0) {
            return null;
        }
        ceVar.f = a2;
        return ceVar;
    }

    private boolean b(ce ceVar, HttpResponse httpResponse) {
        ck a2 = a(httpResponse);
        if (a2 == null) {
            ALLog.trace("@_18_6_@", "@_18_6_10_@");
            return false;
        }
        if (this.e == null) {
            this.e = by.a(this.a);
        }
        if (ceVar.a == 0) {
            this.e.a(a2);
        } else {
            this.e.a(a2, ceVar.c, ceVar.d, this.a);
        }
        return true;
    }

    public ck a(HttpResponse httpResponse) {
        try {
            List list = httpResponse.headers.get("Content-Encoding");
            String str = (list == null || list.size() <= 0) ? null : (String) list.get(0);
            byte[] bArr = httpResponse.body;
            if (bArr == null || bArr.length <= 0) {
                return null;
            }
            if ("gzip".equals(str)) {
                bArr = d.b(bArr);
            }
            return ck.a(ByteBuffer.wrap(bArr));
        } catch (Exception e2) {
            return null;
        }
    }

    public void a(byte b2, int i) {
        this.f = true;
        try {
            ce b3 = b(b2, i);
            if (b3 == null || this.b.httpClient == null) {
                this.f = false;
                return;
            }
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.url = OfflineConfig.sUseTestNet ? "http://aps.testing.amap.com/LoadOfflineData/repeatData" : "http://offline.aps.amap.com/LoadOfflineData/repeatData";
            httpRequest.headers = b3.e;
            httpRequest.body = b3.f;
            a(b3, this.b.httpClient.post(httpRequest));
        } catch (Throwable th) {
            this.f = false;
            ALLog.trace("@_18_6_@", "@_18_6_2_@" + Log.getStackTraceString(th));
        }
    }

    public void a(@NonNull OfflineConfig offlineConfig) {
        this.b = offlineConfig;
    }

    public boolean a() {
        ALLog.trace("@_18_6_@", "@_18_6_5_@" + this.f);
        return this.f;
    }
}
