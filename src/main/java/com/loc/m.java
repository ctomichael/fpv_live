package com.loc;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.view.PointerIconCompat;
import android.text.TextUtils;
import android.webkit.WebView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.location.APSService;
import com.amap.api.location.LocationManagerBase;
import com.amap.api.location.UmidtokenInfo;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import dji.diagnostics.model.DJIDiagnosticsError;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: AmapLocationManager */
public final class m implements LocationManagerBase {
    /* access modifiers changed from: private */
    public static boolean E = false;
    /* access modifiers changed from: private */
    public boolean A = false;
    private volatile boolean B = false;
    private boolean C = true;
    /* access modifiers changed from: private */
    public boolean D = true;
    private q F = null;
    private ServiceConnection G = new ServiceConnection() {
        /* class com.loc.m.AnonymousClass1 */

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.loc.m.a(com.loc.m, boolean):boolean
         arg types: [com.loc.m, int]
         candidates:
          com.loc.m.a(int, android.os.Bundle):void
          com.loc.m.a(android.content.Intent, boolean):void
          com.loc.m.a(com.amap.api.location.AMapLocation, long):void
          com.loc.m.a(com.loc.cs, com.autonavi.aps.amapapi.model.AMapLocationServer):void
          com.loc.m.a(com.loc.m, android.os.Bundle):void
          com.loc.m.a(com.loc.m, android.os.Message):void
          com.loc.m.a(com.loc.m, com.amap.api.location.AMapLocationListener):void
          com.loc.m.a(com.loc.m, boolean):boolean */
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                m.this.h = new Messenger(iBinder);
                boolean unused = m.this.A = true;
                m.this.q = true;
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "onServiceConnected");
            }
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.loc.m.a(com.loc.m, boolean):boolean
         arg types: [com.loc.m, int]
         candidates:
          com.loc.m.a(int, android.os.Bundle):void
          com.loc.m.a(android.content.Intent, boolean):void
          com.loc.m.a(com.amap.api.location.AMapLocation, long):void
          com.loc.m.a(com.loc.cs, com.autonavi.aps.amapapi.model.AMapLocationServer):void
          com.loc.m.a(com.loc.m, android.os.Bundle):void
          com.loc.m.a(com.loc.m, android.os.Message):void
          com.loc.m.a(com.loc.m, com.amap.api.location.AMapLocationListener):void
          com.loc.m.a(com.loc.m, boolean):boolean */
        public final void onServiceDisconnected(ComponentName componentName) {
            m.this.h = null;
            boolean unused = m.this.A = false;
        }
    };
    AMapLocationClientOption a = new AMapLocationClientOption();
    public c b;
    p c = null;
    ArrayList<AMapLocationListener> d = new ArrayList<>();
    boolean e = false;
    public boolean f = true;
    r g;
    Messenger h = null;
    Messenger i = null;
    Intent j = null;
    int k = 0;
    b l = null;
    boolean m = false;
    AMapLocationClientOption.AMapLocationMode n = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
    Object o = new Object();
    ey p = null;
    boolean q = false;
    n r = null;
    String s = null;
    AMapLocationQualityReport t = null;
    boolean u = false;
    boolean v = false;
    a w = null;
    String x = null;
    boolean y = false;
    private Context z;

    /* compiled from: AmapLocationManager */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        public final void handleMessage(Message message) {
            try {
                super.handleMessage(message);
                switch (message.what) {
                    case 1002:
                        m.a(m.this, (AMapLocationListener) message.obj);
                        return;
                    case 1003:
                        try {
                            m.this.c();
                            return;
                        } catch (Throwable th) {
                            es.a(th, "AMapLocationManage$MHandlerr", "handleMessage START_LOCATION");
                            return;
                        }
                    case 1004:
                        try {
                            m.this.d();
                            return;
                        } catch (Throwable th2) {
                            es.a(th2, "AMapLocationManage$MHandlerr", "handleMessage STOP_LOCATION");
                            return;
                        }
                    case 1005:
                        try {
                            m.b(m.this, (AMapLocationListener) message.obj);
                            return;
                        } catch (Throwable th3) {
                            es.a(th3, "AMapLocationManage$MHandlerr", "handleMessage REMOVE_LISTENER");
                            return;
                        }
                    case 1006:
                    case 1007:
                    case 1010:
                    case 1012:
                    case 1013:
                    case 1019:
                    case PointerIconCompat.TYPE_GRAB /*1020*/:
                    case 1021:
                    case 1022:
                    default:
                        return;
                    case 1008:
                        try {
                            m.g(m.this);
                            return;
                        } catch (Throwable th4) {
                            es.a(th4, "AMapLocationManage$MHandlerr", "handleMessage START_SOCKET");
                            return;
                        }
                    case 1009:
                        try {
                            m.h(m.this);
                            return;
                        } catch (Throwable th5) {
                            es.a(th5, "AMapLocationManage$MHandlerr", "handleMessage STOP_SOCKET");
                            return;
                        }
                    case 1011:
                        try {
                            m.this.a();
                            return;
                        } catch (Throwable th6) {
                            es.a(th6, "AMapLocationManage$MHandlerr", "handleMessage DESTROY");
                            return;
                        }
                    case 1014:
                        m.b(m.this, message);
                        return;
                    case 1015:
                        try {
                            m.this.c.a(m.this.a);
                            m.this.a(1025, (Object) null, 300000);
                            return;
                        } catch (Throwable th7) {
                            es.a(th7, "AMapLocationManage$MHandlerr", "handleMessage START_GPS_LOCATION");
                            return;
                        }
                    case 1016:
                        try {
                            if (m.this.c.b()) {
                                m.this.a(1016, (Object) null, 1000);
                                return;
                            } else {
                                m.d(m.this);
                                return;
                            }
                        } catch (Throwable th8) {
                            es.a(th8, "AMapLocationManage$MHandlerr", "handleMessage START_LBS_LOCATION");
                            return;
                        }
                    case 1017:
                        try {
                            m.this.c.a();
                            m.this.a(1025);
                            return;
                        } catch (Throwable th9) {
                            es.a(th9, "AMapLocationManage$MHandlerr", "handleMessage STOP_GPS_LOCATION");
                            return;
                        }
                    case 1018:
                        try {
                            m.this.a = (AMapLocationClientOption) message.obj;
                            if (m.this.a != null) {
                                m.f(m.this);
                                return;
                            }
                            return;
                        } catch (Throwable th10) {
                            es.a(th10, "AMapLocationManage$MHandlerr", "handleMessage SET_OPTION");
                            return;
                        }
                    case DJIDiagnosticsError.Camera.INTERNAL_STORAGE_INVALID /*1023*/:
                        try {
                            m.c(m.this, message);
                            return;
                        } catch (Throwable th11) {
                            es.a(th11, "AMapLocationManage$MHandlerr", "handleMessage ACTION_ENABLE_BACKGROUND");
                            return;
                        }
                    case 1024:
                        try {
                            m.d(m.this, message);
                            return;
                        } catch (Throwable th12) {
                            es.a(th12, "AMapLocationManage$MHandlerr", "handleMessage ACTION_DISABLE_BACKGROUND");
                            return;
                        }
                    case 1025:
                        try {
                            if (m.this.c != null) {
                                if (fa.c() - m.this.c.d > 300000) {
                                    m.this.c.a();
                                    m.this.c.a(m.this.a);
                                }
                                m.this.a(1025, (Object) null, 300000);
                                return;
                            }
                            return;
                        } catch (Throwable th13) {
                            es.a(th13, "AMapLocationManage$MHandlerr", "handleMessage ACTION_REBOOT_GPS_LOCATION");
                            return;
                        }
                }
            } catch (Throwable th14) {
                es.a(th14, "AMapLocationManage$MHandlerr", "handleMessage");
            }
            es.a(th14, "AMapLocationManage$MHandlerr", "handleMessage");
        }
    }

    /* compiled from: AmapLocationManager */
    static class b extends HandlerThread {
        m a = null;

        public b(String str, m mVar) {
            super(str);
            this.a = mVar;
        }

        /* access modifiers changed from: protected */
        public final void onLooperPrepared() {
            try {
                this.a.g.a();
                this.a.f();
                super.onLooperPrepared();
            } catch (Throwable th) {
            }
        }

        public final void run() {
            try {
                super.run();
            } catch (Throwable th) {
            }
        }
    }

    /* compiled from: AmapLocationManager */
    public class c extends Handler {
        public c() {
        }

        public c(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void handleMessage(android.os.Message r4) {
            /*
                r3 = this;
                super.handleMessage(r4)     // Catch:{ Throwable -> 0x002b }
                com.loc.m r0 = com.loc.m.this     // Catch:{ Throwable -> 0x002b }
                boolean r0 = r0.m     // Catch:{ Throwable -> 0x002b }
                if (r0 == 0) goto L_0x0010
                boolean r0 = com.loc.es.d()     // Catch:{ Throwable -> 0x002b }
                if (r0 != 0) goto L_0x0010
            L_0x000f:
                return
            L_0x0010:
                int r0 = r4.what     // Catch:{ Throwable -> 0x002b }
                switch(r0) {
                    case 1: goto L_0x0016;
                    case 2: goto L_0x003c;
                    case 3: goto L_0x000f;
                    case 4: goto L_0x0015;
                    case 5: goto L_0x004d;
                    case 6: goto L_0x0070;
                    case 7: goto L_0x00d1;
                    case 8: goto L_0x0036;
                    case 9: goto L_0x00ef;
                    default: goto L_0x0015;
                }
            L_0x0015:
                goto L_0x000f
            L_0x0016:
                android.os.Bundle r0 = r4.getData()     // Catch:{ Throwable -> 0x0020 }
                com.loc.m r1 = com.loc.m.this     // Catch:{ Throwable -> 0x0020 }
                com.loc.m.a(r1, r0)     // Catch:{ Throwable -> 0x0020 }
                goto L_0x000f
            L_0x0020:
                r0 = move-exception
                java.lang.String r1 = "AmapLocationManager$ActionHandler"
                java.lang.String r2 = "handleMessage RESULT_LBS_LOCATIONSUCCESS"
                com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x002b }
                goto L_0x000f
            L_0x002b:
                r0 = move-exception
                java.lang.String r1 = "AmapLocationManager$MainHandler"
                java.lang.String r2 = "handleMessage"
                com.loc.es.a(r0, r1, r2)
                goto L_0x000f
            L_0x0036:
                r0 = 0
                r1 = 2141(0x85d, float:3.0E-42)
                com.loc.ey.a(r0, r1)     // Catch:{ Throwable -> 0x002b }
            L_0x003c:
                com.loc.m r0 = com.loc.m.this     // Catch:{ Throwable -> 0x0042 }
                com.loc.m.a(r0, r4)     // Catch:{ Throwable -> 0x0042 }
                goto L_0x000f
            L_0x0042:
                r0 = move-exception
                java.lang.String r1 = "AmapLocationManager$ActionHandler"
                java.lang.String r2 = "handleMessage RESULT_GPS_LOCATIONSUCCESS"
                com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x002b }
                goto L_0x000f
            L_0x004d:
                android.os.Bundle r0 = r4.getData()     // Catch:{ Throwable -> 0x0065 }
                java.lang.String r1 = "optBundle"
                com.loc.m r2 = com.loc.m.this     // Catch:{ Throwable -> 0x0065 }
                com.amap.api.location.AMapLocationClientOption r2 = r2.a     // Catch:{ Throwable -> 0x0065 }
                android.os.Bundle r2 = com.loc.es.a(r2)     // Catch:{ Throwable -> 0x0065 }
                r0.putBundle(r1, r2)     // Catch:{ Throwable -> 0x0065 }
                com.loc.m r1 = com.loc.m.this     // Catch:{ Throwable -> 0x0065 }
                r1.a(10, r0)     // Catch:{ Throwable -> 0x0065 }
                goto L_0x000f
            L_0x0065:
                r0 = move-exception
                java.lang.String r1 = "AmapLocationManager$ActionHandler"
                java.lang.String r2 = "handleMessage RESULT_GPS_LOCATIONCHANGE"
                com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x002b }
                goto L_0x000f
            L_0x0070:
                android.os.Bundle r0 = r4.getData()     // Catch:{ Throwable -> 0x00c5 }
                com.loc.m r1 = com.loc.m.this     // Catch:{ Throwable -> 0x00c5 }
                com.loc.p r1 = r1.c     // Catch:{ Throwable -> 0x00c5 }
                if (r1 == 0) goto L_0x000f
                com.loc.m r1 = com.loc.m.this     // Catch:{ Throwable -> 0x00c5 }
                com.loc.p r1 = r1.c     // Catch:{ Throwable -> 0x00c5 }
                if (r0 == 0) goto L_0x000f
                java.lang.Class<com.amap.api.location.AMapLocation> r2 = com.amap.api.location.AMapLocation.class
                java.lang.ClassLoader r2 = r2.getClassLoader()     // Catch:{ Throwable -> 0x00b9 }
                r0.setClassLoader(r2)     // Catch:{ Throwable -> 0x00b9 }
                java.lang.String r2 = "I_MAX_GEO_DIS"
                int r2 = r0.getInt(r2)     // Catch:{ Throwable -> 0x00b9 }
                r1.g = r2     // Catch:{ Throwable -> 0x00b9 }
                java.lang.String r2 = "I_MIN_GEO_DIS"
                int r2 = r0.getInt(r2)     // Catch:{ Throwable -> 0x00b9 }
                r1.h = r2     // Catch:{ Throwable -> 0x00b9 }
                java.lang.String r2 = "loc"
                android.os.Parcelable r0 = r0.getParcelable(r2)     // Catch:{ Throwable -> 0x00b9 }
                com.amap.api.location.AMapLocation r0 = (com.amap.api.location.AMapLocation) r0     // Catch:{ Throwable -> 0x00b9 }
                java.lang.String r2 = r0.getAdCode()     // Catch:{ Throwable -> 0x00b9 }
                boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x00b9 }
                if (r2 != 0) goto L_0x000f
                java.lang.Object r2 = r1.o     // Catch:{ Throwable -> 0x00b9 }
                monitor-enter(r2)     // Catch:{ Throwable -> 0x00b9 }
                r1.y = r0     // Catch:{ all -> 0x00b6 }
                monitor-exit(r2)     // Catch:{ all -> 0x00b6 }
                goto L_0x000f
            L_0x00b6:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x00b6 }
                throw r0     // Catch:{ Throwable -> 0x00b9 }
            L_0x00b9:
                r0 = move-exception
                java.lang.String r1 = "GpsLocation"
                java.lang.String r2 = "setLastGeoLocation"
                com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00c5 }
                goto L_0x000f
            L_0x00c5:
                r0 = move-exception
                java.lang.String r1 = "AmapLocationManager$ActionHandler"
                java.lang.String r2 = "handleMessage RESULT_GPS_GEO_SUCCESS"
                com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x002b }
                goto L_0x000f
            L_0x00d1:
                android.os.Bundle r0 = r4.getData()     // Catch:{ Throwable -> 0x00e3 }
                com.loc.m r1 = com.loc.m.this     // Catch:{ Throwable -> 0x00e3 }
                java.lang.String r2 = "ngpsAble"
                boolean r0 = r0.getBoolean(r2)     // Catch:{ Throwable -> 0x00e3 }
                boolean unused = r1.D = r0     // Catch:{ Throwable -> 0x00e3 }
                goto L_0x000f
            L_0x00e3:
                r0 = move-exception
                java.lang.String r1 = "AmapLocationManager$ActionHandler"
                java.lang.String r2 = "handleMessage RESULT_NGPS_ABLE"
                com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x002b }
                goto L_0x000f
            L_0x00ef:
                android.os.Bundle r0 = r4.getData()     // Catch:{ Throwable -> 0x00ff }
                java.lang.String r1 = "installMockApp"
                boolean r0 = r0.getBoolean(r1)     // Catch:{ Throwable -> 0x00ff }
                boolean unused = com.loc.m.E = r0     // Catch:{ Throwable -> 0x00ff }
                goto L_0x000f
            L_0x00ff:
                r0 = move-exception
                java.lang.String r1 = "AmapLocationManager$ActionHandler"
                java.lang.String r2 = "handleMessage RESULT_INSTALLED_MOCK_APP"
                com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x002b }
                goto L_0x000f
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.m.c.handleMessage(android.os.Message):void");
        }
    }

    public m(Context context, Intent intent) {
        this.z = context;
        this.j = intent;
        try {
            if (Looper.myLooper() == null) {
                this.b = new c(this.z.getMainLooper());
            } else {
                this.b = new c();
            }
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "init 1");
        }
        try {
            this.g = new r(this.z);
        } catch (Throwable th2) {
            es.a(th2, "AmapLocationManager", "init 5");
        }
        this.l = new b("amapLocManagerThread", this);
        this.l.setPriority(5);
        this.l.start();
        this.w = a(this.l.getLooper());
        try {
            this.c = new p(this.z, this.b);
        } catch (Throwable th3) {
            es.a(th3, "AmapLocationManager", "init 3");
        }
        if (this.p == null) {
            this.p = new ey();
        }
    }

    private AMapLocationServer a(cs csVar) {
        if (this.a.isLocationCacheEnable()) {
            try {
                return csVar.j();
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "doFirstCacheLoc");
            }
        }
        return null;
    }

    private a a(Looper looper) {
        a aVar;
        synchronized (this.o) {
            this.w = new a(looper);
            aVar = this.w;
        }
        return aVar;
    }

    /* access modifiers changed from: private */
    public void a(int i2) {
        synchronized (this.o) {
            if (this.w != null) {
                this.w.removeMessages(i2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(int i2, Bundle bundle) {
        if (bundle == null) {
            try {
                bundle = new Bundle();
            } catch (Throwable th) {
                Throwable th2 = th;
                boolean z2 = (th2 instanceof IllegalStateException) && th2.getMessage().contains("sending message to a Handler on a dead thread");
                if ((th2 instanceof RemoteException) || z2) {
                    this.h = null;
                    this.A = false;
                }
                es.a(th2, "AmapLocationManager", "sendLocMessage");
                return;
            }
        }
        if (TextUtils.isEmpty(this.s)) {
            this.s = es.b(this.z);
        }
        bundle.putString("c", this.s);
        Message obtain = Message.obtain();
        obtain.what = i2;
        obtain.setData(bundle);
        obtain.replyTo = this.i;
        if (this.h != null) {
            this.h.send(obtain);
        }
    }

    /* access modifiers changed from: private */
    public void a(int i2, Object obj, long j2) {
        synchronized (this.o) {
            if (this.w != null) {
                Message obtain = Message.obtain();
                obtain.what = i2;
                if (obj instanceof Bundle) {
                    obtain.setData((Bundle) obj);
                } else {
                    obtain.obj = obj;
                }
                this.w.sendMessageDelayed(obtain, j2);
            }
        }
    }

    private void a(Intent intent, boolean z2) {
        if (this.z != null) {
            if (Build.VERSION.SDK_INT < 26 || !z2) {
                this.z.startService(intent);
            } else {
                try {
                    this.z.getClass().getMethod("startForegroundService", Intent.class).invoke(this.z, intent);
                } catch (Throwable th) {
                    this.z.startService(intent);
                }
            }
            this.y = true;
        }
    }

    private void a(AMapLocation aMapLocation) {
        try {
            if (aMapLocation.getErrorCode() != 0) {
                aMapLocation.setLocationType(0);
            }
            if (aMapLocation.getErrorCode() == 0) {
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                if ((latitude == 0.0d && longitude == 0.0d) || latitude < -90.0d || latitude > 90.0d || longitude < -180.0d || longitude > 180.0d) {
                    ey.a("errorLatLng", aMapLocation.toStr());
                    aMapLocation.setLocationType(0);
                    aMapLocation.setErrorCode(8);
                    aMapLocation.setLocationDetail("LatLng is error#0802");
                }
            }
            if ("gps".equalsIgnoreCase(aMapLocation.getProvider()) || !this.c.b()) {
                aMapLocation.setAltitude(fa.b(aMapLocation.getAltitude()));
                aMapLocation.setBearing(fa.a(aMapLocation.getBearing()));
                aMapLocation.setSpeed(fa.a(aMapLocation.getSpeed()));
                Iterator<AMapLocationListener> it2 = this.d.iterator();
                while (it2.hasNext()) {
                    try {
                        it2.next().onLocationChanged(aMapLocation);
                    } catch (Throwable th) {
                    }
                }
            }
        } catch (Throwable th2) {
        }
    }

    private synchronized void a(AMapLocation aMapLocation, long j2) {
        if (aMapLocation == null) {
            aMapLocation = new AMapLocation("");
            aMapLocation.setErrorCode(8);
            aMapLocation.setLocationDetail("amapLocation is null#0801");
        }
        if (!"gps".equalsIgnoreCase(aMapLocation.getProvider())) {
            aMapLocation.setProvider("lbs");
        }
        if (this.t == null) {
            this.t = new AMapLocationQualityReport();
        }
        this.t.setLocationMode(this.a.getLocationMode());
        if (this.c != null) {
            this.t.setGPSSatellites(this.c.d());
            this.t.setGpsStatus(this.c.c());
        }
        this.t.setWifiAble(fa.h(this.z));
        this.t.setNetworkType(fa.i(this.z));
        if (aMapLocation.getLocationType() == 1 || "gps".equalsIgnoreCase(aMapLocation.getProvider())) {
            j2 = 0;
        }
        this.t.setNetUseTime(j2);
        this.t.setInstallHighDangerMockApp(E);
        aMapLocation.setLocationQualityReport(this.t);
        try {
            if (this.B) {
                String str = this.x;
                Bundle bundle = new Bundle();
                bundle.putParcelable("loc", aMapLocation);
                bundle.putString("lastLocNb", str);
                a(1014, bundle, 0);
                ey.a(this.z, aMapLocation);
                ey.b(this.z, aMapLocation);
                a(aMapLocation.clone());
            }
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "handlerLocation part3");
        }
        if (!this.m || es.d()) {
            if (this.a.isOnceLocation()) {
                d();
            }
        }
        return;
    }

    private static void a(cs csVar, AMapLocationServer aMapLocationServer) {
        if (aMapLocationServer != null) {
            try {
                if (aMapLocationServer.getErrorCode() == 0) {
                    csVar.b(aMapLocationServer);
                }
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "apsLocation:doFirstAddCache");
            }
        }
    }

    static /* synthetic */ void a(m mVar, Bundle bundle) {
        long j2;
        AMapLocation aMapLocation;
        long j3;
        AMapLocation aMapLocation2 = null;
        if (bundle != null) {
            try {
                bundle.setClassLoader(AMapLocation.class.getClassLoader());
                aMapLocation = (AMapLocation) bundle.getParcelable("loc");
                mVar.x = bundle.getString("nb");
                long j4 = bundle.getLong("netUseTime", 0);
                if (!(aMapLocation == null || aMapLocation.getErrorCode() != 0 || mVar.c == null)) {
                    mVar.c.w = 0;
                    if (!TextUtils.isEmpty(aMapLocation.getAdCode())) {
                        mVar.c.y = aMapLocation;
                    }
                }
                j2 = j4;
            } catch (Throwable th) {
                th = th;
                j3 = 0;
                es.a(th, "AmapLocationManager", "resultLbsLocationSuccess");
                j2 = j3;
                mVar.a(aMapLocation2, j2);
            }
        } else {
            j2 = 0;
            aMapLocation = null;
        }
        try {
            if (mVar.c != null) {
                aMapLocation = mVar.c.a(aMapLocation, mVar.x);
            }
            aMapLocation2 = aMapLocation;
        } catch (Throwable th2) {
            th = th2;
            j3 = j2;
            es.a(th, "AmapLocationManager", "resultLbsLocationSuccess");
            j2 = j3;
            mVar.a(aMapLocation2, j2);
        }
        mVar.a(aMapLocation2, j2);
    }

    static /* synthetic */ void a(m mVar, Message message) {
        try {
            AMapLocation aMapLocation = (AMapLocation) message.obj;
            if (mVar.f && mVar.h != null) {
                Bundle bundle = new Bundle();
                bundle.putBundle("optBundle", es.a(mVar.a));
                mVar.a(0, bundle);
                mVar.f = false;
            }
            mVar.a(aMapLocation, 0);
            if (mVar.D) {
                mVar.a(7, (Bundle) null);
            }
            mVar.a(1025);
            mVar.a(1025, (Object) null, 300000);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "resultGpsLocationSuccess");
        }
    }

    static /* synthetic */ void a(m mVar, AMapLocationListener aMapLocationListener) {
        if (aMapLocationListener == null) {
            throw new IllegalArgumentException("listener参数不能为null");
        }
        if (mVar.d == null) {
            mVar.d = new ArrayList<>();
        }
        if (!mVar.d.contains(aMapLocationListener)) {
            mVar.d.add(aMapLocationListener);
        }
    }

    private AMapLocationServer b(cs csVar) {
        Throwable th;
        AMapLocationServer aMapLocationServer;
        ex exVar;
        long j2;
        AMapLocation aMapLocation;
        String str;
        boolean z2 = false;
        boolean z3 = true;
        try {
            exVar = new ex();
            exVar.a(fa.c());
            String apikey = AMapLocationClientOption.getAPIKEY();
            if (!TextUtils.isEmpty(apikey)) {
                v.a(this.z, apikey);
            }
        } catch (Throwable th2) {
            th = th2;
            aMapLocationServer = null;
        }
        try {
            String umidtoken = UmidtokenInfo.getUmidtoken();
            if (!TextUtils.isEmpty(umidtoken)) {
                x.a(umidtoken);
            }
        } catch (Throwable th3) {
            es.a(th3, "AmapLocationManager", "apsLocation setUmidToken");
        }
        try {
            csVar.a(this.z);
            csVar.a(this.a);
            csVar.i();
        } catch (Throwable th4) {
            es.a(th4, "AmapLocationManager", "initApsBase");
        }
        long j3 = 0;
        boolean v2 = er.v();
        AMapLocationServer a2 = a(csVar);
        if (a2 == null) {
            if (!v2) {
                z2 = true;
            }
            try {
                a2 = csVar.a(z2);
                if (a2 != null) {
                    j3 = a2.k();
                }
                if (!v2) {
                    a(csVar, a2);
                }
                j2 = j3;
            } catch (Throwable th5) {
                th = th5;
                aMapLocationServer = a2;
                try {
                    es.a(th, "AmapLocationManager", "apsLocation");
                    try {
                        csVar.f();
                        return aMapLocationServer;
                    } catch (Throwable th6) {
                        return aMapLocationServer;
                    }
                } catch (Throwable th7) {
                }
            }
        } else {
            j2 = 0;
            z3 = false;
        }
        exVar.b(fa.c());
        exVar.a(a2);
        if (a2 != null) {
            String l2 = a2.l();
            aMapLocation = a2.clone();
            str = l2;
        } else {
            aMapLocation = null;
            str = null;
        }
        try {
            if (this.a.isLocationCacheEnable() && this.g != null) {
                aMapLocation = this.g.a(aMapLocation, str, this.a.getLastLocationLifeCycle());
            }
        } catch (Throwable th8) {
            es.a(th8, "AmapLocationManager", "fixLastLocation");
        }
        try {
            Bundle bundle = new Bundle();
            if (aMapLocation != null) {
                bundle.putParcelable("loc", aMapLocation);
                bundle.putString("nb", a2.l());
                bundle.putLong("netUseTime", j2);
            }
            Message obtain = Message.obtain();
            obtain.setData(bundle);
            obtain.what = 1;
            this.b.sendMessage(obtain);
        } catch (Throwable th9) {
            es.a(th9, "AmapLocationManager", "apsLocation:callback");
        }
        ey.a(this.z, exVar);
        if (z3 && v2) {
            try {
                csVar.c();
                a(csVar, csVar.a(true));
            } catch (Throwable th10) {
                es.a(th10, "AmapLocationManager", "apsLocation:doFirstNetLocate 2");
            }
        }
        try {
            csVar.f();
            return a2;
        } catch (Throwable th11) {
            return a2;
        }
        throw th;
    }

    static /* synthetic */ void b(m mVar, Message message) {
        try {
            Bundle data = message.getData();
            AMapLocation aMapLocation = (AMapLocation) data.getParcelable("loc");
            String string = data.getString("lastLocNb");
            if (aMapLocation != null) {
                AMapLocation aMapLocation2 = null;
                try {
                    if (r.b != null) {
                        aMapLocation2 = r.b.a();
                    } else if (mVar.g != null) {
                        aMapLocation2 = mVar.g.b();
                    }
                    ey.a(aMapLocation2, aMapLocation);
                } catch (Throwable th) {
                }
            }
            if (mVar.g.a(aMapLocation, string)) {
                mVar.g.d();
            }
        } catch (Throwable th2) {
            es.a(th2, "AmapLocationManager", "doSaveLastLocation");
        }
    }

    static /* synthetic */ void b(m mVar, AMapLocationListener aMapLocationListener) {
        if (!mVar.d.isEmpty() && mVar.d.contains(aMapLocationListener)) {
            mVar.d.remove(aMapLocationListener);
        }
        if (mVar.d.isEmpty()) {
            mVar.d();
        }
    }

    private boolean b() {
        boolean z2 = true;
        int i2 = 0;
        do {
            try {
                if (this.h != null) {
                    break;
                }
                Thread.sleep(100);
                i2++;
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "checkAPSManager");
                z2 = false;
            }
        } while (i2 < 50);
        if (this.h == null) {
            Message obtain = Message.obtain();
            Bundle bundle = new Bundle();
            AMapLocation aMapLocation = new AMapLocation("");
            aMapLocation.setErrorCode(10);
            if (!fa.l(this.z.getApplicationContext())) {
                aMapLocation.setLocationDetail("请检查配置文件是否配置服务，并且manifest中service标签是否配置在application标签内#1003");
            } else {
                aMapLocation.setLocationDetail("启动ApsServcie失败#1001");
            }
            bundle.putParcelable("loc", aMapLocation);
            obtain.setData(bundle);
            obtain.what = 1;
            this.b.sendMessage(obtain);
            z2 = false;
        }
        if (!z2) {
            if (!fa.l(this.z.getApplicationContext())) {
                ey.a((String) null, 2103);
            } else {
                ey.a((String) null, 2101);
            }
        }
        return z2;
    }

    /* access modifiers changed from: private */
    public synchronized void c() {
        long j2 = 0;
        synchronized (this) {
            if (this.a == null) {
                this.a = new AMapLocationClientOption();
            }
            if (!this.B) {
                this.B = true;
                switch (this.a.getLocationMode()) {
                    case Battery_Saving:
                        a(1017, (Object) null, 0);
                        a(1016, (Object) null, 0);
                        break;
                    case Device_Sensors:
                        a(1016);
                        a(1015, (Object) null, 0);
                        break;
                    case Hight_Accuracy:
                        a(1015, (Object) null, 0);
                        if (this.a.isGpsFirst() && this.a.isOnceLocation()) {
                            j2 = this.a.getGpsFirstTimeout();
                        }
                        a(1016, (Object) null, j2);
                        break;
                }
            }
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.m.a(android.content.Intent, boolean):void
     arg types: [android.content.Intent, int]
     candidates:
      com.loc.m.a(int, android.os.Bundle):void
      com.loc.m.a(com.amap.api.location.AMapLocation, long):void
      com.loc.m.a(com.loc.cs, com.autonavi.aps.amapapi.model.AMapLocationServer):void
      com.loc.m.a(com.loc.m, android.os.Bundle):void
      com.loc.m.a(com.loc.m, android.os.Message):void
      com.loc.m.a(com.loc.m, com.amap.api.location.AMapLocationListener):void
      com.loc.m.a(com.loc.m, boolean):boolean
      com.loc.m.a(android.content.Intent, boolean):void */
    static /* synthetic */ void c(m mVar, Message message) {
        if (message != null) {
            try {
                Bundle data = message.getData();
                if (data != null) {
                    int i2 = data.getInt("i", 0);
                    Intent g2 = mVar.g();
                    g2.putExtra("i", i2);
                    g2.putExtra("h", (Notification) data.getParcelable("h"));
                    g2.putExtra("g", 1);
                    mVar.a(g2, true);
                }
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "doEnableBackgroundLocation");
            }
        }
    }

    /* access modifiers changed from: private */
    public void d() {
        try {
            a(1025);
            if (this.c != null) {
                this.c.a();
            }
            a(1016);
            this.B = false;
            this.k = 0;
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "stopLocation");
        }
    }

    static /* synthetic */ void d(m mVar) {
        try {
            if (mVar.C) {
                mVar.C = false;
                AMapLocationServer b2 = mVar.b(new cs());
                if (mVar.b()) {
                    Bundle bundle = new Bundle();
                    String str = "0";
                    if (b2 != null && (b2.getLocationType() == 2 || b2.getLocationType() == 4)) {
                        str = "1";
                    }
                    bundle.putBundle("optBundle", es.a(mVar.a));
                    bundle.putString("isCacheLoc", str);
                    mVar.a(0, bundle);
                }
            } else {
                try {
                    if (mVar.q && !mVar.isStarted() && !mVar.v) {
                        mVar.v = true;
                        mVar.f();
                    }
                } catch (Throwable th) {
                    mVar.v = true;
                    es.a(th, "AmapLocationManager", "doLBSLocation reStartService");
                }
                if (mVar.b()) {
                    mVar.v = false;
                    Bundle bundle2 = new Bundle();
                    bundle2.putBundle("optBundle", es.a(mVar.a));
                    bundle2.putString("d", UmidtokenInfo.getUmidtoken());
                    if (!mVar.c.b()) {
                        mVar.a(1, bundle2);
                    }
                }
            }
            try {
                if (!mVar.a.isOnceLocation()) {
                    mVar.e();
                }
            } catch (Throwable th2) {
            }
        } catch (Throwable th3) {
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.m.a(android.content.Intent, boolean):void
     arg types: [android.content.Intent, int]
     candidates:
      com.loc.m.a(int, android.os.Bundle):void
      com.loc.m.a(com.amap.api.location.AMapLocation, long):void
      com.loc.m.a(com.loc.cs, com.autonavi.aps.amapapi.model.AMapLocationServer):void
      com.loc.m.a(com.loc.m, android.os.Bundle):void
      com.loc.m.a(com.loc.m, android.os.Message):void
      com.loc.m.a(com.loc.m, com.amap.api.location.AMapLocationListener):void
      com.loc.m.a(com.loc.m, boolean):boolean
      com.loc.m.a(android.content.Intent, boolean):void */
    static /* synthetic */ void d(m mVar, Message message) {
        if (message != null) {
            try {
                Bundle data = message.getData();
                if (data != null) {
                    boolean z2 = data.getBoolean("j", true);
                    Intent g2 = mVar.g();
                    g2.putExtra("j", z2);
                    g2.putExtra("g", 2);
                    mVar.a(g2, false);
                }
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "doDisableBackgroundLocation");
            }
        }
    }

    private void e() {
        long j2 = 1000;
        if (this.a.getLocationMode() != AMapLocationClientOption.AMapLocationMode.Device_Sensors) {
            if (this.a.getInterval() >= 1000) {
                j2 = this.a.getInterval();
            }
            a(1016, (Object) null, j2);
        }
    }

    /* access modifiers changed from: private */
    public void f() {
        try {
            if (this.i == null) {
                this.i = new Messenger(this.b);
            }
            this.z.bindService(g(), this.G, 1);
        } catch (Throwable th) {
        }
    }

    static /* synthetic */ void f(m mVar) {
        p pVar = mVar.c;
        AMapLocationClientOption aMapLocationClientOption = mVar.a;
        if (aMapLocationClientOption == null) {
            aMapLocationClientOption = new AMapLocationClientOption();
        }
        pVar.c = aMapLocationClientOption;
        if (!(pVar.c.getLocationMode() == AMapLocationClientOption.AMapLocationMode.Device_Sensors || pVar.a == null)) {
            pVar.a.removeMessages(8);
        }
        if (pVar.r != pVar.c.getGeoLanguage()) {
            synchronized (pVar.o) {
                pVar.y = null;
            }
        }
        pVar.r = pVar.c.getGeoLanguage();
        if (mVar.B && !mVar.a.getLocationMode().equals(mVar.n)) {
            mVar.d();
            mVar.c();
        }
        mVar.n = mVar.a.getLocationMode();
        if (mVar.p != null) {
            if (mVar.a.isOnceLocation()) {
                mVar.p.a(mVar.z, 0);
            } else {
                mVar.p.a(mVar.z, 1);
            }
            mVar.p.a(mVar.z, mVar.a);
        }
    }

    private Intent g() {
        if (this.j == null) {
            this.j = new Intent(this.z, APSService.class);
        }
        String str = "";
        try {
            str = !TextUtils.isEmpty(AMapLocationClientOption.getAPIKEY()) ? AMapLocationClientOption.getAPIKEY() : u.f(this.z);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "startServiceImpl p2");
        }
        this.j.putExtra("a", str);
        this.j.putExtra("b", u.c(this.z));
        this.j.putExtra("d", UmidtokenInfo.getUmidtoken());
        this.j.putExtra("f", AMapLocationClientOption.isDownloadCoordinateConvertLibrary());
        return this.j;
    }

    static /* synthetic */ void g(m mVar) {
        try {
            if (mVar.h != null) {
                mVar.k = 0;
                Bundle bundle = new Bundle();
                bundle.putBundle("optBundle", es.a(mVar.a));
                mVar.a(2, bundle);
                return;
            }
            mVar.k++;
            if (mVar.k < 10) {
                mVar.a(1008, (Object) null, 50);
            }
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "startAssistantLocationImpl");
        }
    }

    static /* synthetic */ void h(m mVar) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("optBundle", es.a(mVar.a));
            mVar.a(3, bundle);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "stopAssistantLocationImpl");
        }
    }

    /* access modifiers changed from: package-private */
    public final void a() {
        a(12, (Bundle) null);
        this.C = true;
        this.f = true;
        this.A = false;
        this.q = false;
        d();
        if (this.p != null) {
            this.p.b(this.z);
        }
        ey.a(this.z);
        if (this.r != null) {
            this.r.d.sendEmptyMessage(11);
        } else if (this.G != null) {
            this.z.unbindService(this.G);
        }
        try {
            if (this.y) {
                this.z.stopService(g());
            }
        } catch (Throwable th) {
        }
        this.y = false;
        if (this.d != null) {
            this.d.clear();
            this.d = null;
        }
        this.G = null;
        synchronized (this.o) {
            if (this.w != null) {
                this.w.removeCallbacksAndMessages(null);
            }
            this.w = null;
        }
        if (this.l != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                try {
                    ew.a(this.l, HandlerThread.class, "quitSafely", new Object[0]);
                } catch (Throwable th2) {
                    this.l.quit();
                }
            } else {
                this.l.quit();
            }
        }
        this.l = null;
        if (this.b != null) {
            this.b.removeCallbacksAndMessages(null);
        }
        if (this.g != null) {
            this.g.c();
            this.g = null;
        }
    }

    public final void disableBackgroundLocation(boolean z2) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("j", z2);
            a(1024, bundle, 0);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "disableBackgroundLocation");
        }
    }

    public final void enableBackgroundLocation(int i2, Notification notification) {
        if (i2 != 0 && notification != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putInt("i", i2);
                bundle.putParcelable("h", notification);
                a((int) DJIDiagnosticsError.Camera.INTERNAL_STORAGE_INVALID, bundle, 0);
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "disableBackgroundLocation");
            }
        }
    }

    public final AMapLocation getLastKnownLocation() {
        AMapLocation aMapLocation = null;
        try {
            if (!(this.g == null || (aMapLocation = this.g.b()) == null)) {
                aMapLocation.setTrustedLevel(3);
            }
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "getLastKnownLocation");
        }
        return aMapLocation;
    }

    public final boolean isStarted() {
        return this.A;
    }

    public final void onDestroy() {
        try {
            if (this.F != null) {
                this.F.b();
                this.F = null;
            }
            a(1011, (Object) null, 0);
            this.m = true;
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "onDestroy");
        }
    }

    public final void setLocationListener(AMapLocationListener aMapLocationListener) {
        try {
            a(1002, aMapLocationListener, 0);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "setLocationListener");
        }
    }

    public final void setLocationOption(AMapLocationClientOption aMapLocationClientOption) {
        try {
            a(1018, aMapLocationClientOption.clone(), 0);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "setLocationOption");
        }
    }

    public final void startAssistantLocation() {
        try {
            a(1008, (Object) null, 0);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "startAssistantLocation");
        }
    }

    public final void startAssistantLocation(WebView webView) {
        if (this.F == null) {
            this.F = new q(this.z, webView);
        }
        this.F.a();
    }

    public final void startLocation() {
        try {
            a(1003, (Object) null, 0);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "startLocation");
        }
    }

    public final void stopAssistantLocation() {
        try {
            if (this.F != null) {
                this.F.b();
                this.F = null;
            }
            a(1009, (Object) null, 0);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "stopAssistantLocation");
        }
    }

    public final void stopLocation() {
        try {
            a(1004, (Object) null, 0);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "stopLocation");
        }
    }

    public final void unRegisterLocationListener(AMapLocationListener aMapLocationListener) {
        try {
            a(1005, aMapLocationListener, 0);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "unRegisterLocationListener");
        }
    }
}
