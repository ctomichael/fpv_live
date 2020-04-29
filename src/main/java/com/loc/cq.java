package com.loc;

import java.util.HashMap;
import java.util.Map;

/* compiled from: CoRequest */
public final class cq extends bj {
    byte[] a = null;
    private String b = null;
    private Map<String, String> f = new HashMap();

    public final void a(String str) {
        this.b = str;
    }

    public final void a(Map<String, String> map) {
        this.f = map;
    }

    public final Map<String, String> b() {
        return this.f;
    }

    public final Map<String, String> b_() {
        return null;
    }

    public final String c() {
        return this.b;
    }

    public final byte[] d() {
        return this.a;
    }

    public final boolean l() {
        return true;
    }
}
