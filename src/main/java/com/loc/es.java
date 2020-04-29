package com.loc;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import com.loc.ac;
import dji.publics.LogReport.base.Fields;
import dji.publics.protocol.ResponseBase;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONObject;

/* compiled from: CoreUtil */
public final class es {
    static String a = "http://apilocate.amap.com/mobile/binary";
    static String b = "";
    static String c = "001;11B;11C;11F;11G;11H;11I;11K;122;135;13J;13S;14S;157;15O;15U;16P";
    public static String d = null;
    public static String e = null;
    public static int f = 30000;
    public static String g = null;
    public static String h = null;
    static String i;
    static HashMap<String, String> j;
    static boolean k = false;
    static boolean l = false;
    private static final String[] m = {"com.amap.api.location", "com.loc", "com.amap.api.fence"};
    private static ac n = null;
    private static boolean o = false;
    private static boolean p = false;

    public static Bundle a(AMapLocationClientOption aMapLocationClientOption) {
        Bundle bundle = new Bundle();
        if (aMapLocationClientOption == null) {
            aMapLocationClientOption = new AMapLocationClientOption();
        }
        try {
            bundle.putParcelable("opt", aMapLocationClientOption);
        } catch (Throwable th) {
            a(th, "CoreUtil", "getOptionBundle");
        }
        return bundle;
    }

    public static AMapLocation a(AMapLocation aMapLocation, AMapLocation aMapLocation2) {
        if (aMapLocation2 != null) {
            try {
                aMapLocation.setCountry(aMapLocation2.getCountry());
                aMapLocation.setRoad(aMapLocation2.getRoad());
                aMapLocation.setPoiName(aMapLocation2.getPoiName());
                aMapLocation.setStreet(aMapLocation2.getStreet());
                aMapLocation.setNumber(aMapLocation2.getStreetNum());
                String cityCode = aMapLocation2.getCityCode();
                String adCode = aMapLocation2.getAdCode();
                aMapLocation.setCityCode(cityCode);
                aMapLocation.setAdCode(adCode);
                aMapLocation.setCity(aMapLocation2.getCity());
                aMapLocation.setDistrict(aMapLocation2.getDistrict());
                aMapLocation.setProvince(aMapLocation2.getProvince());
                aMapLocation.setAoiName(aMapLocation2.getAoiName());
                aMapLocation.setAddress(aMapLocation2.getAddress());
                aMapLocation.setDescription(aMapLocation2.getDescription());
                try {
                    if (aMapLocation.getExtras() != null) {
                        aMapLocation.getExtras().putString("citycode", aMapLocation2.getCityCode());
                        aMapLocation.getExtras().putString("desc", aMapLocation2.getExtras().getString("desc"));
                        aMapLocation.getExtras().putString("adcode", aMapLocation2.getAdCode());
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("citycode", aMapLocation2.getCityCode());
                        bundle.putString("desc", aMapLocation2.getExtras().getString("desc"));
                        bundle.putString("adcode", aMapLocation2.getAdCode());
                        aMapLocation.setExtras(bundle);
                    }
                } catch (Throwable th) {
                }
            } catch (Throwable th2) {
            }
        }
        return aMapLocation;
    }

    public static AMapLocationClientOption a(Bundle bundle) {
        AMapLocationClientOption aMapLocationClientOption;
        AMapLocationClientOption aMapLocationClientOption2 = new AMapLocationClientOption();
        if (bundle == null) {
            return aMapLocationClientOption2;
        }
        try {
            bundle.setClassLoader(AMapLocationClientOption.class.getClassLoader());
            aMapLocationClientOption = (AMapLocationClientOption) bundle.getParcelable("opt");
        } catch (Throwable th) {
            a(th, "CoreUtil", "getOptionFromBundle");
            aMapLocationClientOption = aMapLocationClientOption2;
        }
        return aMapLocationClientOption;
    }

    public static String a() {
        return a;
    }

