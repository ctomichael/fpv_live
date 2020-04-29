package com.loc;

import android.os.Bundle;
import com.amap.api.fence.DistrictItem;
import com.amap.api.fence.GeoFence;
import com.amap.api.fence.PoiItem;
import com.amap.api.location.DPoint;
import dji.publics.protocol.ResponseBase;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: GeoFenceSearchResultParser */
public final class l {
    private static long a = 0;

    public static int a(String str, List<GeoFence> list, Bundle bundle) {
        JSONArray optJSONArray;
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("status", 0);
            int optInt2 = jSONObject.optInt("infocode", 0);
            if (optInt != 1 || (optJSONArray = jSONObject.optJSONArray("pois")) == null) {
                return optInt2;
            }
            for (int i = 0; i < optJSONArray.length(); i++) {
                GeoFence geoFence = new GeoFence();
                PoiItem poiItem = new PoiItem();
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                poiItem.setPoiId(jSONObject2.optString(ResponseBase.STRING_ID));
                poiItem.setPoiName(jSONObject2.optString("name"));
                poiItem.setPoiType(jSONObject2.optString("type"));
                poiItem.setTypeCode(jSONObject2.optString("typecode"));
                poiItem.setAddress(jSONObject2.optString("address"));
                String optString = jSONObject2.optString(ResponseBase.STRING_LOCATION);
                if (optString != null) {
                    String[] split = optString.split(",");
                    poiItem.setLongitude(Double.parseDouble(split[0]));
                    poiItem.setLatitude(Double.parseDouble(split[1]));
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    DPoint dPoint = new DPoint(poiItem.getLatitude(), poiItem.getLongitude());
                    arrayList2.add(dPoint);
                    arrayList.add(arrayList2);
                    geoFence.setPointList(arrayList);
                    geoFence.setCenter(dPoint);
                }
                poiItem.setTel(jSONObject2.optString("tel"));
                poiItem.setProvince(jSONObject2.optString("pname"));
                poiItem.setCity(jSONObject2.optString("cityname"));
                poiItem.setAdname(jSONObject2.optString("adname"));
                geoFence.setPoiItem(poiItem);
                geoFence.setFenceId(new StringBuilder().append(a()).toString());
                geoFence.setCustomId(bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID));
                geoFence.setPendingIntentAction(bundle.getString("pendingIntentAction"));
                geoFence.setType(2);
                geoFence.setRadius(bundle.getFloat("fenceRadius"));
                geoFence.setExpiration(bundle.getLong("expiration"));
                geoFence.setActivatesAction(bundle.getInt("activatesAction", 1));
                list.add(geoFence);
            }
            return optInt2;
        } catch (Throwable th) {
            return 5;
        }
    }

    public static synchronized long a() {
        long j;
        synchronized (l.class) {
            long c = fa.c();
            if (c > a) {
                a = c;
            } else {
                a++;
            }
            j = a;
        }
        return j;
    }

    private List<DPoint> a(List<DPoint> list, float f) {
        double d;
        double d2;
        if (list == null) {
            return null;
        }
        if (list.size() <= 2) {
            return list;
        }
        double d3 = 0.0d;
        int i = 0;
        ArrayList arrayList = new ArrayList();
        DPoint dPoint = list.get(0);
        DPoint dPoint2 = list.get(list.size() - 1);
        int i2 = 1;
        while (true) {
            int i3 = i2;
            if (i3 >= list.size() - 1) {
                break;
            }
            DPoint dPoint3 = list.get(i3);
            double longitude = dPoint3.getLongitude() - dPoint.getLongitude();
            double latitude = dPoint3.getLatitude() - dPoint.getLatitude();
            double longitude2 = dPoint2.getLongitude() - dPoint.getLongitude();
            double latitude2 = dPoint2.getLatitude() - dPoint.getLatitude();
            double d4 = ((longitude * longitude2) + (latitude * latitude2)) / ((longitude2 * longitude2) + (latitude2 * latitude2));
            boolean z = dPoint.getLongitude() == dPoint2.getLongitude() && dPoint.getLatitude() == dPoint2.getLatitude();
            if (d4 < 0.0d || z) {
                d = dPoint.getLongitude();
                d2 = dPoint.getLatitude();
            } else if (d4 > 1.0d) {
                d = dPoint2.getLongitude();
                d2 = dPoint2.getLatitude();
            } else {
                d = (d4 * longitude2) + dPoint.getLongitude();
                d2 = dPoint.getLatitude() + (d4 * latitude2);
            }
            double a2 = (double) fa.a(new DPoint(dPoint3.getLatitude(), dPoint3.getLongitude()), new DPoint(d2, d));
            if (a2 > d3) {
                i = i3;
                d3 = a2;
            }
            i2 = i3 + 1;
        }
        if (d3 < ((double) f)) {
            arrayList.add(dPoint);
            arrayList.add(dPoint2);
            return arrayList;
        }
        List<DPoint> a3 = a(list.subList(0, i + 1), f);
        List<DPoint> a4 = a(list.subList(i, list.size()), f);
        arrayList.addAll(a3);
        arrayList.remove(arrayList.size() - 1);
        arrayList.addAll(a4);
        return arrayList;
    }

    public final int b(String str, List<GeoFence> list, Bundle bundle) {
        JSONArray optJSONArray;
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("status", 0);
            int optInt2 = jSONObject.optInt("infocode", 0);
            String string = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
            String string2 = bundle.getString("pendingIntentAction");
            float f = bundle.getFloat("fenceRadius");
            long j = bundle.getLong("expiration");
            int i = bundle.getInt("activatesAction", 1);
            if (optInt != 1 || (optJSONArray = jSONObject.optJSONArray("districts")) == null) {
                return optInt2;
            }
            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                GeoFence geoFence = new GeoFence();
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i2);
                String optString = jSONObject2.optString("citycode");
                String optString2 = jSONObject2.optString("adcode");
                String optString3 = jSONObject2.optString("name");
                String string3 = jSONObject2.getString("center");
                DPoint dPoint = new DPoint();
                if (string3 != null) {
                    String[] split = string3.split(",");
                    dPoint.setLatitude(Double.parseDouble(split[1]));
                    dPoint.setLongitude(Double.parseDouble(split[0]));
                    geoFence.setCenter(dPoint);
                }
                geoFence.setCustomId(string);
                geoFence.setPendingIntentAction(string2);
                geoFence.setType(3);
                geoFence.setRadius(f);
                geoFence.setExpiration(j);
                geoFence.setActivatesAction(i);
                geoFence.setFenceId(new StringBuilder().append(a()).toString());
                String optString4 = jSONObject2.optString("polyline");
                float f2 = Float.MIN_VALUE;
                if (optString4 != null) {
                    String[] split2 = optString4.split("\\|");
                    int length = split2.length;
                    int i3 = 0;
                    float f3 = Float.MAX_VALUE;
                    while (i3 < length) {
                        String str2 = split2[i3];
                        DistrictItem districtItem = new DistrictItem();
                        List arrayList3 = new ArrayList();
                        districtItem.setCitycode(optString);
                        districtItem.setAdcode(optString2);
                        districtItem.setDistrictName(optString3);
                        String[] split3 = str2.split(";");
                        for (String str3 : split3) {
                            String[] split4 = str3.split(",");
                            if (split4.length > 1) {
                                arrayList3.add(new DPoint(Double.parseDouble(split4[1]), Double.parseDouble(split4[0])));
                            }
                        }
                        if (((float) arrayList3.size()) > 100.0f) {
                            arrayList3 = a(arrayList3, 100.0f);
                        }
                        arrayList2.add(arrayList3);
                        districtItem.setPolyline(arrayList3);
                        arrayList.add(districtItem);
                        f2 = Math.max(f2, j.b(dPoint, arrayList3));
                        i3++;
                        f3 = Math.min(f3, j.a(dPoint, arrayList3));
                    }
                    geoFence.setMaxDis2Center(f2);
                    geoFence.setMinDis2Center(f3);
                    geoFence.setDistrictItemList(arrayList);
                    geoFence.setPointList(arrayList2);
                    list.add(geoFence);
                }
            }
            return optInt2;
        } catch (Throwable th) {
            return 5;
        }
    }
}
