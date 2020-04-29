package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: TWifi */
public final class aq extends fd {
    public static int a(fc fcVar) {
        return fcVar.e();
    }

    public static int a(fc fcVar, int i) {
        fcVar.f(1);
        b(fcVar, i);
        return a(fcVar);
    }

    public static int a(fc fcVar, int[] iArr) {
        fcVar.a(4, iArr.length, 4);
        for (int length = iArr.length - 1; length >= 0; length--) {
            fcVar.e(iArr[length]);
        }
        return fcVar.b();
    }

    public static void b(fc fcVar, int i) {
        fcVar.c(0, i, 0);
    }
}
