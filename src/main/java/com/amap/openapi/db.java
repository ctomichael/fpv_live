package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.GnssStatus;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import com.amap.location.common.log.ALLog;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiresApi(api = 24)
/* compiled from: GnssStatusManager */
public class db extends GnssStatus.Callback {
    /* access modifiers changed from: private */
    public final List<a> a = new CopyOnWriteArrayList();
    /* access modifiers changed from: private */
    public cz b;
    private Context c;
    private b d = new b(this);

    /* compiled from: GnssStatusManager */
    private static class a {
        private Handler a;

        /* access modifiers changed from: package-private */
        public void a(int i, Object obj) {
            Message obtainMessage = this.a.obtainMessage();
            obtainMessage.what = i;
            obtainMessage.obj = obj;
            obtainMessage.sendToTarget();
        }
    }

    /* compiled from: GnssStatusManager */
    private class b extends BroadcastReceiver {
        private GnssStatus.Callback b;

        public b(GnssStatus.Callback callback) {
            this.b = callback;
        }

        public void onReceive(Context context, Intent intent) {
            if (cr.a(context).a("gps")) {
                synchronized (db.this.a) {
                    if (db.this.a.size() > 0) {
                        try {
                            db.this.b.b(this.b);
                            db.this.b.a(this.b);
                        } catch (SecurityException e) {
                            ALLog.trace("@_24_5_@", "卫星老接口权限异常", e);
                        }
                    }
                }
                return;
            }
            return;
        }
    }

    public db(cz czVar, Context context) {
        this.b = czVar;
        this.c = context;
    }

    public void onFirstFix(int i) {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a(3, Integer.valueOf(i));
            }
        }
    }

    public void onSatelliteStatusChanged(GnssStatus gnssStatus) {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a(4, gnssStatus);
            }
        }
    }

    public void onStarted() {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a(1, null);
            }
        }
    }

    public void onStopped() {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a(2, null);
            }
        }
    }
}
