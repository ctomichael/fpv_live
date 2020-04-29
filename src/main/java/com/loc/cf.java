package com.loc;

/* compiled from: UpdateStrategy */
public abstract class cf {
    cf a;

    public cf() {
    }

    public cf(cf cfVar) {
        this.a = cfVar;
    }

    public void a(int i) {
        if (this.a != null) {
            this.a.a(i);
        }
    }

    public void a(boolean z) {
        if (this.a != null) {
            this.a.a(z);
        }
    }

    /* access modifiers changed from: protected */
    public abstract boolean a();

    public int b() {
        return Math.min(Integer.MAX_VALUE, this.a != null ? this.a.b() : Integer.MAX_VALUE);
    }

    public final boolean c() {
        if (!(this.a != null ? this.a.c() : true)) {
            return false;
        }
        return a();
    }
}
