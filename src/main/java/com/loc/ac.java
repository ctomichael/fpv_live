package com.loc;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

@aw(a = "a")
/* compiled from: SDKInfo */
public class ac {
    @ax(a = "a1", b = 6)
    private String a;
    @ax(a = "a2", b = 6)
    private String b;
    @ax(a = "a6", b = 2)
    private int c;
    @ax(a = "a3", b = 6)
    private String d;
    @ax(a = "a4", b = 6)
    private String e;
    @ax(a = "a5", b = 6)
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String[] l;

    /* compiled from: SDKInfo */
    public static class a {
        /* access modifiers changed from: private */
        public String a;
        /* access modifiers changed from: private */
        public String b;
        /* access modifiers changed from: private */
        public String c;
        /* access modifiers changed from: private */
        public String d;
        /* access modifiers changed from: private */
        public boolean e = true;
        /* access modifiers changed from: private */
        public String f = "standard";
        /* access modifiers changed from: private */
        public String[] g = null;

        public a(String str, String str2, String str3) {
            this.a = str2;
            this.b = str2;
            this.d = str3;
            this.c = str;
        }

        public final a a(String str) {
            this.b = str;
            return this;
        }

        public final a a(String[] strArr) {
            if (strArr != null) {
                this.g = (String[]) strArr.clone();
            }
            return this;
        }

        public final ac a() throws t {
            if (this.g != null) {
                return new ac(this, (byte) 0);
            }
            throw new t("sdk packages is null");
        }
    }

    private ac() {
        this.c = 1;
        this.l = null;
    }

    private ac(a aVar) {
        int i2 = 1;
        this.c = 1;
        this.l = null;
        this.g = aVar.a;
        this.h = aVar.b;
        this.j = aVar.c;
        this.i = aVar.d;
        this.c = !aVar.e ? 0 : i2;
        this.k = aVar.f;
        this.l = aVar.g;
        this.b = ad.b(this.h);
        this.a = ad.b(this.j);
        this.d = ad.b(this.i);
        this.e = ad.b(a(this.l));
        this.f = ad.b(this.k);
    }

    /* synthetic */ ac(a aVar, byte b2) {
        this(aVar);
    }

    public static String a(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("a1", ad.b(str));
        return av.a((Map<String, String>) hashMap);
    }

    private static String a(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (String str : strArr) {
                sb.append(str).append(";");
            }
            return sb.toString();
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private static String[] b(String str) {
        try {
            return str.split(";");
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static String g() {
        return "a6=1";
    }

    public final String a() {
        if (TextUtils.isEmpty(this.j) && !TextUtils.isEmpty(this.a)) {
            this.j = ad.c(this.a);
        }
        return this.j;
    }

    public final void a(boolean z) {
        this.c = z ? 1 : 0;
    }

    public final String b() {
        return this.g;
    }

    public final String c() {
        if (TextUtils.isEmpty(this.h) && !TextUtils.isEmpty(this.b)) {
            this.h = ad.c(this.b);
        }
        return this.h;
    }

    public final String d() {
        if (TextUtils.isEmpty(this.k) && !TextUtils.isEmpty(this.f)) {
            this.k = ad.c(this.f);
        }
        if (TextUtils.isEmpty(this.k)) {
            this.k = "standard";
        }
        return this.k;
    }

    public final boolean e() {
        return this.c == 1;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return getClass() == obj.getClass() && hashCode() == ((ac) obj).hashCode();
    }

    public final String[] f() {
        if ((this.l == null || this.l.length == 0) && !TextUtils.isEmpty(this.e)) {
            this.l = b(ad.c(this.e));
        }
        return (String[]) this.l.clone();
    }

    public int hashCode() {
        am amVar = new am();
        amVar.a(this.j).a(this.g).a(this.h).a((Object[]) this.l);
        return amVar.a();
    }
}
