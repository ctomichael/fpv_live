package com.dji.analytics.e;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import com.dji.analytics.DJIA;
import com.dji.analytics.ReportConfig;
import com.dji.analytics.a.a;
import com.dji.analytics.d.b;
import com.dji.analytics.f.d;
import com.dji.analytics.f.e;
import java.util.HashMap;

/* compiled from: SenderService */
public class c {
    @SuppressLint({"HandlerLeak"})
    public Handler a = new Handler() {
        /* class com.dji.analytics.e.c.AnonymousClass1 */

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                c.this.a((a) message.obj);
            } else if (i == 1) {
                c.this.a();
            }
        }
    };
    private int b;
    private ReportConfig c = null;
    private int d = 0;

    public c(int i, ReportConfig reportConfig) {
        this.b = i;
        this.c = reportConfig;
    }

    /* access modifiers changed from: private */
    public void a() {
        while (d.a(this.c.getContext()) && b.a(this.c)) {
            HashMap<String, a> d2 = com.dji.analytics.b.a.a().d();
            if (d2 == null || d2.size() <= 0) {
                com.dji.analytics.b.a.a().b();
                return;
            }
            if (DJIA.DEV_FLAG) {
                DJIA.log.a(DJIA.LOG_TAG, " SenderService handler id is " + this.b + " data len is:" + d2.size());
            }
            if (a(d2)) {
                com.dji.analytics.b.a.a().a(d2);
            }
        }
    }

    public void a(a aVar) {
        com.dji.analytics.b.a.a().a(aVar);
    }

    private boolean a(HashMap<String, a> hashMap) {
        if (this.c == null) {
            return false;
        }
        a aVar = new a(this.c.getEventReportUrl());
        aVar.a(ReportConfig.a.LOG_DATA);
        try {
            return aVar.a(e.a(hashMap), this.c);
        } catch (Exception e) {
            if (!DJIA.DEV_FLAG) {
                return false;
            }
            DJIA.log.a(DJIA.LOG_TAG, " SenderService exception = " + e.getClass());
            return false;
        }
    }
}
