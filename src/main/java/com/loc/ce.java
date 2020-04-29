package com.loc;

import android.content.Context;
import android.text.TextUtils;

/* compiled from: TimeUpdateStrategy */
public final class ce extends cf {
    private int b;
    private long c;
    private String d;
    private Context e;

    public ce(Context context, int i, String str, cf cfVar) {
        super(cfVar);
        this.b = i;
        this.d = str;
        this.e = context;
    }

    public final void a(boolean z) {
        super.a(z);
        if (z) {
            String str = this.d;
            long currentTimeMillis = System.currentTimeMillis();
            this.c = currentTimeMillis;
            ao.a(this.e, str, String.valueOf(currentTimeMillis));
        }
    }

    /* access modifiers changed from: protected */
    public final boolean a() {
        long j = 0;
        if (this.c == 0) {
            String a = ao.a(this.e, this.d);
            if (!TextUtils.isEmpty(a)) {
                j = Long.parseLong(a);
            }
            this.c = j;
        }
        return System.currentTimeMillis() - this.c >= ((long) this.b);
    }
}
