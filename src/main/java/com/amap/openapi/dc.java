package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.os.Handler;
import android.os.Message;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: GpsStatusManager */
public class dc implements GpsStatus.Listener {
    /* access modifiers changed from: private */
    public final List<b> a = new CopyOnWriteArrayList();
    /* access modifiers changed from: private */
    public cz b;
    private Context c;
    private a d = new a(this);

    /* compiled from: GpsStatusManager */
    private class a extends BroadcastReceiver {
        private GpsStatus.Listener b;

        public a(GpsStatus.Listener listener) {
            this.b = listener;
        }

        public void onReceive(Context context, Intent intent) {
            if (cr.a(context).a("gps")) {
                synchronized (dc.this.a) {
                    if (dc.this.a.size() > 0) {
                        dc.this.b.b(this.b);
                        dc.this.b.a(this.b);
                    }
                }
            }
        }
    }

    /* compiled from: GpsStatusManager */
    private static class b {
        private Handler a;

        /* access modifiers changed from: package-private */
        public void a(int i) {
            Message obtainMessage = this.a.obtainMessage();
            obtainMessage.arg1 = i;
            obtainMessage.sendToTarget();
        }
    }

    public dc(cz czVar, Context context) {
        this.b = czVar;
        this.c = context;
    }

    public void onGpsStatusChanged(int i) {
        synchronized (this.a) {
            for (b bVar : this.a) {
                bVar.a(i);
            }
        }
    }
}
