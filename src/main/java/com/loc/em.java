package com.loc;

import java.util.Map;

/* compiled from: HttpRequest */
public final class em extends bj {
    Map<String, String> a = null;
    Map<String, String> b = null;
    String f = "";
    byte[] g = null;

    public final void a(String str) {
        this.f = str;
    }

    public final void a(Map<String, String> map) {
        this.a = map;
    }

    public final Map<String, String> b() {
        return this.a;
    }

    public final void b(Map<String, String> map) {
        this.b = map;
    }

    public final Map<String, String> b_() {
        return this.b;
    }

    public final String c() {
        return this.f;
    }

    public final byte[] d() {
        return this.g;
    }
}
