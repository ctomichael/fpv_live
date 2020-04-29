package com.loc;

import java.util.Map;

/* compiled from: ADIURequest */
public final class bf extends bj {
    private byte[] a;
    private Map<String, String> b;

    public bf(byte[] bArr, Map<String, String> map) {
        this.a = bArr;
        this.b = map;
    }

    public final Map<String, String> b() {
        return null;
    }

    public final Map<String, String> b_() {
        return this.b;
    }

    public final String c() {
        return "https://adiu.amap.com/ws/device/adius";
    }

    public final byte[] d() {
        return this.a;
    }
}
