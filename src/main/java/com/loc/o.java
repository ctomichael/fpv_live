package com.loc;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.text.TextUtils;
import com.amap.api.location.APSServiceBase;
import com.loc.n;

/* compiled from: ApsServiceCore */
public final class o implements APSServiceBase {
    n a = null;
    Context b = null;
    Messenger c = null;

    public o(Context context) {
        this.b = context.getApplicationContext();
        this.a = new n(this.b);
    }

    public final IBinder onBind(Intent intent) {
        n nVar = this.a;
        String stringExtra = intent.getStringExtra("a");
        if (!TextUtils.isEmpty(stringExtra)) {
            v.a(nVar.e, stringExtra);
        }
        nVar.a = intent.getStringExtra("b");
        u.a(nVar.a);
        String stringExtra2 = intent.getStringExtra("d");
        if (!TextUtils.isEmpty(stringExtra2)) {
            x.a(stringExtra2);
        }
        er.a = intent.getBooleanExtra("f", true);
        n nVar2 = this.a;
        if ("true".equals(intent.getStringExtra("as")) && nVar2.d != null) {
            nVar2.d.sendEmptyMessageDelayed(9, 100);
        }
        this.c = new Messenger(this.a.d);
        return this.c.getBinder();
    }

    public final void onCreate() {
        try {
            n.c();
            this.a.j = fa.c();
            this.a.k = fa.b();
            n nVar = this.a;
            nVar.i = new ey();
            nVar.b = new n.b("amapLocCoreThread");
            nVar.b.setPriority(5);
            nVar.b.start();
            nVar.d = new n.a(nVar.b.getLooper());
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "onCreate");
        }
    }

    public final void onDestroy() {
        try {
            if (this.a != null) {
                this.a.d.sendEmptyMessage(11);
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "onDestroy");
        }
    }

    public final int onStartCommand(Intent intent, int i, int i2) {
        return 0;
    }
}
