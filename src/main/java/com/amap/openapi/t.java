package com.amap.openapi;

import android.content.Context;
import android.os.Looper;
import com.amap.openapi.bi;
import com.mapzen.android.lost.internal.FusionEngine;
import java.util.ArrayList;

/* compiled from: DataManager */
public class t {
    private static final String a = t.class.getSimpleName();
    private Looper b;
    private bi<s> c = new bi<>();
    private a d = new a();
    /* access modifiers changed from: private */
    public u e;

    /* compiled from: DataManager */
    private class a implements bi.b<s> {
        private a() {
        }

        public void a() {
        }

        public void a(ArrayList<s> arrayList) {
            if (arrayList != null && arrayList.size() != 0) {
                t.this.e.a(arrayList);
            }
        }

        public boolean a(long j) {
            return t.this.e.a(j);
        }

        public void b() {
            t.this.e.a();
        }

        public long c() {
            return 10240;
        }

        public long d() {
            return FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS;
        }
    }

    public t(Context context, Looper looper) {
        this.b = looper;
        this.e = new u(context);
    }

    public au a(boolean z, int i, long j) {
        return this.e.a(z, i, j);
    }

    public void a() {
        this.c.a(this.d, this.b);
    }

    public void a(int i, byte[] bArr) {
        this.c.a(new s(i, bArr));
    }

    public void a(au auVar) {
        this.e.a(auVar);
    }

    public void b() {
        try {
            this.c.a();
        } catch (Throwable th) {
        }
    }

    public int c() {
        return this.e.b();
    }

    public int d() {
        return this.e.c();
    }
}
