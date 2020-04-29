package com.amap.openapi;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.amap.location.common.HeaderConfig;
import com.amap.location.common.a;
import com.loc.fc;

/* compiled from: HeaderBuilder */
public class bk {
    public static int a(fc fcVar, Context context) {
        a();
        try {
            int a = fcVar.a(context.getPackageName());
            int a2 = fcVar.a(HeaderConfig.getProductVerion());
            int a3 = TextUtils.isEmpty(a.c(context)) ? Integer.MIN_VALUE : fcVar.a(a.c(context));
            String b = a.b(context);
            int a4 = TextUtils.isEmpty(b) ? Integer.MIN_VALUE : fcVar.a(b);
            String a5 = a.a(context);
            int a6 = TextUtils.isEmpty(a5) ? Integer.MIN_VALUE : fcVar.a(a5);
            String d = a.d(context);
            int a7 = TextUtils.isEmpty(d) ? Integer.MIN_VALUE : fcVar.a(d);
            String str = Build.BRAND;
            if (str != null && str.length() > 16) {
                str = str.substring(0, 16);
            }
            int a8 = TextUtils.isEmpty(str) ? Integer.MIN_VALUE : fcVar.a(str);
            String str2 = Build.MODEL;
            if (str2 != null && str2.length() > 16) {
                str2 = str2.substring(0, 16);
            }
            int a9 = TextUtils.isEmpty(str2) ? Integer.MIN_VALUE : fcVar.a(str2);
            int a10 = TextUtils.isEmpty(HeaderConfig.getLicense()) ? Integer.MIN_VALUE : fcVar.a(HeaderConfig.getLicense());
            int a11 = TextUtils.isEmpty(HeaderConfig.getMapkey()) ? Integer.MIN_VALUE : fcVar.a(HeaderConfig.getMapkey());
            bl.a(fcVar);
            bl.a(fcVar, HeaderConfig.getProductId());
            bl.a(fcVar, a);
            bl.b(fcVar, a2);
            bl.b(fcVar, (byte) a.d());
            bl.a(fcVar, a.e(context));
            if (a3 != Integer.MIN_VALUE) {
                bl.c(fcVar, a3);
            }
            if (a4 != Integer.MIN_VALUE) {
                bl.d(fcVar, a4);
            }
            if (a6 != Integer.MIN_VALUE) {
                bl.e(fcVar, a6);
            }
            if (a7 != Integer.MIN_VALUE) {
                bl.f(fcVar, a7);
            }
            if (a8 != Integer.MIN_VALUE) {
                bl.g(fcVar, a8);
            }
            if (a9 != Integer.MIN_VALUE) {
                bl.h(fcVar, a9);
            }
            if (a10 != Integer.MIN_VALUE) {
                bl.i(fcVar, a10);
            }
            if (a11 != Integer.MIN_VALUE) {
                bl.j(fcVar, a11);
            }
            return bl.b(fcVar);
        } catch (Error | Exception e) {
            return 0;
        }
    }

    private static void a() {
        if (HeaderConfig.getProductId() < 0 || TextUtils.isEmpty(HeaderConfig.getProductVerion())) {
            throw new RuntimeException("必须在 HeaderBuildre 中，设置好 productId, productVerion" + ((int) HeaderConfig.getProductId()) + HeaderConfig.getProductVerion() + "， 以及其他的值");
        }
    }
}
