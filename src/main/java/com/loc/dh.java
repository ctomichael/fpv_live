package com.loc;

import org.json.JSONArray;
import org.json.JSONObject;

final class dh {
    private String a;
    private String[] b;
    private long c;
    private long d;

    dh(String str) {
        JSONObject jSONObject = new JSONObject(str);
        this.a = jSONObject.getString("host");
        JSONArray jSONArray = jSONObject.getJSONArray("ips");
        int length = jSONArray.length();
        this.b = new String[length];
        for (int i = 0; i < length; i++) {
            this.b[i] = jSONArray.getString(i);
        }
        this.c = jSONObject.getLong("ttl");
        this.d = System.currentTimeMillis() / 1000;
    }

    /* access modifiers changed from: package-private */
    public final String[] a() {
        return this.b;
    }

    /* access modifiers changed from: package-private */
    public final boolean b() {
        return this.d + this.c < System.currentTimeMillis() / 1000;
    }

    public final String toString() {
        String str = "host: " + this.a + " ip cnt: " + this.b.length + " ttl: " + this.c;
        for (int i = 0; i < this.b.length; i++) {
            str = str + "\n ip: " + this.b[i];
        }
        return str;
    }
}
