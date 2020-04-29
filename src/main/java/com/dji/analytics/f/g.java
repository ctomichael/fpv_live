package com.dji.analytics.f;

import android.content.Context;
import android.content.SharedPreferences;
import com.dji.analytics.DJIA;

/* compiled from: SharedPrefUtil */
public class g {
    private static g a;
    private final SharedPreferences b;

    private g(Context context) {
        this.b = context.getSharedPreferences("com.dji.analytics.sharedpreinfo", 0);
    }

    public static synchronized void a(Context context) {
        synchronized (g.class) {
            if (a == null) {
                a = new g(context);
                if (DJIA.DEV_FLAG) {
                    DJIA.log.a(DJIA.LOG_TAG, g.class.getSimpleName() + " init success.");
                }
            }
        }
    }

    public static synchronized g a() {
        g gVar;
        synchronized (g.class) {
            if (a == null) {
                throw new IllegalStateException(g.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
            }
            gVar = a;
        }
        return gVar;
    }

    public void a(String str, boolean z) {
        this.b.edit().putBoolean(str, z).apply();
    }

    public boolean a(String str) {
        return this.b.getBoolean(str, false);
    }

    public boolean b(String str, boolean z) {
        return this.b.getBoolean(str, z);
    }

    public void a(String str, String str2) {
        this.b.edit().putString(str, str2).apply();
    }

    public String b(String str) {
        return this.b.getString(str, null);
    }

    public void c(String str) {
        this.b.edit().remove(str).apply();
    }

    public void d(String str) {
        a("com.dji.analytics.uuid", str);
    }

    public String b() {
        if (this.b.contains("com.dji.analytics.uuid")) {
            a("com.dji.analytics.dji_yh_d", b("com.dji.analytics.uuid"));
            c("com.dji.analytics.uuid");
        }
        return b("com.dji.analytics.dji_yh_d");
    }

    public void a(boolean z) {
        a("com.dji.analytics.baseinfosentsign", z);
    }

    public boolean c() {
        return a("com.dji.analytics.baseinfosentsign");
    }

    public void b(boolean z) {
        a("com.dji.analytics.canreport", z);
    }

    public boolean d() {
        return b("com.dji.analytics.canreport", false);
    }
}