    public static void a(Context context) {
        try {
            if (ad.b(context)) {
                a = "http://abroad.apilocate.amap.com/mobile/binary";
                return;
            }
            try {
                if (j == null) {
                    j = new HashMap<>(16);
                }
                j.clear();
                j.put("a9a9d23668a1a7ea93de9b21d67e436a", "F13160D440C7D0229DA95450F66AF92154AC84DF088F8CA3100B2E8131D57F3DC67124D4C466056E7A3DFBE035E1B9A4B9DA4DB68AE65A43EDFD92F5C60EF0C9");
                j.put("fe643c382e5c3b3962141f1a2e815a78", "FB923EE67A8B4032DAA517DD8CD7A26FF7C25B0C3663F92A0B61251C4FFFA858DF169D61321C3E7919CB67DF8EFEC827");
                j.put("b2e8bd171989cb2c3c13bd89b4c1067a", "239CE372F804D4BE4EAFFD183668379BDF274440E6F246AB16BBE6F5D1D30DEACFBBF0C942485727FF12288228760A9E");
                j.put("9a571aa113ad987d626c0457828962e6", "D2FF99A88BEB04683D89470D4FA72B1749DA456AB0D0F1A476477CE5A6874F53A9106423D905F9D808C0FCE8E7F1E04AC642F01FE41D0C7D933971F45CBA72B7");
                j.put("668319f11506def6208d6afe320dfd52", "53E53D46011A6BBAEA4FAE5442E659E0577CDD336F930C28635C322FB3F51C3C63F7FBAC9EAE448DFA2E5E5D716C4807");
                j.put("256b0f26bb2a9506be6cfdb84028ae08", "AF2228680EDC323FBA035362EB7E1E38A0C33E1CF6F6FB805EE553A230CBA754CD9552EB9B546542CBE619E8293151BE");
            } catch (Throwable th) {
                a(th, "CoreUtil", "initUrlHash");
            }
            String a2 = eh.a(u.f(context));
            i = a2;
            if (a2 == null) {
                return;
            }
            if (a2.length() != 0 && j != null && j.containsKey(a2)) {
                String str = j.get(a2);
                String str2 = (str == null || str.length() <= 0) ? null : new String(eh.d(a(str), a2), "utf-8");
                if (str2 != null && str2.length() > 0 && str2.contains("http:")) {
                    b = str2;
                    a = str2;
                }
            }
        } catch (Throwable th2) {
            a(th2, "CoreUtil", "checkUrl");
        }
    }

    public static void a(AMapLocation aMapLocation, JSONObject jSONObject) {
        if (jSONObject != null && aMapLocation != null) {
            try {
                double optDouble = jSONObject.optDouble(ResponseBase.STRING_LAT, aMapLocation.getLatitude());
                double optDouble2 = jSONObject.optDouble("lon", aMapLocation.getLongitude());
                aMapLocation.setProvider(jSONObject.optString(ResponseBase.STRING_PROVIDER, aMapLocation.getProvider()));
                aMapLocation.setLatitude(optDouble);
                aMapLocation.setLongitude(optDouble2);
                aMapLocation.setAltitude(jSONObject.optDouble("altitude", aMapLocation.getAltitude()));
                try {
                    String optString = jSONObject.optString("accuracy");
                    if (!TextUtils.isEmpty(optString)) {
                        aMapLocation.setAccuracy(Float.parseFloat(optString));
                    }
                } catch (Throwable th) {
                }
                try {
                    String optString2 = jSONObject.optString(Fields.Dgo_poi_2_running_click_go.speed);
                    if (!TextUtils.isEmpty(optString2)) {
                        aMapLocation.setSpeed(Float.parseFloat(optString2));
                    }
                } catch (Throwable th2) {
                }
                try {
                    String optString3 = jSONObject.optString("bearing");
                    if (!TextUtils.isEmpty(optString3)) {
                        aMapLocation.setBearing(Float.parseFloat(optString3));
                    }
                } catch (Throwable th3) {
                }
                aMapLocation.setAdCode(jSONObject.optString("adcode", aMapLocation.getAdCode()));
                aMapLocation.setCityCode(jSONObject.optString("citycode", aMapLocation.getCityCode()));
                aMapLocation.setAddress(jSONObject.optString("address", aMapLocation.getAddress()));
                String optString4 = jSONObject.optString("desc", "");
                aMapLocation.setCountry(jSONObject.optString("country", aMapLocation.getCountry()));
                aMapLocation.setProvince(jSONObject.optString(ResponseBase.STRING_PROVINCE, aMapLocation.getProvince()));
                aMapLocation.setCity(jSONObject.optString(ResponseBase.STRING_CITY, aMapLocation.getCity()));
                aMapLocation.setDistrict(jSONObject.optString("district", aMapLocation.getDistrict()));
                aMapLocation.setRoad(jSONObject.optString("road", aMapLocation.getRoad()));
                aMapLocation.setStreet(jSONObject.optString("street", aMapLocation.getStreet()));
                aMapLocation.setNumber(jSONObject.optString("number", aMapLocation.getStreetNum()));
                aMapLocation.setPoiName(jSONObject.optString("poiname", aMapLocation.getPoiName()));
                aMapLocation.setAoiName(jSONObject.optString("aoiname", aMapLocation.getAoiName()));
                aMapLocation.setErrorCode(jSONObject.optInt("errorCode", aMapLocation.getErrorCode()));
                aMapLocation.setErrorInfo(jSONObject.optString("errorInfo", aMapLocation.getErrorInfo()));
                aMapLocation.setLocationType(jSONObject.optInt("locationType", aMapLocation.getLocationType()));
                aMapLocation.setLocationDetail(jSONObject.optString("locationDetail", aMapLocation.getLocationDetail()));
                aMapLocation.setTime(jSONObject.optLong("time", aMapLocation.getTime()));
                boolean optBoolean = jSONObject.optBoolean("isOffset", aMapLocation.isOffset());
                aMapLocation.setOffset(optBoolean);
                aMapLocation.setBuildingId(jSONObject.optString("poiid", aMapLocation.getBuildingId()));
                aMapLocation.setFloor(jSONObject.optString("floor", aMapLocation.getFloor()));
                aMapLocation.setDescription(jSONObject.optString(ResponseBase.STRING_DESCRIPTION, aMapLocation.getDescription()));
                if (jSONObject.has("coordType")) {
                    aMapLocation.setCoordType(jSONObject.optString("coordType", AMapLocation.COORD_TYPE_GCJ02));
                } else if (!a(optDouble, optDouble2) || !optBoolean) {
                    aMapLocation.setCoordType(AMapLocation.COORD_TYPE_WGS84);
                } else {
                    aMapLocation.setCoordType(AMapLocation.COORD_TYPE_GCJ02);
                }
                Bundle bundle = new Bundle();
                bundle.putString("citycode", aMapLocation.getCityCode());
                bundle.putString("desc", optString4.toString());
                bundle.putString("adcode", aMapLocation.getAdCode());
                aMapLocation.setExtras(bundle);
            } catch (Throwable th4) {
                a(th4, "CoreUtil", "transformLocation");
            }
        }
    }

