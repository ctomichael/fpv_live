package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: TData */
public final class ai extends fd {
    public static int a(fc fcVar) {
        return fcVar.e();
    }

    public static int a(fc fcVar, byte b, int i) {
        fcVar.f(2);
        a(fcVar, i);
        a(fcVar, b);
        return a(fcVar);
    }

    public static int a(fc fcVar, byte[] bArr) {
        fcVar.a(1, bArr.length, 1);
        for (int length = bArr.length - 1; length >= 0; length--) {
            fcVar.b(bArr[length]);
        }
        return fcVar.b();
    }

    public static void a(fc fcVar, byte b) {
        fcVar.a(0, b, 0);
    }

    public static void a(fc fcVar, int i) {
        fcVar.c(1, i, 0);
    }
}
