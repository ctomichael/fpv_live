package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: RootTTrack */
public final class ac extends fd {
    public static int a(fc fcVar) {
        return fcVar.e();
    }

    public static int a(fc fcVar, int i, int i2, byte b) {
        fcVar.f(3);
        b(fcVar, i2);
        a(fcVar, i);
        a(fcVar, b);
        return a(fcVar);
    }

    public static int a(fc fcVar, int[] iArr) {
        fcVar.a(4, iArr.length, 4);
        for (int length = iArr.length - 1; length >= 0; length--) {
            fcVar.e(iArr[length]);
        }
        return fcVar.b();
    }

    public static void a(fc fcVar, byte b) {
        fcVar.a(2, b, 0);
    }

    public static void a(fc fcVar, int i) {
        fcVar.c(0, i, 0);
    }

    public static void b(fc fcVar, int i) {
        fcVar.c(1, i, 0);
    }
}
