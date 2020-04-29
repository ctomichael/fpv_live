package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: UpTunnelManager */
public class du {
    /* access modifiers changed from: private */
    public HandlerThread a;
    /* access modifiers changed from: private */
    public Looper b;
    /* access modifiers changed from: private */
    public b c;
    /* access modifiers changed from: private */
    public ds d;
    /* access modifiers changed from: private */
    public dt e;
    /* access modifiers changed from: private */
    public a f;
    /* access modifiers changed from: private */
    public final Object g = new byte[0];
    /* access modifiers changed from: private */
    public ArrayList<Message> h = new ArrayList<>();

    /* compiled from: UpTunnelManager */
    private final class a extends BroadcastReceiver {
        private a() {
        }

        public final void onReceive(Context context, Intent intent) {
            if (context != null && intent != null) {
                String action = intent.getAction();
                char c = 65535;
                switch (action.hashCode()) {
                    case -1172645946:
                        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                            c = 0;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        if (du.this.c != null) {
                            du.this.c.sendEmptyMessage(11);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* compiled from: UpTunnelManager */
    class b extends Handler {
        private boolean b;

        b(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (!this.b) {
                if (message.what == 10) {
                    du.this.d.a(message);
                } else if (message.what == 11) {
                    du.this.d.a();
                } else if (message.what == 12) {
                    du.this.d.a((dm) message.obj);
                } else if (message.what == 13) {
                    this.b = true;
                    removeCallbacksAndMessages(null);
                    du.this.d.b();
                    try {
                        du.this.e.a().unregisterReceiver(du.this.f);
                    } catch (Exception e) {
                    }
                    post(new Runnable() {
                        /* class com.amap.openapi.du.b.AnonymousClass1 */

                        public void run() {
                            try {
                                b.this.getLooper().quit();
                            } catch (Throwable th) {
                            }
                        }
                    });
                }
            }
        }
    }

    public du(@NonNull final Context context, @NonNull final dk dkVar) {
        this.a = new HandlerThread("UpTunnelWorkThread") {
            /* class com.amap.openapi.du.AnonymousClass1 */

            /* access modifiers changed from: protected */
            public void onLooperPrepared() {
                synchronized (du.this.g) {
                    dt unused = du.this.e = new dt(context);
                    Looper unused2 = du.this.b = du.this.a.getLooper();
                    b unused3 = du.this.c = new b(du.this.b);
                    ds unused4 = du.this.d = new ds();
                    du.this.d.a(du.this.e, dkVar, du.this.b);
                    du.this.b();
                    Iterator it2 = du.this.h.iterator();
                    while (it2.hasNext()) {
                        du.this.c.sendMessage((Message) it2.next());
                    }
                    du.this.h.clear();
                }
            }
        };
        this.a.start();
    }

    private void a(int i, int i2, int i3, Object obj) {
        if (this.c != null) {
            this.c.obtainMessage(i, i2, i3, obj).sendToTarget();
            return;
        }
        synchronized (this.g) {
            if (this.c != null) {
                this.c.obtainMessage(i, i2, i3, obj).sendToTarget();
            } else {
                this.h.add(Message.obtain(null, i, i2, i3, obj));
            }
        }
    }

    /* access modifiers changed from: private */
    public void b() {
        this.f = new a();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        try {
            this.e.a().registerReceiver(this.f, intentFilter);
        } catch (Exception e2) {
        }
    }

    public void a() {
        a(13, 0, 0, null);
    }

    public void a(int i) {
        a(10, 1, i, null);
    }

    public void a(int i, byte[] bArr) {
        a(10, 2, i, bArr);
    }
}