    public static void a(Throwable th, String str, String str2) {
        if (!"reportError".equals(str2) && !(th instanceof t)) {
            try {
                aq.b(th, str, str2);
            } catch (Throwable th2) {
            }
        }
    }

    public static boolean a(double d2, double d3) {
        int i2 = (int) ((d3 - 73.0d) / 0.5d);
        int i3 = (int) ((d2 - 3.5d) / 0.5d);
        if (i3 < 0 || i3 >= 101 || i2 < 0 || i2 >= 124) {
            return false;
        }
        try {
            return "00000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001100000001011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011101010111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000110111111111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111101111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000110111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011010111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001110011100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000110000001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001010011100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111100110001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111000111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111110011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111000000000111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111100000000000010111110100000011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111110000000001111111111111111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111000000111111111111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111101111111111111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111000000000000000000000000000000000000000000000000000000000000000000000000000000000000101111111111111111111111111111111111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000001111111111111111111111111111111111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000111111111111111111111111111111111111111111111000000000000000000000000000000000000000000000000000000000000000000000000000000001111111111111111111111111111111111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111111111100000000000000000000000000000000000000000000000000000000000000000011110000000001111111111111111111111111111111111111111111110000000000000000000000000000000000000000000000000000000000011000011111100000000111111111111111111111111111111111111111111111100000000000000000000000000000000000000000000000000001111111100111111111100110111111111111111111111111111111111111111111111110000000000000000000000000000000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111100000000000000000000000000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000000000000000000111111111111111111111111111111111111111111111111111111111111111111111111111111100000000000000000000000000000000000000000101111111111111111111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111111111111111100000000000000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111111110000000000000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111111110000000000000000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111111111111111110000000000000000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000000000111111111111111111111111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000000000000000111111111111111111111111111111111111111111111111111111111111111111111111111111111111100000000000000000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111100000000000000000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111100000000000000000000000000000000000000111111111111111111111111111111111111111111111111111111111111111111111111111111111111110000000000000000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111111111111111111110000000000000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111100000000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111110000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000000111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111011111000000000000000000000000000111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111100100000000000000000000000000000111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111100011100000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111000111110000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111110011111110000000000000000000000111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111110111111110000000000000000000000111011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111110000000000000000000000001111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111100000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111000000000000000000000000011111111111111111111111111111111111111111111111100001111111111111111111111111111111111111111111111111010000000000000000000000111111111111111111111111111111111111111111110000000000000001111111111111111111111111111111111111111111100000000000000000000011111111111111111111111111111111100000000000000000000000000001111111111111111111111111111111111111111110000000000000000000001111111111111111111111111111111100000000000000000000000000000001111111111111111111111111111111111111111000000000000000000000111111111111111111111111111111110000000000000000000000000000001111111111111111111111111111111111111111100000000000000000000111111111111111111111111111111000000000000000000000000000000000111111111111111111111111111111111111111111000000000000000000001111111111111111111111111110000000000000000000000000000000000001110011111111111111111111111111111111111111100000000000000000000011111111111111111100000000000000000000000000000000000000000000000001111111111111111111111111111111111111000000000000000000001111111111111111111000000000000000000000000000000000000000000000000011111111111111111111111111111111111100000000000000000000011111111111111111100000000000000000000000000000000000000000000000000011111111111111111111111111111111111000000000000000000001111111111111111100000000000000000000000000000000000000000000000000000000111111111111111111111111111111110000000000000000000000000111111111100000000000000000000000000000000000000000000000000111111111111111111111111111111111111111000000000000000000000000011111111100000000000000000000000000000000000000000000000000011111111111111111111111111111110001111100000000000000000000000000111110000000000000000000000000000000000000000000000000000001111111111111111111111111111111000000000000000000000000000000000001110000000000000000000000000000000000000000000000000000000011111111111111111111111111111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111111111111111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111111111111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111111111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010110000000000000000000000".charAt(i2 + (i3 * PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH)) == '1';
        } catch (Throwable th) {
            a(th, "CoreUtil", "isChina");
            return true;
        }
    }

