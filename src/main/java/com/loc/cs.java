package com.loc;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import com.drew.metadata.exif.makernotes.OlympusCameraSettingsMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusImageProcessingMakernoteDirectory;
import dji.midware.Lifecycle;
import dji.publics.protocol.ResponseBase;
import java.util.ArrayList;
import java.util.Locale;

@SuppressLint({"NewApi"})
/* compiled from: Aps */
public final class cs {
    static int D = -1;
    public static boolean H = true;
    private static boolean M = false;
    private static int O = -1;
    int A = 12;
    eb B = null;
    boolean C = false;
    cu E = null;
    String F = null;
    ef G = null;
    IntentFilter I = null;
    LocationManager J = null;
    private int K = 0;
    private String L = null;
    private String N = null;
    private boolean P = true;
    Context a = null;
    ConnectivityManager b = null;
    eg c = null;
    ee d = null;
    ei e = null;
    ct f = null;
    ep g = null;
    ArrayList<ScanResult> h = new ArrayList<>();
    a i = null;
    AMapLocationClientOption j = new AMapLocationClientOption();
    AMapLocationServer k = null;
    long l = 0;
    eq m = null;
    boolean n = false;
    en o = null;
    StringBuilder p = new StringBuilder();
    boolean q = true;
    boolean r = true;
    AMapLocationClientOption.GeoLanguage s = AMapLocationClientOption.GeoLanguage.DEFAULT;
    boolean t = true;
    boolean u = false;
    WifiInfo v = null;
    boolean w = true;
    StringBuilder x = null;
    boolean y = false;
    public boolean z = false;

    /* compiled from: Aps */
    class a extends BroadcastReceiver {
        a() {
        }

        public final void onReceive(Context context, Intent intent) {
            if (context != null && intent != null) {
                try {
                    String action = intent.getAction();
                    if (TextUtils.isEmpty(action)) {
                        return;
                    }
                    if (action.equals("android.net.wifi.SCAN_RESULTS")) {
                        if (cs.this.c != null) {
                            cs.this.c.e();
                        }
                    } else if (action.equals("android.net.wifi.WIFI_STATE_CHANGED") && cs.this.c != null) {
                        cs.this.c.f();
                    }
                } catch (Throwable th) {
                    es.a(th, "Aps", "onReceive");
                }
            }
        }
    }

    private static AMapLocationServer a(int i2, String str) {
        AMapLocationServer aMapLocationServer = new AMapLocationServer("");
        aMapLocationServer.setErrorCode(i2);
        aMapLocationServer.setLocationDetail(str);
        if (i2 == 15) {
            ey.a((String) null, 2151);
        }
        return aMapLocationServer;
    }

    private AMapLocationServer a(AMapLocationServer aMapLocationServer, bk bkVar) {
        if (bkVar != null) {
            try {
                if (!(bkVar.a == null || bkVar.a.length == 0)) {
                    ep epVar = new ep();
                    String str = new String(bkVar.a, "UTF-8");
                    if (str.contains("\"status\":\"0\"")) {
                        AMapLocationServer a2 = epVar.a(str, this.a, bkVar);
                        a2.h(this.x.toString());
                        return a2;
                    } else if (!str.contains("</body></html>")) {
                        return null;
                    } else {
                        aMapLocationServer.setErrorCode(5);
                        if (this.c == null || !this.c.a(this.b)) {
                            this.p.append("请求可能被劫持了#0502");
                            ey.a((String) null, (int) OlympusCameraSettingsMakernoteDirectory.TagStackedImage);
                        } else {
                            this.p.append("您连接的是一个需要登录的网络，请确认已经登入网络#0501");
                            ey.a((String) null, 2051);
                        }
                        aMapLocationServer.setLocationDetail(this.p.toString());
                        return aMapLocationServer;
                    }
                }
            } catch (Throwable th) {
                aMapLocationServer.setErrorCode(4);
                es.a(th, "Aps", "checkResponseEntity");
                this.p.append("check response exception ex is" + th.getMessage() + "#0403");
                aMapLocationServer.setLocationDetail(this.p.toString());
                return aMapLocationServer;
            }
        }
        aMapLocationServer.setErrorCode(4);
        this.p.append("网络异常,请求异常#0403");
        aMapLocationServer.h(this.x.toString());
        aMapLocationServer.setLocationDetail(this.p.toString());
        if (bkVar == null) {
            return aMapLocationServer;
        }
        ey.a(bkVar.d, 2041);
        return aMapLocationServer;
    }

