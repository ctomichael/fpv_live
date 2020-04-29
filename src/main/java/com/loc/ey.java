package com.loc;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import com.drew.metadata.exif.makernotes.OlympusCameraSettingsMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusImageProcessingMakernoteDirectory;
import dji.midware.Lifecycle;
import dji.publics.LogReport.base.Fields;
import dji.publics.protocol.ResponseBase;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: ReportUtil */
public final class ey {
    static AMapLocation g = null;
    static boolean h = false;
    private static List<bq> i = new ArrayList();
    private static JSONArray j = null;
    public SparseArray<Long> a = new SparseArray<>();
    public int b = -1;
    public long c = 0;
    String[] d = {"ol", "cl", "gl", "ha", "bs", "ds"};
    public int e = -1;
    public long f = -1;

    public static void a(long j2, long j3) {
        try {
            if (!h) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("gpsTime:").append(fa.a(j2, "yyyy-MM-dd HH:mm:ss.SSS")).append(",");
                stringBuffer.append("sysTime:").append(fa.a(j3, "yyyy-MM-dd HH:mm:ss.SSS")).append(",");
                long E = er.E();
                String str = "0";
                if (0 != E) {
                    str = fa.a(E, "yyyy-MM-dd HH:mm:ss.SSS");
                }
                stringBuffer.append("serverTime:").append(str);
                a("checkgpstime", stringBuffer.toString());
                if (0 != E && Math.abs(j2 - E) < 31536000000L) {
                    stringBuffer.append(", correctError");
                    a("checkgpstimeerror", stringBuffer.toString());
                }
                stringBuffer.delete(0, stringBuffer.length());
                h = true;
            }
        } catch (Throwable th) {
        }
    }

    public static void a(Context context) {
        if (context != null) {
            try {
                if (er.i()) {
                    if (i != null && i.size() > 0) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.addAll(i);
                        br.a(arrayList, context);
                        i.clear();
                    }
                    f(context);
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", Lifecycle.STATUS_DESTROY);
            }
        }
    }

    public static void a(Context context, int i2, int i3, long j2, long j3) {
        if (i2 != -1 && i3 != -1 && context != null) {
            try {
                if (er.i()) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("param_int_first", i2);
                    jSONObject.put("param_int_second", i3);
                    jSONObject.put("param_long_first", j2);
                    jSONObject.put("param_long_second", j3);
                    a(context, "O012", jSONObject);
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "reportServiceAliveTime");
            }
        }
    }

    public static void a(Context context, long j2, boolean z) {
        if (context != null) {
            try {
                if (er.i()) {
                    int intValue = Long.valueOf(j2).intValue();
                    String str = "domestic";
                    if (!z) {
                        str = "abroad";
                    }
                    a(context, "O015", str, null, intValue, Integer.MAX_VALUE);
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "reportGPSLocUseTime");
            }
        }
    }

    public static void a(Context context, AMapLocation aMapLocation) {
        String str;
        int i2 = 0;
        if (aMapLocation != null) {
            try {
                if (!"gps".equalsIgnoreCase(aMapLocation.getProvider()) && aMapLocation.getLocationType() != 1) {
                    String str2 = "domestic";
                    if (a(aMapLocation)) {
                        str2 = "abroad";
                    }
                    if (aMapLocation.getErrorCode() == 0) {
                        switch (aMapLocation.getLocationType()) {
                            case 5:
                            case 6:
                                str = "net";
                                i2 = 1;
                                break;
                            default:
                                str = Fields.Dgo_appset.EVENT_CACHE;
                                i2 = 1;
                                break;
                        }
                    } else {
                        switch (aMapLocation.getErrorCode()) {
                            case 4:
                            case 5:
                            case 6:
                            case 11:
                                str = "net";
                                break;
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            default:
                                str = Fields.Dgo_appset.EVENT_CACHE;
                                break;
                        }
                    }
                    a(context, "O016", str, str2, i2, Integer.MAX_VALUE);
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "reportBatting");
            }
        }
    }

    public static void a(Context context, ex exVar) {
        String str;
        if (context != null) {
            try {
                if (er.i()) {
                    AMapLocationServer c2 = exVar.c();
                    if (fa.a(c2) && !"gps".equalsIgnoreCase(c2.getProvider()) && c2.getLocationType() != 1) {
                        int intValue = Long.valueOf(exVar.b() - exVar.a()).intValue();
                        boolean z = false;
                        int intValue2 = Long.valueOf(c2.k()).intValue();
                        switch (c2.getLocationType()) {
                            case 5:
                            case 6:
                                str = "net";
                                break;
                            default:
                                str = Fields.Dgo_appset.EVENT_CACHE;
                                z = true;
                                break;
                        }
                        String str2 = "domestic";
                        if (a(c2)) {
                            str2 = "abroad";
                        }
                        if (!z) {
                            a(context, "O014", str2, null, intValue2, intValue);
                        }
                        a(context, "O013", str, str2, intValue, Integer.MAX_VALUE);
                    }
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "reportLBSLocUseTime");
            }
        }
    }

    public static void a(Context context, String str, int i2) {
        if (context != null) {
            try {
                if (er.i()) {
                    JSONObject jSONObject = new JSONObject();
                    if (!TextUtils.isEmpty(str)) {
                        jSONObject.put("param_string_first", str);
                    }
                    if (i2 != Integer.MAX_VALUE) {
                        jSONObject.put("param_int_first", i2);
                    }
                    a(context, "O010", jSONObject);
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "reportDexFunction");
            }
        }
    }

    private static void a(Context context, String str, String str2, String str3, int i2, int i3) {
        if (context != null) {
            try {
                if (er.i()) {
                    JSONObject jSONObject = new JSONObject();
                    if (!TextUtils.isEmpty(str2)) {
                        jSONObject.put("param_string_first", str2);
                    }
                    if (!TextUtils.isEmpty(str3)) {
                        jSONObject.put("param_string_second", str3);
                    }
                    if (i2 != Integer.MAX_VALUE) {
                        jSONObject.put("param_int_first", i2);
                    }
                    if (i3 != Integer.MAX_VALUE) {
                        jSONObject.put("param_int_second", i3);
                    }
                    a(context, str, jSONObject);
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "applyStatisticsEx");
            }
        }
    }

    private static void a(Context context, String str, JSONObject jSONObject) {
        if (context != null) {
            try {
                if (er.i()) {
                    bq bqVar = new bq(context, "loc", "4.7.1", str);
                    bqVar.a(jSONObject.toString());
                    i.add(bqVar);
                    if (i.size() >= 100) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.addAll(i);
                        br.a(arrayList, context);
                        i.clear();
                    }
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "applyStatistics");
            }
        }
    }

    public static void a(AMapLocation aMapLocation, AMapLocation aMapLocation2) {
        try {
            if (g == null) {
                if (!fa.a(aMapLocation)) {
                    g = aMapLocation2;
                    return;
                }
                g = aMapLocation.clone();
            }
            if (fa.a(g) && fa.a(aMapLocation2)) {
                AMapLocation clone = aMapLocation2.clone();
                if (!(g.getLocationType() == 1 || g.getLocationType() == 9 || "gps".equalsIgnoreCase(g.getProvider()) || g.getLocationType() == 7 || clone.getLocationType() == 1 || clone.getLocationType() == 9 || "gps".equalsIgnoreCase(clone.getProvider()) || clone.getLocationType() == 7)) {
                    long abs = Math.abs(clone.getTime() - g.getTime()) / 1000;
                    if (abs <= 0) {
                        abs = 1;
                    }
                    if (abs <= 1800) {
                        float a2 = fa.a(g, clone);
                        float f2 = a2 / ((float) abs);
                        if (a2 > 30000.0f && f2 > 1000.0f) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(g.getLatitude()).append(",");
                            sb.append(g.getLongitude()).append(",");
                            sb.append(g.getAccuracy()).append(",");
                            sb.append(g.getLocationType()).append(",");
                            if (aMapLocation.getTime() != 0) {
                                sb.append(fa.a(g.getTime(), "yyyyMMdd_HH:mm:ss:SS"));
                            } else {
                                sb.append(g.getTime());
                            }
                            sb.append("#");
                            sb.append(clone.getLatitude()).append(",");
                            sb.append(clone.getLongitude()).append(",");
                            sb.append(clone.getAccuracy()).append(",");
                            sb.append(clone.getLocationType()).append(",");
                            if (clone.getTime() != 0) {
                                sb.append(fa.a(clone.getTime(), "yyyyMMdd_HH:mm:ss:SS"));
                            } else {
                                sb.append(clone.getTime());
                            }
                            a("bigshiftstatistics", sb.toString());
                            sb.delete(0, sb.length());
                        }
                    }
                }
                g = clone;
            }
        } catch (Throwable th) {
        }
    }

    public static void a(String str, int i2) {
        String valueOf = String.valueOf(i2);
        String str2 = "";
        switch (i2) {
            case 2011:
                str2 = "ContextIsNull";
                break;
            case 2021:
                str2 = "OnlyMainWifi";
                break;
            case 2022:
                str2 = "OnlyOneWifiButNotMain";
                break;
            case 2031:
                str2 = "CreateApsReqException";
                break;
            case 2041:
                str2 = "ResponseResultIsNull";
                break;
            case 2051:
                str2 = "NeedLoginNetWork\t";
                break;
            case OlympusCameraSettingsMakernoteDirectory.TagStackedImage /*2052*/:
                str2 = "MaybeIntercepted";
                break;
            case OlympusImageProcessingMakernoteDirectory.TagSensorCalibration /*2053*/:
                str2 = "DecryptResponseException";
                break;
            case 2054:
                str2 = "ParserDataException";
                break;
            case 2061:
                str2 = "ServerRetypeError";
                break;
            case 2062:
                str2 = "ServerLocFail";
                break;
            case 2081:
                str2 = "LocalLocException";
                break;
            case 2091:
                str2 = "InitException";
                break;
            case 2101:
                str2 = "BindAPSServiceException";
                break;
            case 2102:
                str2 = "AuthClientScodeFail";
                break;
            case 2103:
                str2 = "NotConfigAPSService";
                break;
            case 2111:
                str2 = "ErrorCgiInfo";
                break;
            case 2121:
                str2 = "NotLocPermission";
                break;
            case 2131:
                str2 = "NoCgiOAndWifiInfo";
                break;
            case 2132:
                str2 = "AirPlaneModeAndWifiOff";
                break;
            case 2133:
                str2 = "NoCgiAndWifiOff";
                break;
            case 2141:
                str2 = "NoEnoughStatellites";
                break;
            case 2151:
                str2 = "MaybeMockNetLoc";
                break;
            case 2152:
                str2 = "MaybeMockGPSLoc";
                break;
        }
        a(str, valueOf, str2);
    }

    public static void a(String str, String str2) {
        try {
            aq.b(es.b(), str2, str);
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "reportLog");
        }
    }

    public static void a(String str, String str2, String str3) {
        try {
            aq.a(es.b(), "/mobile/binary", str3, str, str2);
        } catch (Throwable th) {
        }
    }

    public static void a(String str, Throwable th) {
        try {
            if (th instanceof t) {
                aq.a(es.b(), str, (t) th);
            }
        } catch (Throwable th2) {
        }
    }

    private static boolean a(AMapLocation aMapLocation) {
        return fa.a(aMapLocation) ? !es.a(aMapLocation.getLatitude(), aMapLocation.getLongitude()) : "http://abroad.apilocate.amap.com/mobile/binary".equals(es.a);
    }

    public static void b(Context context, AMapLocation aMapLocation) {
        boolean z;
        int i2 = 2;
        try {
            if (fa.a(aMapLocation)) {
                switch (aMapLocation.getLocationType()) {
                    case 1:
                        z = true;
                        i2 = 0;
                        break;
                    case 2:
                    case 4:
                        z = true;
                        i2 = 1;
                        break;
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        z = false;
                        i2 = 0;
                        break;
                    case 8:
                        z = true;
                        i2 = 3;
                        break;
                    case 9:
                        z = true;
                        break;
                }
                if (z) {
                    if (j == null) {
                        j = new JSONArray();
                    }
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("lon", fa.c(aMapLocation.getLongitude()));
                    jSONObject.put(ResponseBase.STRING_LAT, fa.c(aMapLocation.getLatitude()));
                    jSONObject.put("type", i2);
                    jSONObject.put("timestamp", fa.b());
                    if (aMapLocation.getCoordType().equalsIgnoreCase(AMapLocation.COORD_TYPE_WGS84)) {
                        jSONObject.put("coordType", 1);
                    } else {
                        jSONObject.put("coordType", 2);
                    }
                    if (i2 == 0) {
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("accuracy", fa.b((double) aMapLocation.getAccuracy()));
                        jSONObject2.put("altitude", fa.b(aMapLocation.getAltitude()));
                        jSONObject2.put("bearing", fa.b((double) aMapLocation.getBearing()));
                        jSONObject2.put(Fields.Dgo_poi_2_running_click_go.speed, fa.b((double) aMapLocation.getSpeed()));
                        jSONObject.put("extension", jSONObject2);
                    }
                    JSONArray put = j.put(jSONObject);
                    j = put;
                    if (put.length() >= er.j()) {
                        f(context);
                    }
                }
            }
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "recordOfflineLocLog");
        }
    }

    private static void f(Context context) {
        try {
            if (j != null && j.length() > 0) {
                bp.a(new bo(context, es.b(), j.toString()), context);
                j = null;
            }
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "writeOfflineLocLog");
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
    public final void a(Context context, int i2) {
        try {
            if (this.b != i2) {
                if (!(this.b == -1 || this.b == i2)) {
                    long c2 = fa.c() - this.c;
                    this.a.append(this.b, Long.valueOf(this.a.get(this.b, 0L).longValue() + c2));
                }
                this.c = fa.c() - ez.b(context, "pref", this.d[i2], 0L);
                this.b = i2;
            }
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "setLocationType");
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
    public final void a(Context context, AMapLocationClientOption aMapLocationClientOption) {
        int i2;
        try {
            switch (aMapLocationClientOption.getLocationMode()) {
                case Battery_Saving:
                    i2 = 4;
                    break;
                case Device_Sensors:
                    i2 = 5;
                    break;
                case Hight_Accuracy:
                    i2 = 3;
                    break;
                default:
                    i2 = -1;
                    break;
            }
            if (this.e != i2) {
                if (!(this.e == -1 || this.e == i2)) {
                    this.a.append(this.e, Long.valueOf((fa.c() - this.f) + this.a.get(this.e, 0L).longValue()));
                }
                this.f = fa.c() - ez.b(context, "pref", this.d[i2], 0L);
                this.e = i2;
            }
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "setLocationMode");
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
    public final void b(Context context) {
        try {
            long c2 = fa.c() - this.c;
            if (this.b != -1) {
                this.a.append(this.b, Long.valueOf(this.a.get(this.b, 0L).longValue() + c2));
            }
            long c3 = fa.c() - this.f;
            if (this.e != -1) {
                this.a.append(this.e, Long.valueOf(this.a.get(this.e, 0L).longValue() + c3));
            }
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.d.length) {
                    long longValue = this.a.get(i3, 0L).longValue();
                    if (longValue > 0 && longValue > ez.b(context, "pref", this.d[i3], 0L)) {
                        ez.a(context, "pref", this.d[i3], longValue);
                    }
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "saveLocationTypeAndMode");
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
    public final int c(Context context) {
        try {
            long b2 = ez.b(context, "pref", this.d[2], 0L);
            long b3 = ez.b(context, "pref", this.d[0], 0L);
            long b4 = ez.b(context, "pref", this.d[1], 0L);
            if (b2 == 0 && b3 == 0 && b4 == 0) {
                return -1;
            }
            long j2 = b3 - b2;
            long j3 = b4 - b2;
            return b2 > j2 ? b2 > j3 ? 2 : 1 : j2 > j3 ? 0 : 1;
        } catch (Throwable th) {
            return -1;
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
    public final int d(Context context) {
        try {
            long b2 = ez.b(context, "pref", this.d[3], 0L);
            long b3 = ez.b(context, "pref", this.d[4], 0L);
            long b4 = ez.b(context, "pref", this.d[5], 0L);
            if (b2 == 0 && b3 == 0 && b4 == 0) {
                return -1;
            }
            return b2 > b3 ? b2 > b4 ? 3 : 5 : b3 > b4 ? 4 : 5;
        } catch (Throwable th) {
            return -1;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, int):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, boolean):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void */
    public final void e(Context context) {
        int i2 = 0;
        while (i2 < this.d.length) {
            try {
                ez.a(context, "pref", this.d[i2], 0L);
                i2++;
            } catch (Throwable th) {
                return;
            }
        }
    }
}
