package com.loc;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.adobe.xmp.XMPConst;
import dji.component.accountcenter.IMemberProtocol;
import dji.component.flysafe.FlyForbidProtocol;
import dji.publics.protocol.ResponseBase;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: AuthConfigManager */
public final class v {
    public static int a = -1;
    public static String b = "";

    /* compiled from: AuthConfigManager */
    public static class a {
        @Deprecated
        public c A;
        public c B;
        public b C;
        public b D;
        public b E;
        public b F;
        public e G;
        /* access modifiers changed from: private */
        public boolean H;
        public String a;
        public int b = -1;
        @Deprecated
        public JSONObject c;
        @Deprecated
        public JSONObject d;
        @Deprecated
        public JSONObject e;
        @Deprecated
        public JSONObject f;
        @Deprecated
        public JSONObject g;
        @Deprecated
        public JSONObject h;
        @Deprecated
        public JSONObject i;
        @Deprecated
        public JSONObject j;
        @Deprecated
        public JSONObject k;
        @Deprecated
        public JSONObject l;
        @Deprecated
        public JSONObject m;
        @Deprecated
        public JSONObject n;
        @Deprecated
        public JSONObject o;
        @Deprecated
        public JSONObject p;
        @Deprecated
        public JSONObject q;
        @Deprecated
        public JSONObject r;
        @Deprecated
        public JSONObject s;
        @Deprecated
        public JSONObject t;
        @Deprecated
        public JSONObject u;
        @Deprecated
        public JSONObject v;
        public JSONObject w;
        public C0017a x;
        public d y;
        public f z;

        /* renamed from: com.loc.v$a$a  reason: collision with other inner class name */
        /* compiled from: AuthConfigManager */
        public static class C0017a {
            public boolean a;
            public boolean b;
            public JSONObject c;
        }

        /* compiled from: AuthConfigManager */
        public static class b {
            public boolean a;
            public String b;
            public String c;
            public String d;
            public boolean e;
        }

        /* compiled from: AuthConfigManager */
        public static class c {
            public String a;
            public String b;
        }

        /* compiled from: AuthConfigManager */
        public static class d {
            public String a;
            public String b;
            public String c;
        }

        /* compiled from: AuthConfigManager */
        public static class e {
            public boolean a;
            public boolean b;
            public boolean c;
            public String d;
            public String e;
            public String f;
        }

        /* compiled from: AuthConfigManager */
        public static class f {
            public boolean a;
        }
    }

    /* compiled from: AuthConfigManager */
    static class b extends bh {
        private String f;
        private Map<String, String> g = null;
        private boolean h;

        b(Context context, ac acVar, String str) {
            super(context, acVar);
            this.f = str;
            this.h = Build.VERSION.SDK_INT != 19;
        }

        public final boolean a() {
            return this.h;
        }

        public final byte[] a_() {
            return null;
        }

        public final Map<String, String> b() {
            return null;
        }

        public final String c() {
            return this.h ? "https://restapi.amap.com/v3/iasdkauth" : "http://restapi.amap.com/v3/iasdkauth";
        }

