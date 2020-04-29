package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: TCellInfo */
public final class ah extends fd {
    public static int a(fc fcVar) {
        return fcVar.e();
    }

    public static int a(fc fcVar, byte b, byte b2, short s, byte b3, int i) {
        fcVar.f(5);
        a(fcVar, i);
        a(fcVar, s);
        c(fcVar, b3);
        b(fcVar, b2);
        a(fcVar, b);
        return a(fcVar);
    }

    public static void a(fc fcVar, byte b) {
        fcVar.a(0, b, 0);
    }

    public static void a(fc fcVar, int i) {
        fcVar.c(4, i, 0);
    }

    public static void a(fc fcVar, short s) {
        fcVar.a(2, s, 0);
    }

    public static void b(fc fcVar, byte b) {
        fcVar.a(1, b, 0);
    }

    public static void c(fc fcVar, byte b) {
        fcVar.a(3, b, 0);
    }
}
