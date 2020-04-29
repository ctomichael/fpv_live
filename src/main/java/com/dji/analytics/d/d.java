package com.dji.analytics.d;

import com.dji.analytics.f.c;

/* compiled from: SessionManager */
public class d {
    private static String a = "";

    public static String a() {
        synchronized (a) {
            if (a.isEmpty()) {
                a = c.a();
            }
        }
        return a;
    }
}
