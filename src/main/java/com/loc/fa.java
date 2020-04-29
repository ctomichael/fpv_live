package com.loc;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.DPoint;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import com.billy.cc.core.component.CCUtil;
import com.dji.permission.Permission;
import dji.pilot.fpv.util.DJIFlurryReport;
import dji.sdksharedlib.keycatalog.extension.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import org.json.JSONObject;

/* compiled from: Utils */
public final class fa {
    static WifiManager a = null;
    private static int b = 0;
    private static String[] c = null;
    private static Hashtable<String, Long> d = new Hashtable<>();
    private static SparseArray<String> e = null;
    private static String[] f = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION};
    private static String g = "android.permission.ACCESS_BACKGROUND_LOCATION";
    private static boolean h = false;

    public static double a(double d2) {
        return ((double) ((long) (d2 * 1000000.0d))) / 1000000.0d;
    }

    public static float a(float f2) {
        return (float) (((double) ((long) (((double) f2) * 100.0d))) / 100.0d);
    }

    public static float a(AMapLocation aMapLocation, AMapLocation aMapLocation2) {
        return a(new double[]{aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation2.getLatitude(), aMapLocation2.getLongitude()});
    }

    public static float a(DPoint dPoint, DPoint dPoint2) {
        return a(new double[]{dPoint.getLatitude(), dPoint.getLongitude(), dPoint2.getLatitude(), dPoint2.getLongitude()});
    }

    public static float a(double[] dArr) {
        float[] fArr = new float[1];
        Location.distanceBetween(dArr[0], dArr[1], dArr[2], dArr[3], fArr);
        return fArr[0];
    }

    public static int a(int i) {
        return (i * 2) - 113;
    }

    public static int a(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }

    public static Object a(Context context, String str) {
        if (context == null) {
            return null;
        }
        try {
            return context.getApplicationContext().getSystemService(str);
        } catch (Throwable th) {
            es.a(th, Utils.TAG, "getServ");
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0020 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(long r4, java.lang.String r6) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 == 0) goto L_0x0009
            java.lang.String r6 = "yyyy-MM-dd HH:mm:ss"
        L_0x0009:
            r2 = 0
            java.text.SimpleDateFormat r1 = new java.text.SimpleDateFormat     // Catch:{ Throwable -> 0x0024 }
            java.util.Locale r0 = java.util.Locale.CHINA     // Catch:{ Throwable -> 0x0024 }
            r1.<init>(r6, r0)     // Catch:{ Throwable -> 0x0024 }
            r1.applyPattern(r6)     // Catch:{ Throwable -> 0x0039 }
        L_0x0014:
            r2 = 0
            int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r0 > 0) goto L_0x001e
            long r4 = java.lang.System.currentTimeMillis()
        L_0x001e:
            if (r1 != 0) goto L_0x0030
            java.lang.String r0 = "NULL"
        L_0x0023:
            return r0
        L_0x0024:
            r0 = move-exception
            r1 = r2
        L_0x0026:
            java.lang.String r2 = "Utils"
            java.lang.String r3 = "formatUTC"
            com.loc.es.a(r0, r2, r3)
            goto L_0x0014
        L_0x0030:
            java.lang.Long r0 = java.lang.Long.valueOf(r4)
            java.lang.String r0 = r1.format(r0)
            goto L_0x0023
        L_0x0039:
            r0 = move-exception
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.fa.a(long, java.lang.String):java.lang.String");
    }

    public static void a() {
    }

    public static boolean a(long j, long j2) {
        String a2 = a(j, "yyyyMMddHH");
        String a3 = a(j2, "yyyyMMddHH");
        if ("NULL".equals(a2) || "NULL".equals(a3)) {
            return false;
        }
        return a2.equals(a3);
    }

    public static boolean a(Context context) {
        if (context == null) {
            return false;
        }
        try {
            return d() < 17 ? d(context, "android.provider.Settings$System") : d(context, "android.provider.Settings$Global");
        } catch (Throwable th) {
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0064, code lost:
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0067, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006d, code lost:
        if (r2 != null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006f, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0072, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        return true;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x006c A[ExcHandler: all (r0v1 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:4:0x0019] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0076  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(android.database.sqlite.SQLiteDatabase r6, java.lang.String r7) {
        /*
            r2 = 0
            r1 = 1
            r0 = 0
            boolean r3 = android.text.TextUtils.isEmpty(r7)
            if (r3 == 0) goto L_0x000a
        L_0x0009:
            return r0
        L_0x000a:
            java.lang.String r3 = "2.0.201501131131"
            java.lang.String r4 = "."
            java.lang.String r5 = ""
            java.lang.String r3 = r3.replace(r4, r5)
            if (r6 == 0) goto L_0x0009
            boolean r4 = r6.isOpen()     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            if (r4 == 0) goto L_0x0009
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            r4.<init>()     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            java.lang.String r5 = "SELECT count(*) as c FROM sqlite_master WHERE type = 'table' AND name = '"
            r4.append(r5)     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            java.lang.String r5 = r7.trim()     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            java.lang.StringBuilder r5 = r4.append(r5)     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            java.lang.StringBuilder r3 = r5.append(r3)     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            java.lang.String r5 = "' "
            r3.append(r5)     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            java.lang.String r3 = r4.toString()     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            r5 = 0
            android.database.Cursor r2 = r6.rawQuery(r3, r5)     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            if (r2 == 0) goto L_0x0055
            boolean r3 = r2.moveToFirst()     // Catch:{ Throwable -> 0x0073, all -> 0x006c }
            if (r3 == 0) goto L_0x0055
            r3 = 0
            int r3 = r2.getInt(r3)     // Catch:{ Throwable -> 0x0073, all -> 0x006c }
            if (r3 <= 0) goto L_0x0055
            r0 = r1
        L_0x0055:
            r3 = 0
            int r5 = r4.length()     // Catch:{ Throwable -> 0x0073, all -> 0x006c }
            r4.delete(r3, r5)     // Catch:{ Throwable -> 0x0073, all -> 0x006c }
            if (r2 == 0) goto L_0x0009
            r2.close()
            goto L_0x0009
        L_0x0063:
            r0 = move-exception
            r0 = r2
        L_0x0065:
            if (r0 == 0) goto L_0x0076
            r0.close()
            r0 = r1
            goto L_0x0009
        L_0x006c:
            r0 = move-exception
            if (r2 == 0) goto L_0x0072
            r2.close()
        L_0x0072:
            throw r0
        L_0x0073:
            r0 = move-exception
            r0 = r2
            goto L_0x0065
        L_0x0076:
            r0 = r1
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.fa.a(android.database.sqlite.SQLiteDatabase, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x001f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(android.location.Location r7, int r8) {
        /*
            r6 = 0
            r2 = 1
            r3 = 0
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r3)
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 18
            if (r0 < r4) goto L_0x0022
            java.lang.String r0 = "isFromMockProvider"
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0021 }
            java.lang.Object r0 = com.loc.ew.a(r7, r0, r4)     // Catch:{ Throwable -> 0x0021 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x0021 }
        L_0x0019:
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0024
            r0 = r2
        L_0x0020:
            return r0
        L_0x0021:
            r0 = move-exception
        L_0x0022:
            r0 = r1
            goto L_0x0019
        L_0x0024:
            android.os.Bundle r0 = r7.getExtras()
            if (r0 == 0) goto L_0x0055
            java.lang.String r1 = "satellites"
            int r0 = r0.getInt(r1)
        L_0x0031:
            if (r0 > 0) goto L_0x0035
            r0 = r2
            goto L_0x0020
        L_0x0035:
            if (r8 != 0) goto L_0x0053
            double r0 = r7.getAltitude()
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x0053
            float r0 = r7.getBearing()
            int r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x0053
            float r0 = r7.getSpeed()
            int r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x0053
            r0 = r2
            goto L_0x0020
        L_0x0053:
            r0 = r3
            goto L_0x0020
        L_0x0055:
            r0 = r3
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.fa.a(android.location.Location, int):boolean");
    }

    public static boolean a(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            return b(aMapLocation);
        }
        return false;
    }

    public static boolean a(AMapLocationServer aMapLocationServer) {
        if (aMapLocationServer != null && !"8".equals(aMapLocationServer.d()) && !"5".equals(aMapLocationServer.d()) && !"6".equals(aMapLocationServer.d())) {
            return b(aMapLocationServer);
        }
        return false;
    }

    public static boolean a(String str) {
        return !TextUtils.isEmpty(str) && !"00:00:00:00:00:00".equals(str) && !str.contains(" :");
    }

    public static boolean a(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        ArrayList<String> d2 = d(str);
        String[] split = str2.toString().split("#");
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < split.length; i3++) {
            if (split[i3].contains(",nb") || split[i3].contains(",access")) {
                i++;
                if (d2.contains(split[i3])) {
                    i2++;
                }
            }
        }
        return ((double) (i2 * 2)) >= ((double) (d2.size() + i)) * 0.618d;
    }

    public static boolean a(JSONObject jSONObject, String str) {
        return ad.a(jSONObject, str);
    }

    public static byte[] a(int i, byte[] bArr) {
        if (bArr == null || bArr.length < 2) {
            bArr = new byte[2];
        }
        bArr[0] = (byte) (i & 255);
        bArr[1] = (byte) ((65280 & i) >> 8);
        return bArr;
    }

    public static byte[] a(long j) {
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) ((int) ((j >> (i * 8)) & 255));
        }
        return bArr;
    }

    public static byte[] a(byte[] bArr) {
        try {
            return ad.b(bArr);
        } catch (Throwable th) {
            es.a(th, Utils.TAG, "gz");
            return null;
        }
    }

    public static String[] a(TelephonyManager telephonyManager) {
        int i;
        String str = null;
        if (telephonyManager != null) {
            str = telephonyManager.getNetworkOperator();
        }
        String[] strArr = {"0", "0"};
        if (TextUtils.isEmpty(str) ? false : !TextUtils.isDigitsOnly(str) ? false : str.length() > 4) {
            strArr[0] = str.substring(0, 3);
            char[] charArray = str.substring(3).toCharArray();
            int i2 = 0;
            while (i2 < charArray.length && Character.isDigit(charArray[i2])) {
                i2++;
            }
            strArr[1] = str.substring(3, i2 + 3);
        }
        try {
            i = Integer.parseInt(strArr[0]);
        } catch (Throwable th) {
            es.a(th, Utils.TAG, "getMccMnc");
            i = 0;
        }
        if (i == 0) {
            strArr[0] = "0";
        }
        if ("0".equals(strArr[0]) || "0".equals(strArr[1])) {
            return (!"0".equals(strArr[0]) || !"0".equals(strArr[1]) || c == null) ? strArr : c;
        }
        c = strArr;
        return strArr;
    }

    public static double b(double d2) {
        return ((double) ((long) (d2 * 100.0d))) / 100.0d;
    }

    public static long b() {
        return System.currentTimeMillis();
    }

    public static String b(int i) {
        switch (i) {
            case 0:
                return DJIFlurryReport.FlightRecord.V2_EVENT_FLIGHTDATA_SYNCHRONOUS_SUBKEY;
            case 1:
                return "重要参数为空";
            case 2:
                return "WIFI信息不足";
            case 3:
                return "请求参数获取出现异常";
            case 4:
                return "网络连接异常";
            case 5:
                return "解析数据异常";
            case 6:
                return "定位结果错误";
            case 7:
                return "KEY错误";
            case 8:
                return "其他错误";
            case 9:
                return "初始化异常";
            case 10:
                return "定位服务启动失败";
            case 11:
                return "错误的基站信息，请检查是否插入SIM卡";
            case 12:
                return "缺少定位权限";
            case 13:
                return "网络定位失败，请检查设备是否插入sim卡，是否开启移动网络或开启了wifi模块";
            case 14:
                return "GPS 定位失败，由于设备当前 GPS 状态差,建议持设备到相对开阔的露天场所再次尝试";
            case 15:
                return "当前返回位置为模拟软件返回，请关闭模拟软件，或者在option中设置允许模拟";
            case 16:
            case 17:
            default:
                return "其他错误";
            case 18:
                return "定位失败，飞行模式下关闭了WIFI开关，请关闭飞行模式或者打开WIFI开关";
            case 19:
                return "定位失败，没有检查到SIM卡，并且关闭了WIFI开关，请打开WIFI开关或者插入SIM卡";
        }
    }

    public static String b(Context context) {
        PackageInfo packageInfo;
        CharSequence charSequence = null;
        if (!TextUtils.isEmpty(es.g)) {
            return es.g;
        }
        if (context == null) {
            return null;
        }
        try {
            packageInfo = context.getPackageManager().getPackageInfo(u.c(context), 64);
        } catch (Throwable th) {
            es.a(th, Utils.TAG, "getAppName part");
            packageInfo = null;
        }
        try {
            if (TextUtils.isEmpty(es.h)) {
                es.h = null;
            }
        } catch (Throwable th2) {
            es.a(th2, Utils.TAG, "getAppName");
        }
        StringBuilder sb = new StringBuilder();
        if (packageInfo != null) {
            if (packageInfo.applicationInfo != null) {
                charSequence = packageInfo.applicationInfo.loadLabel(context.getPackageManager());
            }
            if (charSequence != null) {
                sb.append(charSequence.toString());
            }
            if (!TextUtils.isEmpty(packageInfo.versionName)) {
                sb.append(packageInfo.versionName);
            }
        }
        String c2 = u.c(context);
        if (!TextUtils.isEmpty(c2)) {
            sb.append(",").append(c2);
        }
        if (!TextUtils.isEmpty(es.h)) {
            sb.append(",").append(es.h);
        }
        String sb2 = sb.toString();
        es.g = sb2;
        return sb2;
    }

    public static String b(TelephonyManager telephonyManager) {
        int i = 0;
        if (e == null) {
            SparseArray<String> sparseArray = new SparseArray<>();
            e = sparseArray;
            sparseArray.append(0, "UNKWN");
            e.append(1, "GPRS");
            e.append(2, "EDGE");
            e.append(3, "UMTS");
            e.append(4, "CDMA");
            e.append(5, "EVDO_0");
            e.append(6, "EVDO_A");
            e.append(7, "1xRTT");
            e.append(8, "HSDPA");
            e.append(9, "HSUPA");
            e.append(10, "HSPA");
            e.append(11, "IDEN");
            e.append(12, "EVDO_B");
            e.append(13, "LTE");
            e.append(14, "EHRPD");
            e.append(15, "HSPAP");
        }
        if (telephonyManager != null) {
            i = telephonyManager.getNetworkType();
        }
        return e.get(i, "UNKWN");
    }

    public static boolean b(long j, long j2) {
        String a2 = a(j, "yyyyMMdd");
        String a3 = a(j2, "yyyyMMdd");
        if ("NULL".equals(a2) || "NULL".equals(a3)) {
            return false;
        }
        return a2.equals(a3);
    }

    public static boolean b(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0) != null;
        } catch (Throwable th) {
            return false;
        }
    }

    public static boolean b(AMapLocation aMapLocation) {
        double longitude = aMapLocation.getLongitude();
        double latitude = aMapLocation.getLatitude();
        return !(longitude == 0.0d && latitude == 0.0d) && longitude <= 180.0d && latitude <= 90.0d && longitude >= -180.0d && latitude >= -90.0d;
    }

    public static byte[] b(int i, byte[] bArr) {
        if (bArr == null || bArr.length < 4) {
            bArr = new byte[4];
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) ((i >> (i2 * 8)) & 255);
        }
        return bArr;
    }

    public static byte[] b(String str) {
        return a(Integer.parseInt(str), (byte[]) null);
    }

    public static double c(double d2) {
        return ((double) ((long) (d2 * 1000000.0d))) / 1000000.0d;
    }

    public static long c() {
        return SystemClock.elapsedRealtime();
    }

    public static NetworkInfo c(Context context) {
        try {
            return x.r(context);
        } catch (Throwable th) {
            es.a(th, Utils.TAG, "getNetWorkInfo");
            return null;
        }
    }

    public static boolean c(long j, long j2) {
        boolean z = true;
        if (!b(j, j2)) {
            return false;
        }
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTimeInMillis(j);
        int i = instance.get(11);
        instance.setTimeInMillis(j2);
        int i2 = instance.get(11);
        if (i <= 12 ? i2 > 12 : i2 <= 12) {
            z = false;
        }
        return z;
    }

    public static boolean c(Context context, String str) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(str, 256);
        } catch (Throwable th) {
        }
        return packageInfo != null;
    }

    public static byte[] c(String str) {
        return b(Integer.parseInt(str), (byte[]) null);
    }

    public static int d() {
        if (b > 0) {
            return b;
        }
        try {
            return ew.b("android.os.Build$VERSION", "SDK_INT");
        } catch (Throwable th) {
            return 0;
        }
    }

    public static ArrayList<String> d(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("#");
            for (int i = 0; i < split.length; i++) {
                if (split[i].contains(",nb") || split[i].contains(",access")) {
                    arrayList.add(split[i]);
                }
            }
        }
        return arrayList;
    }

    public static boolean d(Context context) {
        try {
            NetworkInfo c2 = c(context);
            return c2 != null && c2.isConnectedOrConnecting();
        } catch (Throwable th) {
            return false;
        }
    }

    private static boolean d(Context context, String str) throws Throwable {
        return ((Integer) ew.a(str, "getInt", new Object[]{context.getContentResolver(), ((String) ew.a(str, "AIRPLANE_MODE_ON")).toString()}, new Class[]{ContentResolver.class, String.class})).intValue() == 1;
    }

    public static double e(String str) throws NumberFormatException {
        return Double.parseDouble(str);
    }

    public static String e() {
        return Build.MODEL;
    }

    public static boolean e(Context context) {
        try {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (runningAppProcessInfo.processName.equals(u.c(context))) {
                    return runningAppProcessInfo.importance != 100;
                }
            }
            return false;
        } catch (Throwable th) {
            es.a(th, Utils.TAG, "isApplicationBroughtToBackground");
            return true;
        }
    }

    public static float f(String str) throws NumberFormatException {
        return Float.parseFloat(str);
    }

    public static String f() {
        return Build.VERSION.RELEASE;
    }

    public static boolean f(Context context) {
        int i;
        if (Build.VERSION.SDK_INT < 23 || context.getApplicationInfo().targetSdkVersion < 23) {
            for (String str : f) {
                if (context.checkCallingOrSelfPermission(str) != 0) {
                    return false;
                }
            }
            return true;
        }
        Application application = (Application) context;
        for (String str2 : f) {
            try {
                i = ew.b(application.getBaseContext(), "checkSelfPermission", str2);
            } catch (Throwable th) {
                i = 0;
            }
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    public static int g() {
        return new Random().nextInt(65536) - 32768;
    }

    public static int g(String str) throws NumberFormatException {
        return Integer.parseInt(str);
    }

    public static boolean g(Context context) {
        int i;
        if (context.getApplicationInfo().targetSdkVersion >= 29) {
            try {
                i = ew.b(((Application) context).getBaseContext(), "checkSelfPermission", g);
            } catch (Throwable th) {
                i = 0;
            }
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    public static int h(String str) throws NumberFormatException {
        return Integer.parseInt(str, 16);
    }

    public static void h() {
        d.clear();
    }

    @SuppressLint({"NewApi"})
    public static boolean h(Context context) {
        boolean z;
        if (context == null) {
            return true;
        }
        if (a == null) {
            a = (WifiManager) a(context, "wifi");
        }
        try {
            z = a.isWifiEnabled();
        } catch (Throwable th) {
            z = false;
        }
        if (z || d() <= 17) {
            return z;
        }
        try {
            return "true".equals(String.valueOf(ew.a(a, "isScanAlwaysAvailable", new Object[0])));
        } catch (Throwable th2) {
            return z;
        }
    }

    public static byte i(String str) throws NumberFormatException {
        return Byte.parseByte(str);
    }

    public static String i() {
        try {
            return y.b("S128DF1572465B890OE3F7A13167KLEI".getBytes("UTF-8")).substring(20);
        } catch (Throwable th) {
            return "";
        }
    }

    public static String i(Context context) {
        NetworkInfo c2 = c(context);
        if (c2 == null || !c2.isConnectedOrConnecting()) {
            return "DISCONNECTED";
        }
        int type = c2.getType();
        if (type == 1) {
            return "WIFI";
        }
        if (type != 0) {
            return CCUtil.PROCESS_UNKNOWN;
        }
        String subtypeName = c2.getSubtypeName();
        switch (c2.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return "2G";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return "3G";
            case 13:
                return "4G";
            default:
                return "GSM".equalsIgnoreCase(subtypeName) ? "2G" : ("TD-SCDMA".equalsIgnoreCase(subtypeName) || "WCDMA".equalsIgnoreCase(subtypeName) || "CDMA2000".equalsIgnoreCase(subtypeName)) ? "3G" : subtypeName;
        }
    }

    public static String j() {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator);
        sb.append("amap").append(File.separator);
        sb.append("openamaplocationsdk").append(File.separator);
        return sb.toString();
    }

    public static String j(Context context) {
        String l = x.l(context);
        if (TextUtils.isEmpty(l) || l.equals("00:00:00:00:00:00")) {
            l = "00:00:00:00:00:00";
            if (context != null) {
                l = ez.b(context, "pref", "smac", l);
            }
        }
        if (TextUtils.isEmpty(l)) {
            l = "00:00:00:00:00:00";
        }
        if (!h) {
            if (context != null && !TextUtils.isEmpty(l)) {
                ez.a(context, "pref", "smac", l);
            }
            h = true;
        }
        return l;
    }

    public static boolean k(Context context) {
        return Build.VERSION.SDK_INT >= 28 && context.getApplicationInfo().targetSdkVersion >= 28;
    }

    public static boolean l(Context context) {
        ServiceInfo serviceInfo = null;
        try {
            serviceInfo = context.getPackageManager().getServiceInfo(new ComponentName(context, "com.amap.api.location.APSService"), 128);
        } catch (Throwable th) {
        }
        return serviceInfo != null;
    }
}
