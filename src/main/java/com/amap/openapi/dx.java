package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: CountFB */
public final class dx extends fd {
    public static int a(fc fcVar) {
        return fcVar.e();
    }

    public static int a(fc fcVar, int i, int i2, long j) {
        fcVar.f(3);
        a(fcVar, j);
        b(fcVar, i2);
        a(fcVar, i);
        return a(fcVar);
    }

    public static void a(fc fcVar, int i) {
        fcVar.b(0, i, 0);
    }

    public static void a(fc fcVar, long j) {
        fcVar.a(2, j, 0);
    }

    public static void b(fc fcVar, int i) {
        fcVar.b(1, i, 0);
    }
}
