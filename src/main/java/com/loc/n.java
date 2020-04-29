package com.loc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import dji.publics.protocol.ResponseBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/* compiled from: ApsManager */
public final class n {
    static boolean g = false;
    /* access modifiers changed from: private */
    public r A = null;
    private boolean B = true;
    private String C = "";
    private final int D = 5000;
    private String E = "jsonp1";
    String a = null;
    b b = null;
    AMapLocation c = null;
    a d = null;
    Context e = null;
    cs f = null;
    HashMap<Messenger, Long> h = new HashMap<>();
    ey i = null;
    long j = 0;
    long k = 0;
    String l = null;
    AMapLocationClientOption m = null;
    AMapLocationClientOption n = new AMapLocationClientOption();
    ServerSocket o = null;
    boolean p = false;
    Socket q = null;
    boolean r = false;
    c s = null;
    private boolean t = false;
    private boolean u = false;
    private long v = 0;
    private long w = 0;
    private AMapLocationServer x = null;
    private long y = 0;
    private int z = 0;

    /* compiled from: ApsManager */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: Removed duplicated region for block: B:29:0x0069 A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0078 A[SYNTHETIC, Splitter:B:32:0x0078] */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x0083 A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x008e A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:47:0x00bd A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x00d1 A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:52:0x00dc A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x00e7 A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x00f3 A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:55:0x00ff A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:56:0x010b A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* JADX WARNING: Removed duplicated region for block: B:57:0x0112 A[Catch:{ Throwable -> 0x00b2, Throwable -> 0x006d }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void handleMessage(android.os.Message r7) {
            /*
                r6 = this;
                r3 = 1
                r2 = 0
                android.os.Bundle r1 = r7.getData()     // Catch:{ Throwable -> 0x0057 }
                android.os.Messenger r2 = r7.replyTo     // Catch:{ Throwable -> 0x0119 }
                if (r1 == 0) goto L_0x0064
                boolean r0 = r1.isEmpty()     // Catch:{ Throwable -> 0x011d }
                if (r0 != 0) goto L_0x0064
                java.lang.String r0 = "c"
                java.lang.String r0 = r1.getString(r0)     // Catch:{ Throwable -> 0x011d }
                com.loc.n r4 = com.loc.n.this     // Catch:{ Throwable -> 0x011d }
                java.lang.String r5 = r4.l     // Catch:{ Throwable -> 0x011d }
                boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x011d }
                if (r5 == 0) goto L_0x0029
                android.content.Context r5 = r4.e     // Catch:{ Throwable -> 0x011d }
                java.lang.String r5 = com.loc.es.b(r5)     // Catch:{ Throwable -> 0x011d }
                r4.l = r5     // Catch:{ Throwable -> 0x011d }
            L_0x0029:
                boolean r5 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x011d }
                if (r5 != 0) goto L_0x0037
                java.lang.String r4 = r4.l     // Catch:{ Throwable -> 0x011d }
                boolean r0 = r0.equals(r4)     // Catch:{ Throwable -> 0x011d }
                if (r0 != 0) goto L_0x0055
            L_0x0037:
                r0 = 0
            L_0x0038:
                if (r0 != 0) goto L_0x0064
                int r0 = r7.what     // Catch:{ Throwable -> 0x011d }
                if (r0 != r3) goto L_0x0054
                r0 = 0
                r3 = 2102(0x836, float:2.946E-42)
                com.loc.ey.a(r0, r3)     // Catch:{ Throwable -> 0x011d }
                java.lang.String r0 = "invalid handlder scode!!!#1002"
                com.autonavi.aps.amapapi.model.AMapLocationServer r0 = com.loc.n.a(10, r0)     // Catch:{ Throwable -> 0x011d }
                com.loc.n r3 = com.loc.n.this     // Catch:{ Throwable -> 0x011d }
                java.lang.String r4 = r0.l()     // Catch:{ Throwable -> 0x011d }
                r3.a(r2, r0, r4, 0)     // Catch:{ Throwable -> 0x011d }
            L_0x0054:
                return
            L_0x0055:
                r0 = r3
                goto L_0x0038
            L_0x0057:
                r0 = move-exception
                r1 = r2
                r3 = r2
            L_0x005a:
                java.lang.String r2 = "ApsServiceCore"
                java.lang.String r4 = "ActionHandler handlerMessage"
                com.loc.es.a(r0, r2, r4)     // Catch:{ Throwable -> 0x006d }
                r2 = r3
            L_0x0064:
                int r0 = r7.what     // Catch:{ Throwable -> 0x006d }
                switch(r0) {
                    case 0: goto L_0x0078;
                    case 1: goto L_0x0083;
                    case 2: goto L_0x008e;
                    case 3: goto L_0x00bd;
                    case 4: goto L_0x00d1;
                    case 5: goto L_0x00dc;
                    case 6: goto L_0x0069;
                    case 7: goto L_0x00e7;
                    case 8: goto L_0x0069;
                    case 9: goto L_0x00f3;
                    case 10: goto L_0x00ff;
                    case 11: goto L_0x010b;
                    case 12: goto L_0x0112;
                    default: goto L_0x0069;
                }     // Catch:{ Throwable -> 0x006d }
            L_0x0069:
                super.handleMessage(r7)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0054
            L_0x006d:
                r0 = move-exception
                java.lang.String r1 = "actionHandler"
                java.lang.String r2 = "handleMessage"
                com.loc.es.a(r0, r1, r2)
                goto L_0x0054
            L_0x0078:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.a(r1)     // Catch:{ Throwable -> 0x006d }
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                com.loc.n.a(r0, r2, r1)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x0083:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.a(r1)     // Catch:{ Throwable -> 0x006d }
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                com.loc.n.b(r0, r2, r1)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x008e:
                if (r1 == 0) goto L_0x0054
                boolean r0 = r1.isEmpty()     // Catch:{ Throwable -> 0x006d }
                if (r0 != 0) goto L_0x0054
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r1 = 0
                r0.a(r1)     // Catch:{ Throwable -> 0x006d }
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                boolean r1 = r0.r     // Catch:{ Throwable -> 0x00b2 }
                if (r1 != 0) goto L_0x0069
                com.loc.n$c r1 = new com.loc.n$c     // Catch:{ Throwable -> 0x00b2 }
                r1.<init>()     // Catch:{ Throwable -> 0x00b2 }
                r0.s = r1     // Catch:{ Throwable -> 0x00b2 }
                com.loc.n$c r1 = r0.s     // Catch:{ Throwable -> 0x00b2 }
                r1.start()     // Catch:{ Throwable -> 0x00b2 }
                r1 = 1
                r0.r = r1     // Catch:{ Throwable -> 0x00b2 }
                goto L_0x0069
            L_0x00b2:
                r0 = move-exception
                java.lang.String r1 = "ApsServiceCore"
                java.lang.String r2 = "startSocket"
                com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x00bd:
                if (r1 == 0) goto L_0x0054
                boolean r0 = r1.isEmpty()     // Catch:{ Throwable -> 0x006d }
                if (r0 != 0) goto L_0x0054
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r1 = 0
                r0.a(r1)     // Catch:{ Throwable -> 0x006d }
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.a()     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x00d1:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.a(r1)     // Catch:{ Throwable -> 0x006d }
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                com.loc.n.a(r0)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x00dc:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.a(r1)     // Catch:{ Throwable -> 0x006d }
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                com.loc.n.b(r0)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x00e7:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.a(r1)     // Catch:{ Throwable -> 0x006d }
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                com.loc.n.c(r0)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x00f3:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.a(r1)     // Catch:{ Throwable -> 0x006d }
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                com.loc.n.a(r0, r2)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x00ff:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.a(r1)     // Catch:{ Throwable -> 0x006d }
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.a(r2, r1)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x010b:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.b()     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x0112:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x006d }
                r0.h.remove(r2)     // Catch:{ Throwable -> 0x006d }
                goto L_0x0069
            L_0x0119:
                r0 = move-exception
                r3 = r2
                goto L_0x005a
            L_0x011d:
                r0 = move-exception
                r3 = r2
                goto L_0x005a
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.n.a.handleMessage(android.os.Message):void");
        }
    }

    /* compiled from: ApsManager */
    class b extends HandlerThread {
        public b(String str) {
            super(str);
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void onLooperPrepared() {
            /*
                r3 = this;
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x001b }
                com.loc.r r1 = new com.loc.r     // Catch:{ Throwable -> 0x001b }
                com.loc.n r2 = com.loc.n.this     // Catch:{ Throwable -> 0x001b }
                android.content.Context r2 = r2.e     // Catch:{ Throwable -> 0x001b }
                r1.<init>(r2)     // Catch:{ Throwable -> 0x001b }
                com.loc.r unused = r0.A = r1     // Catch:{ Throwable -> 0x001b }
            L_0x000e:
                com.loc.n r0 = com.loc.n.this     // Catch:{ Throwable -> 0x0026 }
                com.loc.cs r1 = new com.loc.cs     // Catch:{ Throwable -> 0x0026 }
                r1.<init>()     // Catch:{ Throwable -> 0x0026 }
                r0.f = r1     // Catch:{ Throwable -> 0x0026 }
                super.onLooperPrepared()     // Catch:{ Throwable -> 0x0026 }
            L_0x001a:
                return
            L_0x001b:
                r0 = move-exception
                java.lang.String r1 = "APSManager$ActionThread"
                java.lang.String r2 = "init 2"
                com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0026 }
                goto L_0x000e
            L_0x0026:
                r0 = move-exception
                java.lang.String r1 = "APSManager$ActionThread"
                java.lang.String r2 = "onLooperPrepared"
                com.loc.es.a(r0, r1, r2)
                goto L_0x001a
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.n.b.onLooperPrepared():void");
        }

        public final void run() {
            try {
                super.run();
            } catch (Throwable th) {
                es.a(th, "APSManager$ActionThread", "run");
            }
        }
    }

    /* compiled from: ApsManager */
    class c extends Thread {
        c() {
        }

        public final void run() {
            try {
                if (!n.this.p) {
                    n.this.p = true;
                    n.this.o = new ServerSocket(43689);
                }
                while (n.this.p && n.this.o != null) {
                    n.this.q = n.this.o.accept();
                    n.a(n.this, n.this.q);
                }
            } catch (Throwable th) {
                es.a(th, "ApsServiceCore", "run");
            }
            super.run();
        }
    }

    public n(Context context) {
        this.e = context;
    }

    /* access modifiers changed from: private */
    public static AMapLocationServer a(int i2, String str) {
        try {
            AMapLocationServer aMapLocationServer = new AMapLocationServer("");
            aMapLocationServer.setErrorCode(i2);
            aMapLocationServer.setLocationDetail(str);
            return aMapLocationServer;
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "newInstanceAMapLoc");
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void a(Bundle bundle) {
        try {
            if (!this.t) {
                es.a(this.e);
                if (bundle != null) {
                    this.n = es.a(bundle.getBundle("optBundle"));
                }
                this.f.a(this.e);
                this.f.a();
                a(this.n);
                this.f.b();
                this.t = true;
                this.B = true;
                this.C = "";
            }
        } catch (Throwable th) {
            this.B = false;
            this.C = th.getMessage();
            es.a(th, "ApsServiceCore", "init");
        }
    }

    private void a(Messenger messenger) {
        try {
            cs.b(this.e);
            if (er.k()) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("ngpsAble", er.m());
                a(messenger, 7, bundle);
                er.l();
            }
            if (er.t()) {
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("installMockApp", true);
                a(messenger, 9, bundle2);
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "initAuth");
        }
    }

    private static void a(Messenger messenger, int i2, Bundle bundle) {
        if (messenger != null) {
            try {
                Message obtain = Message.obtain();
                obtain.setData(bundle);
                obtain.what = i2;
                messenger.send(obtain);
            } catch (Throwable th) {
                es.a(th, "ApsServiceCore", "sendMessage");
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(Messenger messenger, AMapLocation aMapLocation, String str, long j2) {
        Bundle bundle = new Bundle();
        bundle.setClassLoader(AMapLocation.class.getClassLoader());
        bundle.putParcelable("loc", aMapLocation);
        bundle.putString("nb", str);
        bundle.putLong("netUseTime", j2);
        this.h.put(messenger, Long.valueOf(fa.c()));
        a(messenger, 1, bundle);
    }

    private void a(AMapLocationClientOption aMapLocationClientOption) {
        try {
            if (this.f != null) {
                this.f.a(aMapLocationClientOption);
            }
            if (aMapLocationClientOption != null) {
                g = aMapLocationClientOption.isKillProcess();
                if (!(this.m == null || (aMapLocationClientOption.isOffset() == this.m.isOffset() && aMapLocationClientOption.isNeedAddress() == this.m.isNeedAddress() && aMapLocationClientOption.isLocationCacheEnable() == this.m.isLocationCacheEnable() && this.m.getGeoLanguage() == aMapLocationClientOption.getGeoLanguage()))) {
                    this.w = 0;
                    this.c = null;
                }
                this.m = aMapLocationClientOption;
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "setExtra");
        }
    }

    static /* synthetic */ void a(n nVar) {
        try {
            if (nVar.z < er.b()) {
                nVar.z++;
                nVar.f.e();
                nVar.d.sendEmptyMessageDelayed(4, 2000);
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "doGpsFusion");
        }
    }

    static /* synthetic */ void a(n nVar, Messenger messenger) {
        try {
            nVar.a(messenger);
            er.e(nVar.e);
            try {
                nVar.f.h();
            } catch (Throwable th) {
            }
        } catch (Throwable th2) {
            es.a(th2, "ApsServiceCore", "doCallOtherSer");
        }
    }

    static /* synthetic */ void a(n nVar, Messenger messenger, Bundle bundle) {
        if (bundle != null) {
            try {
                if (!bundle.isEmpty() && !nVar.u) {
                    nVar.u = true;
                    nVar.a(messenger);
                    er.e(nVar.e);
                    try {
                        nVar.f.g();
                    } catch (Throwable th) {
                    }
                    nVar.d();
                    if (er.a(nVar.y) && "1".equals(bundle.getString("isCacheLoc"))) {
                        nVar.y = fa.c();
                        nVar.f.e();
                    }
                    nVar.f();
                }
            } catch (Throwable th2) {
                es.a(th2, "ApsServiceCore", "doInitAuth");
            }
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:95:0x0131=Splitter:B:95:0x0131, B:78:0x0102=Splitter:B:78:0x0102, B:33:0x0063=Splitter:B:33:0x0063, B:67:0x00e8=Splitter:B:67:0x00e8} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void a(com.loc.n r6, java.net.Socket r7) {
        /*
            r2 = 0
            if (r7 != 0) goto L_0x0004
        L_0x0003:
            return
        L_0x0004:
            int r4 = com.loc.es.f     // Catch:{ Throwable -> 0x0035 }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            java.io.InputStreamReader r0 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            java.io.InputStream r3 = r7.getInputStream()     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            java.lang.String r5 = "UTF-8"
            r0.<init>(r3, r5)     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            r1.<init>(r0)     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            r6.a(r1)     // Catch:{ Throwable -> 0x0143 }
            java.lang.String r0 = r6.e()     // Catch:{ Throwable -> 0x0143 }
            com.loc.es.f = r4     // Catch:{ Throwable -> 0x0035 }
            r6.b(r0)     // Catch:{ Throwable -> 0x0040 }
            r1.close()     // Catch:{ Throwable -> 0x002a }
            r7.close()     // Catch:{ Throwable -> 0x002a }
            goto L_0x0003
        L_0x002a:
            r0 = move-exception
            java.lang.String r1 = "ApsServiceCore"
            java.lang.String r2 = "invokeSocketLocation part3"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0003
        L_0x0035:
            r0 = move-exception
            java.lang.String r1 = "ApsServiceCore"
            java.lang.String r2 = "invokeSocketLocation part4"
            com.loc.es.a(r0, r1, r2)
            goto L_0x0003
        L_0x0040:
            r0 = move-exception
            java.lang.String r2 = "ApsServiceCore"
            java.lang.String r3 = "invokeSocketLocation part2"
            com.loc.es.a(r0, r2, r3)     // Catch:{ all -> 0x005c }
            r1.close()     // Catch:{ Throwable -> 0x0051 }
            r7.close()     // Catch:{ Throwable -> 0x0051 }
            goto L_0x0003
        L_0x0051:
            r0 = move-exception
            java.lang.String r1 = "ApsServiceCore"
            java.lang.String r2 = "invokeSocketLocation part3"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0003
        L_0x005c:
            r0 = move-exception
            r1.close()     // Catch:{ Throwable -> 0x0064 }
            r7.close()     // Catch:{ Throwable -> 0x0064 }
        L_0x0063:
            throw r0     // Catch:{ Throwable -> 0x0035 }
        L_0x0064:
            r1 = move-exception
            java.lang.String r2 = "ApsServiceCore"
            java.lang.String r3 = "invokeSocketLocation part3"
            com.loc.es.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0063
        L_0x006f:
            r0 = move-exception
            r1 = r2
        L_0x0071:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x013d }
            r3.<init>()     // Catch:{ all -> 0x013d }
            java.lang.String r5 = r6.E     // Catch:{ all -> 0x013d }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ all -> 0x013d }
            java.lang.String r5 = "&&"
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ all -> 0x013d }
            java.lang.String r5 = r6.E     // Catch:{ all -> 0x013d }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ all -> 0x013d }
            java.lang.String r5 = "({'package':'"
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ all -> 0x013d }
            java.lang.String r5 = r6.a     // Catch:{ all -> 0x013d }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ all -> 0x013d }
            java.lang.String r5 = "','error_code':1,'error':'params error'})"
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ all -> 0x013d }
            java.lang.String r2 = r3.toString()     // Catch:{ all -> 0x013d }
            java.lang.String r3 = "ApsServiceCore"
            java.lang.String r5 = "invokeSocketLocation"
            com.loc.es.a(r0, r3, r5)     // Catch:{ all -> 0x0140 }
            com.loc.es.f = r4     // Catch:{ Throwable -> 0x0035 }
            r6.b(r2)     // Catch:{ Throwable -> 0x00c3 }
            r1.close()     // Catch:{ Throwable -> 0x00b7 }
            r7.close()     // Catch:{ Throwable -> 0x00b7 }
            goto L_0x0003
        L_0x00b7:
            r0 = move-exception
            java.lang.String r1 = "ApsServiceCore"
            java.lang.String r2 = "invokeSocketLocation part3"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0003
        L_0x00c3:
            r0 = move-exception
            java.lang.String r2 = "ApsServiceCore"
            java.lang.String r3 = "invokeSocketLocation part2"
            com.loc.es.a(r0, r2, r3)     // Catch:{ all -> 0x00e1 }
            r1.close()     // Catch:{ Throwable -> 0x00d5 }
            r7.close()     // Catch:{ Throwable -> 0x00d5 }
            goto L_0x0003
        L_0x00d5:
            r0 = move-exception
            java.lang.String r1 = "ApsServiceCore"
            java.lang.String r2 = "invokeSocketLocation part3"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0003
        L_0x00e1:
            r0 = move-exception
            r1.close()     // Catch:{ Throwable -> 0x00e9 }
            r7.close()     // Catch:{ Throwable -> 0x00e9 }
        L_0x00e8:
            throw r0     // Catch:{ Throwable -> 0x0035 }
        L_0x00e9:
            r1 = move-exception
            java.lang.String r2 = "ApsServiceCore"
            java.lang.String r3 = "invokeSocketLocation part3"
            com.loc.es.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x00e8
        L_0x00f4:
            r0 = move-exception
            r1 = r2
            r3 = r2
        L_0x00f7:
            com.loc.es.f = r4     // Catch:{ Throwable -> 0x0035 }
            r6.b(r3)     // Catch:{ Throwable -> 0x010e }
            r1.close()     // Catch:{ Throwable -> 0x0103 }
            r7.close()     // Catch:{ Throwable -> 0x0103 }
        L_0x0102:
            throw r0     // Catch:{ Throwable -> 0x0035 }
        L_0x0103:
            r1 = move-exception
            java.lang.String r2 = "ApsServiceCore"
            java.lang.String r3 = "invokeSocketLocation part3"
            com.loc.es.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0102
        L_0x010e:
            r2 = move-exception
            java.lang.String r3 = "ApsServiceCore"
            java.lang.String r4 = "invokeSocketLocation part2"
            com.loc.es.a(r2, r3, r4)     // Catch:{ all -> 0x012a }
            r1.close()     // Catch:{ Throwable -> 0x011f }
            r7.close()     // Catch:{ Throwable -> 0x011f }
            goto L_0x0102
        L_0x011f:
            r1 = move-exception
            java.lang.String r2 = "ApsServiceCore"
            java.lang.String r3 = "invokeSocketLocation part3"
            com.loc.es.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0102
        L_0x012a:
            r0 = move-exception
            r1.close()     // Catch:{ Throwable -> 0x0132 }
            r7.close()     // Catch:{ Throwable -> 0x0132 }
        L_0x0131:
            throw r0     // Catch:{ Throwable -> 0x0035 }
        L_0x0132:
            r1 = move-exception
            java.lang.String r2 = "ApsServiceCore"
            java.lang.String r3 = "invokeSocketLocation part3"
            com.loc.es.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0131
        L_0x013d:
            r0 = move-exception
            r3 = r2
            goto L_0x00f7
        L_0x0140:
            r0 = move-exception
            r3 = r2
            goto L_0x00f7
        L_0x0143:
            r0 = move-exception
            goto L_0x0071
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.n.a(com.loc.n, java.net.Socket):void");
    }

    private void a(BufferedReader bufferedReader) throws Exception {
        String[] split;
        String[] split2;
        String[] split3;
        String readLine = bufferedReader.readLine();
        int i2 = 30000;
        if (readLine != null && readLine.length() > 0 && (split = readLine.split(" ")) != null && split.length > 1 && (split2 = split[1].split("\\?")) != null && split2.length > 1 && (split3 = split2[1].split("&")) != null && split3.length > 0) {
            for (String str : split3) {
                String[] split4 = str.split("=");
                if (split4 != null && split4.length > 1) {
                    if ("to".equals(split4[0])) {
                        i2 = fa.g(split4[1]);
                    }
                    if ("callback".equals(split4[0])) {
                        this.E = split4[1];
                    }
                }
            }
        }
        es.f = i2;
    }

    private AMapLocationClientOption b(Bundle bundle) {
        AMapLocationClientOption a2 = es.a(bundle.getBundle("optBundle"));
        a(a2);
        try {
            String string = bundle.getString("d");
            if (!TextUtils.isEmpty(string)) {
                x.a(string);
            }
        } catch (Throwable th) {
            es.a(th, "APSManager", "parseBundle");
        }
        return a2;
    }

    static /* synthetic */ void b(n nVar) {
        try {
            if (er.e()) {
                nVar.f.e();
            } else if (!fa.e(nVar.e)) {
                nVar.f.e();
            }
            nVar.d.sendEmptyMessageDelayed(5, (long) (er.d() * 1000));
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "doOffFusion");
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void b(com.loc.n r11, android.os.Messenger r12, android.os.Bundle r13) {
        /*
            r10 = 9
            r2 = 0
            r1 = 0
            if (r13 == 0) goto L_0x000d
            boolean r0 = r13.isEmpty()     // Catch:{ Throwable -> 0x0073 }
            if (r0 == 0) goto L_0x000e
        L_0x000d:
            return
        L_0x000e:
            com.amap.api.location.AMapLocationClientOption r6 = r11.b(r13)     // Catch:{ Throwable -> 0x0073 }
            java.util.HashMap<android.os.Messenger, java.lang.Long> r0 = r11.h     // Catch:{ Throwable -> 0x0073 }
            boolean r0 = r0.containsKey(r12)     // Catch:{ Throwable -> 0x0073 }
            if (r0 == 0) goto L_0x0038
            boolean r0 = r6.isOnceLocation()     // Catch:{ Throwable -> 0x0073 }
            if (r0 != 0) goto L_0x0038
            java.util.HashMap<android.os.Messenger, java.lang.Long> r0 = r11.h     // Catch:{ Throwable -> 0x0073 }
            java.lang.Object r0 = r0.get(r12)     // Catch:{ Throwable -> 0x0073 }
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ Throwable -> 0x0073 }
            long r4 = r0.longValue()     // Catch:{ Throwable -> 0x0073 }
            long r8 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0073 }
            long r4 = r8 - r4
            r8 = 800(0x320, double:3.953E-321)
            int r0 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r0 < 0) goto L_0x000d
        L_0x0038:
            boolean r0 = r11.B     // Catch:{ Throwable -> 0x0073 }
            if (r0 != 0) goto L_0x007e
            r0 = 9
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r2 = "init error : "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r2 = r11.C     // Catch:{ Throwable -> 0x0073 }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r2 = "#0901"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0073 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = a(r0, r1)     // Catch:{ Throwable -> 0x0073 }
            r11.x = r0     // Catch:{ Throwable -> 0x0073 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r2 = r11.x     // Catch:{ Throwable -> 0x0073 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r3 = r0.l()     // Catch:{ Throwable -> 0x0073 }
            r4 = 0
            r0 = r11
            r1 = r12
            r0.a(r1, r2, r3, r4)     // Catch:{ Throwable -> 0x0073 }
            r0 = 0
            r1 = 2091(0x82b, float:2.93E-42)
            com.loc.ey.a(r0, r1)     // Catch:{ Throwable -> 0x0073 }
            goto L_0x000d
        L_0x0073:
            r0 = move-exception
            java.lang.String r1 = "ApsServiceCore"
            java.lang.String r2 = "doLocation"
            com.loc.es.a(r0, r1, r2)
            goto L_0x000d
        L_0x007e:
            long r4 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0073 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            boolean r0 = com.loc.fa.a(r0)     // Catch:{ Throwable -> 0x0073 }
            if (r0 == 0) goto L_0x00e4
            long r8 = r11.w     // Catch:{ Throwable -> 0x0073 }
            long r4 = r4 - r8
            r8 = 600(0x258, double:2.964E-321)
            int r0 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r0 >= 0) goto L_0x00e4
            com.autonavi.aps.amapapi.model.AMapLocationServer r2 = r11.x     // Catch:{ Throwable -> 0x0073 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r3 = r0.l()     // Catch:{ Throwable -> 0x0073 }
            r4 = 0
            r0 = r11
            r1 = r12
            r0.a(r1, r2, r3, r4)     // Catch:{ Throwable -> 0x0073 }
        L_0x00a2:
            r11.a(r12)     // Catch:{ Throwable -> 0x0073 }
            boolean r0 = com.loc.er.u()     // Catch:{ Throwable -> 0x0073 }
            if (r0 == 0) goto L_0x00ae
            r11.d()     // Catch:{ Throwable -> 0x0073 }
        L_0x00ae:
            long r0 = r11.y     // Catch:{ Throwable -> 0x0073 }
            boolean r0 = com.loc.er.a(r0)     // Catch:{ Throwable -> 0x0073 }
            if (r0 == 0) goto L_0x00df
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            if (r0 == 0) goto L_0x00df
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            int r0 = r0.getLocationType()     // Catch:{ Throwable -> 0x0073 }
            r1 = 2
            if (r0 == r1) goto L_0x00d4
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            int r0 = r0.getLocationType()     // Catch:{ Throwable -> 0x0073 }
            r1 = 4
            if (r0 == r1) goto L_0x00d4
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            int r0 = r0.getLocationType()     // Catch:{ Throwable -> 0x0073 }
            if (r0 != r10) goto L_0x00df
        L_0x00d4:
            long r0 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0073 }
            r11.y = r0     // Catch:{ Throwable -> 0x0073 }
            com.loc.cs r0 = r11.f     // Catch:{ Throwable -> 0x0073 }
            r0.e()     // Catch:{ Throwable -> 0x0073 }
        L_0x00df:
            r11.f()     // Catch:{ Throwable -> 0x0073 }
            goto L_0x000d
        L_0x00e4:
            com.loc.ex r7 = new com.loc.ex     // Catch:{ Throwable -> 0x0073 }
            r7.<init>()     // Catch:{ Throwable -> 0x0073 }
            long r4 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0073 }
            r7.a(r4)     // Catch:{ Throwable -> 0x0073 }
            com.loc.cs r0 = r11.f     // Catch:{ Throwable -> 0x0175 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r0.d()     // Catch:{ Throwable -> 0x0175 }
            r11.x = r0     // Catch:{ Throwable -> 0x0175 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0175 }
            int r0 = r0.getLocationType()     // Catch:{ Throwable -> 0x0175 }
            r4 = 6
            if (r0 == r4) goto L_0x010a
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0175 }
            int r0 = r0.getLocationType()     // Catch:{ Throwable -> 0x0175 }
            r4 = 5
            if (r0 != r4) goto L_0x0110
        L_0x010a:
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0175 }
            long r2 = r0.k()     // Catch:{ Throwable -> 0x0175 }
        L_0x0110:
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0175 }
            r7.a(r0)     // Catch:{ Throwable -> 0x0175 }
            com.loc.cs r0 = r11.f     // Catch:{ Throwable -> 0x0175 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r4 = r11.x     // Catch:{ Throwable -> 0x0175 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r0.a(r4)     // Catch:{ Throwable -> 0x0175 }
            r11.x = r0     // Catch:{ Throwable -> 0x0175 }
            r4 = r2
        L_0x0120:
            long r2 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0073 }
            r7.b(r2)     // Catch:{ Throwable -> 0x0073 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            boolean r0 = com.loc.fa.a(r0)     // Catch:{ Throwable -> 0x0073 }
            if (r0 == 0) goto L_0x0135
            long r2 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0073 }
            r11.w = r2     // Catch:{ Throwable -> 0x0073 }
        L_0x0135:
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            if (r0 != 0) goto L_0x0144
            r0 = 8
            java.lang.String r2 = "loc is null#0801"
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = a(r0, r2)     // Catch:{ Throwable -> 0x0073 }
            r11.x = r0     // Catch:{ Throwable -> 0x0073 }
        L_0x0144:
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            if (r0 == 0) goto L_0x01b7
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r3 = r0.l()     // Catch:{ Throwable -> 0x0073 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r11.x     // Catch:{ Throwable -> 0x0073 }
            com.amap.api.location.AMapLocation r0 = r0.clone()     // Catch:{ Throwable -> 0x0073 }
        L_0x0154:
            boolean r1 = r6.isLocationCacheEnable()     // Catch:{ Throwable -> 0x01ab }
            if (r1 == 0) goto L_0x0168
            com.loc.r r1 = r11.A     // Catch:{ Throwable -> 0x01ab }
            if (r1 == 0) goto L_0x0168
            com.loc.r r1 = r11.A     // Catch:{ Throwable -> 0x01ab }
            long r8 = r6.getLastLocationLifeCycle()     // Catch:{ Throwable -> 0x01ab }
            com.amap.api.location.AMapLocation r0 = r1.a(r0, r3, r8)     // Catch:{ Throwable -> 0x01ab }
        L_0x0168:
            r2 = r0
        L_0x0169:
            r0 = r11
            r1 = r12
            r0.a(r1, r2, r3, r4)     // Catch:{ Throwable -> 0x0073 }
            android.content.Context r0 = r11.e     // Catch:{ Throwable -> 0x0073 }
            com.loc.ey.a(r0, r7)     // Catch:{ Throwable -> 0x0073 }
            goto L_0x00a2
        L_0x0175:
            r0 = move-exception
            r4 = 0
            r5 = 2081(0x821, float:2.916E-42)
            com.loc.ey.a(r4, r5)     // Catch:{ Throwable -> 0x0073 }
            r4 = 8
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r8 = "loc error : "
            r5.<init>(r8)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r8 = r0.getMessage()     // Catch:{ Throwable -> 0x0073 }
            java.lang.StringBuilder r5 = r5.append(r8)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r8 = "#0801"
            java.lang.StringBuilder r5 = r5.append(r8)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0073 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r4 = a(r4, r5)     // Catch:{ Throwable -> 0x0073 }
            r11.x = r4     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r4 = "ApsServiceCore"
            java.lang.String r5 = "run part2"
            com.loc.es.a(r0, r4, r5)     // Catch:{ Throwable -> 0x0073 }
            r4 = r2
            goto L_0x0120
        L_0x01ab:
            r1 = move-exception
            java.lang.String r2 = "ApsServiceCore"
            java.lang.String r6 = "fixLastLocation"
            com.loc.es.a(r1, r2, r6)     // Catch:{ Throwable -> 0x0073 }
            r2 = r0
            goto L_0x0169
        L_0x01b7:
            r0 = r1
            r3 = r1
            goto L_0x0154
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.n.b(com.loc.n, android.os.Messenger, android.os.Bundle):void");
    }

    private void b(String str) throws UnsupportedEncodingException, IOException {
        PrintStream printStream = new PrintStream(this.q.getOutputStream(), true, "UTF-8");
        printStream.println("HTTP/1.0 200 OK");
        printStream.println("Content-Length:" + str.getBytes("UTF-8").length);
        printStream.println();
        printStream.println(str);
        printStream.close();
    }

    public static void c() {
        g = false;
    }

    static /* synthetic */ void c(n nVar) {
        try {
            if (er.a(nVar.e, nVar.v)) {
                nVar.v = fa.b();
                nVar.f.e();
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "doNGps");
        }
    }

    private void d() {
        try {
            this.d.removeMessages(4);
            if (er.a()) {
                this.d.sendEmptyMessage(4);
            }
            this.d.removeMessages(5);
            if (er.c() && er.d() > 2) {
                this.d.sendEmptyMessage(5);
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "checkConfig");
        }
    }

    private String e() {
        long currentTimeMillis = System.currentTimeMillis();
        if (fa.e(this.e)) {
            return this.E + "&&" + this.E + "({'package':'" + this.a + "','error_code':36,'error':'app is background'})";
        }
        if (this.x == null || currentTimeMillis - this.x.getTime() > 5000) {
            try {
                this.x = this.f.d();
            } catch (Throwable th) {
                es.a(th, "ApsServiceCore", "getSocketLocResult");
            }
        }
        return this.x == null ? this.E + "&&" + this.E + "({'package':'" + this.a + "','error_code':8,'error':'unknown error'})" : this.x.getErrorCode() != 0 ? this.E + "&&" + this.E + "({'package':'" + this.a + "','error_code':" + this.x.getErrorCode() + ",'error':'" + this.x.getErrorInfo() + "'})" : this.E + "&&" + this.E + "({'package':'" + this.a + "','error_code':0,'error':'','location':{'y':" + this.x.getLatitude() + ",'precision':" + this.x.getAccuracy() + ",'x':" + this.x.getLongitude() + "},'version_code':'4.7.1','version':'4.7.1'})";
    }

    private void f() {
        try {
            if (this.f != null) {
                this.f.k();
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "startColl");
        }
    }

    public final void a() {
        try {
            if (this.q != null) {
                this.q.close();
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "doStopScocket 1");
        }
        try {
            if (this.o != null) {
                this.o.close();
            }
        } catch (Throwable th2) {
            es.a(th2, "ApsServiceCore", "doStopScocket 2");
        }
        try {
            if (this.s != null) {
                this.s.interrupt();
            }
        } catch (Throwable th3) {
        }
        this.s = null;
        this.o = null;
        this.p = false;
        this.r = false;
    }

    /* access modifiers changed from: package-private */
    public final void a(Messenger messenger, Bundle bundle) {
        float f2;
        if (bundle != null) {
            try {
                if (!bundle.isEmpty() && er.q()) {
                    double d2 = bundle.getDouble(ResponseBase.STRING_LAT);
                    double d3 = bundle.getDouble("lon");
                    b(bundle);
                    if (this.c != null) {
                        f2 = fa.a(new double[]{d2, d3, this.c.getLatitude(), this.c.getLongitude()});
                        if (f2 < ((float) (er.r() * 3))) {
                            Bundle bundle2 = new Bundle();
                            bundle2.setClassLoader(AMapLocation.class.getClassLoader());
                            bundle2.putInt("I_MAX_GEO_DIS", er.r() * 3);
                            bundle2.putInt("I_MIN_GEO_DIS", er.r());
                            bundle2.putParcelable("loc", this.c);
                            a(messenger, 6, bundle2);
                        }
                    } else {
                        f2 = -1.0f;
                    }
                    if (f2 == -1.0f || f2 > ((float) er.r())) {
                        a(bundle);
                        this.c = this.f.a(d2, d3);
                    }
                }
            } catch (Throwable th) {
                es.a(th, "ApsServiceCore", "doLocationGeo");
            }
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void b() {
        /*
            r8 = this;
            r4 = 0
            java.util.HashMap<android.os.Messenger, java.lang.Long> r0 = r8.h     // Catch:{ Throwable -> 0x00a5 }
            r0.clear()     // Catch:{ Throwable -> 0x00a5 }
            r0 = 0
            r8.h = r0     // Catch:{ Throwable -> 0x00a5 }
            com.loc.cs r0 = r8.f     // Catch:{ Throwable -> 0x00a5 }
            if (r0 == 0) goto L_0x0013
            android.content.Context r0 = r8.e     // Catch:{ Throwable -> 0x00a5 }
            com.loc.cs.b(r0)     // Catch:{ Throwable -> 0x00a5 }
        L_0x0013:
            com.loc.n$a r0 = r8.d     // Catch:{ Throwable -> 0x00a5 }
            if (r0 == 0) goto L_0x001d
            com.loc.n$a r0 = r8.d     // Catch:{ Throwable -> 0x00a5 }
            r1 = 0
            r0.removeCallbacksAndMessages(r1)     // Catch:{ Throwable -> 0x00a5 }
        L_0x001d:
            com.loc.n$b r0 = r8.b     // Catch:{ Throwable -> 0x00a5 }
            if (r0 == 0) goto L_0x0034
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x00a5 }
            r1 = 18
            if (r0 < r1) goto L_0x00b0
            com.loc.n$b r0 = r8.b     // Catch:{ Throwable -> 0x009e }
            java.lang.Class<android.os.HandlerThread> r1 = android.os.HandlerThread.class
            java.lang.String r2 = "quitSafely"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x009e }
            com.loc.ew.a(r0, r1, r2, r3)     // Catch:{ Throwable -> 0x009e }
        L_0x0034:
            r0 = 0
            r8.b = r0     // Catch:{ Throwable -> 0x00a5 }
            r0 = 0
            r8.d = r0     // Catch:{ Throwable -> 0x00a5 }
            com.loc.r r0 = r8.A     // Catch:{ Throwable -> 0x00a5 }
            if (r0 == 0) goto L_0x0046
            com.loc.r r0 = r8.A     // Catch:{ Throwable -> 0x00a5 }
            r0.c()     // Catch:{ Throwable -> 0x00a5 }
            r0 = 0
            r8.A = r0     // Catch:{ Throwable -> 0x00a5 }
        L_0x0046:
            r8.a()     // Catch:{ Throwable -> 0x00a5 }
            r0 = 0
            r8.t = r0     // Catch:{ Throwable -> 0x00a5 }
            r0 = 0
            r8.u = r0     // Catch:{ Throwable -> 0x00a5 }
            com.loc.cs r0 = r8.f     // Catch:{ Throwable -> 0x00a5 }
            r0.f()     // Catch:{ Throwable -> 0x00a5 }
            android.content.Context r0 = r8.e     // Catch:{ Throwable -> 0x00a5 }
            com.loc.ey.a(r0)     // Catch:{ Throwable -> 0x00a5 }
            com.loc.ey r0 = r8.i     // Catch:{ Throwable -> 0x00a5 }
            if (r0 == 0) goto L_0x008f
            long r0 = r8.j     // Catch:{ Throwable -> 0x00a5 }
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 == 0) goto L_0x008f
            long r0 = r8.k     // Catch:{ Throwable -> 0x00a5 }
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 == 0) goto L_0x008f
            long r0 = com.loc.fa.c()     // Catch:{ Throwable -> 0x00a5 }
            long r2 = r8.j     // Catch:{ Throwable -> 0x00a5 }
            long r6 = r0 - r2
            com.loc.ey r0 = r8.i     // Catch:{ Throwable -> 0x00a5 }
            android.content.Context r1 = r8.e     // Catch:{ Throwable -> 0x00a5 }
            int r2 = r0.c(r1)     // Catch:{ Throwable -> 0x00a5 }
            com.loc.ey r0 = r8.i     // Catch:{ Throwable -> 0x00a5 }
            android.content.Context r1 = r8.e     // Catch:{ Throwable -> 0x00a5 }
            int r3 = r0.d(r1)     // Catch:{ Throwable -> 0x00a5 }
            android.content.Context r1 = r8.e     // Catch:{ Throwable -> 0x00a5 }
            long r4 = r8.k     // Catch:{ Throwable -> 0x00a5 }
            com.loc.ey.a(r1, r2, r3, r4, r6)     // Catch:{ Throwable -> 0x00a5 }
            com.loc.ey r0 = r8.i     // Catch:{ Throwable -> 0x00a5 }
            android.content.Context r1 = r8.e     // Catch:{ Throwable -> 0x00a5 }
            r0.e(r1)     // Catch:{ Throwable -> 0x00a5 }
        L_0x008f:
            com.loc.aq.b()     // Catch:{ Throwable -> 0x00a5 }
            boolean r0 = com.loc.n.g     // Catch:{ Throwable -> 0x00a5 }
            if (r0 == 0) goto L_0x009d
            int r0 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x00a5 }
            android.os.Process.killProcess(r0)     // Catch:{ Throwable -> 0x00a5 }
        L_0x009d:
            return
        L_0x009e:
            r0 = move-exception
            com.loc.n$b r0 = r8.b     // Catch:{ Throwable -> 0x00a5 }
            r0.quit()     // Catch:{ Throwable -> 0x00a5 }
            goto L_0x0034
        L_0x00a5:
            r0 = move-exception
            java.lang.String r1 = "ApsServiceCore"
            java.lang.String r2 = "threadDestroy"
            com.loc.es.a(r0, r1, r2)
            goto L_0x009d
        L_0x00b0:
            com.loc.n$b r0 = r8.b     // Catch:{ Throwable -> 0x00a5 }
            r0.quit()     // Catch:{ Throwable -> 0x00a5 }
            goto L_0x0034
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.n.b():void");
    }
}
