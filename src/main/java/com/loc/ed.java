package com.loc;

import android.support.v4.os.EnvironmentCompat;
import java.util.Locale;
import org.json.JSONObject;

/* compiled from: Cgi */
public final class ed {
    public int a = 0;
    public int b = 0;
    public int c = 0;
    public int d = 0;
    public int e = 0;
    public int f = 0;
    public int g = 0;
    public int h = 0;
    public int i = 0;
    public int j = -113;
    public int k = 0;
    public short l = 0;
    public long m = 0;
    public boolean n = false;
    public int o = 32767;
    public boolean p = true;

    public ed(int i2, boolean z) {
        this.k = i2;
        this.n = z;
    }

    public final JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", this.k);
            jSONObject.put("registered", this.n);
            jSONObject.put("mcc", this.a);
            jSONObject.put("mnc", this.b);
            jSONObject.put("lac", this.c);
            jSONObject.put("cid", this.d);
            jSONObject.put("sid", this.g);
            jSONObject.put("nid", this.h);
            jSONObject.put("bid", this.i);
            jSONObject.put("sig", this.j);
            jSONObject.put("pci", this.o);
        } catch (Throwable th) {
            es.a(th, "cgi", "toJson");
        }
        return jSONObject;
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ed)) {
            return false;
        }
        ed edVar = (ed) obj;
        switch (edVar.k) {
            case 1:
                return this.k == 1 && edVar.c == this.c && edVar.d == this.d && edVar.b == this.b;
            case 2:
                return this.k == 2 && edVar.i == this.i && edVar.h == this.h && edVar.g == this.g;
            case 3:
                return this.k == 3 && edVar.c == this.c && edVar.d == this.d && edVar.b == this.b;
            case 4:
                return this.k == 4 && edVar.c == this.c && edVar.d == this.d && edVar.b == this.b;
            default:
                return false;
        }
    }

    public final int hashCode() {
        int hashCode = String.valueOf(this.k).hashCode();
        return this.k == 2 ? hashCode + String.valueOf(this.i).hashCode() + String.valueOf(this.h).hashCode() + String.valueOf(this.g).hashCode() : hashCode + String.valueOf(this.c).hashCode() + String.valueOf(this.d).hashCode() + String.valueOf(this.b).hashCode();
    }

    public final String toString() {
        switch (this.k) {
            case 1:
                return String.format(Locale.CHINA, "GSM lac=%d, cid=%d, mnc=%s, valid=%b, sig=%d, age=%d, reg=%b", Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n));
            case 2:
                return String.format(Locale.CHINA, "CDMA bid=%d, nid=%d, sid=%d, valid=%b, sig=%d, age=%d, reg=%b", Integer.valueOf(this.i), Integer.valueOf(this.h), Integer.valueOf(this.g), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n));
            case 3:
                return String.format(Locale.CHINA, "LTE lac=%d, cid=%d, mnc=%s, valid=%b, sig=%d, age=%d, reg=%b, pci=%d", Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n), Integer.valueOf(this.o));
            case 4:
                return String.format(Locale.CHINA, "WCDMA lac=%d, cid=%d, mnc=%s, valid=%b, sig=%d, age=%d, reg=%b, pci=%d", Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n), Integer.valueOf(this.o));
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }
}
