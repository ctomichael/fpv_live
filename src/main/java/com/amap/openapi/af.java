package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: TCell */
public final class af extends fd {
    public static int a(fc fcVar) {
        return fcVar.e();
    }

    public static int a(fc fcVar, int i, byte b, int i2, int i3) {
        fcVar.f(4);
        c(fcVar, i3);
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
        fcVar.a(1, b, 0);
    }

    public static void a(fc fcVar, int i) {
        fcVar.c(0, i, 0);
    }

    public static int b(fc fcVar, int[] iArr) {
        fcVar.a(4, iArr.length, 4);
        for (int length = iArr.length - 1; length >= 0; length--) {
            fcVar.e(iArr[length]);
        }
        return fcVar.b();
    }

    public static void b(fc fcVar, int i) {
        fcVar.c(2, i, 0);
    }

    public static void c(fc fcVar, int i) {
        fcVar.c(3, i, 0);
    }
}
