package com.amap.openapi;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: CloudCommand */
public class a {
    protected long a = 43200000;
    protected String b;
    protected int c;
    protected long d;
    protected C0009a e;
    private final long f = 300000;
    private final long g = 259200000;

    /* renamed from: com.amap.openapi.a$a  reason: collision with other inner class name */
    /* compiled from: CloudCommand */
    public class C0009a {
        private JSONObject b;

        C0009a(String str) throws JSONException {
            this.b = new JSONObject(str);
        }
    }

    protected a() {
    }

    public String a() {
        return this.b;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.min(long, long):long}
     arg types: [long, int]
     candidates:
      ClspMth{java.lang.Math.min(double, double):double}
      ClspMth{java.lang.Math.min(float, float):float}
      ClspMth{java.lang.Math.min(int, int):int}
      ClspMth{java.lang.Math.min(long, long):long} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [long, int]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    /* access modifiers changed from: protected */
    public boolean a(String str) {
        boolean z;
        String str2 = null;
        try {
            str2 = str.substring(str.indexOf("$") + 1, str.length());
            JSONObject jSONObject = new JSONObject(str2);
            this.a = Math.min(jSONObject.optLong("p", 43200000), 259200000L);
            this.a = Math.max(this.a, 300000L);
            this.c = jSONObject.optInt("v");
            this.e = new C0009a(str2);
            z = true;
        } catch (Exception e2) {
            z = false;
        }
        if (z) {
            this.b = str2;
        }
        return z;
    }
}
