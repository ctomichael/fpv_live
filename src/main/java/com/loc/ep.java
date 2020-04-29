package com.loc;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.adobe.xmp.XMPConst;
import com.amap.api.location.AMapLocationClientOption;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import dji.publics.protocol.ResponseBase;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: Parser */
public final class ep {
    private StringBuilder a = new StringBuilder();
    private AMapLocationClientOption b = new AMapLocationClientOption();

    private void a(AMapLocationServer aMapLocationServer, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(str)) {
            sb.append(str).append(" ");
        }
        if (!TextUtils.isEmpty(str2)) {
            if (this.b.getGeoLanguage() == AMapLocationClientOption.GeoLanguage.EN) {
                if (!str2.equals(str)) {
                    sb.append(str2).append(" ");
                }
            } else if (!str.contains("市") || !str.equals(str2)) {
                sb.append(str2).append(" ");
            }
        }
        if (!TextUtils.isEmpty(str3)) {
            sb.append(str3).append(" ");
        }
        if (!TextUtils.isEmpty(str4)) {
            sb.append(str4).append(" ");
        }
        if (!TextUtils.isEmpty(str5)) {
            sb.append(str5).append(" ");
        }
        if (!TextUtils.isEmpty(str6)) {
            if (TextUtils.isEmpty(str7) || this.b.getGeoLanguage() == AMapLocationClientOption.GeoLanguage.EN) {
                sb.append("Near " + str6);
                aMapLocationServer.setDescription("Near " + str6);
            } else {
                sb.append("靠近");
                sb.append(str6).append(" ");
                aMapLocationServer.setDescription("在" + str6 + "附近");
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("citycode", aMapLocationServer.getCityCode());
        bundle.putString("desc", sb.toString());
        bundle.putString("adcode", aMapLocationServer.getAdCode());
        aMapLocationServer.setExtras(bundle);
        aMapLocationServer.g(sb.toString());
        String adCode = aMapLocationServer.getAdCode();
        if (adCode == null || adCode.trim().length() <= 0 || this.b.getGeoLanguage() == AMapLocationClientOption.GeoLanguage.EN) {
            aMapLocationServer.setAddress(sb.toString());
        } else {
            aMapLocationServer.setAddress(sb.toString().replace(" ", ""));
        }
    }

    private static String b(String str) {
        return XMPConst.ARRAY_ITEM_NAME.equals(str) ? "" : str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:124:0x026c, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x026d, code lost:
        r2 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x0276, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x0277, code lost:
        r4 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x027b, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x027c, code lost:
        r5 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x0280, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x0281, code lost:
        r6 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x0285, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x0286, code lost:
        r7 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x028a, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x028b, code lost:
        r8 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x029c, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x029d, code lost:
        r1 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x02e4, code lost:
        r9.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x02e8, code lost:
        r0 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x01fe A[Catch:{ Throwable -> 0x029c, all -> 0x02e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x021c A[Catch:{ Throwable -> 0x029c, all -> 0x02e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x0235 A[Catch:{ Throwable -> 0x029c, all -> 0x02e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x0252  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x025d  */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x02e4  */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x02e8 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:7:0x0029] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x019e A[Catch:{ Throwable -> 0x029c, all -> 0x02e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01ae A[Catch:{ Throwable -> 0x029c, all -> 0x02e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x01e1 A[Catch:{ Throwable -> 0x029c, all -> 0x02e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01f1 A[Catch:{ Throwable -> 0x029c, all -> 0x02e8 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.autonavi.aps.amapapi.model.AMapLocationServer a(com.autonavi.aps.amapapi.model.AMapLocationServer r12, byte[] r13) {
        /*
            r11 = this;
            r1 = 0
            if (r13 != 0) goto L_0x0025
            r0 = 5
            r12.setErrorCode(r0)     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            java.lang.StringBuilder r0 = r11.a     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            java.lang.String r2 = "binaryResult is null#0504"
            r0.append(r2)     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            java.lang.StringBuilder r0 = r11.a     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            r12.setLocationDetail(r0)     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            java.lang.StringBuilder r0 = r11.a     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            r2 = 0
            java.lang.StringBuilder r3 = r11.a     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            int r3 = r3.length()     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            r0.delete(r2, r3)     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
        L_0x0024:
            return r12
        L_0x0025:
            java.nio.ByteBuffer r9 = java.nio.ByteBuffer.wrap(r13)     // Catch:{ Throwable -> 0x02ed, all -> 0x02e0 }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            if (r0 != 0) goto L_0x0043
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r12.b(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.clear()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            if (r9 == 0) goto L_0x0024
            r9.clear()
            goto L_0x0024
        L_0x0043:
            int r0 = r9.getInt()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            double r0 = (double) r0     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r2 = 4696837146684686336(0x412e848000000000, double:1000000.0)
            double r0 = r0 / r2
            double r0 = com.loc.fa.a(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r12.setLongitude(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            int r0 = r9.getInt()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            double r0 = (double) r0     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r2 = 4696837146684686336(0x412e848000000000, double:1000000.0)
            double r0 = r0 / r2
            double r0 = com.loc.fa.a(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r12.setLatitude(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            float r0 = (float) r0     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r12.setAccuracy(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r12.c(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r12.d(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r1 = 1
            if (r0 != r1) goto L_0x018c
            java.lang.String r1 = ""
            java.lang.String r3 = ""
            java.lang.String r4 = ""
            java.lang.String r5 = ""
            java.lang.String r6 = ""
            java.lang.String r7 = ""
            java.lang.String r8 = ""
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r2 = new java.lang.String     // Catch:{ Throwable -> 0x0311, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r2.<init>(r0, r10)     // Catch:{ Throwable -> 0x0311, all -> 0x02e8 }
            r12.setCountry(r2)     // Catch:{ Throwable -> 0x0311, all -> 0x02e8 }
        L_0x00b7:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r2 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r2)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x026b, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r2, r10)     // Catch:{ Throwable -> 0x026b, all -> 0x02e8 }
            r12.setProvince(r0)     // Catch:{ Throwable -> 0x030e, all -> 0x02e8 }
            r2 = r0
        L_0x00ce:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r1)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0270, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x0270, all -> 0x02e8 }
            r12.setCity(r0)     // Catch:{ Throwable -> 0x030b, all -> 0x02e8 }
            r3 = r0
        L_0x00e5:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r1)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0275, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x0275, all -> 0x02e8 }
            r12.setDistrict(r0)     // Catch:{ Throwable -> 0x0308, all -> 0x02e8 }
            r4 = r0
        L_0x00fc:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r1)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x027a, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x027a, all -> 0x02e8 }
            r12.setStreet(r0)     // Catch:{ Throwable -> 0x0305, all -> 0x02e8 }
            r12.setRoad(r0)     // Catch:{ Throwable -> 0x0305, all -> 0x02e8 }
            r5 = r0
        L_0x0116:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r1)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x027f, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x027f, all -> 0x02e8 }
            r12.setNumber(r0)     // Catch:{ Throwable -> 0x0302, all -> 0x02e8 }
            r6 = r0
        L_0x012d:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r1)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0284, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x0284, all -> 0x02e8 }
            r12.setPoiName(r0)     // Catch:{ Throwable -> 0x0300, all -> 0x02e8 }
            r7 = r0
        L_0x0144:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x02fd, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r1.<init>(r0, r10)     // Catch:{ Throwable -> 0x02fd, all -> 0x02e8 }
            r12.setAoiName(r1)     // Catch:{ Throwable -> 0x02fd, all -> 0x02e8 }
        L_0x015a:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r1)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0289, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x0289, all -> 0x02e8 }
            r12.setAdCode(r0)     // Catch:{ Throwable -> 0x02fb, all -> 0x02e8 }
            r8 = r0
        L_0x0171:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x02f8, all -> 0x02e8 }
            java.lang.String r10 = "UTF-8"
            r1.<init>(r0, r10)     // Catch:{ Throwable -> 0x02f8, all -> 0x02e8 }
            r12.setCityCode(r1)     // Catch:{ Throwable -> 0x02f8, all -> 0x02e8 }
        L_0x0187:
            r0 = r11
            r1 = r12
            r0.a(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
        L_0x018c:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r1 = 1
            if (r0 != r1) goto L_0x01a7
            r9.getInt()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.getInt()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.getShort()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
        L_0x01a7:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r1 = 1
            if (r0 != r1) goto L_0x01da
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x02f5, all -> 0x02e8 }
            java.lang.String r2 = "UTF-8"
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x02f5, all -> 0x02e8 }
            r12.setBuildingId(r1)     // Catch:{ Throwable -> 0x02f5, all -> 0x02e8 }
        L_0x01c4:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x02f2, all -> 0x02e8 }
            java.lang.String r2 = "UTF-8"
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x02f2, all -> 0x02e8 }
            r12.setFloor(r1)     // Catch:{ Throwable -> 0x02f2, all -> 0x02e8 }
        L_0x01da:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r1 = 1
            if (r0 != r1) goto L_0x01ea
            r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.getInt()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
        L_0x01ea:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r1 = 1
            if (r0 != r1) goto L_0x01f8
            long r0 = r9.getLong()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r12.setTime(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
        L_0x01f8:
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            if (r0 <= 0) goto L_0x0216
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            int r1 = r0.length     // Catch:{ Throwable -> 0x02ef, all -> 0x02e8 }
            if (r1 <= 0) goto L_0x0216
            r1 = 0
            byte[] r0 = android.util.Base64.decode(r0, r1)     // Catch:{ Throwable -> 0x02ef, all -> 0x02e8 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x02ef, all -> 0x02e8 }
            java.lang.String r2 = "UTF-8"
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x02ef, all -> 0x02e8 }
            r12.a(r1)     // Catch:{ Throwable -> 0x02ef, all -> 0x02e8 }
        L_0x0216:
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            if (r0 <= 0) goto L_0x0221
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r9.get(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
        L_0x0221:
            java.lang.String r0 = "5.1"
            java.lang.Double r0 = java.lang.Double.valueOf(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            double r0 = r0.doubleValue()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            r2 = 4617428107952285286(0x4014666666666666, double:5.1)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x0250
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            java.lang.String r1 = "-1"
            java.lang.String r2 = r12.d()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            boolean r1 = r1.equals(r2)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            if (r1 != 0) goto L_0x0292
            r1 = -1
            if (r0 != r1) goto L_0x028e
            r0 = 0
        L_0x024a:
            r12.setConScenario(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
        L_0x024d:
            r9.get()     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
        L_0x0250:
            if (r9 == 0) goto L_0x0255
            r9.clear()
        L_0x0255:
            java.lang.StringBuilder r0 = r11.a
            int r0 = r0.length()
            if (r0 <= 0) goto L_0x0024
            java.lang.StringBuilder r0 = r11.a
            r1 = 0
            java.lang.StringBuilder r2 = r11.a
            int r2 = r2.length()
            r0.delete(r1, r2)
            goto L_0x0024
        L_0x026b:
            r0 = move-exception
            r0 = r1
        L_0x026d:
            r2 = r0
            goto L_0x00ce
        L_0x0270:
            r0 = move-exception
            r0 = r3
        L_0x0272:
            r3 = r0
            goto L_0x00e5
        L_0x0275:
            r0 = move-exception
            r0 = r4
        L_0x0277:
            r4 = r0
            goto L_0x00fc
        L_0x027a:
            r0 = move-exception
            r0 = r5
        L_0x027c:
            r5 = r0
            goto L_0x0116
        L_0x027f:
            r0 = move-exception
            r0 = r6
        L_0x0281:
            r6 = r0
            goto L_0x012d
        L_0x0284:
            r0 = move-exception
            r0 = r7
        L_0x0286:
            r7 = r0
            goto L_0x0144
        L_0x0289:
            r0 = move-exception
            r0 = r8
        L_0x028b:
            r8 = r0
            goto L_0x0171
        L_0x028e:
            if (r0 != 0) goto L_0x024a
            r0 = -1
            goto L_0x024a
        L_0x0292:
            r1 = 101(0x65, float:1.42E-43)
            if (r0 != r1) goto L_0x0298
            r0 = 100
        L_0x0298:
            r12.setConScenario(r0)     // Catch:{ Throwable -> 0x029c, all -> 0x02e8 }
            goto L_0x024d
        L_0x029c:
            r0 = move-exception
            r1 = r9
        L_0x029e:
            com.autonavi.aps.amapapi.model.AMapLocationServer r12 = new com.autonavi.aps.amapapi.model.AMapLocationServer     // Catch:{ all -> 0x02ea }
            java.lang.String r2 = ""
            r12.<init>(r2)     // Catch:{ all -> 0x02ea }
            r2 = 5
            r12.setErrorCode(r2)     // Catch:{ all -> 0x02ea }
            java.lang.StringBuilder r2 = r11.a     // Catch:{ all -> 0x02ea }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x02ea }
            java.lang.String r4 = "parser data error:"
            r3.<init>(r4)     // Catch:{ all -> 0x02ea }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x02ea }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ all -> 0x02ea }
            java.lang.String r3 = "#0505"
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ all -> 0x02ea }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02ea }
            r2.append(r0)     // Catch:{ all -> 0x02ea }
            r0 = 0
            r2 = 2054(0x806, float:2.878E-42)
            com.loc.ey.a(r0, r2)     // Catch:{ all -> 0x02ea }
            java.lang.StringBuilder r0 = r11.a     // Catch:{ all -> 0x02ea }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02ea }
            r12.setLocationDetail(r0)     // Catch:{ all -> 0x02ea }
            if (r1 == 0) goto L_0x0255
            r1.clear()
            goto L_0x0255
        L_0x02e0:
            r0 = move-exception
            r9 = r1
        L_0x02e2:
            if (r9 == 0) goto L_0x02e7
            r9.clear()
        L_0x02e7:
            throw r0
        L_0x02e8:
            r0 = move-exception
            goto L_0x02e2
        L_0x02ea:
            r0 = move-exception
            r9 = r1
            goto L_0x02e2
        L_0x02ed:
            r0 = move-exception
            goto L_0x029e
        L_0x02ef:
            r0 = move-exception
            goto L_0x0216
        L_0x02f2:
            r0 = move-exception
            goto L_0x01da
        L_0x02f5:
            r0 = move-exception
            goto L_0x01c4
        L_0x02f8:
            r0 = move-exception
            goto L_0x0187
        L_0x02fb:
            r1 = move-exception
            goto L_0x028b
        L_0x02fd:
            r0 = move-exception
            goto L_0x015a
        L_0x0300:
            r1 = move-exception
            goto L_0x0286
        L_0x0302:
            r1 = move-exception
            goto L_0x0281
        L_0x0305:
            r1 = move-exception
            goto L_0x027c
        L_0x0308:
            r1 = move-exception
            goto L_0x0277
        L_0x030b:
            r1 = move-exception
            goto L_0x0272
        L_0x030e:
            r1 = move-exception
            goto L_0x026d
        L_0x0311:
            r0 = move-exception
            goto L_0x00b7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ep.a(com.autonavi.aps.amapapi.model.AMapLocationServer, byte[]):com.autonavi.aps.amapapi.model.AMapLocationServer");
    }

    public final AMapLocationServer a(String str) {
        String str2;
        try {
            AMapLocationServer aMapLocationServer = new AMapLocationServer("");
            JSONObject optJSONObject = new JSONObject(str).optJSONObject("regeocode");
            JSONObject optJSONObject2 = optJSONObject.optJSONObject("addressComponent");
            aMapLocationServer.setCountry(b(optJSONObject2.optString("country")));
            String b2 = b(optJSONObject2.optString(ResponseBase.STRING_PROVINCE));
            aMapLocationServer.setProvince(b2);
            String b3 = b(optJSONObject2.optString("citycode"));
            aMapLocationServer.setCityCode(b3);
            String optString = optJSONObject2.optString(ResponseBase.STRING_CITY);
            if (!b3.endsWith("010") && !b3.endsWith("021") && !b3.endsWith("022") && !b3.endsWith("023")) {
                optString = b(optString);
                aMapLocationServer.setCity(optString);
            } else if (b2 != null && b2.length() > 0) {
                aMapLocationServer.setCity(b2);
                optString = b2;
            }
            if (TextUtils.isEmpty(optString)) {
                aMapLocationServer.setCity(b2);
                optString = b2;
            }
            String b4 = b(optJSONObject2.optString("district"));
            aMapLocationServer.setDistrict(b4);
            String b5 = b(optJSONObject2.optString("adcode"));
            aMapLocationServer.setAdCode(b5);
            JSONObject optJSONObject3 = optJSONObject2.optJSONObject("streetNumber");
            String b6 = b(optJSONObject3.optString("street"));
            aMapLocationServer.setStreet(b6);
            aMapLocationServer.setRoad(b6);
            String b7 = b(optJSONObject3.optString("number"));
            aMapLocationServer.setNumber(b7);
            JSONArray optJSONArray = optJSONObject.optJSONArray("pois");
            if (optJSONArray.length() > 0) {
                str2 = b(optJSONArray.getJSONObject(0).optString("name"));
                aMapLocationServer.setPoiName(str2);
            } else {
                str2 = null;
            }
            JSONArray optJSONArray2 = optJSONObject.optJSONArray("aois");
            if (optJSONArray2.length() > 0) {
                aMapLocationServer.setAoiName(b(optJSONArray2.getJSONObject(0).optString("name")));
            }
            a(aMapLocationServer, b2, optString, b4, b6, b7, str2, b5);
            return aMapLocationServer;
        } catch (Throwable th) {
            return null;
        }
    }

    public final AMapLocationServer a(String str, Context context, bk bkVar) {
        AMapLocationServer aMapLocationServer = new AMapLocationServer("");
        aMapLocationServer.setErrorCode(7);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("#SHA1AndPackage#").append(u.e(context));
            String str2 = (String) bkVar.b.get("gsid").get(0);
            if (!TextUtils.isEmpty(str2)) {
                stringBuffer.append("#gsid#").append(str2);
            }
            String str3 = bkVar.c;
            if (!TextUtils.isEmpty(str3)) {
                stringBuffer.append("#csid#" + str3);
            }
        } catch (Throwable th) {
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status") || !jSONObject.has("info")) {
                this.a.append("json is error:").append(str).append(stringBuffer).append("#0702");
            }
            String string = jSONObject.getString("status");
            String string2 = jSONObject.getString("info");
            String string3 = jSONObject.getString("infocode");
            if ("0".equals(string)) {
                this.a.append("auth fail:").append(string2).append(stringBuffer).append("#0701");
                ey.a(bkVar.d, string3, string2);
            }
        } catch (Throwable th2) {
            this.a.append("json exception error:").append(th2.getMessage()).append(stringBuffer).append("#0703");
            es.a(th2, "parser", "paseAuthFailurJson");
        }
        aMapLocationServer.setLocationDetail(this.a.toString());
        if (this.a.length() > 0) {
            this.a.delete(0, this.a.length());
        }
        return aMapLocationServer;
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        if (aMapLocationClientOption == null) {
            this.b = new AMapLocationClientOption();
        } else {
            this.b = aMapLocationClientOption;
        }
    }
}
