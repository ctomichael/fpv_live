package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: TGpsStatus */
public final class ak extends fd {
    public static int a(fc fcVar) {
        return fcVar.e();
    }

    public static int a(fc fcVar, byte b, byte b2, byte b3, short s, byte b4) {
        fcVar.f(6);
        a(fcVar, s);
        d(fcVar, b4);
        c(fcVar, b3);
        b(fcVar, b2);
        a(fcVar, b);
        return a(fcVar);
    }

    public static void a(fc fcVar, byte b) {
        fcVar.a(0, b, 0);
    }

    public static void a(fc fcVar, short s) {
        fcVar.a(3, s, 0);
    }

    public static void b(fc fcVar, byte b) {
        fcVar.a(1, b, 0);
    }

    public static void c(fc fcVar, byte b) {
        fcVar.a(2, b, 0);
    }

    public static void d(fc fcVar, byte b) {
        fcVar.a(4, b, 0);
    }
}
