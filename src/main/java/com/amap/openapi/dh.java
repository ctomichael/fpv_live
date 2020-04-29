package com.amap.openapi;

import android.content.Context;
import android.os.Handler;
import com.amap.openapi.df;
import java.util.ArrayList;
import java.util.List;

/* compiled from: WifiScanListener */
public class dh implements df.a {
    private final List<a> a = new ArrayList();
    private di b;

    /* compiled from: WifiScanListener */
    private static class a {
        private Handler a;

        public void a() {
            this.a.sendEmptyMessage(0);
        }
    }

    public dh(Context context, di diVar) {
        this.b = diVar;
        this.b.a(context, this);
    }

    public void a() {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a();
            }
        }
    }
}
