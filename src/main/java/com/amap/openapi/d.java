package com.amap.openapi;

import com.amap.location.common.network.IHttpClient;

/* compiled from: CloudConfig */
public class d {
    public static boolean a = false;
    private byte b = -1;
    private String c = "";
    private String d = "";
    private String e = "";
    private String f = "";
    private String g = "";
    private IHttpClient h;

    public byte a() {
        return this.b;
    }

    public void a(byte b2) {
        this.b = b2;
    }

    public void a(IHttpClient iHttpClient) {
        this.h = iHttpClient;
    }

    public void a(String str) {
        this.c = str;
    }

    public String b() {
        return this.c;
    }

    public void b(String str) {
        this.d = str;
    }

    public String c() {
        return this.d;
    }

    public void c(String str) {
        this.e = str;
    }

    public String d() {
        return this.e;
    }

    public void d(String str) {
        if (str != null) {
            this.g = str;
        }
    }

    public String e() {
        return this.g;
    }

    public void e(String str) {
        if (str != null) {
            this.f = str;
        }
    }

    public IHttpClient f() {
        return this.h;
    }
}
