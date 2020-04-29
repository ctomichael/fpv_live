package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresPermission;
import com.amap.location.common.log.ALLog;
import com.dji.permission.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: SatelliteStatusManager */
public class dd {
    /* access modifiers changed from: private */
    public final List<a> a = new CopyOnWriteArrayList();
    /* access modifiers changed from: private */
    public cz b;
    private Context c;
    private b d = new b();
    /* access modifiers changed from: private */
    public GnssStatus.Callback e;
    /* access modifiers changed from: private */
    public GpsStatus.Listener f;
    /* access modifiers changed from: private */
    public GpsStatus g;

    /* compiled from: SatelliteStatusManager */
    private static class a {
        cu a;
        private Handler b;

        /* renamed from: com.amap.openapi.dd$a$a  reason: collision with other inner class name */
        /* compiled from: SatelliteStatusManager */
        private static class C0012a extends Handler {
            private cu a;

            C0012a(cu cuVar, Looper looper) {
                super(looper);
                this.a = cuVar;
            }

            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        this.a.a();
                        return;
                    case 2:
                        this.a.b();
                        return;
                    case 3:
                        this.a.a(((Integer) message.obj).intValue());
                        return;
                    case 4:
                        c cVar = (c) message.obj;
                        this.a.a(cVar.a, cVar.b, cVar.c, cVar.d);
                        return;
                    default:
                        return;
                }
            }
        }

        a(cu cuVar, Looper looper) {
            this.a = cuVar;
            this.b = new C0012a(this.a, looper == null ? Looper.getMainLooper() : looper);
        }

        /* access modifiers changed from: package-private */
        public void a(int i, Object obj) {
            Message obtainMessage = this.b.obtainMessage();
            obtainMessage.what = i;
            obtainMessage.obj = obj;
            obtainMessage.sendToTarget();
        }

        /* access modifiers changed from: package-private */
        public boolean a(cu cuVar, Looper looper) {
            if (looper == null) {
                looper = Looper.getMainLooper();
            }
            return this.a == cuVar && this.b.getLooper() == looper;
        }
    }

    /* compiled from: SatelliteStatusManager */
    private class b extends BroadcastReceiver {
        private b() {
        }

        public void onReceive(Context context, Intent intent) {
            if (cr.a(context).a("gps")) {
                synchronized (dd.this.a) {
                    if (dd.this.a.size() > 0) {
                        try {
                            if (Build.VERSION.SDK_INT >= 24) {
                                if (dd.this.e != null) {
                                    dd.this.b.b(dd.this.e);
                                    dd.this.b.a(dd.this.e);
                                }
                            } else if (dd.this.f != null) {
                                dd.this.b.b(dd.this.f);
                                dd.this.b.a(dd.this.f);
                            }
                        } catch (SecurityException e) {
                            try {
                                ALLog.trace("@_24_5_@", "卫星接口权限异常", e);
                            } catch (SecurityException e2) {
                                ALLog.trace("@_24_5_@", "卫星接口权限异常", e2);
                            }
                        }
                    }
                }
                return;
            }
            return;
        }
    }

    /* compiled from: SatelliteStatusManager */
    private class c {
        int a;
        int b;
        float c;
        List<ct> d;

        public c(int i, int i2, float f, List<ct> list) {
            this.a = i;
            this.b = i2;
            this.c = f;
            this.d = list;
        }
    }

    public dd(cz czVar, Context context) {
        this.b = czVar;
        this.c = context;
        if (Build.VERSION.SDK_INT >= 24) {
            this.e = new GnssStatus.Callback() {
                /* class com.amap.openapi.dd.AnonymousClass1 */

                public void onFirstFix(int i) {
                    dd.this.a(i);
                }

                public void onSatelliteStatusChanged(GnssStatus gnssStatus) {
                    dd.this.a(gnssStatus);
                }

                public void onStarted() {
                    dd.this.a();
                }

                public void onStopped() {
                    dd.this.b();
                }
            };
        } else {
            this.f = new GpsStatus.Listener() {
                /* class com.amap.openapi.dd.AnonymousClass2 */

                public void onGpsStatusChanged(int i) {
                    if (i == 1) {
                        dd.this.a();
                    } else if (i == 2) {
                        dd.this.b();
                    } else if (i == 3) {
                        if (dd.this.g == null) {
                            GpsStatus unused = dd.this.g = dd.this.b.a((GpsStatus) null);
                        } else {
                            dd.this.b.a(dd.this.g);
                        }
                        if (dd.this.g != null) {
                            dd.this.a(dd.this.g.getTimeToFirstFix());
                        }
                    } else if (i == 4) {
                        if (dd.this.g == null) {
                            GpsStatus unused2 = dd.this.g = dd.this.b.a((GpsStatus) null);
                        } else {
                            dd.this.b.a(dd.this.g);
                        }
                        if (dd.this.g != null) {
                            dd.this.a(dd.this.g.getSatellites());
                        }
                    }
                }
            };
        }
    }

    /* access modifiers changed from: private */
    public void a() {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a(1, (Object) null);
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(int i) {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a(3, Integer.valueOf(i));
            }
        }
    }

    private void a(int i, int i2, float f2, List<ct> list) {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a(4, new c(i, i2, f2, list));
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(GnssStatus gnssStatus) {
        float f2;
        int i;
        try {
            if (Build.VERSION.SDK_INT >= 24 && gnssStatus != null) {
                int satelliteCount = gnssStatus.getSatelliteCount();
                float f3 = 0.0f;
                ArrayList arrayList = new ArrayList();
                int i2 = 0;
                int i3 = 0;
                while (i2 < satelliteCount) {
                    arrayList.add(new ct(gnssStatus.usedInFix(i2), gnssStatus.getSvid(i2), gnssStatus.getCn0DbHz(i2), gnssStatus.getElevationDegrees(i2), gnssStatus.getAzimuthDegrees(i2), gnssStatus.getConstellationType(i2)));
                    if (gnssStatus.usedInFix(i2)) {
                        i = i3 + 1;
                        f2 = gnssStatus.getCn0DbHz(i2) + f3;
                    } else {
                        f2 = f3;
                        i = i3;
                    }
                    i2++;
                    f3 = f2;
                    i3 = i;
                }
                if (i3 != 0) {
                    f3 /= (float) i3;
                }
                a(i3, satelliteCount, f3, arrayList);
            }
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private */
    public void a(Iterable<GpsSatellite> iterable) {
        float f2;
        int i;
        if (iterable != null) {
            float f3 = 0.0f;
            try {
                ArrayList arrayList = new ArrayList();
                int i2 = 0;
                int i3 = 0;
                for (GpsSatellite gpsSatellite : iterable) {
                    if (gpsSatellite != null) {
                        int i4 = i2 + 1;
                        arrayList.add(new ct(gpsSatellite.usedInFix(), gpsSatellite.getPrn(), gpsSatellite.getSnr(), gpsSatellite.getElevation(), gpsSatellite.getAzimuth(), 0));
                        if (gpsSatellite.usedInFix()) {
                            i = i3 + 1;
                            f2 = gpsSatellite.getSnr() + f3;
                        } else {
                            f2 = f3;
                            i = i3;
                        }
                        f3 = f2;
                        i2 = i4;
                        i3 = i;
                    }
                }
                if (i3 != 0) {
                    f3 /= (float) i3;
                }
                a(i3, i2, f3, arrayList);
            } catch (Exception e2) {
            }
        }
    }

    private a b(cu cuVar) {
        for (a aVar : this.a) {
            if (aVar.a == cuVar) {
                return aVar;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void b() {
        synchronized (this.a) {
            for (a aVar : this.a) {
                aVar.a(2, (Object) null);
            }
        }
    }

    public void a(cu cuVar) {
        if (cuVar != null) {
            synchronized (this.a) {
                a b2 = b(cuVar);
                if (b2 != null) {
                    boolean remove = this.a.remove(b2);
                    if (this.a.size() == 0 && remove) {
                        try {
                            if (Build.VERSION.SDK_INT >= 24) {
                                if (this.e != null) {
                                    this.b.b(this.e);
                                }
                            } else if (this.f != null) {
                                this.b.b(this.f);
                            }
                            this.c.unregisterReceiver(this.d);
                        } catch (Exception e2) {
                            ALLog.trace("@_24_5_@", "@_24_5_2_@", e2);
                        }
                    }
                }
            }
            return;
        }
        return;
    }

    @RequiresPermission(Permission.ACCESS_FINE_LOCATION)
    public boolean a(cu cuVar, Looper looper) {
        boolean z = false;
        if (cuVar == null) {
            return false;
        }
        synchronized (this.a) {
            a b2 = b(cuVar);
            if (b2 != null) {
                boolean a2 = b2.a(cuVar, looper);
                return a2;
            }
            a aVar = new a(cuVar, looper);
            this.a.add(aVar);
            if (this.a.size() != 1) {
                return true;
            }
            try {
                if (Build.VERSION.SDK_INT >= 24) {
                    if (this.e != null) {
                        z = this.b.a(this.e);
                    }
                } else if (this.f != null) {
                    z = this.b.a(this.f);
                }
            } catch (SecurityException e2) {
                ALLog.trace("@_24_5_@", "卫星接口权限异常", e2);
            }
            if (!z) {
                this.a.remove(aVar);
            } else {
                try {
                    this.c.registerReceiver(this.d, new IntentFilter("android.location.PROVIDERS_CHANGED"));
                } catch (Exception e3) {
                    ALLog.trace("@_24_6_@", "@_24_6_1_@", e3);
                }
            }
        }
        return z;
    }
}
