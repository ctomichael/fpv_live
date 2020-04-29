package com.loc;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import dji.publics.protocol.ResponseBase;
import org.json.JSONObject;

/* compiled from: H5LocationClient */
public final class q {
    Object a = new Object();
    AMapLocationClientOption b = null;
    a c = null;
    private Context d;
    private AMapLocationClient e = null;
    /* access modifiers changed from: private */
    public WebView f = null;
    /* access modifiers changed from: private */
    public String g = "AMap.Geolocation.cbk";
    /* access modifiers changed from: private */
    public volatile boolean h = false;

    /* compiled from: H5LocationClient */
    class a implements AMapLocationListener {
        a() {
        }

        public final void onLocationChanged(AMapLocation aMapLocation) {
            if (q.this.h) {
                q.a(q.this, q.b(aMapLocation));
            }
        }
    }

    public q(Context context, WebView webView) {
        this.d = context.getApplicationContext();
        this.f = webView;
        this.c = new a();
    }

    static /* synthetic */ void a(q qVar, final String str) {
        try {
            if (qVar.f == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 19) {
                qVar.f.evaluateJavascript("javascript:" + qVar.g + "('" + str + "')", new ValueCallback<String>() {
                    /* class com.loc.q.AnonymousClass1 */

                    public final /* bridge */ /* synthetic */ void onReceiveValue(Object obj) {
                    }
                });
            } else {
                qVar.f.post(new Runnable() {
                    /* class com.loc.q.AnonymousClass2 */

                    public final void run() {
                        q.this.f.loadUrl("javascript:" + q.this.g + "('" + str + "')");
                    }
                });
            }
        } catch (Throwable th) {
            es.a(th, "H5LocationClient", "callbackJs()");
        }
    }

    /* access modifiers changed from: private */
    public static String b(AMapLocation aMapLocation) {
        JSONObject jSONObject = new JSONObject();
        if (aMapLocation == null) {
            try {
                jSONObject.put("errorCode", -1);
                jSONObject.put("errorInfo", "unknownError");
            } catch (Throwable th) {
            }
        } else if (aMapLocation.getErrorCode() == 0) {
            jSONObject.put("errorCode", 0);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("x", aMapLocation.getLongitude());
            jSONObject2.put("y", aMapLocation.getLatitude());
            jSONObject2.put("precision", (double) aMapLocation.getAccuracy());
            jSONObject2.put("type", aMapLocation.getLocationType());
            jSONObject2.put("country", aMapLocation.getCountry());
            jSONObject2.put(ResponseBase.STRING_PROVINCE, aMapLocation.getProvince());
            jSONObject2.put(ResponseBase.STRING_CITY, aMapLocation.getCity());
            jSONObject2.put("cityCode", aMapLocation.getCityCode());
            jSONObject2.put("district", aMapLocation.getDistrict());
            jSONObject2.put("adCode", aMapLocation.getAdCode());
            jSONObject2.put("street", aMapLocation.getStreet());
            jSONObject2.put("streetNum", aMapLocation.getStreetNum());
            jSONObject2.put("floor", aMapLocation.getFloor());
            jSONObject2.put("address", aMapLocation.getAddress());
            jSONObject.put("result", jSONObject2);
        } else {
            jSONObject.put("errorCode", aMapLocation.getErrorCode());
            jSONObject.put("errorInfo", aMapLocation.getErrorInfo());
            jSONObject.put("locationDetail", aMapLocation.getLocationDetail());
        }
        return jSONObject.toString();
    }

    public final void a() {
        if (this.f != null && this.d != null && Build.VERSION.SDK_INT >= 17 && !this.h) {
            try {
                this.f.getSettings().setJavaScriptEnabled(true);
                this.f.addJavascriptInterface(this, "AMapAndroidLoc");
                if (!TextUtils.isEmpty(this.f.getUrl())) {
                    this.f.reload();
                }
                if (this.e == null) {
                    this.e = new AMapLocationClient(this.d);
                    this.e.setLocationListener(this.c);
                }
                this.h = true;
            } catch (Throwable th) {
            }
        }
    }

