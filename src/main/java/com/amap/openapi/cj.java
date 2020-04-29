package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: ReqOfflineCellWiFi */
public final class cj extends fd {
    public static int a(fc fcVar, int[] iArr) {
        fcVar.a(4, iArr.length, 4);
        for (int length = iArr.length - 1; length >= 0; length--) {
            fcVar.e(iArr[length]);
        }
        return fcVar.b();
    }

    public static int a(fc fcVar, long[] jArr) {
        fcVar.a(8, jArr.length, 8);
        for (int length = jArr.length - 1; length >= 0; length--) {
            fcVar.b(jArr[length]);
        }
        return fcVar.b();
    }

    public static void a(fc fcVar) {
        fcVar.f(6);
    }

    public static void a(fc fcVar, byte b) {
        fcVar.a(0, b, 0);
    }

    public static void a(fc fcVar, int i) {
        fcVar.c(1, i, 0);
    }

    public static void a(fc fcVar, long j) {
        fcVar.a(2, j, 0);
    }

    public static int b(fc fcVar) {
        return fcVar.e();
    }

    public static void b(fc fcVar, int i) {
        fcVar.c(3, i, 0);
    }

    public static void c(fc fcVar, int i) {
        fcVar.c(4, i, 0);
    }

    public static void d(fc fcVar, int i) {
        fcVar.c(5, i, 0);
    }

    public static void e(fc fcVar, int i) {
        fcVar.h(i);
    }
}
