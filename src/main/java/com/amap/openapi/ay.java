package com.amap.openapi;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.collection.CollectionConfig;
import com.amap.location.common.network.IHttpClient;
import org.json.JSONObject;

/* compiled from: CloudWrapper */
public class ay {
    private static final String a = ay.class.getSimpleName();
    private Context b;
    private CollectionConfig c;
    private b d;
    private IHttpClient e;
    private a f;
    private f g = new f() {
        /* class com.amap.openapi.ay.AnonymousClass1 */

        public void a() {
        }

        public void a(a aVar) {
            ay.this.a(aVar.a());
        }
    };

    /* compiled from: CloudWrapper */
    public interface a {
        void a();
    }

    public ay(Context context, CollectionConfig collectionConfig, IHttpClient iHttpClient, a aVar) {
        this.b = context;
        this.c = collectionConfig;
        this.e = iHttpClient;
        this.f = aVar;
    }

    /* access modifiers changed from: private */
    public void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                if (a(new JSONObject(str)) && this.f != null) {
                    this.f.a();
                }
            } catch (Throwable th) {
            }
        }
    }

    private boolean a(JSONObject jSONObject) {
        boolean z = false;
        boolean z2 = true;
        JSONObject optJSONObject = jSONObject.optJSONObject("c");
        if (optJSONObject == null) {
            return false;
        }
        boolean optBoolean = optJSONObject.optBoolean("cls", this.c.getFpsCollectorConfig().isEnabled());
        if (optBoolean != this.c.getFpsCollectorConfig().isEnabled()) {
            this.c.getFpsCollectorConfig().setEnabled(optBoolean);
            z = true;
        }
        boolean optBoolean2 = optJSONObject.optBoolean("cts", this.c.getTrackCollectorConfig().isEnabled());
        if (optBoolean2 != this.c.getTrackCollectorConfig().isEnabled()) {
            this.c.getTrackCollectorConfig().setEnabled(optBoolean2);
            z = true;
        }
        boolean a2 = a(optJSONObject, "cnwuss", this.c.getUploadConfig().isNonWifiUploadEnabled());
        if (a2 != this.c.getUploadConfig().isNonWifiUploadEnabled()) {
            this.c.getUploadConfig().setNonWifiUploadEnabled(a2);
            z = true;
        }
        boolean optBoolean3 = optJSONObject.optBoolean("cfup", this.c.getFpsCollectorConfig().isWifiFilterByUpdated());
        if (optBoolean3 != this.c.getFpsCollectorConfig().isWifiFilterByUpdated()) {
            this.c.getFpsCollectorConfig().setWifiFilterByUpdated(optBoolean3);
        } else {
            z2 = z;
        }
        return z2;
    }

    private boolean a(JSONObject jSONObject, String str, boolean z) {
        return jSONObject.optInt(str, z ? 1 : 0) == 1;
    }

    public void a() {
        this.d = b.a();
        this.d.a(this.g);
        if (this.c.getProductId() == 4) {
            d dVar = new d();
            dVar.a(this.c.getProductId());
            dVar.a(this.c.getProductVersion());
            dVar.c(this.c.getLicense());
            dVar.b(this.c.getMapkey());
            dVar.d(this.c.getUtdid());
            dVar.e(com.amap.location.common.a.c(this.b));
            dVar.a(this.e);
            this.d.a(this.b, dVar);
        }
    }

    public void b() {
        this.d.b(this.g);
        if (this.c.getProductId() == 4) {
            this.d.b();
        }
    }
}
