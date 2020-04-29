package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import com.loc.v;
import dji.publics.protocol.ResponseBase;
import dji.publics.utils.LanguageUtils;
import dji.utils.TimeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: AuthUtil */
public final class er {
    private static long A = 0;
    private static ArrayList<String> B = new ArrayList<>();
    private static boolean C = false;
    private static int D = -1;
    private static long E = 0;
    private static ArrayList<String> F = new ArrayList<>();
    private static boolean G = false;
    private static int H = 3000;
    private static int I = 3000;
    private static boolean J = true;
    private static long K = 300000;
    private static List<ev> L = new ArrayList();
    private static boolean M = false;
    private static long N = 0;
    private static int O = 0;
    private static int P = 0;
    private static List<String> Q = new ArrayList();
    private static boolean R = true;
    private static int S = 80;
    private static boolean T = false;
    private static boolean U = true;
    private static boolean V = false;
    private static boolean W = false;
    private static boolean X = true;
    private static boolean Y = false;
    private static int Z = -1;
    public static boolean a = true;
    private static boolean aa = true;
    private static long ab = -1;
    private static boolean ac = true;
    private static int ad = 1;
    private static long ae = 0;
    static boolean b = false;
    static boolean c = false;
    static int d = TimeUtils.TIMECONSTANT_HOUR;
    static long e = 0;
    static long f = 0;
    static boolean g = false;
    static boolean h = true;
    private static boolean i = false;
    private static boolean j = false;
    private static long k = 0;
    private static long l = 0;
    private static long m = 5000;
    private static boolean n = false;
    private static int o = 0;
    private static boolean p = false;
    private static int q = 0;
    private static boolean r = false;
    private static boolean s = true;
    private static int t = 1000;
    private static int u = 200;
    private static boolean v = false;
    private static int w = 20;
    private static boolean x = true;
    private static boolean y = true;
    private static int z = -1;

    /* compiled from: AuthUtil */
    static class a {
        boolean a = false;
        String b = "0";
        boolean c = false;
        int d = 5;

        a() {
        }
    }

    public static boolean A() {
        return aa;
    }

    public static long B() {
        return ab;
    }

    public static boolean C() {
        return ac && ad > 0;
    }

    public static int D() {
        return ad;
    }

