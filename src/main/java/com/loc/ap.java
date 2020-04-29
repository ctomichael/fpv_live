package com.loc;

import java.util.HashMap;
import java.util.Map;

/* compiled from: LogUpdateRequest */
public final class ap extends bj {
    private byte[] a;
    private String b = "1";

    public ap(byte[] bArr, String str) {
        this.a = (byte[]) bArr.clone();
        this.b = str;
    }

    public final Map<String, String> b() {
        HashMap hashMap = new HashMap();
        hashMap.put("Content-Type", "application/zip");
        hashMap.put("Content-Length", String.valueOf(this.a.length));
        return hashMap;
    }

    public final Map<String, String> b_() {
        return null;
    }

    public final String c() {
        String c = ad.c(al.c);
        byte[] a2 = ad.a(al.b);
        byte[] bArr = new byte[(a2.length + 50)];
        System.arraycopy(this.a, 0, bArr, 0, 50);
        System.arraycopy(a2, 0, bArr, 50, a2.length);
        return String.format(c, "1", this.b, "1", "open", aa.a(bArr));
    }

    public final byte[] d() {
        return this.a;
    }
}
