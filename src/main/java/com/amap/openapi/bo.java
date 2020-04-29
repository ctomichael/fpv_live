package com.amap.openapi;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.offline.IOfflineCloudConfig;
import com.amap.location.offline.OfflineConfig;
import org.json.JSONObject;

/* compiled from: CloudWrapper */
public class bo {
    private Context a;
    private OfflineConfig b;
    private com.amap.location.offline.a c;
    private b d;
    private f e = new f() {
        /* class com.amap.openapi.bo.AnonymousClass1 */

        public void a() {
        }

        public void a(a aVar) {
            bo.this.a(aVar.a());
        }
    };

    /* compiled from: CloudWrapper */
    private static class a implements IOfflineCloudConfig {
        private boolean a = true;
        private long b = 0;
        private boolean c = false;
        private int d = 6;
        private int e = 8;
        private String[] f;
        private int g = 10;
        private int h = 5;
        private int i = 100;
        private boolean j = false;

        a(JSONObject jSONObject) {
            if (jSONObject != null) {
                this.a = jSONObject.optBoolean("loe", true);
                this.b = jSONObject.optLong("loct", 0);
                this.c = jSONObject.optBoolean("loca", false);
                this.d = jSONObject.optInt("lott", 6);
                this.e = jSONObject.optInt("lomwn", 8);
                try {
                    this.f = jSONObject.optString("locpl", "").split(",");
                } catch (Exception e2) {
                }
                this.g = jSONObject.optInt("lomrt", 10);
                this.h = jSONObject.optInt("lomnwrt", 5);
                this.i = jSONObject.optInt("lomnpr", 100);
                this.j = jSONObject.optBoolean("lonfd", false);
            }
        }

        public boolean clearAll() {
            return this.c;
        }

        public long getConfigTime() {
            return this.b;
        }

        public String[] getContentProviderList() {
            return this.f;
        }

        public int getMaxNonWifiRequestTimes() {
            return this.h;
        }

        public int getMaxNumPerRequest() {
            return this.i;
        }

        public int getMaxRequestTimes() {
            return this.g;
        }

        public int getMinWifiNum() {
            return this.e;
        }

        public boolean getNeedFirstDownload() {
            return this.j;
        }

        public int getTrainingThreshold() {
            return this.d;
        }

        public boolean isEnable() {
            return this.a;
        }
    }

    public bo(Context context, OfflineConfig offlineConfig, com.amap.location.offline.a aVar) {
        this.a = context;
        this.b = offlineConfig;
        this.c = aVar;
    }

    /* access modifiers changed from: private */
    public void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.c.a = new a(jSONObject);
            } catch (Throwable th) {
            }
        }
    }

    public void a() {
        if (this.b.productId == 4 && this.b.locEnable && this.c.isEnable()) {
            this.d = b.a();
            this.d.a(this.e);
            d dVar = new d();
            dVar.a(this.b.productId);
            dVar.a(this.b.productVersion);
            dVar.c(this.b.license);
            dVar.b(this.b.mapKey);
            dVar.d(this.b.uuid);
            dVar.e(com.amap.location.common.a.c(this.a));
            dVar.a(this.b.httpClient);
            this.d.a(this.a, dVar);
        }
    }

    public void b() {
        if (this.b.productId == 4 && this.d != null) {
            this.d.b(this.e);
            this.d.b();
        }
    }
}
