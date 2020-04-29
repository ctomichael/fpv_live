package com.loc;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import dji.midware.Lifecycle;
import java.util.List;
import org.json.JSONObject;

/* compiled from: LastLocationManager */
public final class r {
    static ej b = null;
    static av e = null;
    static long g = 0;
    String a = null;
    ej c = null;
    ej d = null;
    long f = 0;
    boolean h = false;
    private Context i;

    public r(Context context) {
        this.i = context.getApplicationContext();
    }

    private void e() {
        if (b == null || fa.c() - g > 180000) {
            ej f2 = f();
            g = fa.c();
            if (f2 != null && fa.a(f2.a())) {
                b = f2;
            }
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.av.a(java.lang.String, java.lang.Class, boolean):java.util.List<T>
     arg types: [java.lang.String, java.lang.Class, int]
     candidates:
      com.loc.av.a(android.database.Cursor, java.lang.Class, com.loc.aw):T
      com.loc.av.a(java.lang.String, java.lang.Class, boolean):java.util.List<T> */
    private ej f() {
        ej ejVar;
        String str;
        byte[] d2;
        byte[] d3;
        String str2 = null;
        if (this.i == null) {
            return null;
        }
        a();
        try {
            if (e == null) {
                return null;
            }
            List a2 = e.a("_id=1", ej.class, false);
            if (a2.size() > 0) {
                ejVar = (ej) a2.get(0);
                try {
                    byte[] b2 = y.b(ejVar.c());
                    str = (b2 == null || b2.length <= 0 || (d3 = eh.d(b2, this.a)) == null || d3.length <= 0) ? null : new String(d3, "UTF-8");
                    byte[] b3 = y.b(ejVar.b());
                    if (b3 != null && b3.length > 0 && (d2 = eh.d(b3, this.a)) != null && d2.length > 0) {
                        str2 = new String(d2, "UTF-8");
                    }
                    ejVar.a(str2);
                } catch (Throwable th) {
                    th = th;
                    es.a(th, "LastLocationManager", "readLastFix");
                    return ejVar;
                }
            } else {
                ejVar = null;
                str = null;
            }
            if (TextUtils.isEmpty(str)) {
                return ejVar;
            }
            AMapLocation aMapLocation = new AMapLocation("");
            es.a(aMapLocation, new JSONObject(str));
            if (!fa.b(aMapLocation)) {
                return ejVar;
            }
            ejVar.a(aMapLocation);
            return ejVar;
        } catch (Throwable th2) {
            th = th2;
            ejVar = null;
            es.a(th, "LastLocationManager", "readLastFix");
            return ejVar;
        }
    }

    public final AMapLocation a(AMapLocation aMapLocation, String str, long j) {
        Throwable th;
        boolean z = true;
        if (aMapLocation == null || aMapLocation.getErrorCode() == 0 || aMapLocation.getLocationType() == 1 || aMapLocation.getErrorCode() == 7) {
            return aMapLocation;
        }
        try {
            e();
            if (b == null || b.a() == null) {
                return aMapLocation;
            }
            if (TextUtils.isEmpty(str)) {
                long c2 = fa.c() - b.d();
                if (c2 < 0 || c2 > j) {
                    z = false;
                }
                aMapLocation.setTrustedLevel(3);
            } else {
                if (!fa.a(b.b(), str)) {
                    z = false;
                }
                aMapLocation.setTrustedLevel(2);
            }
            if (!z) {
                return aMapLocation;
            }
            AMapLocation a2 = b.a();
            try {
                a2.setLocationType(9);
                a2.setFixLastLocation(true);
                a2.setLocationDetail(aMapLocation.getLocationDetail());
                return a2;
            } catch (Throwable th2) {
                th = th2;
                aMapLocation = a2;
                es.a(th, "LastLocationManager", "fixLastLocation");
                return aMapLocation;
            }
        } catch (Throwable th3) {
            th = th3;
            es.a(th, "LastLocationManager", "fixLastLocation");
            return aMapLocation;
        }
    }

    public final void a() {
        if (!this.h) {
            try {
                if (this.a == null) {
                    this.a = eh.a("MD5", x.u(this.i));
                }
                if (e == null) {
                    e = new av(this.i, av.a((Class<? extends au>) ek.class), fa.j());
                }
            } catch (Throwable th) {
                es.a(th, "LastLocationManager", "<init>:DBOperation");
            }
            this.h = true;
        }
    }

    public final boolean a(AMapLocation aMapLocation, String str) {
        if (this.i == null || aMapLocation == null || !fa.a(aMapLocation) || aMapLocation.getLocationType() == 2 || aMapLocation.isMock() || aMapLocation.isFixLastLocation()) {
            return false;
        }
        ej ejVar = new ej();
        ejVar.a(aMapLocation);
        if (aMapLocation.getLocationType() == 1) {
            ejVar.a((String) null);
        } else {
            ejVar.a(str);
        }
        try {
            b = ejVar;
            g = fa.c();
            this.c = ejVar;
            return (this.d == null || fa.a(this.d.a(), ejVar.a()) > 500.0f) && fa.c() - this.f > 30000;
        } catch (Throwable th) {
            es.a(th, "LastLocationManager", "setLastFix");
            return false;
        }
    }

    public final AMapLocation b() {
        e();
        if (b != null && fa.a(b.a())) {
            return b.a();
        }
        return null;
    }

    public final void c() {
        try {
            d();
            this.f = 0;
            this.h = false;
            this.c = null;
            this.d = null;
        } catch (Throwable th) {
            es.a(th, "LastLocationManager", Lifecycle.STATUS_DESTROY);
        }
    }

    public final void d() {
        String str;
        String str2;
        try {
            a();
            if (this.c != null && fa.a(this.c.a()) && e != null && this.c != this.d && this.c.d() == 0) {
                String str3 = this.c.a().toStr();
                String b2 = this.c.b();
                this.d = this.c;
                if (!TextUtils.isEmpty(str3)) {
                    str2 = y.b(eh.c(str3.getBytes("UTF-8"), this.a));
                    str = !TextUtils.isEmpty(b2) ? y.b(eh.c(b2.getBytes("UTF-8"), this.a)) : null;
                } else {
                    str = null;
                    str2 = null;
                }
                if (!TextUtils.isEmpty(str2)) {
                    ej ejVar = new ej();
                    ejVar.b(str2);
                    ejVar.a(fa.c());
                    ejVar.a(str);
                    e.a(ejVar, "_id=1");
                    this.f = fa.c();
                    if (b != null) {
                        b.a(fa.c());
                    }
                }
            }
        } catch (Throwable th) {
            es.a(th, "LastLocationManager", "saveLastFix");
        }
    }
}
