package com.dji.analytics.d;

import android.os.Handler;
import android.os.Looper;
import com.dji.analytics.DJIA;
import com.dji.analytics.ReportConfig;

/* compiled from: ReportManager */
public class c {
    /* access modifiers changed from: private */
    public static ReportConfig c = null;
    private int a;
    /* access modifiers changed from: private */
    public Handler b;
    /* access modifiers changed from: private */
    public final Object d;

    /* compiled from: ReportManager */
    private static final class b {
        /* access modifiers changed from: private */
        public static final c a = new c();
    }

    private c() {
        this.a = 0;
        this.d = new Object();
    }

    public static c a() {
        return b.a;
    }

    public void a(ReportConfig reportConfig) {
        c = reportConfig;
        com.dji.analytics.b.a.a().b();
        this.a = DJIA.getConfig().getDelayMillSeconds();
        new a(0).start();
    }

    public void a(com.dji.analytics.a.a aVar) {
        com.dji.analytics.b.a.a().a(aVar);
        b();
    }

    /* access modifiers changed from: package-private */
    public void b() {
        if (this.b == null) {
            try {
                synchronized (this.d) {
                    this.d.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (c == null) {
            if (DJIA.DEV_FLAG) {
                DJIA.log.a(DJIA.LOG_TAG, "mConfig is null before distribute");
            }
        } else if (!a.a().b() && DJIA.getIsEnableReport()) {
            a.a().c();
            if (DJIA.DEV_FLAG) {
                DJIA.log.a(DJIA.LOG_TAG, "BaseInfo is not send.");
            }
        } else if (!this.b.hasMessages(1)) {
            this.b.sendEmptyMessageDelayed(1, (long) this.a);
        }
    }

    /* compiled from: ReportManager */
    private class a extends Thread {
        int a = 0;

        a(int i) {
            this.a = i;
        }

        public void run() {
            Looper.prepare();
            Handler unused = c.this.b = new com.dji.analytics.e.c(this.a, c.c).a;
            synchronized (c.this.d) {
                c.this.d.notifyAll();
            }
            c.a().b();
            Looper.loop();
        }
    }
}