    @SuppressLint({"NewApi"})
    private AMapLocationServer a(boolean z2, boolean z3) {
        AMapLocationServer aMapLocationServer;
        AMapLocationServer aMapLocationServer2 = new AMapLocationServer("");
        try {
            if (this.m == null) {
                this.m = new eq();
            }
            if (this.j == null) {
                this.j = new AMapLocationClientOption();
            }
            this.m.a(this.a, this.j.isNeedAddress(), this.j.isOffset(), this.d, this.c, this.b, this.G != null ? this.G.b() : null, this.F);
            byte[] a2 = this.m.a();
            this.l = fa.c();
            try {
                es.c(this.a);
                eo a3 = this.o.a(this.a, a2, es.a(), z3);
                el.a(this.a).a(a3);
                bk a4 = this.o.a(a3);
                el.a(this.a).a();
                String str = "";
                if (a4 != null) {
                    el.a(this.a).b();
                    aMapLocationServer2.a((long) this.o.a());
                    if (!TextUtils.isEmpty(a4.c)) {
                        this.p.append("#csid:" + a4.c);
                    }
                    str = a4.d;
                    aMapLocationServer2.h(this.x.toString());
                }
                if (!z2) {
                    AMapLocationServer a5 = a(aMapLocationServer2, a4);
                    if (a5 != null) {
                        return a5;
                    }
                    byte[] a6 = eh.a(a4.a);
                    if (a6 == null) {
                        aMapLocationServer2.setErrorCode(5);
                        this.p.append("解密数据失败#0503");
                        aMapLocationServer2.setLocationDetail(this.p.toString());
                        ey.a(str, (int) OlympusImageProcessingMakernoteDirectory.TagSensorCalibration);
                        return aMapLocationServer2;
                    }
                    AMapLocationServer a7 = this.g.a(aMapLocationServer2, a6);
                    if (!fa.a(a7)) {
                        this.L = a7.b();
                        if (!TextUtils.isEmpty(this.L)) {
                            ey.a(str, 2062);
                        } else {
                            ey.a(str, 2061);
                        }
                        a7.setErrorCode(6);
                        this.p.append("location faile retype:" + a7.d() + " rdesc:" + (TextUtils.isEmpty(this.L) ? "" : this.L) + "#0601");
                        a7.h(this.x.toString());
                        a7.setLocationDetail(this.p.toString());
                        return a7;
                    }
                    if (a7.getErrorCode() == 0 && a7.getLocationType() == 0) {
                        if (AmapLoc.RESULT_TYPE_STANDARD.equals(a7.d()) || "1".equals(a7.d()) || "2".equals(a7.d()) || "14".equals(a7.d()) || AmapLoc.RESULT_TYPE_NEW_FUSED.equals(a7.d()) || AmapLoc.RESULT_TYPE_AMAP_INDOOR.equals(a7.d())) {
                            a7.setLocationType(5);
                        } else {
                            a7.setLocationType(6);
                        }
                    }
                    a7.setOffset(this.r);
                    a7.a(this.q);
                    a7.f(String.valueOf(this.s));
                    aMapLocationServer = a7;
                } else {
                    aMapLocationServer = aMapLocationServer2;
                }
                aMapLocationServer.e(AmapLoc.TYPE_NEW);
                aMapLocationServer.setLocationDetail(this.p.toString());
                this.F = aMapLocationServer.a();
                return aMapLocationServer;
            } catch (Throwable th) {
                el.a(this.a).c();
                es.a(th, "Aps", "getApsLoc req");
                ey.a("/mobile/binary", th);
                if (!fa.d(this.a)) {
                    this.p.append("网络异常，未连接到网络，请连接网络#0401");
                } else if (!(th instanceof t)) {
                    this.p.append("网络异常,请求异常#0403");
                } else if (((t) th).a().contains("网络异常状态码")) {
                    this.p.append("网络异常，状态码错误#0404").append(((t) th).e());
                } else if (((t) th).e() == 23 || Math.abs((fa.c() - this.l) - this.j.getHttpTimeOut()) < 500) {
                    this.p.append("网络异常，连接超时#0402");
                } else {
                    this.p.append("网络异常,请求异常#0403");
                }
                AMapLocationServer a8 = a(4, this.p.toString());
                a8.h(this.x.toString());
                return a8;
            }
        } catch (Throwable th2) {
            this.p.append("get parames error:" + th2.getMessage() + "#0301");
            ey.a((String) null, 2031);
            AMapLocationServer a9 = a(3, this.p.toString());
            a9.h(this.x.toString());
            return a9;
        }
    }

