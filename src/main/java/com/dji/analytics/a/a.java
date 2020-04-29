package com.dji.analytics.a;

import com.dji.analytics.DJIA;
import com.dji.analytics.d.d;
import com.dji.analytics.f.c;
import com.dji.analytics.f.f;
import java.util.HashMap;
import java.util.Set;

/* compiled from: DJIReport */
public class a {
    private HashMap<String, Object> a = new HashMap<>();
    private String b = "";

    public HashMap<String, Object> a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public a(String str, HashMap<String, String> hashMap, boolean z) {
        String a2 = a(str);
        this.a.put("uuid", com.dji.analytics.d.a.a().e());
        this.a.put("type", a2);
        this.b = c.a();
        this.a.put("reportid", this.b);
        this.a.put("time", Long.valueOf(System.currentTimeMillis()));
        this.a.put("sessionid", d.a());
        HashMap<String, String> extraData = DJIA.getConfig().getExtraData();
        if (extraData != null) {
            for (String str2 : extraData.keySet()) {
                this.a.put(str2, extraData.get(str2));
            }
        }
        if (hashMap != null) {
            HashMap hashMap2 = new HashMap();
            for (String str3 : hashMap.keySet()) {
                hashMap2.put(str3, a(hashMap.get(str3)));
            }
            this.a.put("eventvalue", hashMap2);
        }
        this.a.put("appver", com.dji.analytics.d.a.a().d());
    }

    public a(byte[] bArr) {
        this.a = a(bArr);
    }

    private HashMap<String, Object> a(byte[] bArr) {
        HashMap<String, Object> a2;
        return (bArr == null || bArr.length <= 0 || (a2 = f.a(bArr)) == null) ? new HashMap<>() : a2;
    }

    public byte[] c() {
        return f.a(this.a);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = this.a.keySet();
        sb.append("{");
        for (String str : keySet) {
            sb.append("{");
            sb.append(str);
            sb.append(":");
            sb.append(this.a.get(str));
            sb.append("},");
        }
        sb.append("}");
        return sb.toString();
    }

    private String a(String str) {
        if (str == null) {
            return "null_value_add_by_djia";
        }
        return str;
    }

    public void d() {
        if (this.a.containsKey("dji_id")) {
            this.a.put("uuid", this.a.get("dji_id"));
            this.a.remove("dji_id");
        }
    }

    public void e() {
        if (this.a.containsKey("uuid")) {
            this.a.put("dji_id", this.a.get("uuid"));
            this.a.remove("uuid");
        }
    }
}