    public static long E() {
        return ae;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.v.a(java.lang.String, boolean):boolean
     arg types: [java.lang.String, int]
     candidates:
      com.loc.v.a(org.json.JSONObject, java.lang.String):java.lang.String
      com.loc.v.a(android.content.Context, java.lang.String):void
      com.loc.v.a(com.loc.v$a, org.json.JSONObject):void
      com.loc.v.a(org.json.JSONObject, com.loc.v$a$b):void
      com.loc.v.a(org.json.JSONObject, com.loc.v$a$c):void
      com.loc.v.a(java.lang.String, boolean):boolean */
    private static a a(JSONObject jSONObject, String str) {
        a aVar;
        if (jSONObject != null) {
            try {
                JSONObject jSONObject2 = jSONObject.getJSONObject(str);
                if (jSONObject2 != null) {
                    aVar = new a();
                    try {
                        aVar.a = v.a(jSONObject2.optString("b"), false);
                        aVar.b = jSONObject2.optString("t");
                        aVar.c = v.a(jSONObject2.optString("st"), false);
                        aVar.d = jSONObject2.optInt("i", 0);
                        return aVar;
                    } catch (Throwable th) {
                        th = th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                aVar = null;
                es.a(th, "AuthUtil", "getLocateObj");
                return aVar;
            }
        }
        return null;
    }

    public static boolean a() {
        return n;
    }

    public static boolean a(long j2) {
        long c2 = fa.c();
        return j && c2 - l <= k && c2 - j2 >= m;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, int):int
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, java.lang.String):java.lang.String
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean */
    public static boolean a(Context context) {
        boolean z2 = false;
        x = true;
        try {
            i = ez.b(context, "pref", "oda", false);
            z2 = a(context, v.a(context, es.b(), es.c()));
        } catch (Throwable th) {
            es.a(th, "AuthUtil", "getConfig");
        }
        f = fa.c();
        e = fa.c();
        return z2;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, int):int
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, java.lang.String):java.lang.String
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long */
    public static boolean a(Context context, long j2) {
        if (!G) {
            return false;
        }
        long b2 = fa.b();
        if (b2 - j2 < ((long) H)) {
            return false;
        }
        if (I == -1) {
            return true;
        }
        if (!fa.c(b2, ez.b(context, "pref", "ngpsTime", 0L))) {
            try {
                SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
                edit.putLong("ngpsTime", b2);
                edit.putInt("ngpsCount", 0);
                ez.a(edit);
            } catch (Throwable th) {
                es.a(th, "AuthUtil", "resetPrefsNGPS");
            }
            ez.a(context, "pref", "ngpsCount", 1);
            return true;
        }
        int b3 = ez.b(context, "pref", "ngpsCount", 0);
        if (b3 >= I) {
            return false;
        }
        ez.a(context, "pref", "ngpsCount", b3 + 1);
        return true;
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
     method: com.loc.v.a(java.lang.String, boolean):boolean
     arg types: [java.lang.String, int]
     candidates:
      com.loc.v.a(org.json.JSONObject, java.lang.String):java.lang.String
      com.loc.v.a(android.content.Context, java.lang.String):void
      com.loc.v.a(com.loc.v$a, org.json.JSONObject):void
      com.loc.v.a(org.json.JSONObject, com.loc.v$a$b):void
      com.loc.v.a(org.json.JSONObject, com.loc.v$a$c):void
      com.loc.v.a(java.lang.String, boolean):boolean */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, boolean):void
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, int):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, long):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void
      com.loc.ez.a(android.content.Context, java.lang.String, java.lang.String, boolean):void */
    private static boolean a(Context context, v.a aVar) {
        JSONArray optJSONArray;
        try {
            JSONObject jSONObject = aVar.g;
            if (jSONObject != null) {
                d = jSONObject.optInt("at", 123) * 60 * 1000;
                try {
                    T = v.a(jSONObject.optString("ila"), T);
                } catch (Throwable th) {
                    es.a(th, "AuthUtil", "loadConfigData_indoor");
                }
                if (!(context == null || jSONObject == null)) {
                    try {
                        h = v.a(jSONObject.optString("re"), h);
                        ez.a(context, "pref", "fr", h);
                    } catch (Throwable th2) {
                        es.a(th2, "AuthUtil", "checkReLocationAble");
                    }
                }
                try {
                    U = v.a(jSONObject.optString("nla"), U);
                } catch (Throwable th3) {
                }
                try {
                    i = v.a(jSONObject.optString("oda"), i);
                    ez.a(context, "pref", "oda", i);
                } catch (Throwable th4) {
                }
                try {
                    aa = v.a(jSONObject.optString("asw"), aa);
                    ez.a(context, "pref", "asw", aa);
                } catch (Throwable th5) {
                }
                try {
                    JSONArray optJSONArray2 = jSONObject.optJSONArray("mlpl");
                    if (optJSONArray2 != null && optJSONArray2.length() > 0) {
                        int i2 = 0;
                        while (i2 < optJSONArray2.length()) {
                            boolean c2 = fa.c(context, optJSONArray2.getString(i2));
                            V = c2;
                            if (!c2) {
                                i2++;
                            }
                        }
                    }
                } catch (Throwable th6) {
                }
            }
        } catch (Throwable th7) {
            es.a(th7, "AuthUtil", "loadConfigAbleStatus");
        }
        try {
            JSONObject jSONObject2 = aVar.h;
            if (jSONObject2 != null) {
                y = v.a(jSONObject2.optString("callamapflag"), y);
                b = v.a(jSONObject2.optString("co"), false) && y;
                if (y) {
                    z = jSONObject2.optInt("count", z);
                    A = jSONObject2.optLong("sysTime", A);
                    JSONArray optJSONArray3 = jSONObject2.optJSONArray(ResponseBase.STRING_SN);
                    if (optJSONArray3 != null && optJSONArray3.length() > 0) {
                        for (int i3 = 0; i3 < optJSONArray3.length(); i3++) {
                            B.add(optJSONArray3.getString(i3));
                        }
                    }
                    if (!(z == -1 || A == 0)) {
                        if (!fa.b(A, ez.b(context, "pref", "nowtime", 0L))) {
                            h(context);
                        }
                    }
                }
            }
        } catch (Throwable th8) {
            return false;
        }
        try {
            JSONObject jSONObject3 = aVar.k;
            if (jSONObject3 != null) {
                boolean a2 = v.a(jSONObject3.optString("amappushflag"), C);
                C = a2;
                if (a2) {
                    D = jSONObject3.optInt("count", D);
                    E = jSONObject3.optLong("sysTime", E);
                    JSONArray optJSONArray4 = jSONObject3.optJSONArray(ResponseBase.STRING_SN);
                    if (optJSONArray4 != null && optJSONArray4.length() > 0) {
                        for (int i4 = 0; i4 < optJSONArray4.length(); i4++) {
                            F.add(optJSONArray4.getString(i4));
                        }
                    }
                    if (!(D == -1 || E == 0)) {
                        if (!fa.b(E, ez.b(context, "pref", "pushSerTime", 0L))) {
                            i(context);
                        }
                    }
                }
            }
        } catch (Throwable th9) {
            es.a(th9, "AuthUtil", "loadConfigDataCallAMapPush");
        }
        try {
            v.a.C0017a aVar2 = aVar.x;
            if (aVar2 != null) {
                s = aVar2.a;
                ez.a(context, "pref", "exception", s);
                JSONObject jSONObject4 = aVar2.c;
                t = jSONObject4.optInt("fn", t);
                int optInt = jSONObject4.optInt("mpn", u);
                u = optInt;
                if (optInt > 500) {
                    u = 500;
                }
                if (u < 30) {
                    u = 30;
                }
                v = v.a(jSONObject4.optString("igu"), v);
                w = jSONObject4.optInt("ms", w);
                bp.a(t, v, w);
                ez.a(context, "pref", "fn", t);
                ez.a(context, "pref", "mpn", u);
                ez.a(context, "pref", "igu", v);
                ez.a(context, "pref", "ms", w);
            }
        } catch (Throwable th10) {
            es.a(th10, "AuthUtil", "loadConfigDataUploadException");
        }
        try {
            JSONObject jSONObject5 = aVar.m;
            if (jSONObject5 != null && v.a(jSONObject5.optString("able"), false)) {
                a a3 = a(jSONObject5, "fs");
                if (a3 != null) {
                    n = a3.c;
                    o = Integer.parseInt(a3.b);
                }
                a a4 = a(jSONObject5, "us");
                if (a4 != null) {
                    p = a4.c;
                    r = a4.a;
                    try {
                        q = Integer.parseInt(a4.b);
                    } catch (Throwable th11) {
                        es.a(th11, "AuthUtil", "loadconfig part1");
                    }
                    if (q < 2) {
                        p = false;
                    }
                }
                a a5 = a(jSONObject5, "rs");
                if (a5 != null) {
                    boolean z2 = a5.c;
                    j = z2;
                    if (z2) {
                        l = fa.c();
                        m = (long) (a5.d * 1000);
                    }
                    try {
                        k = (long) (Integer.parseInt(a5.b) * 1000);
                    } catch (Throwable th12) {
                        es.a(th12, "AuthUtil", "loadconfig part");
                    }
                }
            }
        } catch (Throwable th13) {
            es.a(th13, "AuthUtil", "loadConfigDataLocate");
        }
        try {
            JSONObject jSONObject6 = aVar.o;
            if (jSONObject6 != null) {
                boolean a6 = v.a(jSONObject6.optString("able"), G);
                G = a6;
                if (a6) {
                    int optInt2 = jSONObject6.optInt("c", 0);
                    if (optInt2 == 0) {
                        H = 3000;
                    } else {
                        H = optInt2 * 1000;
                    }
                    I = jSONObject6.getInt("t") / 2;
                }
            }
        } catch (Throwable th14) {
            es.a(th14, "AuthUtil", "loadConfigDataNgps");
        }
        try {
            JSONObject jSONObject7 = aVar.p;
            if (jSONObject7 != null) {
                boolean a7 = v.a(jSONObject7.optString("able"), J);
                J = a7;
                if (a7) {
                    K = (long) (jSONObject7.optInt("c", 300) * 1000);
                }
                ez.a(context, "pref", "ca", J);
                ez.a(context, "pref", "ct", K);
            }
        } catch (Throwable th15) {
            es.a(th15, "AuthUtil", "loadConfigDataCacheAble");
        }
        try {
            JSONObject jSONObject8 = aVar.j;
            if (jSONObject8 != null) {
                M = v.a(jSONObject8.optString("able"), M);
                N = jSONObject8.optLong("sysTime", fa.b());
                O = jSONObject8.optInt("n", 1);
                P = jSONObject8.optInt("nh", 1);
                boolean z3 = O == -1 || O >= P;
                if (M && z3) {
                    JSONArray optJSONArray5 = jSONObject8.optJSONArray("l");
                    for (int i5 = 0; i5 < optJSONArray5.length(); i5++) {
                        try {
                            JSONObject optJSONObject = optJSONArray5.optJSONObject(i5);
                            ev evVar = new ev();
                            boolean a8 = v.a(optJSONObject.optString("able", "false"), false);
                            evVar.a = a8;
                            if (a8) {
                                evVar.b = optJSONObject.optString("pn", "");
                                evVar.c = optJSONObject.optString(LanguageUtils.CN_DJI_LANG_CODE, "");
                                evVar.e = optJSONObject.optString("a", "");
                                JSONArray optJSONArray6 = optJSONObject.optJSONArray("b");
                                if (optJSONArray6 != null) {
                                    ArrayList arrayList = new ArrayList();
                                    for (int i6 = 0; i6 < optJSONArray6.length(); i6++) {
                                        JSONObject optJSONObject2 = optJSONArray6.optJSONObject(i6);
                                        HashMap hashMap = new HashMap(16);
                                        try {
                                            hashMap.put(optJSONObject2.optString("k"), optJSONObject2.optString("v"));
                                            arrayList.add(hashMap);
                                        } catch (Throwable th16) {
                                        }
                                    }
                                    evVar.d = arrayList;
                                }
                                evVar.f = v.a(optJSONObject.optString("is", "false"), false);
                                L.add(evVar);
                            }
                        } catch (Throwable th17) {
                        }
                    }
                    JSONArray optJSONArray7 = jSONObject8.optJSONArray("sl");
                    if (optJSONArray7 != null) {
                        for (int i7 = 0; i7 < optJSONArray7.length(); i7++) {
                            String optString = optJSONArray7.optJSONObject(i7).optString("pan");
                            if (!TextUtils.isEmpty(optString)) {
                                Q.add(optString);
                            }
                        }
                    }
                }
            }
        } catch (Throwable th18) {
            es.a(th18, "AuthUtil", "loadConfigData_otherServiceList");
        }
        try {
            JSONObject jSONObject9 = aVar.i;
            if (jSONObject9 != null) {
                boolean a9 = v.a(jSONObject9.optString("able"), R);
                R = a9;
                if (a9) {
                    S = jSONObject9.optInt("c", S);
                }
            }
        } catch (Throwable th19) {
            es.a(th19, "AuthUtil", "loadConfigDataGpsGeoAble");
        }
        JSONObject jSONObject10 = aVar.w;
        if (jSONObject10 != null) {
            try {
                JSONObject optJSONObject3 = jSONObject10.optJSONObject("157");
                if (optJSONObject3 != null) {
                    boolean a10 = v.a(optJSONObject3.optString("able"), W);
                    W = a10;
                    if (a10) {
                        Z = optJSONObject3.optInt("cv", -1);
                        X = v.a(optJSONObject3.optString("co"), X);
                        Y = v.a(optJSONObject3.optString("oo"), Y);
                    } else {
                        X = false;
                        Y = false;
                    }
                    ez.a(context, "pref", "ok0", W);
                    ez.a(context, "pref", "ok2", X);
                    ez.a(context, "pref", "ok3", Y);
                }
            } catch (Throwable th20) {
                es.a(th20, "AuthUtil", "loadConfigDataNewCollectionOffline");
            }
        }
        if (!(context == null || jSONObject10 == null)) {
            try {
                JSONObject optJSONObject4 = jSONObject10.optJSONObject("15O");
                if (optJSONObject4 != null) {
                    if (!v.a(optJSONObject4.optString("able"), false) || ((optJSONArray = optJSONObject4.optJSONArray("fl")) != null && optJSONArray.length() > 0 && !optJSONArray.toString().contains(Build.MANUFACTURER))) {
                        ab = -1;
                    } else {
                        ab = (long) (optJSONObject4.optInt("iv", 30) * 1000);
                    }
                    ez.a(context, "pref", "awsi", ab);
                }
            } catch (Throwable th21) {
            }
        }
        if (!(context == null || jSONObject10 == null)) {
            try {
                JSONObject optJSONObject5 = jSONObject10.optJSONObject("15U");
                if (optJSONObject5 != null) {
                    boolean a11 = v.a(optJSONObject5.optString("able"), ac);
                    int optInt3 = optJSONObject5.optInt("yn", ad);
                    ae = optJSONObject5.optLong("sysTime", ae);
                    ez.a(context, "pref", "15ua", a11);
                    ez.a(context, "pref", "15un", optInt3);
                    ez.a(context, "pref", "15ust", ae);
                }
            } catch (Throwable th22) {
            }
        }
        if (context == null || jSONObject10 == null) {
            return true;
        }
        try {
            JSONObject optJSONObject6 = jSONObject10.optJSONObject("16P");
            if (optJSONObject6 == null) {
                ez.a(context, "pref", "dnab", false);
                return true;
            }
            boolean a12 = v.a(optJSONObject6.optString("able"), false);
            int optInt4 = optJSONObject6.optInt("mi");
            int optInt5 = optJSONObject6.optInt("ma");
            ez.a(context, "pref", "dnab", a12);
            ez.a(context, "pref", "dnmi", optInt4);
            ez.a(context, "pref", "dnma", optInt5);
            return true;
        } catch (Throwable th23) {
            return true;
        }
    }

    public static int b() {
        return o;
    }

    public static boolean b(long j2) {
        if (!J) {
            return false;
        }
        return K < 0 || fa.b() - j2 < K;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, int):int
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, java.lang.String):java.lang.String
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long */
    public static boolean b(Context context) {
        if (!y) {
            return false;
        }
        if (z == -1 || A == 0) {
            return true;
        }
        if (!fa.b(A, ez.b(context, "pref", "nowtime", 0L))) {
            h(context);
            ez.a(context, "pref", "count", 1);
            return true;
        }
        int b2 = ez.b(context, "pref", "count", 0);
        if (b2 >= z) {
            return false;
        }
        ez.a(context, "pref", "count", b2 + 1);
        return true;
    }

    public static boolean c() {
        return p;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, int):int
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, java.lang.String):java.lang.String
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long */
    public static boolean c(Context context) {
        if (!C) {
            return false;
        }
        if (D == -1 || E == 0) {
            return true;
        }
        if (!fa.b(E, ez.b(context, "pref", "pushSerTime", 0L))) {
            i(context);
            ez.a(context, "pref", "pushCount", 1);
            return true;
        }
        int b2 = ez.b(context, "pref", "pushCount", 0);
        if (b2 >= D) {
            return false;
        }
        ez.a(context, "pref", "pushCount", b2 + 1);
        return true;
    }

    public static int d() {
        return q;
    }

    public static void d(Context context) {
        try {
            s = ez.b(context, "pref", "exception", s);
            e(context);
        } catch (Throwable th) {
            es.a(th, "AuthUtil", "loadLastAbleState p1");
        }
        try {
            t = ez.b(context, "pref", "fn", t);
            u = ez.b(context, "pref", "mpn", u);
            v = ez.b(context, "pref", "igu", v);
            w = ez.b(context, "pref", "ms", w);
            bp.a(t, v, w);
        } catch (Throwable th2) {
        }
        try {
            J = ez.b(context, "pref", "ca", J);
            K = ez.b(context, "pref", "ct", K);
        } catch (Throwable th3) {
        }
        try {
            h = ez.b(context, "pref", "fr", h);
        } catch (Throwable th4) {
        }
        try {
            W = ez.b(context, "pref", "ok0", W);
            X = ez.b(context, "pref", "ok2", X);
            Y = ez.b(context, "pref", "ok3", Y);
        } catch (Throwable th5) {
        }
        try {
            aa = ez.b(context, "pref", "asw", aa);
        } catch (Throwable th6) {
        }
        try {
            ab = ez.b(context, "pref", "awsi", ab);
        } catch (Throwable th7) {
        }
        try {
            ac = ez.b(context, "pref", "15ua", ac);
            ad = ez.b(context, "pref", "15un", ad);
            ae = ez.b(context, "pref", "15ust", ae);
        } catch (Throwable th8) {
        }
    }

    public static void e(Context context) {
        try {
            ac b2 = es.b();
            b2.a(s);
            aq.a(context, b2);
        } catch (Throwable th) {
        }
    }

    public static boolean e() {
        return r;
    }

    public static boolean f() {
        return b;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long
     arg types: [android.content.Context, java.lang.String, java.lang.String, int]
     candidates:
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, int):int
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, java.lang.String):java.lang.String
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, boolean):boolean
      com.loc.ez.b(android.content.Context, java.lang.String, java.lang.String, long):long */
    public static boolean f(Context context) {
        boolean z2 = O != -1 && O < P;
        if (!M || O == 0 || P == 0 || N == 0 || z2) {
            return false;
        }
        if (Q != null && Q.size() > 0) {
            for (String str : Q) {
                if (fa.b(context, str)) {
                    return false;
                }
            }
        }
        if (O == -1 && P == -1) {
            return true;
        }
        long b2 = ez.b(context, "pref", "ots", 0L);
        long b3 = ez.b(context, "pref", "otsh", 0L);
        int b4 = ez.b(context, "pref", "otn", 0);
        int b5 = ez.b(context, "pref", "otnh", 0);
        if (O != -1) {
            if (!fa.b(N, b2)) {
                try {
                    SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
                    edit.putLong("ots", N);
                    edit.putLong("otsh", N);
                    edit.putInt("otn", 0);
                    edit.putInt("otnh", 0);
                    ez.a(edit);
                } catch (Throwable th) {
                    es.a(th, "AuthUtil", "resetPrefsBind");
                }
                ez.a(context, "pref", "otn", 1);
                ez.a(context, "pref", "otnh", 1);
                return true;
            } else if (b4 < O) {
                if (P == -1) {
                    ez.a(context, "pref", "otn", b4 + 1);
                    ez.a(context, "pref", "otnh", 0);
                    return true;
                } else if (!fa.a(N, b3)) {
                    ez.a(context, "pref", "otsh", N);
                    ez.a(context, "pref", "otn", b4 + 1);
                    ez.a(context, "pref", "otnh", 1);
                    return true;
                } else if (b5 < P) {
                    ez.a(context, "pref", "otn", b4 + 1);
                    ez.a(context, "pref", "otnh", b5 + 1);
                    return true;
                }
            }
        }
        if (O == -1) {
            ez.a(context, "pref", "otn", 0);
            if (P == -1) {
                ez.a(context, "pref", "otnh", 0);
                return true;
            } else if (!fa.a(N, b3)) {
                ez.a(context, "pref", "otsh", N);
                ez.a(context, "pref", "otnh", 1);
                return true;
            } else if (b5 < P) {
                ez.a(context, "pref", "otnh", b5 + 1);
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> g() {
        return B;
    }

    public static boolean g(Context context) {
        if (context == null) {
            return false;
        }
        try {
            if (fa.c() - f < ((long) d)) {
                return false;
            }
            g = true;
            return true;
        } catch (Throwable th) {
            es.a(th, "Aps", "isConfigNeedUpdate");
            return false;
        }
    }

    public static ArrayList<String> h() {
        return F;
    }

    private static void h(Context context) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
            edit.putLong("nowtime", A);
            edit.putInt("count", 0);
            ez.a(edit);
        } catch (Throwable th) {
            es.a(th, "AuthUtil", "resetPrefsBind");
        }
    }

    private static void i(Context context) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
            edit.putLong("pushSerTime", E);
            edit.putInt("pushCount", 0);
            ez.a(edit);
        } catch (Throwable th) {
            es.a(th, "AuthUtil", "resetPrefsBind");
        }
    }

    public static boolean i() {
        return s;
    }

    public static int j() {
        return u;
    }

    public static boolean k() {
        return x;
    }

    public static void l() {
        x = false;
    }

    public static boolean m() {
        return G;
    }

    public static long n() {
        return K;
    }

    public static boolean o() {
        return J;
    }

    public static List<ev> p() {
        return L;
    }

    public static boolean q() {
        return R;
    }

    public static int r() {
        return S;
    }

    public static boolean s() {
        return U;
    }

    public static boolean t() {
        return V;
    }

    public static boolean u() {
        if (!g) {
            return g;
        }
        g = false;
        return true;
    }

    public static boolean v() {
        return h;
    }

    public static boolean w() {
        return W;
    }

    public static boolean x() {
        return Y;
    }

    public static boolean y() {
        return X;
    }

    public static int z() {
        return Z;
    }
}
