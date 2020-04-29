package com.loc;

import android.content.Context;
import java.util.zip.Adler32;

/* compiled from: DeviceInfo */
public final class cy {
    static String a = "d6fc3a4a06adbde89223bvefedc24fecde188aaa9161";
    static final Object b = new Object();
    private static cx c = null;

    public static synchronized cx a(Context context) {
        cx cxVar;
        synchronized (cy.class) {
            if (c != null) {
                cxVar = c;
            } else if (context != null) {
                cxVar = b(context);
                c = cxVar;
            } else {
                cxVar = null;
            }
        }
        return cxVar;
    }

    private static cx b(Context context) {
        long j;
        if (context != null) {
            new cx();
            synchronized (b) {
                String a2 = cz.a(context).a();
                if (!dw.a(a2)) {
                    String substring = a2.endsWith("\n") ? a2.substring(0, a2.length() - 1) : a2;
                    cx cxVar = new cx();
                    long currentTimeMillis = System.currentTimeMillis();
                    String a3 = dv.a(context);
                    String b2 = dv.b(context);
                    cxVar.c(a3);
                    cxVar.a(a3);
                    cxVar.b(currentTimeMillis);
                    cxVar.b(b2);
                    cxVar.d(substring);
                    String format = String.format("%s%s%s%s%s", cxVar.e(), cxVar.d(), Long.valueOf(cxVar.a()), cxVar.c(), cxVar.b());
                    if (!dw.a(format)) {
                        Adler32 adler32 = new Adler32();
                        adler32.reset();
                        adler32.update(format.getBytes());
                        j = adler32.getValue();
                    } else {
                        j = 0;
                    }
                    cxVar.a(j);
                    return cxVar;
                }
            }
        }
        return null;
    }
}
