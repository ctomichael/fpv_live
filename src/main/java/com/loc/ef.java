package com.loc;

import android.content.Context;
import android.net.wifi.ScanResult;
import com.amap.location.common.model.AmapLoc;
import com.amap.opensdk.co.CoManager;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import dji.midware.Lifecycle;
import java.util.List;
import org.json.JSONObject;

/* compiled from: CoManager */
public final class ef {
    boolean a = false;
    boolean b = false;
    private Context c;
    private CoManager d = null;
    private int e = -1;

    public ef(Context context) {
        this.c = context;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{org.json.JSONObject.put(java.lang.String, boolean):org.json.JSONObject throws org.json.JSONException}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{org.json.JSONObject.put(java.lang.String, double):org.json.JSONObject throws org.json.JSONException}
      ClspMth{org.json.JSONObject.put(java.lang.String, long):org.json.JSONObject throws org.json.JSONException}
      ClspMth{org.json.JSONObject.put(java.lang.String, int):org.json.JSONObject throws org.json.JSONException}
      ClspMth{org.json.JSONObject.put(java.lang.String, java.lang.Object):org.json.JSONObject throws org.json.JSONException}
      ClspMth{org.json.JSONObject.put(java.lang.String, boolean):org.json.JSONObject throws org.json.JSONException} */
    private static String a(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("sv", "4.7.1");
            jSONObject.put("als", "S128DF1572465B890OE3F7A13167KLEI");
            jSONObject.put("pn", u.c(context));
            jSONObject.put("ak", u.f(context));
            jSONObject.put("ud", x.g(context));
            jSONObject.put("au", x.a(context));
            jSONObject.put("isimei", true);
            return jSONObject.toString();
        } catch (Throwable th) {
            return null;
        }
    }

