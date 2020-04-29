package com.loc;

import android.content.Context;
import android.text.TextUtils;
import dji.component.accountcenter.IMemberProtocol;
import dji.proto.djigo_services.AbTestRequestWrapper;
import dji.publics.protocol.ResponseBase;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import java.util.HashMap;
import java.util.Map;

/* compiled from: GeoFenceNetManager */
public final class k {
    bg a = null;

    public k(Context context) {
        try {
            z.a().a(context);
        } catch (Throwable th) {
        }
        this.a = bg.a();
    }

    public static String a(Context context, String str, String str2) {
        Map<String, String> b = b(context, str2, null, null, null, null, null, null);
        b.put("extensions", "all");
        return a(context, str, b);
    }

    public static String a(Context context, String str, String str2, String str3, String str4, String str5) {
        Map<String, String> b = b(context, str2, str3, str4, str5, null, null, null);
        b.put("children", "1");
        b.put("page", "1");
        b.put("extensions", "base");
        return a(context, str, b);
    }

    public static String a(Context context, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        Map<String, String> b = b(context, str2, str3, null, str4, str5, str6, str7);
        b.put("children", "1");
        b.put("page", "1");
        b.put("extensions", "base");
        return a(context, str, b);
    }

    private static String a(Context context, String str, Map<String, String> map) {
        byte[] b;
        try {
            HashMap hashMap = new HashMap(16);
            em emVar = new em();
            hashMap.clear();
            hashMap.put("Content-Type", AbTestRequestWrapper.CONTENT_TYPE);
            hashMap.put(DJISDKCacheKeys.CONNECTION, "Keep-Alive");
            hashMap.put("User-Agent", "AMAP_Location_SDK_Android 4.7.1");
            String a2 = w.a();
            String a3 = w.a(context, a2, ad.b(map));
            map.put("ts", a2);
            map.put("scode", a3);
            emVar.b(map);
            emVar.a(hashMap);
            emVar.a(str);
            emVar.a(ab.a(context));
            emVar.a(es.f);
            emVar.b(es.f);
            if (fa.k(context)) {
                emVar.a(str.replace("http:", "https:"));
                b = bg.a(emVar);
            } else {
                b = bg.b(emVar);
            }
            return new String(b, "utf-8");
        } catch (Throwable th) {
            return null;
        }
    }

    private static Map<String, String> b(Context context, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        HashMap hashMap = new HashMap(16);
        hashMap.put(IMemberProtocol.STRING_KEY, u.f(context));
        if (!TextUtils.isEmpty(str)) {
            hashMap.put("keywords", str);
        }
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put("types", str2);
        }
        if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(str6)) {
            hashMap.put(ResponseBase.STRING_LOCATION, str6 + "," + str5);
        }
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put(ResponseBase.STRING_CITY, str3);
        }
        if (!TextUtils.isEmpty(str4)) {
            hashMap.put("offset", str4);
        }
        if (!TextUtils.isEmpty(str7)) {
            hashMap.put("radius", str7);
        }
        return hashMap;
    }
}