    public final void b() {
        synchronized (this.a) {
            this.h = false;
            if (this.e != null) {
                this.e.unRegisterLocationListener(this.c);
                this.e.stopLocation();
                this.e.onDestroy();
                this.e = null;
            }
            this.b = null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        return;
     */
    @android.webkit.JavascriptInterface
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void getLocation(java.lang.String r13) {
        /*
            r12 = this;
            r2 = 30000(0x7530, double:1.4822E-319)
            r0 = 5
            r6 = 1
            r4 = 0
            java.lang.Object r7 = r12.a
            monitor-enter(r7)
            boolean r1 = r12.h     // Catch:{ all -> 0x008c }
            if (r1 != 0) goto L_0x000e
            monitor-exit(r7)     // Catch:{ all -> 0x008c }
        L_0x000d:
            return
        L_0x000e:
            com.amap.api.location.AMapLocationClientOption r1 = r12.b     // Catch:{ all -> 0x008c }
            if (r1 != 0) goto L_0x0019
            com.amap.api.location.AMapLocationClientOption r1 = new com.amap.api.location.AMapLocationClientOption     // Catch:{ all -> 0x008c }
            r1.<init>()     // Catch:{ all -> 0x008c }
            r12.b = r1     // Catch:{ all -> 0x008c }
        L_0x0019:
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ Throwable -> 0x00a5 }
            r8.<init>(r13)     // Catch:{ Throwable -> 0x00a5 }
            java.lang.String r1 = "to"
            r10 = 30000(0x7530, double:1.4822E-319)
            long r2 = r8.optLong(r1, r10)     // Catch:{ Throwable -> 0x00a5 }
            java.lang.String r1 = "useGPS"
            r5 = 1
            int r1 = r8.optInt(r1, r5)     // Catch:{ Throwable -> 0x00a5 }
            if (r1 != r6) goto L_0x008f
            r5 = r6
        L_0x0032:
            java.lang.String r1 = "watch"
            r9 = 0
            int r1 = r8.optInt(r1, r9)     // Catch:{ Throwable -> 0x00a9 }
            if (r1 != r6) goto L_0x0091
            r1 = r6
        L_0x003d:
            java.lang.String r9 = "interval"
            r10 = 5
            int r0 = r8.optInt(r9, r10)     // Catch:{ Throwable -> 0x0099 }
            java.lang.String r9 = "callback"
            r10 = 0
            java.lang.String r8 = r8.optString(r9, r10)     // Catch:{ Throwable -> 0x0099 }
            boolean r9 = android.text.TextUtils.isEmpty(r8)     // Catch:{ Throwable -> 0x0099 }
            if (r9 != 0) goto L_0x0093
            r12.g = r8     // Catch:{ Throwable -> 0x0099 }
        L_0x0055:
            com.amap.api.location.AMapLocationClientOption r8 = r12.b     // Catch:{ Throwable -> 0x00a3 }
            r8.setHttpTimeOut(r2)     // Catch:{ Throwable -> 0x00a3 }
            if (r5 == 0) goto L_0x009b
            com.amap.api.location.AMapLocationClientOption r2 = r12.b     // Catch:{ Throwable -> 0x00a3 }
            com.amap.api.location.AMapLocationClientOption$AMapLocationMode r3 = com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Hight_Accuracy     // Catch:{ Throwable -> 0x00a3 }
            r2.setLocationMode(r3)     // Catch:{ Throwable -> 0x00a3 }
        L_0x0063:
            com.amap.api.location.AMapLocationClientOption r2 = r12.b     // Catch:{ Throwable -> 0x00a3 }
            if (r1 != 0) goto L_0x0068
            r4 = r6
        L_0x0068:
            r2.setOnceLocation(r4)     // Catch:{ Throwable -> 0x00a3 }
            if (r1 == 0) goto L_0x0075
            com.amap.api.location.AMapLocationClientOption r1 = r12.b     // Catch:{ Throwable -> 0x00a3 }
            int r0 = r0 * 1000
            long r2 = (long) r0     // Catch:{ Throwable -> 0x00a3 }
            r1.setInterval(r2)     // Catch:{ Throwable -> 0x00a3 }
        L_0x0075:
            com.amap.api.location.AMapLocationClient r0 = r12.e     // Catch:{ all -> 0x008c }
            if (r0 == 0) goto L_0x008a
            com.amap.api.location.AMapLocationClient r0 = r12.e     // Catch:{ all -> 0x008c }
            com.amap.api.location.AMapLocationClientOption r1 = r12.b     // Catch:{ all -> 0x008c }
            r0.setLocationOption(r1)     // Catch:{ all -> 0x008c }
            com.amap.api.location.AMapLocationClient r0 = r12.e     // Catch:{ all -> 0x008c }
            r0.stopLocation()     // Catch:{ all -> 0x008c }
            com.amap.api.location.AMapLocationClient r0 = r12.e     // Catch:{ all -> 0x008c }
            r0.startLocation()     // Catch:{ all -> 0x008c }
        L_0x008a:
            monitor-exit(r7)     // Catch:{ all -> 0x008c }
            goto L_0x000d
        L_0x008c:
            r0 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x008c }
            throw r0
        L_0x008f:
            r5 = r4
            goto L_0x0032
        L_0x0091:
            r1 = r4
            goto L_0x003d
        L_0x0093:
            java.lang.String r8 = "AMap.Geolocation.cbk"
            r12.g = r8     // Catch:{ Throwable -> 0x0099 }
            goto L_0x0055
        L_0x0099:
            r8 = move-exception
            goto L_0x0055
        L_0x009b:
            com.amap.api.location.AMapLocationClientOption r2 = r12.b     // Catch:{ Throwable -> 0x00a3 }
            com.amap.api.location.AMapLocationClientOption$AMapLocationMode r3 = com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Battery_Saving     // Catch:{ Throwable -> 0x00a3 }
            r2.setLocationMode(r3)     // Catch:{ Throwable -> 0x00a3 }
            goto L_0x0063
        L_0x00a3:
            r0 = move-exception
            goto L_0x0075
        L_0x00a5:
            r1 = move-exception
            r1 = r4
            r5 = r4
            goto L_0x0055
        L_0x00a9:
            r1 = move-exception
            r1 = r4
            goto L_0x0055
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.q.getLocation(java.lang.String):void");
    }

    @JavascriptInterface
    public final void stopLocation() {
        if (this.h && this.e != null) {
            this.e.stopLocation();
        }
    }
}
