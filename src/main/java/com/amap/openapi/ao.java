package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: TMainCellGSMHistory */
public final class ao extends fd {
    public static int a(fc fcVar) {
        return fcVar.e();
    }

    public static int a(fc fcVar, byte b, int i, int i2, short s) {
        fcVar.f(4);
        b(fcVar, i2);
        a(fcVar, i);
        a(fcVar, s);
        a(fcVar, b);
        return a(fcVar);
    }

    public static void a(fc fcVar, byte b) {
        fcVar.a(0, b, 0);
    }

    public static void a(fc fcVar, int i) {
        fcVar.b(1, i, 0);
    }

    public static void a(fc fcVar, short s) {
        fcVar.a(3, s, 0);
    }

    public static void b(fc fcVar, int i) {
        fcVar.b(2, i, 0);
    }
}
