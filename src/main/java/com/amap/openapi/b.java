package com.amap.openapi;

import android.content.Context;

/* compiled from: LocationCloudManager */
public class b {
    private static volatile b a;
    private c b = new c();
    private volatile boolean c = false;

    private b() {
    }

    public static b a() {
        if (a == null) {
            synchronized (b.class) {
                if (a == null) {
                    a = new b();
                }
            }
        }
        return a;
    }

    public synchronized void a(Context context, d dVar) {
        if (!this.c) {
            this.b.a(context, dVar);
            this.c = true;
        }
    }

    public void a(f fVar) {
        this.b.a(fVar);
    }

    public synchronized void b() {
        if (this.c) {
            this.b.a();
            this.c = false;
        }
    }

    public void b(f fVar) {
        this.b.b(fVar);
    }
}