    private static boolean a(double d2, double d3, double d4, double d5, double d6, double d7) {
        return Math.abs(((d4 - d2) * (d7 - d3)) - ((d6 - d2) * (d5 - d3))) < 1.0E-9d && (d2 - d4) * (d2 - d6) <= 0.0d && (d3 - d5) * (d3 - d7) <= 0.0d;
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00ae A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(com.amap.api.location.DPoint r29, java.util.List<com.amap.api.location.DPoint> r30) {
        /*
            r28 = 0
            r7 = 0
            double r2 = r29.getLongitude()
            double r4 = r29.getLatitude()
            double r24 = r29.getLatitude()
            r6 = 0
            r0 = r30
            java.lang.Object r6 = r0.get(r6)
            com.amap.api.location.DPoint r6 = (com.amap.api.location.DPoint) r6
            int r8 = r30.size()
            int r8 = r8 + -1
            r0 = r30
            java.lang.Object r8 = r0.get(r8)
            boolean r6 = r6.equals(r8)
            if (r6 != 0) goto L_0x0036
            r6 = 0
            r0 = r30
            java.lang.Object r6 = r0.get(r6)
            r0 = r30
            r0.add(r6)
        L_0x0036:
            r6 = 0
            r26 = r7
            r27 = r6
        L_0x003b:
            int r6 = r30.size()
            int r6 = r6 + -1
            r0 = r27
            if (r0 >= r6) goto L_0x012a
            r0 = r30
            r1 = r27
            java.lang.Object r6 = r0.get(r1)
            com.amap.api.location.DPoint r6 = (com.amap.api.location.DPoint) r6
            double r6 = r6.getLongitude()
            r0 = r30
            r1 = r27
            java.lang.Object r8 = r0.get(r1)
            com.amap.api.location.DPoint r8 = (com.amap.api.location.DPoint) r8
            double r8 = r8.getLatitude()
            int r10 = r27 + 1
            r0 = r30
            java.lang.Object r10 = r0.get(r10)
            com.amap.api.location.DPoint r10 = (com.amap.api.location.DPoint) r10
            double r10 = r10.getLongitude()
            int r12 = r27 + 1
            r0 = r30
            java.lang.Object r12 = r0.get(r12)
            com.amap.api.location.DPoint r12 = (com.amap.api.location.DPoint) r12
            double r12 = r12.getLatitude()
            boolean r14 = a(r2, r4, r6, r8, r10, r12)
            if (r14 == 0) goto L_0x0085
            r2 = 1
        L_0x0084:
            return r2
        L_0x0085:
            double r14 = r12 - r8
            double r14 = java.lang.Math.abs(r14)
            r16 = 4472406533629990549(0x3e112e0be826d695, double:1.0E-9)
            int r14 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r14 < 0) goto L_0x0135
            r22 = 4640537203540230144(0x4066800000000000, double:180.0)
            r14 = r6
            r16 = r8
            r18 = r2
            r20 = r4
            boolean r14 = a(r14, r16, r18, r20, r22, r24)
            if (r14 == 0) goto L_0x00b5
            int r6 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r6 <= 0) goto L_0x0135
            int r26 = r26 + 1
            r6 = r26
        L_0x00ae:
            int r7 = r27 + 1
            r26 = r6
            r27 = r7
            goto L_0x003b
        L_0x00b5:
            r18 = 4640537203540230144(0x4066800000000000, double:180.0)
            r14 = r2
            r16 = r4
            r20 = r24
            boolean r14 = a(r10, r12, r14, r16, r18, r20)
            if (r14 == 0) goto L_0x00ce
            int r6 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r6 <= 0) goto L_0x0135
            int r26 = r26 + 1
            r6 = r26
            goto L_0x00ae
        L_0x00ce:
            r14 = 0
            double r16 = r10 - r6
            double r18 = r24 - r4
            double r16 = r16 * r18
            double r18 = r12 - r8
            r20 = 4640537203540230144(0x4066800000000000, double:180.0)
            double r20 = r20 - r2
            double r18 = r18 * r20
            double r16 = r16 - r18
            r18 = 0
            int r15 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r15 == 0) goto L_0x0139
            double r18 = r8 - r4
            r20 = 4640537203540230144(0x4066800000000000, double:180.0)
            double r20 = r20 - r2
            double r18 = r18 * r20
            double r20 = r6 - r2
            double r22 = r24 - r4
            double r20 = r20 * r22
            double r18 = r18 - r20
            double r18 = r18 / r16
            double r20 = r8 - r4
            double r10 = r10 - r6
            double r10 = r10 * r20
            double r6 = r6 - r2
            double r8 = r12 - r8
            double r6 = r6 * r8
            double r6 = r10 - r6
            double r6 = r6 / r16
            r8 = 0
            int r8 = (r18 > r8 ? 1 : (r18 == r8 ? 0 : -1))
            if (r8 < 0) goto L_0x0139
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r8 = (r18 > r8 ? 1 : (r18 == r8 ? 0 : -1))
            if (r8 > 0) goto L_0x0139
            r8 = 0
            int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r8 < 0) goto L_0x0139
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 > 0) goto L_0x0139
            r6 = 1
        L_0x0123:
            if (r6 == 0) goto L_0x0135
            int r26 = r26 + 1
            r6 = r26
            goto L_0x00ae
        L_0x012a:
            int r2 = r26 % 2
            if (r2 == 0) goto L_0x0131
            r2 = 1
            goto L_0x0084
        L_0x0131:
            r2 = r28
            goto L_0x0084
        L_0x0135:
            r6 = r26
            goto L_0x00ae
        L_0x0139:
            r6 = r14
            goto L_0x0123
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.es.a(com.amap.api.location.DPoint, java.util.List):boolean");
    }

    private static byte[] a(String str) {
        if (str == null || str.length() < 2) {
            return new byte[0];
        }
        String lowerCase = str.toLowerCase(Locale.US);
        int length = lowerCase.length() / 2;
        byte[] bArr = new byte[length];
        for (int i2 = 0; i2 < length; i2++) {
            bArr[i2] = (byte) (fa.h(lowerCase.substring(i2 * 2, (i2 * 2) + 2)) & 255);
        }
        return bArr;
    }

    public static ac b() {
        try {
            if (n == null) {
                n = new ac.a("loc", "4.7.1", "AMAP_Location_SDK_Android 4.7.1").a((String[]) m.clone()).a("4.7.1").a();
            }
        } catch (Throwable th) {
            a(th, "CoreUtil", "getSDKInfo");
        }
        return n;
    }

    public static String b(Context context) {
        return aa.b(u.e(context));
    }

    public static String c() {
        return c;
    }

    public static void c(Context context) {
        try {
            if (ad.b(context)) {
                a = "http://abroad.apilocate.amap.com/mobile/binary";
            } else if (TextUtils.isEmpty(b)) {
                a = "http://apilocate.amap.com/mobile/binary";
            } else {
                a = b;
            }
        } catch (Throwable th) {
            a(th, "CoreUtil", "changeUrl");
        }
    }

    public static boolean d() {
        if (!k) {
            k = true;
            l = false;
        }
        return l;
    }
}