    private StringBuilder a(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder(700);
        } else {
            sb.delete(0, sb.length());
        }
        sb.append(this.d.l());
        sb.append(this.c.i());
        return sb;
    }

    public static void b(Context context) {
        try {
            if (O == -1 || er.g(context)) {
                O = 1;
                er.a(context);
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "initAuth");
        }
    }

    private void c(AMapLocationServer aMapLocationServer) {
        if (aMapLocationServer != null) {
            this.k = aMapLocationServer;
        }
    }

    private void l() {
        int i2;
        boolean z2 = true;
        if (this.o != null) {
            try {
                if (this.j == null) {
                    this.j = new AMapLocationClientOption();
                }
                if (this.j.getGeoLanguage() != null) {
                    switch (this.j.getGeoLanguage()) {
                        case DEFAULT:
                            i2 = 0;
                            break;
                        case ZH:
                            i2 = 1;
                            break;
                        case EN:
                            i2 = 2;
                            break;
                        default:
                            i2 = 0;
                            break;
                    }
                } else {
                    i2 = 0;
                }
                en enVar = this.o;
                long httpTimeOut = this.j.getHttpTimeOut();
                if (!this.j.getLocationProtocol().equals(AMapLocationClientOption.AMapLocationProtocol.HTTPS)) {
                    z2 = false;
                }
                enVar.a(httpTimeOut, z2, i2);
            } catch (Throwable th) {
            }
        }
    }

    private void m() {
        try {
            if (this.i == null) {
                this.i = new a();
            }
            if (this.I == null) {
                this.I = new IntentFilter();
                this.I.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                this.I.addAction("android.net.wifi.SCAN_RESULTS");
            }
            this.a.registerReceiver(this.i, this.I);
        } catch (Throwable th) {
            es.a(th, "Aps", "initBroadcastListener");
        }
    }

    private String n() {
        String format;
        int f2 = this.d.f();
        ed c2 = this.d.c();
        boolean z2 = this.h == null || this.h.isEmpty();
        if (c2 != null || !z2) {
            this.v = this.c.g();
            this.w = eg.a(this.v);
            switch (f2) {
                case 0:
                    boolean z3 = !this.h.isEmpty() || this.w;
                    if (!this.w || !this.h.isEmpty()) {
                        if (this.h.size() == 1) {
                            this.A = 2;
                            if (!this.w) {
                                this.p.append("当前基站为伪基站，并且搜到的WIFI数量不足，请移动到WIFI比较丰富的区域#0202");
                                ey.a((String) null, 2022);
                                return "";
                            }
                            if (this.c.g().getBSSID().equals(this.h.get(0).BSSID)) {
                                this.p.append("当前基站为伪基站，并且搜到的WIFI数量不足，请移动到WIFI比较丰富的区域#0202");
                                ey.a((String) null, 2021);
                                return "";
                            }
                        }
                        format = String.format(Locale.US, "#%s#", "network");
                        if (!z3) {
                            if ("network".equals("network")) {
                                format = "";
                                this.A = 2;
                                if (!this.c.p) {
                                    this.p.append("当前基站为伪基站,并且关闭了WIFI开关，请在设置中打开WIFI开关#0203");
                                } else {
                                    this.p.append("当前基站为伪基站,并且没有搜索到WIFI，请移动到WIFI比较丰富的区域#0204");
                                }
                                ey.a((String) null, 2022);
                                break;
                            }
                        } else {
                            format = format + "wifi";
                            break;
                        }
                    } else {
                        this.A = 2;
                        this.p.append("当前基站为伪基站，并且WIFI权限被禁用，请在安全软件中打开应用的定位权限#0201");
                        ey.a((String) null, 2021);
                        return "";
                    }
                    break;
                case 1:
                    if (c2 != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(c2.a).append("#");
                        sb.append(c2.b).append("#");
                        sb.append(c2.c).append("#");
                        sb.append(c2.d).append("#");
                        sb.append("network").append("#");
                        sb.append((!this.h.isEmpty() || this.w) ? "cgiwifi" : "cgi");
                        format = sb.toString();
                        break;
                    }
                    format = "";
                    break;
                case 2:
                    if (c2 != null) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(c2.a).append("#");
                        sb2.append(c2.b).append("#");
                        sb2.append(c2.g).append("#");
                        sb2.append(c2.h).append("#");
                        sb2.append(c2.i).append("#");
                        sb2.append("network").append("#");
                        sb2.append((!this.h.isEmpty() || this.w) ? "cgiwifi" : "cgi");
                        format = sb2.toString();
                        break;
                    }
                    format = "";
                    break;
                default:
                    this.A = 11;
                    ey.a((String) null, 2111);
                    this.p.append("get cgi failure#1101");
                    format = "";
                    break;
            }
            if (!TextUtils.isEmpty(format)) {
                if (!format.startsWith("#")) {
                    format = "#" + format;
                }
                format = fa.i() + format;
            }
            return format;
        }
        if (this.b == null) {
            this.b = (ConnectivityManager) fa.a(this.a, "connectivity");
        }
        if (!fa.a(this.a) || this.c.p) {
            if (fa.d() >= 28) {
                if (this.J == null) {
                    this.J = (LocationManager) this.a.getApplicationContext().getSystemService(ResponseBase.STRING_LOCATION);
                }
                if (!((Boolean) ew.a(this.J, "isLocationEnabled", new Object[0])).booleanValue()) {
                    this.A = 12;
                    this.p.append("定位服务没有开启，请在设置中打开定位服务开关#1206");
                    ey.a((String) null, 2121);
                    return "";
                }
            }
            if (!fa.f(this.a)) {
                this.A = 12;
                this.p.append("定位权限被禁用,请授予应用定位权限#1201");
                ey.a((String) null, 2121);
                return "";
            } else if (fa.d() < 24 || fa.d() >= 28 || Settings.Secure.getInt(this.a.getContentResolver(), "location_mode", 0) != 0) {
                String j2 = this.d.j();
                String b2 = this.c.b();
                if (this.c.a(this.b) && b2 != null) {
                    this.A = 12;
                    this.p.append("获取基站与获取WIFI的权限都被禁用，请在安全软件中打开应用的定位权限#1202");
                    ey.a((String) null, 2121);
                    return "";
                } else if (j2 != null) {
                    this.A = 12;
                    if (!this.c.p) {
                        this.p.append("WIFI开关关闭，并且获取基站权限被禁用，请在安全软件中打开应用的定位权限或者打开WIFI开关#1204");
                    } else {
                        this.p.append("获取的WIFI列表为空，并且获取基站权限被禁用，请在安全软件中打开应用的定位权限#1205");
                    }
                    ey.a((String) null, 2121);
                    return "";
                } else if (!this.c.p && !this.d.m()) {
                    this.A = 19;
                    this.p.append("没有检查到SIM卡，并且WIFI开关关闭，请打开WIFI开关或者插入SIM卡#1901");
                    ey.a((String) null, 2133);
                    return "";
                } else if (!fa.g(this.a)) {
                    this.A = 12;
                    this.p.append("后台定位服务没有开启，请在设置中打开后台定位服务开关#1207");
                    ey.a((String) null, 2121);
                    return "";
                } else {
                    if (!this.c.p) {
                        this.p.append("获取到的基站为空，并且关闭了WIFI开关，请您打开WIFI开关再发起定位#1301");
                    } else {
                        this.p.append("获取到的基站和WIFI信息均为空，请移动到有WIFI的区域，若确定当前区域有WIFI，请检查是否授予APP定位权限#1302");
                    }
                    this.A = 13;
                    ey.a((String) null, 2131);
                    return "";
                }
            } else {
                this.A = 12;
                this.p.append("定位服务没有开启，请在设置中打开定位服务开关#1206");
                ey.a((String) null, 2121);
                return "";
            }
        } else {
            this.A = 18;
            this.p.append("飞行模式下关闭了WIFI开关，请关闭飞行模式或者打开WIFI开关#1801");
            ey.a((String) null, 2132);
            return "";
        }
    }

    private boolean o() {
        this.h = this.c.c();
        return this.h == null || this.h.size() <= 0;
    }

    public final AMapLocationServer a(double d2, double d3) {
        try {
            String a2 = this.o.a(this.a, d2, d3);
            if (a2.contains("\"status\":\"1\"")) {
                AMapLocationServer a3 = this.g.a(a2);
                a3.setLatitude(d2);
                a3.setLongitude(d3);
                return a3;
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public final AMapLocationServer a(AMapLocationServer aMapLocationServer) {
        this.E.a(this.t);
        return this.E.a(aMapLocationServer);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.cs.a(boolean, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer
     arg types: [int, boolean]
     candidates:
      com.loc.cs.a(int, java.lang.String):com.autonavi.aps.amapapi.model.AMapLocationServer
      com.loc.cs.a(com.autonavi.aps.amapapi.model.AMapLocationServer, com.loc.bk):com.autonavi.aps.amapapi.model.AMapLocationServer
      com.loc.cs.a(double, double):com.autonavi.aps.amapapi.model.AMapLocationServer
      com.loc.cs.a(boolean, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer */
    public final AMapLocationServer a(boolean z2) {
        if (this.a == null) {
            this.p.append("context is null#0101");
            ey.a((String) null, 2011);
            return a(1, this.p.toString());
        } else if (this.c.h()) {
            return a(15, "networkLocation has been mocked!#1502");
        } else {
            a();
            if (TextUtils.isEmpty(this.N)) {
                return a(this.A, this.p.toString());
            }
            AMapLocationServer a2 = a(false, z2);
            if (!fa.a(a2)) {
                return a2;
            }
            this.e.a(this.x.toString());
            this.e.a(this.d.c());
            c(a2);
            return a2;
        }
    }

    public final void a() {
        this.o = en.a(this.a);
        l();
        if (this.b == null) {
            this.b = (ConnectivityManager) fa.a(this.a, "connectivity");
        }
        if (this.m == null) {
            this.m = new eq();
        }
    }

    public final void a(Context context) {
        try {
            if (this.a == null) {
                this.E = new cu();
                this.a = context.getApplicationContext();
                er.d(this.a);
                fa.b(this.a);
                if (this.c == null) {
                    this.c = new eg(this.a, (WifiManager) fa.a(this.a, "wifi"));
                }
                if (this.d == null) {
                    this.d = new ee(this.a);
                }
                if (this.e == null) {
                    this.e = new ei();
                }
                if (this.g == null) {
                    this.g = new ep();
                }
                if (this.G == null) {
                    this.G = new ef(this.a);
                }
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "initBase");
        }
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        boolean z2;
        boolean z3;
        boolean z4;
        this.j = aMapLocationClientOption;
        if (this.j == null) {
            this.j = new AMapLocationClientOption();
        }
        if (this.c != null) {
            eg egVar = this.c;
            this.j.isWifiActiveScan();
            egVar.a(this.j.isWifiScan(), this.j.isMockEnable(), AMapLocationClientOption.isOpenAlwaysScanWifi(), aMapLocationClientOption.getScanWifiInterval());
        }
        l();
        if (this.e != null) {
            this.e.a(this.j);
        }
        if (this.g != null) {
            this.g.a(this.j);
        }
        AMapLocationClientOption.GeoLanguage geoLanguage = AMapLocationClientOption.GeoLanguage.DEFAULT;
        try {
            geoLanguage = this.j.getGeoLanguage();
            z3 = this.j.isNeedAddress();
            try {
                z4 = this.j.isOffset();
                try {
                    z2 = this.j.isLocationCacheEnable();
                    try {
                        this.u = this.j.isOnceLocationLatest();
                        this.C = this.j.isSensorEnable();
                        if (!(z4 == this.r && z3 == this.q && z2 == this.t && geoLanguage == this.s)) {
                            if (this.e != null) {
                                this.e.a();
                            }
                            c(null);
                            this.P = false;
                            if (this.E != null) {
                                this.E.a();
                            }
                        }
                    } catch (Throwable th) {
                    }
                } catch (Throwable th2) {
                    z2 = true;
                }
            } catch (Throwable th3) {
                z2 = true;
                z4 = true;
            }
        } catch (Throwable th4) {
            z2 = true;
            z3 = true;
            z4 = true;
        }
        this.r = z4;
        this.q = z3;
        this.t = z2;
        this.s = geoLanguage;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ee.a(boolean, boolean):void
     arg types: [int, boolean]
     candidates:
      com.loc.ee.a(com.loc.ee, long):long
      com.loc.ee.a(android.telephony.CellInfoCdma, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.CellInfoGsm, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.CellInfoLte, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.CellInfoWcdma, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.NeighboringCellInfo, java.lang.String[]):com.loc.ed
      com.loc.ee.a(com.loc.ee, int):void
      com.loc.ee.a(int, int):boolean
      com.loc.ee.a(boolean, boolean):void */
    public final void b() {
        if (this.B == null) {
            this.B = new eb(this.a);
        }
        if (this.f == null) {
            this.f = new ct(this.a);
        }
        m();
        this.c.b(false);
        this.h = this.c.c();
        this.d.a(false, o());
        this.e.a(this.a);
        this.f.b();
        try {
            if (this.a.checkCallingOrSelfPermission(ad.c("EYW5kcm9pZC5wZXJtaXNzaW9uLldSSVRFX1NFQ1VSRV9TRVRUSU5HUw==")) == 0) {
                this.n = true;
            }
        } catch (Throwable th) {
        }
        this.z = true;
    }

    public final void b(AMapLocationServer aMapLocationServer) {
        if (fa.a(aMapLocationServer)) {
            this.e.a(this.N, this.x, aMapLocationServer, this.a, true);
        }
    }

    public final void c() {
        if (this.p.length() > 0) {
            this.p.delete(0, this.p.length());
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ee.a(boolean, boolean):void
     arg types: [int, boolean]
     candidates:
      com.loc.ee.a(com.loc.ee, long):long
      com.loc.ee.a(android.telephony.CellInfoCdma, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.CellInfoGsm, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.CellInfoLte, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.CellInfoWcdma, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.NeighboringCellInfo, java.lang.String[]):com.loc.ed
      com.loc.ee.a(com.loc.ee, int):void
      com.loc.ee.a(int, int):boolean
      com.loc.ee.a(boolean, boolean):void */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.cs.a(boolean, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer
     arg types: [int, int]
     candidates:
      com.loc.cs.a(int, java.lang.String):com.autonavi.aps.amapapi.model.AMapLocationServer
      com.loc.cs.a(com.autonavi.aps.amapapi.model.AMapLocationServer, com.loc.bk):com.autonavi.aps.amapapi.model.AMapLocationServer
      com.loc.cs.a(double, double):com.autonavi.aps.amapapi.model.AMapLocationServer
      com.loc.cs.a(boolean, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer */
    public final AMapLocationServer d() throws Throwable {
        boolean z2;
        c();
        if (this.a == null) {
            this.p.append("context is null#0101");
            return a(1, this.p.toString());
        }
        this.K++;
        if (this.K == 1 && this.c != null) {
            this.c.a(this.n);
        }
        long j2 = this.l;
        if (!this.P) {
            this.P = true;
            z2 = false;
        } else {
            if (fa.c() - j2 < 800) {
                long j3 = 0;
                if (fa.a(this.k)) {
                    j3 = fa.b() - this.k.getTime();
                }
                if (j3 <= 10000) {
                    z2 = true;
                }
            }
            z2 = false;
        }
        if (!z2 || !fa.a(this.k)) {
            if (this.B != null) {
                if (this.C) {
                    this.B.a();
                } else {
                    this.B.b();
                }
            }
            try {
                this.c.b(this.j.isOnceLocationLatest() || !this.j.isOnceLocation());
                this.h = this.c.c();
            } catch (Throwable th) {
                es.a(th, "Aps", "getLocation getScanResultsParam");
            }
            try {
                this.d.a(false, o());
            } catch (Throwable th2) {
                es.a(th2, "Aps", "getLocation getCgiListParam");
            }
            this.N = n();
            if (TextUtils.isEmpty(this.N)) {
                this.k = this.f.e();
                if (this.k == null) {
                    return a(this.A, this.p.toString());
                }
                this.k.setLocationDetail(this.p.toString());
                this.k.setTrustedLevel(4);
                return this.k;
            }
            this.x = a(this.x);
            if (this.c.h()) {
                AMapLocationServer a2 = a(15, "networkLocation has been mocked!#1502");
                a2.setMock(true);
                a2.setTrustedLevel(4);
                return a2;
            }
            AMapLocationServer a3 = this.e.a(this.d, this.l == 0 ? true : fa.c() - this.l > 20000, this.k, this.c, this.x, this.N, this.a);
            if (fa.a(a3)) {
                a3.setTrustedLevel(2);
                c(a3);
            } else {
                this.k = a(false, true);
                if (fa.a(this.k)) {
                    this.k.e(AmapLoc.TYPE_NEW);
                    this.e.a(this.x.toString());
                    this.e.a(this.d.c());
                    c(this.k);
                    if (this.G != null) {
                        this.G.c(this.d, this.h, this.k);
                    }
                }
            }
            try {
                if (!(this.c == null || this.k == null)) {
                    long a4 = eg.a();
                    if (a4 <= 15) {
                        this.k.setTrustedLevel(1);
                    } else if (a4 <= 120) {
                        this.k.setTrustedLevel(2);
                    } else if (a4 <= 600) {
                        this.k.setTrustedLevel(3);
                    } else {
                        this.k.setTrustedLevel(4);
                    }
                }
            } catch (Throwable th3) {
            }
            if (this.G != null) {
                this.G.b(this.d, this.h, this.k);
            }
            this.e.a(this.N, this.x, this.k, this.a, true);
            if (!fa.a(this.k) && this.G != null) {
                this.k = this.G.a(this.d, this.h, this.k);
            }
            this.x.delete(0, this.x.length());
            if (!this.C || this.B == null) {
                this.k.setAltitude(0.0d);
                this.k.setBearing(0.0f);
                this.k.setSpeed(0.0f);
            } else {
                this.k.setAltitude(this.B.f);
                this.k.setBearing(this.B.c());
                this.k.setSpeed((float) this.B.d());
            }
            return this.k;
        }
        if (this.t && er.b(this.k.getTime())) {
            this.k.setLocationType(2);
        }
        return this.k;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.cs.a(boolean, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer
     arg types: [int, int]
     candidates:
      com.loc.cs.a(int, java.lang.String):com.autonavi.aps.amapapi.model.AMapLocationServer
      com.loc.cs.a(com.autonavi.aps.amapapi.model.AMapLocationServer, com.loc.bk):com.autonavi.aps.amapapi.model.AMapLocationServer
      com.loc.cs.a(double, double):com.autonavi.aps.amapapi.model.AMapLocationServer
      com.loc.cs.a(boolean, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer */
    public final void e() {
        try {
            a(this.a);
            a(this.j);
            i();
            b(a(true, true));
        } catch (Throwable th) {
            es.a(th, "Aps", "doFusionLocation");
        }
    }

    @SuppressLint({"NewApi"})
    public final void f() {
        this.F = null;
        this.y = false;
        this.z = false;
        if (this.G != null) {
            this.G.c();
        }
        if (this.f != null) {
            this.f.a();
        }
        if (this.e != null) {
            this.e.b(this.a);
        }
        if (this.E != null) {
            this.E.a();
        }
        if (this.g != null) {
            this.g = null;
        }
        fa.h();
        try {
            if (!(this.a == null || this.i == null)) {
                this.a.unregisterReceiver(this.i);
            }
        } catch (Throwable th) {
            es.a(th, "Aps", Lifecycle.STATUS_DESTROY);
        } finally {
            this.i = null;
        }
        if (this.d != null) {
            this.d.h();
        }
        if (this.c != null) {
            this.c.j();
        }
        if (this.h != null) {
            this.h.clear();
        }
        if (this.B != null) {
            this.B.e();
        }
        el.d();
        this.k = null;
        this.a = null;
        this.x = null;
        this.J = null;
    }

    public final void g() {
        try {
            if (this.f != null) {
                this.f.c();
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "bindAMapService");
        }
    }

    public final void h() {
        try {
            if (this.f != null) {
                this.f.d();
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "bindOtherService");
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ee.a(boolean, boolean):void
     arg types: [int, boolean]
     candidates:
      com.loc.ee.a(com.loc.ee, long):long
      com.loc.ee.a(android.telephony.CellInfoCdma, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.CellInfoGsm, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.CellInfoLte, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.CellInfoWcdma, boolean):com.loc.ed
      com.loc.ee.a(android.telephony.NeighboringCellInfo, java.lang.String[]):com.loc.ed
      com.loc.ee.a(com.loc.ee, int):void
      com.loc.ee.a(int, int):boolean
      com.loc.ee.a(boolean, boolean):void */
    public final void i() {
        try {
            if (!this.y) {
                if (this.N != null) {
                    this.N = null;
                }
                if (this.x != null) {
                    this.x.delete(0, this.x.length());
                }
                if (this.u) {
                    m();
                }
                this.c.b(this.u);
                this.h = this.c.c();
                this.d.a(true, o());
                this.N = n();
                if (!TextUtils.isEmpty(this.N)) {
                    this.x = a(this.x);
                }
                this.y = true;
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "initFirstLocateParam");
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ei.a(android.content.Context, java.lang.String, java.lang.StringBuilder, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer
     arg types: [android.content.Context, java.lang.String, java.lang.StringBuilder, int]
     candidates:
      com.loc.ei.a(java.lang.String, com.amap.api.location.AMapLocation, java.lang.StringBuilder, android.content.Context):void
      com.loc.ei.a(android.content.Context, java.lang.String, java.lang.StringBuilder, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer */
    public final AMapLocationServer j() {
        if (this.c.h()) {
            return a(15, "networkLocation has been mocked!#1502");
        }
        if (TextUtils.isEmpty(this.N)) {
            return a(this.A, this.p.toString());
        }
        AMapLocationServer a2 = this.e.a(this.a, this.N, this.x, true);
        if (!fa.a(a2)) {
            return a2;
        }
        c(a2);
        return a2;
    }

    public final void k() {
        if (this.G != null) {
            this.G.a();
        }
    }
}