    private static String a(ee eeVar) {
        try {
            JSONObject jSONObject = new JSONObject();
            if (eeVar == null) {
                return null;
            }
            ed c2 = eeVar.c();
            ed d2 = eeVar.d();
            if (c2 != null) {
                jSONObject.put("mainCgi", c2.a());
            }
            if (d2 != null) {
                jSONObject.put("mainCgi2", d2.a());
            }
            return jSONObject.toString();
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "buildCgiJsonStr");
            return null;
        }
    }

    private void a(ee eeVar, List<ScanResult> list, AMapLocationServer aMapLocationServer, int i) {
        try {
            if (d() && fa.a(aMapLocationServer)) {
                e();
                if (this.d != null) {
                    String a2 = a(eeVar);
                    ScanResult[] a3 = a(list);
                    if (i == 1) {
                        this.d.trainingFps(a2, a3);
                    } else if (i == 2) {
                        this.d.correctOfflineLocation(a2, a3, aMapLocationServer.getLatitude(), aMapLocationServer.getLongitude());
                    } else {
                        return;
                    }
                    this.b = true;
                }
            }
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "action-" + (1 == i ? "training" : "correct"));
        }
    }

    private static ScanResult[] a(List<ScanResult> list) {
        if (list != null) {
            try {
                if (list.size() > 0) {
                    ScanResult[] scanResultArr = new ScanResult[list.size()];
                    int i = 0;
                    while (true) {
                        int i2 = i;
                        if (i2 >= list.size()) {
                            return scanResultArr;
                        }
                        scanResultArr[i2] = list.get(i2);
                        i = i2 + 1;
                    }
                }
            } catch (Throwable th) {
                es.a(th, "APSCoManager", "buildScanResults");
            }
        }
        return null;
    }

    private boolean d() {
        if (!er.w()) {
            c();
            return false;
        } else if (er.x()) {
            return true;
        } else {
            if (!this.b) {
                return false;
            }
            try {
                if (this.d != null) {
                    this.d.destroyOfflineLoc();
                }
            } catch (Throwable th) {
                es.a(th, "APSCoManager", "destroyOffline");
            }
            this.b = false;
            return false;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, int):int
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, java.lang.String):java.lang.String
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, int):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, boolean):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void e() {
        /*
            r8 = this;
            r0 = 1
            r6 = 0
            r2 = 0
            java.lang.Object[] r0 = new java.lang.Object[r0]
            java.lang.String r1 = "CoManager ==> init "
            r0[r2] = r1
            com.loc.fa.a()
            com.amap.opensdk.co.CoManager r0 = r8.d     // Catch:{ Throwable -> 0x00b3 }
            if (r0 != 0) goto L_0x0094
            android.content.Context r0 = r8.c     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r1 = "pref"
            java.lang.String r2 = "ok5"
            r3 = 0
            int r0 = com.loc.ez.b(r0, r1, r2, r3)     // Catch:{ Throwable -> 0x00b3 }
            android.content.Context r1 = r8.c     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r2 = "pref"
            java.lang.String r3 = "ok7"
            r4 = 0
            long r2 = com.loc.ez.b(r1, r2, r3, r4)     // Catch:{ Throwable -> 0x00b3 }
            if (r0 == 0) goto L_0x0041
            int r1 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r1 == 0) goto L_0x0041
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00b3 }
            long r2 = r4 - r2
            r4 = 259200000(0xf731400, double:1.280618154E-315)
            int r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r1 >= 0) goto L_0x0041
        L_0x0040:
            return
        L_0x0041:
            android.content.Context r1 = r8.c     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r2 = "pref"
            java.lang.String r3 = "ok5"
            int r0 = r0 + 1
            com.loc.ez.a(r1, r2, r3, r0)     // Catch:{ Throwable -> 0x00b3 }
            android.content.Context r0 = r8.c     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r1 = "pref"
            java.lang.String r2 = "ok7"
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00b3 }
            com.loc.ez.a(r0, r1, r2, r4)     // Catch:{ Throwable -> 0x00b3 }
            r0 = 1
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x00b3 }
            r1 = 0
            java.lang.String r2 = "CoManager ==> initForJar "
            r0[r1] = r2     // Catch:{ Throwable -> 0x00b3 }
            com.loc.fa.a()     // Catch:{ Throwable -> 0x00b3 }
            com.amap.opensdk.co.CoManager r0 = new com.amap.opensdk.co.CoManager     // Catch:{ Throwable -> 0x00d9 }
            android.content.Context r1 = r8.c     // Catch:{ Throwable -> 0x00d9 }
            r0.<init>(r1)     // Catch:{ Throwable -> 0x00d9 }
            r8.d = r0     // Catch:{ Throwable -> 0x00d9 }
            android.content.Context r0 = r8.c     // Catch:{ Throwable -> 0x00ce }
            if (r0 != 0) goto L_0x00be
        L_0x0076:
            com.amap.opensdk.co.CoManager r0 = r8.d     // Catch:{ Throwable -> 0x00d9 }
            r0.loadLocalSo()     // Catch:{ Throwable -> 0x00d9 }
        L_0x007b:
            android.content.Context r0 = r8.c     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r1 = "pref"
            java.lang.String r2 = "ok5"
            r3 = 0
            com.loc.ez.a(r0, r1, r2, r3)     // Catch:{ Throwable -> 0x00b3 }
            android.content.Context r0 = r8.c     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r1 = "pref"
            java.lang.String r2 = "ok7"
            r4 = 0
            com.loc.ez.a(r0, r1, r2, r4)     // Catch:{ Throwable -> 0x00b3 }
        L_0x0094:
            int r0 = com.loc.er.z()     // Catch:{ Throwable -> 0x00a8 }
            int r1 = r8.e     // Catch:{ Throwable -> 0x00a8 }
            if (r1 == r0) goto L_0x0040
            r8.e = r0     // Catch:{ Throwable -> 0x00a8 }
            com.amap.opensdk.co.CoManager r1 = r8.d     // Catch:{ Throwable -> 0x00a8 }
            if (r1 == 0) goto L_0x0040
            com.amap.opensdk.co.CoManager r1 = r8.d     // Catch:{ Throwable -> 0x00a8 }
            r1.setCloudConfigVersion(r0)     // Catch:{ Throwable -> 0x00a8 }
            goto L_0x0040
        L_0x00a8:
            r0 = move-exception
            java.lang.String r1 = "APSCoManager"
            java.lang.String r2 = "setCloudVersion"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00b3 }
            goto L_0x0040
        L_0x00b3:
            r0 = move-exception
            java.lang.String r1 = "APSCoManager"
            java.lang.String r2 = "init"
            com.loc.es.a(r0, r1, r2)
            goto L_0x0040
        L_0x00be:
            android.content.Context r0 = r8.c     // Catch:{ Throwable -> 0x00ce }
            java.lang.String r0 = a(r0)     // Catch:{ Throwable -> 0x00ce }
            com.amap.opensdk.co.CoManager r1 = r8.d     // Catch:{ Throwable -> 0x00ce }
            if (r1 == 0) goto L_0x0076
            com.amap.opensdk.co.CoManager r1 = r8.d     // Catch:{ Throwable -> 0x00ce }
            r1.init(r0)     // Catch:{ Throwable -> 0x00ce }
            goto L_0x0076
        L_0x00ce:
            r0 = move-exception
            java.lang.String r1 = "APSCoManager"
            java.lang.String r2 = "setConfig"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00d9 }
            goto L_0x0076
        L_0x00d9:
            r0 = move-exception
            java.lang.String r1 = "APSCoManager"
            java.lang.String r2 = "initForJar"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00b3 }
            goto L_0x007b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ef.e():void");
    }

    public final AMapLocationServer a(ee eeVar, List<ScanResult> list, AMapLocationServer aMapLocationServer) {
        try {
            if (!d()) {
                return aMapLocationServer;
            }
            if (aMapLocationServer != null && aMapLocationServer.getErrorCode() == 7) {
                return aMapLocationServer;
            }
            e();
            if (this.d == null) {
                return aMapLocationServer;
            }
            this.b = true;
            String offlineLoc = this.d.getOfflineLoc(a(eeVar), a(list), false);
            if (offlineLoc == null) {
                return aMapLocationServer;
            }
            JSONObject jSONObject = new JSONObject(offlineLoc);
            AMapLocationServer aMapLocationServer2 = new AMapLocationServer("lbs");
            aMapLocationServer2.b(jSONObject);
            if (fa.a(aMapLocationServer2)) {
                StringBuffer stringBuffer = new StringBuffer();
                if (aMapLocationServer2.e().equals(AmapLoc.TYPE_OFFLINE_CELL)) {
                    stringBuffer.append("基站离线定位");
                } else if (aMapLocationServer2.e().equals(AmapLoc.TYPE_OFFLINE_WIFI)) {
                    stringBuffer.append("WIFI离线定位");
                } else {
                    stringBuffer.append("离线定位，").append(aMapLocationServer2.e());
                }
                if (aMapLocationServer != null) {
                    stringBuffer.append("，在线定位失败原因:" + aMapLocationServer.getErrorInfo());
                }
                aMapLocationServer2.setTrustedLevel(2);
                aMapLocationServer2.setLocationDetail(stringBuffer.toString());
                aMapLocationServer2.setLocationType(8);
            }
            return aMapLocationServer2;
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "getOffLoc");
            return aMapLocationServer;
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a() {
        /*
            r3 = this;
            boolean r0 = com.loc.er.w()     // Catch:{ Throwable -> 0x0021 }
            if (r0 != 0) goto L_0x000a
            r3.c()     // Catch:{ Throwable -> 0x0021 }
        L_0x0009:
            return
        L_0x000a:
            boolean r0 = com.loc.er.y()     // Catch:{ Throwable -> 0x0021 }
            if (r0 != 0) goto L_0x0037
            boolean r0 = r3.a     // Catch:{ Throwable -> 0x0021 }
            if (r0 == 0) goto L_0x0009
            com.amap.opensdk.co.CoManager r0 = r3.d     // Catch:{ Throwable -> 0x002c }
            if (r0 == 0) goto L_0x001d
            com.amap.opensdk.co.CoManager r0 = r3.d     // Catch:{ Throwable -> 0x002c }
            r0.destroyCollect()     // Catch:{ Throwable -> 0x002c }
        L_0x001d:
            r0 = 0
            r3.a = r0     // Catch:{ Throwable -> 0x0021 }
            goto L_0x0009
        L_0x0021:
            r0 = move-exception
            java.lang.String r1 = "APSCoManager"
            java.lang.String r2 = "startCollection"
            com.loc.es.a(r0, r1, r2)
            goto L_0x0009
        L_0x002c:
            r0 = move-exception
            java.lang.String r1 = "APSCoManager"
            java.lang.String r2 = "destroyCollection"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0021 }
            goto L_0x001d
        L_0x0037:
            boolean r0 = r3.a     // Catch:{ Throwable -> 0x0021 }
            if (r0 != 0) goto L_0x0009
            r3.e()     // Catch:{ Throwable -> 0x0021 }
            com.amap.opensdk.co.CoManager r0 = r3.d     // Catch:{ Throwable -> 0x0021 }
            if (r0 == 0) goto L_0x0009
            com.amap.opensdk.co.CoManager r0 = r3.d     // Catch:{ Throwable -> 0x0021 }
            r0.startCollect()     // Catch:{ Throwable -> 0x0021 }
            r0 = 1
            r3.a = r0     // Catch:{ Throwable -> 0x0021 }
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ef.a():void");
    }

    public final String b() {
        try {
            if (!er.w()) {
                c();
                return null;
            } else if (this.d != null) {
                return this.d.getCollectVersion();
            } else {
                return null;
            }
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "getCollectionVersion");
            return null;
        }
    }

    public final void b(ee eeVar, List<ScanResult> list, AMapLocationServer aMapLocationServer) {
        try {
            a(eeVar, list, aMapLocationServer, 1);
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "trainingFps");
        }
    }

    public final void c() {
        try {
            if (this.d != null) {
                this.d.destroy();
            }
            this.a = false;
            this.b = false;
            this.d = null;
        } catch (Throwable th) {
            es.a(th, "APSCoManager", Lifecycle.STATUS_DESTROY);
        }
    }

    public final void c(ee eeVar, List<ScanResult> list, AMapLocationServer aMapLocationServer) {
        try {
            a(eeVar, list, aMapLocationServer, 2);
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "correctOffLoc");
        }
    }
}
