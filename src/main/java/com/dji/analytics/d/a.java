package com.dji.analytics.d;

import android.content.Context;
import com.dji.analytics.DJIA;
import com.dji.analytics.ReportConfig;
import com.dji.analytics.f.d;
import com.dji.analytics.f.e;
import com.dji.analytics.f.g;
import java.util.HashMap;

/* compiled from: BaseInfoManager */
public class a {
    private static final Object e = new Object();
    private static C0023a f = null;
    private static final Object g = new Object();
    private Context a;
    private String b;
    private String c;
    private Boolean d;
    /* access modifiers changed from: private */
    public ReportConfig h;

    /* compiled from: BaseInfoManager */
    private static final class b {
        /* access modifiers changed from: private */
        public static final a a = new a();
    }

    public static a a() {
        return b.a;
    }

    private a() {
        this.c = null;
        this.d = null;
    }

    public void a(Context context, String str) {
        this.a = context;
        this.b = str;
        this.h = DJIA.getConfig();
    }

    /* access modifiers changed from: package-private */
    public boolean b() {
        boolean booleanValue;
        synchronized (e) {
            if (this.d == null) {
                this.d = Boolean.valueOf(g.a().c());
            }
            if (DJIA.DEV_FLAG) {
                DJIA.log.a(DJIA.LOG_TAG, "mIsBaseInfoSent is " + this.d);
            }
            booleanValue = this.d.booleanValue();
        }
        return booleanValue;
    }

    public void c() {
        if (d.a(this.h.getContext())) {
            synchronized (g) {
                f = new C0023a(this.h.getBaseInfoReportUrl(), e.a(new HashMap[]{f()}));
                f.start();
                if (DJIA.DEV_FLAG) {
                    DJIA.log.a(DJIA.LOG_TAG, "start sendBaseInfo");
                }
            }
        }
    }

    public String d() {
        if (this.a != null) {
            return com.dji.analytics.f.a.b(this.a);
        }
        return "";
    }

    public synchronized String e() {
        if (this.c == null) {
            this.c = g.a().b();
            if (this.c == null) {
                this.c = com.dji.analytics.f.a.a(this.a);
                g.a().d(this.c);
            }
        }
        return this.c;
    }

    private HashMap<String, String> f() {
        HashMap<String, String> hashMap = new HashMap<>(32);
        String e2 = e();
        if (DJIA.DEV_FLAG) {
            DJIA.log.a(DJIA.LOG_TAG, "djia uuid=<<<<<<" + e2 + ">>>>>>");
        }
        hashMap.put("uuid", e2);
        hashMap.put("mbrand", com.dji.analytics.f.a.a());
        hashMap.put("mmodel", com.dji.analytics.f.a.b());
        hashMap.put("msize", com.dji.analytics.f.a.c(this.a));
        hashMap.put("ostype", com.dji.analytics.f.a.c() + "");
        hashMap.put("osver", com.dji.analytics.f.a.d());
        hashMap.put("country", com.dji.analytics.f.a.f());
        hashMap.put("lang", com.dji.analytics.f.a.e());
        hashMap.put("channel", com.dji.analytics.f.a.g());
        hashMap.put("appver", a().d());
        hashMap.put("Token", this.b);
        return hashMap;
    }

    /* access modifiers changed from: private */
    public void a(boolean z) {
        synchronized (g) {
            f = null;
            if (z) {
                this.d = true;
                g.a().a(true);
            }
        }
    }

    /* renamed from: com.dji.analytics.d.a$a  reason: collision with other inner class name */
    /* compiled from: BaseInfoManager */
    private class C0023a extends Thread {
        private String b;
        private byte[] c;

        C0023a(String str, byte[] bArr) {
            this.b = str;
            this.c = bArr;
        }

        public void run() {
            com.dji.analytics.e.a aVar = new com.dji.analytics.e.a(this.b);
            try {
                aVar.a(ReportConfig.a.INIT_DATA);
                boolean a2 = aVar.a(this.c, a.this.h);
                if (DJIA.DEV_FLAG) {
                    DJIA.log.a(DJIA.LOG_TAG, "Thread SendBaseInfo finish result is " + a2);
                }
                if (!a.this.b()) {
                    a.this.a(a2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