        public final byte[] e() {
            String u = x.u(this.a);
            if (TextUtils.isEmpty(u)) {
                u = x.h(this.a);
            }
            if (!TextUtils.isEmpty(u)) {
                u = aa.b(new StringBuilder(u).reverse().toString());
            }
            HashMap hashMap = new HashMap();
            hashMap.put("authkey", this.f);
            hashMap.put("plattype", FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
            hashMap.put("product", this.b.a());
            hashMap.put("version", this.b.b());
            hashMap.put("output", "json");
            hashMap.put("androidversion", new StringBuilder().append(Build.VERSION.SDK_INT).toString());
            hashMap.put("deviceId", u);
            hashMap.put("manufacture", Build.MANUFACTURER);
            if (this.g != null && !this.g.isEmpty()) {
                hashMap.putAll(this.g);
            }
            hashMap.put("abitype", ad.a(this.a));
            hashMap.put("ext", this.b.d());
            return ad.a(ad.a(hashMap));
        }

        /* access modifiers changed from: protected */
        public final String f() {
            return "3.0";
        }
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
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x009f, code lost:
        r2 = null;
        r4 = null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x009e A[ExcHandler: IllegalBlockSizeException (e javax.crypto.IllegalBlockSizeException), PHI: r13 10  PHI: (r13v4 java.lang.String) = (r13v0 java.lang.String), (r13v0 java.lang.String), (r13v0 java.lang.String), (r13v8 java.lang.String), (r13v9 java.lang.String), (r13v9 java.lang.String), (r13v10 java.lang.String) binds: [B:1:0x0014, B:2:?, B:3:0x0019, B:21:0x0095, B:15:0x0081, B:16:?, B:4:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:1:0x0014] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.loc.v.a a(android.content.Context r11, com.loc.ac r12, java.lang.String r13) {
        /*
            r10 = 1
            r3 = 0
            r5 = 0
            com.loc.v$a r0 = new com.loc.v$a
            r0.<init>()
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>()
            r0.w = r1
            com.loc.z r1 = com.loc.z.a.a
            r1.a(r11)
            com.loc.bg r1 = new com.loc.bg     // Catch:{ t -> 0x0082, IllegalBlockSizeException -> 0x009e, Throwable -> 0x00a4 }
            r1.<init>()     // Catch:{ t -> 0x0082, IllegalBlockSizeException -> 0x009e, Throwable -> 0x00a4 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ t -> 0x0080, Throwable -> 0x0094, IllegalBlockSizeException -> 0x009e }
            r1.<init>()     // Catch:{ t -> 0x0080, Throwable -> 0x0094, IllegalBlockSizeException -> 0x009e }
            java.lang.StringBuilder r1 = r1.append(r13)     // Catch:{ t -> 0x0080, Throwable -> 0x0094, IllegalBlockSizeException -> 0x009e }
            java.lang.String r2 = ";14N;15K;16H"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ t -> 0x0080, Throwable -> 0x0094, IllegalBlockSizeException -> 0x009e }
            java.lang.String r13 = r1.toString()     // Catch:{ t -> 0x0080, Throwable -> 0x0094, IllegalBlockSizeException -> 0x009e }
            com.loc.v$b r1 = new com.loc.v$b     // Catch:{ t -> 0x0080, Throwable -> 0x0094, IllegalBlockSizeException -> 0x009e }
            r1.<init>(r11, r12, r13)     // Catch:{ t -> 0x0080, Throwable -> 0x0094, IllegalBlockSizeException -> 0x009e }
            boolean r2 = r1.a()     // Catch:{ t -> 0x0080, Throwable -> 0x0094, IllegalBlockSizeException -> 0x009e }
            com.loc.bk r2 = com.loc.bg.a(r1, r2)     // Catch:{ t -> 0x0080, Throwable -> 0x0094, IllegalBlockSizeException -> 0x009e }
            if (r2 == 0) goto L_0x0359
            byte[] r4 = r2.a     // Catch:{ t -> 0x0352, IllegalBlockSizeException -> 0x034b, Throwable -> 0x0344 }
        L_0x003e:
            r1 = 16
            byte[] r1 = new byte[r1]     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            int r6 = r4.length     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            int r6 = r6 + -16
            byte[] r6 = new byte[r6]     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            r7 = 0
            r8 = 0
            r9 = 16
            java.lang.System.arraycopy(r4, r7, r1, r8, r9)     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            r7 = 16
            r8 = 0
            int r9 = r4.length     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            int r9 = r9 + -16
            java.lang.System.arraycopy(r4, r7, r6, r8, r9)     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            javax.crypto.spec.SecretKeySpec r7 = new javax.crypto.spec.SecretKeySpec     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            java.lang.String r8 = "AES"
            r7.<init>(r1, r8)     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            java.lang.String r1 = "AES/CBC/PKCS5Padding"
            javax.crypto.Cipher r1 = javax.crypto.Cipher.getInstance(r1)     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            javax.crypto.spec.IvParameterSpec r8 = new javax.crypto.spec.IvParameterSpec     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            byte[] r9 = com.loc.ad.c()     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            r8.<init>(r9)     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            r9 = 2
            r1.init(r9, r7, r8)     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            byte[] r1 = r1.doFinal(r6)     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            java.lang.String r3 = com.loc.ad.a(r1)     // Catch:{ t -> 0x0356, IllegalBlockSizeException -> 0x034f, Throwable -> 0x0348 }
            r6 = r2
            r1 = r3
        L_0x007d:
            if (r4 != 0) goto L_0x00b3
        L_0x007f:
            return r0
        L_0x0080:
            r1 = move-exception
            throw r1     // Catch:{ t -> 0x0082, IllegalBlockSizeException -> 0x009e, Throwable -> 0x00a4 }
        L_0x0082:
            r1 = move-exception
            r2 = r3
            r4 = r3
        L_0x0085:
            java.lang.String r6 = r1.a()
            r0.a = r6
            java.lang.String r6 = "/v3/iasdkauth"
            com.loc.aq.a(r12, r6, r1)
            r6 = r2
            r1 = r3
            goto L_0x007d
        L_0x0094:
            r1 = move-exception
            com.loc.t r1 = new com.loc.t     // Catch:{ t -> 0x0082, IllegalBlockSizeException -> 0x009e, Throwable -> 0x00a4 }
            java.lang.String r2 = "未知的错误"
            r1.<init>(r2)     // Catch:{ t -> 0x0082, IllegalBlockSizeException -> 0x009e, Throwable -> 0x00a4 }
            throw r1     // Catch:{ t -> 0x0082, IllegalBlockSizeException -> 0x009e, Throwable -> 0x00a4 }
        L_0x009e:
            r1 = move-exception
            r2 = r3
            r4 = r3
        L_0x00a1:
            r6 = r2
            r1 = r3
            goto L_0x007d
        L_0x00a4:
            r1 = move-exception
            r2 = r3
            r4 = r3
        L_0x00a7:
            java.lang.String r6 = "at"
            java.lang.String r7 = "lc"
            com.loc.aq.b(r1, r6, r7)
            r6 = r2
            r1 = r3
            goto L_0x007d
        L_0x00b3:
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x00bd
            java.lang.String r1 = com.loc.ad.a(r4)
        L_0x00bd:
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Throwable -> 0x0175 }
            r3.<init>(r1)     // Catch:{ Throwable -> 0x0175 }
            java.lang.String r1 = "status"
            boolean r1 = r3.has(r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x007f
            java.lang.String r1 = "status"
            int r1 = r3.getInt(r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 != r10) goto L_0x012a
            r1 = 1
            com.loc.v.a = r1     // Catch:{ Throwable -> 0x0175 }
        L_0x00d7:
            java.lang.String r1 = "ver"
            boolean r1 = r3.has(r1)     // Catch:{ Throwable -> 0x0181 }
            if (r1 == 0) goto L_0x00e9
            java.lang.String r1 = "ver"
            int r1 = r3.getInt(r1)     // Catch:{ Throwable -> 0x0181 }
            r0.b = r1     // Catch:{ Throwable -> 0x0181 }
        L_0x00e9:
            java.lang.String r1 = "result"
            boolean r1 = com.loc.ad.a(r3, r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x007f
            com.loc.v$a$a r2 = new com.loc.v$a$a     // Catch:{ Throwable -> 0x0175 }
            r2.<init>()     // Catch:{ Throwable -> 0x0175 }
            r1 = 0
            r2.a = r1     // Catch:{ Throwable -> 0x0175 }
            r1 = 0
            r2.b = r1     // Catch:{ Throwable -> 0x0175 }
            r0.x = r2     // Catch:{ Throwable -> 0x0175 }
            java.lang.String r1 = "result"
            org.json.JSONObject r3 = r3.getJSONObject(r1)     // Catch:{ Throwable -> 0x0175 }
            java.lang.String r1 = ";"
            java.lang.String[] r4 = r13.split(r1)     // Catch:{ Throwable -> 0x018d }
            if (r4 == 0) goto L_0x0197
            int r1 = r4.length     // Catch:{ Throwable -> 0x018d }
            if (r1 <= 0) goto L_0x0197
            int r6 = r4.length     // Catch:{ Throwable -> 0x018d }
            r1 = r5
        L_0x0114:
            if (r1 >= r6) goto L_0x0197
            r5 = r4[r1]     // Catch:{ Throwable -> 0x018d }
            boolean r7 = r3.has(r5)     // Catch:{ Throwable -> 0x018d }
            if (r7 == 0) goto L_0x0127
            org.json.JSONObject r7 = r0.w     // Catch:{ Throwable -> 0x018d }
            java.lang.Object r8 = r3.get(r5)     // Catch:{ Throwable -> 0x018d }
            r7.putOpt(r5, r8)     // Catch:{ Throwable -> 0x018d }
        L_0x0127:
            int r1 = r1 + 1
            goto L_0x0114
        L_0x012a:
            if (r1 != 0) goto L_0x00d7
            java.lang.String r1 = "authcsid"
            java.lang.String r2 = "authgsid"
            if (r6 == 0) goto L_0x0138
            java.lang.String r1 = r6.c     // Catch:{ Throwable -> 0x0175 }
            java.lang.String r2 = r6.d     // Catch:{ Throwable -> 0x0175 }
        L_0x0138:
            com.loc.ad.a(r11, r1, r2, r3)     // Catch:{ Throwable -> 0x0175 }
            r1 = 0
            com.loc.v.a = r1     // Catch:{ Throwable -> 0x0175 }
            java.lang.String r1 = "info"
            boolean r1 = r3.has(r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x0150
            java.lang.String r1 = "info"
            java.lang.String r1 = r3.getString(r1)     // Catch:{ Throwable -> 0x0175 }
            com.loc.v.b = r1     // Catch:{ Throwable -> 0x0175 }
        L_0x0150:
            java.lang.String r1 = ""
            java.lang.String r4 = "infocode"
            boolean r4 = r3.has(r4)     // Catch:{ Throwable -> 0x0175 }
            if (r4 == 0) goto L_0x0163
            java.lang.String r1 = "infocode"
            java.lang.String r1 = r3.getString(r1)     // Catch:{ Throwable -> 0x0175 }
        L_0x0163:
            java.lang.String r4 = "/v3/iasdkauth"
            java.lang.String r6 = com.loc.v.b     // Catch:{ Throwable -> 0x0175 }
            com.loc.aq.a(r12, r4, r6, r2, r1)     // Catch:{ Throwable -> 0x0175 }
            int r1 = com.loc.v.a     // Catch:{ Throwable -> 0x0175 }
            if (r1 != 0) goto L_0x00d7
            java.lang.String r1 = com.loc.v.b     // Catch:{ Throwable -> 0x0175 }
            r0.a = r1     // Catch:{ Throwable -> 0x0175 }
            goto L_0x007f
        L_0x0175:
            r1 = move-exception
            java.lang.String r2 = "at"
            java.lang.String r3 = "lc"
            com.loc.an.a(r1, r2, r3)
            goto L_0x007f
        L_0x0181:
            r1 = move-exception
            java.lang.String r2 = "at"
            java.lang.String r4 = "lc"
            com.loc.an.a(r1, r2, r4)     // Catch:{ Throwable -> 0x0175 }
            goto L_0x00e9
        L_0x018d:
            r1 = move-exception
            java.lang.String r4 = "at"
            java.lang.String r5 = "co"
            com.loc.an.a(r1, r4, r5)     // Catch:{ Throwable -> 0x0175 }
        L_0x0197:
            java.lang.String r1 = "16H"
            boolean r1 = com.loc.ad.a(r3, r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x01b6
            java.lang.String r1 = "16H"
            org.json.JSONObject r1 = r3.getJSONObject(r1)     // Catch:{ Throwable -> 0x0175 }
            java.lang.String r4 = "able"
            java.lang.String r1 = r1.optString(r4)     // Catch:{ Throwable -> 0x0175 }
            r4 = 0
            boolean r1 = a(r1, r4)     // Catch:{ Throwable -> 0x0175 }
            boolean unused = r0.H = r1     // Catch:{ Throwable -> 0x0175 }
        L_0x01b6:
            java.lang.String r1 = "11K"
            boolean r1 = com.loc.ad.a(r3, r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x01e6
            java.lang.String r1 = "11K"
            org.json.JSONObject r1 = r3.getJSONObject(r1)     // Catch:{ Throwable -> 0x0311 }
            java.lang.String r4 = "able"
            java.lang.String r4 = r1.getString(r4)     // Catch:{ Throwable -> 0x0311 }
            r5 = 0
            boolean r4 = a(r4, r5)     // Catch:{ Throwable -> 0x0311 }
            r2.a = r4     // Catch:{ Throwable -> 0x0311 }
            java.lang.String r4 = "off"
            boolean r4 = r1.has(r4)     // Catch:{ Throwable -> 0x0311 }
            if (r4 == 0) goto L_0x01e6
            java.lang.String r4 = "off"
            org.json.JSONObject r1 = r1.getJSONObject(r4)     // Catch:{ Throwable -> 0x0311 }
            r2.c = r1     // Catch:{ Throwable -> 0x0311 }
        L_0x01e6:
            java.lang.String r1 = "001"
            boolean r1 = com.loc.ad.a(r3, r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x0226
            java.lang.String r1 = "001"
            org.json.JSONObject r1 = r3.getJSONObject(r1)     // Catch:{ Throwable -> 0x0175 }
            com.loc.v$a$d r2 = new com.loc.v$a$d     // Catch:{ Throwable -> 0x0175 }
            r2.<init>()     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x0224
            java.lang.String r4 = "md5"
            java.lang.String r4 = a(r1, r4)     // Catch:{ Throwable -> 0x0325 }
            java.lang.String r5 = "url"
            java.lang.String r5 = a(r1, r5)     // Catch:{ Throwable -> 0x0325 }
            java.lang.String r6 = "sdkversion"
            java.lang.String r1 = a(r1, r6)     // Catch:{ Throwable -> 0x0325 }
            boolean r6 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x0325 }
            if (r6 != 0) goto L_0x0224
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x0325 }
            if (r6 != 0) goto L_0x0224
            boolean r6 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0325 }
            if (r6 == 0) goto L_0x031d
        L_0x0224:
            r0.y = r2     // Catch:{ Throwable -> 0x0175 }
        L_0x0226:
            java.lang.String r1 = "002"
            boolean r1 = com.loc.ad.a(r3, r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x0240
            java.lang.String r1 = "002"
            org.json.JSONObject r1 = r3.getJSONObject(r1)     // Catch:{ Throwable -> 0x0175 }
            com.loc.v$a$c r2 = new com.loc.v$a$c     // Catch:{ Throwable -> 0x0175 }
            r2.<init>()     // Catch:{ Throwable -> 0x0175 }
            a(r1, r2)     // Catch:{ Throwable -> 0x0175 }
            r0.A = r2     // Catch:{ Throwable -> 0x0175 }
        L_0x0240:
            java.lang.String r1 = "14S"
            boolean r1 = com.loc.ad.a(r3, r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x025a
            java.lang.String r1 = "14S"
            org.json.JSONObject r1 = r3.getJSONObject(r1)     // Catch:{ Throwable -> 0x0175 }
            com.loc.v$a$c r2 = new com.loc.v$a$c     // Catch:{ Throwable -> 0x0175 }
            r2.<init>()     // Catch:{ Throwable -> 0x0175 }
            a(r1, r2)     // Catch:{ Throwable -> 0x0175 }
            r0.B = r2     // Catch:{ Throwable -> 0x0175 }
        L_0x025a:
            a(r0, r3)     // Catch:{ Throwable -> 0x0175 }
            java.lang.String r1 = "14Z"
            boolean r1 = com.loc.ad.a(r3, r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x02b9
            java.lang.String r1 = "14Z"
            org.json.JSONObject r1 = r3.getJSONObject(r1)     // Catch:{ Throwable -> 0x0175 }
            com.loc.v$a$e r2 = new com.loc.v$a$e     // Catch:{ Throwable -> 0x0175 }
            r2.<init>()     // Catch:{ Throwable -> 0x0175 }
            java.lang.String r4 = "md5"
            java.lang.String r4 = a(r1, r4)     // Catch:{ Throwable -> 0x0331 }
            java.lang.String r5 = "md5info"
            java.lang.String r5 = a(r1, r5)     // Catch:{ Throwable -> 0x0331 }
            java.lang.String r6 = "url"
            java.lang.String r6 = a(r1, r6)     // Catch:{ Throwable -> 0x0331 }
            java.lang.String r7 = "able"
            java.lang.String r7 = a(r1, r7)     // Catch:{ Throwable -> 0x0331 }
            java.lang.String r8 = "on"
            java.lang.String r8 = a(r1, r8)     // Catch:{ Throwable -> 0x0331 }
            java.lang.String r9 = "mobileable"
            java.lang.String r1 = a(r1, r9)     // Catch:{ Throwable -> 0x0331 }
            r2.e = r4     // Catch:{ Throwable -> 0x0331 }
            r2.f = r5     // Catch:{ Throwable -> 0x0331 }
            r2.d = r6     // Catch:{ Throwable -> 0x0331 }
            r4 = 0
            boolean r4 = a(r7, r4)     // Catch:{ Throwable -> 0x0331 }
            r2.a = r4     // Catch:{ Throwable -> 0x0331 }
            r4 = 0
            boolean r4 = a(r8, r4)     // Catch:{ Throwable -> 0x0331 }
            r2.b = r4     // Catch:{ Throwable -> 0x0331 }
            r4 = 0
            boolean r1 = a(r1, r4)     // Catch:{ Throwable -> 0x0331 }
            r2.c = r1     // Catch:{ Throwable -> 0x0331 }
        L_0x02b7:
            r0.G = r2     // Catch:{ Throwable -> 0x0175 }
        L_0x02b9:
            java.lang.String r1 = "151"
            boolean r1 = com.loc.ad.a(r3, r1)     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x02e0
            java.lang.String r1 = "151"
            org.json.JSONObject r1 = r3.getJSONObject(r1)     // Catch:{ Throwable -> 0x0175 }
            com.loc.v$a$f r2 = new com.loc.v$a$f     // Catch:{ Throwable -> 0x0175 }
            r2.<init>()     // Catch:{ Throwable -> 0x0175 }
            if (r1 == 0) goto L_0x02de
            java.lang.String r4 = "able"
            java.lang.String r1 = r1.optString(r4)     // Catch:{ Throwable -> 0x0175 }
            r4 = 0
            boolean r1 = a(r1, r4)     // Catch:{ Throwable -> 0x0175 }
            r2.a = r1     // Catch:{ Throwable -> 0x0175 }
        L_0x02de:
            r0.z = r2     // Catch:{ Throwable -> 0x0175 }
        L_0x02e0:
            a(r0, r3)     // Catch:{ Throwable -> 0x0175 }
            java.lang.String r1 = "15K"
            org.json.JSONObject r1 = r3.getJSONObject(r1)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r2 = "isTargetAble"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ Throwable -> 0x030b }
            r3 = 0
            boolean r2 = a(r2, r3)     // Catch:{ Throwable -> 0x030b }
            java.lang.String r3 = "able"
            java.lang.String r1 = r1.optString(r3)     // Catch:{ Throwable -> 0x030b }
            r3 = 0
            boolean r1 = a(r1, r3)     // Catch:{ Throwable -> 0x030b }
            if (r1 != 0) goto L_0x033d
            com.loc.z r1 = com.loc.z.a.a     // Catch:{ Throwable -> 0x030b }
            r1.b(r11)     // Catch:{ Throwable -> 0x030b }
            goto L_0x007f
        L_0x030b:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ Throwable -> 0x0175 }
            goto L_0x007f
        L_0x0311:
            r1 = move-exception
            java.lang.String r2 = "AuthConfigManager"
            java.lang.String r4 = "loadException"
            com.loc.an.a(r1, r2, r4)     // Catch:{ Throwable -> 0x0175 }
            goto L_0x01e6
        L_0x031d:
            r2.a = r5     // Catch:{ Throwable -> 0x0325 }
            r2.b = r4     // Catch:{ Throwable -> 0x0325 }
            r2.c = r1     // Catch:{ Throwable -> 0x0325 }
            goto L_0x0224
        L_0x0325:
            r1 = move-exception
            java.lang.String r4 = "at"
            java.lang.String r5 = "psu"
            com.loc.an.a(r1, r4, r5)     // Catch:{ Throwable -> 0x0175 }
            goto L_0x0224
        L_0x0331:
            r1 = move-exception
            java.lang.String r4 = "at"
            java.lang.String r5 = "pes"
            com.loc.an.a(r1, r4, r5)     // Catch:{ Throwable -> 0x0175 }
            goto L_0x02b7
        L_0x033d:
            com.loc.z r1 = com.loc.z.a.a     // Catch:{ Throwable -> 0x030b }
            r1.a(r11, r2)     // Catch:{ Throwable -> 0x030b }
            goto L_0x007f
        L_0x0344:
            r1 = move-exception
            r4 = r3
            goto L_0x00a7
        L_0x0348:
            r1 = move-exception
            goto L_0x00a7
        L_0x034b:
            r1 = move-exception
            r4 = r3
            goto L_0x00a1
        L_0x034f:
            r1 = move-exception
            goto L_0x00a1
        L_0x0352:
            r1 = move-exception
            r4 = r3
            goto L_0x0085
        L_0x0356:
            r1 = move-exception
            goto L_0x0085
        L_0x0359:
            r4 = r3
            goto L_0x003e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.v.a(android.content.Context, com.loc.ac, java.lang.String):com.loc.v$a");
    }

    private static String a(JSONObject jSONObject, String str) throws JSONException {
        return (jSONObject != null && jSONObject.has(str) && !jSONObject.getString(str).equals(XMPConst.ARRAY_ITEM_NAME)) ? jSONObject.optString(str) : "";
    }

    public static void a(Context context, String str) {
        u.a(context, str);
    }

    private static void a(a aVar, JSONObject jSONObject) {
        try {
            if (ad.a(jSONObject, "11B")) {
                aVar.h = jSONObject.getJSONObject("11B");
            }
            if (ad.a(jSONObject, "11C")) {
                aVar.k = jSONObject.getJSONObject("11C");
            }
            if (ad.a(jSONObject, "11I")) {
                aVar.l = jSONObject.getJSONObject("11I");
            }
            if (ad.a(jSONObject, "11H")) {
                aVar.m = jSONObject.getJSONObject("11H");
            }
            if (ad.a(jSONObject, "11E")) {
                aVar.n = jSONObject.getJSONObject("11E");
            }
            if (ad.a(jSONObject, "11F")) {
                aVar.o = jSONObject.getJSONObject("11F");
            }
            if (ad.a(jSONObject, "13A")) {
                aVar.q = jSONObject.getJSONObject("13A");
            }
            if (ad.a(jSONObject, "13J")) {
                aVar.i = jSONObject.getJSONObject("13J");
            }
            if (ad.a(jSONObject, "11G")) {
                aVar.p = jSONObject.getJSONObject("11G");
            }
            if (ad.a(jSONObject, "006")) {
                aVar.r = jSONObject.getJSONObject("006");
            }
            if (ad.a(jSONObject, "010")) {
                aVar.s = jSONObject.getJSONObject("010");
            }
            if (ad.a(jSONObject, "11Z")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("11Z");
                a.b bVar = new a.b();
                a(jSONObject2, bVar);
                aVar.C = bVar;
            }
            if (ad.a(jSONObject, "135")) {
                aVar.j = jSONObject.getJSONObject("135");
            }
            if (ad.a(jSONObject, "13S")) {
                aVar.g = jSONObject.getJSONObject("13S");
            }
            if (ad.a(jSONObject, "121")) {
                JSONObject jSONObject3 = jSONObject.getJSONObject("121");
                a.b bVar2 = new a.b();
                a(jSONObject3, bVar2);
                aVar.D = bVar2;
            }
            if (ad.a(jSONObject, "122")) {
                JSONObject jSONObject4 = jSONObject.getJSONObject("122");
                a.b bVar3 = new a.b();
                a(jSONObject4, bVar3);
                aVar.E = bVar3;
            }
            if (ad.a(jSONObject, "123")) {
                JSONObject jSONObject5 = jSONObject.getJSONObject("123");
                a.b bVar4 = new a.b();
                a(jSONObject5, bVar4);
                aVar.F = bVar4;
            }
            if (ad.a(jSONObject, "011")) {
                aVar.c = jSONObject.getJSONObject("011");
            }
            if (ad.a(jSONObject, "012")) {
                aVar.d = jSONObject.getJSONObject("012");
            }
            if (ad.a(jSONObject, "013")) {
                aVar.e = jSONObject.getJSONObject("013");
            }
            if (ad.a(jSONObject, "014")) {
                aVar.f = jSONObject.getJSONObject("014");
            }
            if (ad.a(jSONObject, "145")) {
                aVar.t = jSONObject.getJSONObject("145");
            }
            if (ad.a(jSONObject, "14B")) {
                aVar.u = jSONObject.getJSONObject("14B");
            }
            if (ad.a(jSONObject, "14D")) {
                aVar.v = jSONObject.getJSONObject("14D");
            }
        } catch (Throwable th) {
            aq.b(th, "at", "pe");
        }
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
    private static void a(JSONObject jSONObject, a.b bVar) {
        try {
            String a2 = a(jSONObject, "m");
            String a3 = a(jSONObject, "u");
            String a4 = a(jSONObject, "v");
            String a5 = a(jSONObject, "able");
            String a6 = a(jSONObject, "on");
            bVar.c = a2;
            bVar.b = a3;
            bVar.d = a4;
            bVar.a = a(a5, false);
            bVar.e = a(a6, true);
        } catch (Throwable th) {
            an.a(th, "at", "pe");
        }
    }

    private static void a(JSONObject jSONObject, a.c cVar) {
        if (jSONObject != null) {
            try {
                String a2 = a(jSONObject, ResponseBase.STRING_MD5);
                String a3 = a(jSONObject, "url");
                cVar.b = a2;
                cVar.a = a3;
            } catch (Throwable th) {
                an.a(th, "at", "psc");
            }
        }
    }

    public static boolean a(String str, boolean z) {
        try {
            if (TextUtils.isEmpty(str)) {
                return z;
            }
            String[] split = URLDecoder.decode(str).split(IMemberProtocol.PARAM_SEPERATOR);
            return split[split.length + -1].charAt(4) % 2 == 1;
        } catch (Throwable th) {
            return z;
        }
    }
}
