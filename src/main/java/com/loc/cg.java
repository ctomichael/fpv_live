package com.loc;

import android.content.Context;

/* compiled from: WiFiUplateStrategy */
public final class cg extends cf {
    private Context b;
    private boolean c = false;

    public cg(Context context) {
        this.b = context;
        this.c = false;
    }

    /* access modifiers changed from: protected */
    public final boolean a() {
        return x.q(this.b) == 1 || this.c;
    }
}
