package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import com.amap.location.common.model.CellStatus;
import com.amap.location.common.util.f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: CellCollector */
public class l {
    private static final String a = l.class.getSimpleName();
    /* access modifiers changed from: private */
    public Context b;
    /* access modifiers changed from: private */
    public Handler c;
    /* access modifiers changed from: private */
    public final ReentrantReadWriteLock d;
    private TelephonyManager e;
    /* access modifiers changed from: private */
    public CellLocation f;
    /* access modifiers changed from: private */
    public long g;
    /* access modifiers changed from: private */
    public SignalStrength h;
    /* access modifiers changed from: private */
    public boolean i;
    private CellLocation j;
    private CellInfo k;
    private Location l;
    private q m = new q();
    private q n = new q();
    private final List<CellStatus.HistoryCell> o = new ArrayList(3);
    private BroadcastReceiver p = new BroadcastReceiver() {
        /* class com.amap.openapi.l.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            String action;
            boolean z = false;
            if (intent != null && (action = intent.getAction()) != null) {
                char c = 65535;
                switch (action.hashCode()) {
                    case -1076576821:
                        if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                            c = 0;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        l lVar = l.this;
                        if (!ax.a(l.this.b)) {
                            z = true;
                        }
                        boolean unused = lVar.i = z;
                        if (!l.this.i) {
                            CellLocation unused2 = l.this.f = (CellLocation) null;
                            long unused3 = l.this.g = 0;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    };
    private PhoneStateListener q = new PhoneStateListener() {
        /* class com.amap.openapi.l.AnonymousClass2 */

        public void onCellInfoChanged(List<CellInfo> list) {
            l.this.d.readLock().lock();
            try {
                if (l.this.c != null) {
                    l.this.c.post(new Runnable() {
                        /* class com.amap.openapi.l.AnonymousClass2.AnonymousClass3 */

                        public void run() {
                            l.this.e();
                        }
                    });
                }
            } finally {
                l.this.d.readLock().unlock();
            }
        }

        public void onCellLocationChanged(final CellLocation cellLocation) {
            l.this.d.readLock().lock();
            try {
                if (l.this.c != null) {
                    l.this.c.post(new Runnable() {
                        /* class com.amap.openapi.l.AnonymousClass2.AnonymousClass1 */

                        public void run() {
                            CellLocation unused = l.this.f = cellLocation;
                            long unused2 = l.this.g = SystemClock.elapsedRealtime();
                            l.this.e();
                        }
                    });
                }
            } finally {
                l.this.d.readLock().unlock();
            }
        }

        public void onSignalStrengthsChanged(final SignalStrength signalStrength) {
            l.this.d.readLock().lock();
            try {
                if (l.this.c != null) {
                    l.this.c.post(new Runnable() {
                        /* class com.amap.openapi.l.AnonymousClass2.AnonymousClass2 */

                        public void run() {
                            SignalStrength unused = l.this.h = signalStrength;
                            l.this.e();
                        }
                    });
                }
            } finally {
                l.this.d.readLock().unlock();
            }
        }
    };

    public l(Context context, Looper looper) {
        this.b = context;
        this.e = (TelephonyManager) this.b.getSystemService("phone");
        this.c = new Handler(looper);
        this.d = new ReentrantReadWriteLock();
    }

    private void a(q qVar) {
        synchronized (this.o) {
            Iterator<r> it2 = qVar.c.iterator();
            while (it2.hasNext()) {
                r next = it2.next();
                if (1 == next.b) {
                    CellStatus.HistoryCell historyCell = new CellStatus.HistoryCell();
                    historyCell.lastUpdateTimeMills = SystemClock.elapsedRealtime();
                    historyCell.type = next.a;
                    switch (next.a) {
                        case 1:
                            if (next.f != null) {
                                w wVar = (w) next.f;
                                if (f.a(wVar.c) && f.b(wVar.d)) {
                                    historyCell.lac = wVar.c;
                                    historyCell.cid = wVar.d;
                                    historyCell.rssi = wVar.e;
                                    f.a(historyCell, this.o, 3);
                                    break;
                                }
                            } else {
                                continue;
                            }
                        case 2:
                            if (next.f != null) {
                                p pVar = (p) next.f;
                                if (f.c(pVar.a) && f.d(pVar.b) && f.e(pVar.c)) {
                                    historyCell.sid = pVar.a;
                                    historyCell.nid = pVar.b;
                                    historyCell.bid = pVar.c;
                                    historyCell.rssi = pVar.f;
                                    f.a(historyCell, this.o, 3);
                                    break;
                                }
                            } else {
                                continue;
                            }
                        case 3:
                            if (next.f != null) {
                                x xVar = (x) next.f;
                                if (f.a(xVar.c) && f.b(xVar.d)) {
                                    historyCell.lac = xVar.c;
                                    historyCell.cid = xVar.d;
                                    historyCell.rssi = xVar.f;
                                    f.a(historyCell, this.o, 3);
                                    break;
                                }
                            } else {
                                continue;
                            }
                        case 4:
                            if (next.f != null) {
                                z zVar = (z) next.f;
                                if (f.a(zVar.c) && f.b(zVar.d)) {
                                    historyCell.lac = zVar.c;
                                    historyCell.cid = zVar.d;
                                    historyCell.rssi = zVar.f;
                                    f.a(historyCell, this.o, 3);
                                    break;
                                }
                            } else {
                                continue;
                            }
                        default:
                            continue;
                    }
                }
            }
            this.m.d.clear();
            this.m.d.addAll(this.o);
        }
    }

    private boolean b(Location location) {
        return location.distanceTo(this.l) > ((location.getSpeed() > 10.0f ? 1 : (location.getSpeed() == 10.0f ? 0 : -1)) > 0 ? 2000.0f : (location.getSpeed() > 2.0f ? 1 : (location.getSpeed() == 2.0f ? 0 : -1)) > 0 ? 500.0f : 100.0f);
    }

    private CellLocation c() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (!((this.f == null || this.g == 0 || elapsedRealtime - this.g > 1500) ? false : true)) {
            try {
                this.f = this.e != null ? this.e.getCellLocation() : null;
                this.g = elapsedRealtime;
            } catch (Exception e2) {
                this.f = null;
                this.g = 0;
            }
        }
        return this.f;
    }

    private List<CellInfo> d() {
        try {
            if (this.e == null || Build.VERSION.SDK_INT < 17) {
                return null;
            }
            return this.e.getAllCellInfo();
        } catch (Exception e2) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void e() {
        if (this.i) {
            try {
                CellLocation c2 = c();
                if ((c2 instanceof CdmaCellLocation) && -1 == ((CdmaCellLocation) c2).getNetworkId()) {
                    c2 = null;
                }
                List<CellInfo> d2 = d();
                CellInfo a2 = d2 != null ? ax.a(d2) : null;
                if (c2 != null || a2 != null) {
                    ax.a(this.b, this.n, c2, this.h, d2);
                    as.a(this.n.c);
                }
            } catch (Throwable th) {
            }
        }
    }

    public q a(Location location) {
        if (!this.i) {
            return null;
        }
        CellLocation c2 = c();
        if ((c2 instanceof CdmaCellLocation) && -1 == ((CdmaCellLocation) c2).getNetworkId()) {
            c2 = null;
        }
        List<CellInfo> d2 = d();
        CellInfo a2 = d2 != null ? ax.a(d2) : null;
        if (c2 == null && a2 == null) {
            return null;
        }
        if (!(this.l == null || b(location) || !ax.a(c2, this.j) || !ax.a(a2, this.k))) {
            return null;
        }
        ax.a(this.b, this.m, c2, this.h, d2);
        this.j = c2;
        this.k = a2;
        this.l = location;
        as.a(this.m.c);
        a(this.m);
        return this.m;
    }

    public void a() {
        this.i = !ax.a(this.b);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        try {
            this.b.registerReceiver(this.p, intentFilter, null, this.c);
            if (this.e != null) {
                int i2 = 272;
                if (Build.VERSION.SDK_INT >= 17) {
                    i2 = 1296;
                }
                this.e.listen(this.q, i2);
            }
        } catch (Exception e2) {
        }
    }

    public void b() {
        try {
            this.b.unregisterReceiver(this.p);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (this.e != null) {
            this.e.listen(this.q, 0);
        }
        this.d.writeLock().lock();
        try {
            this.c.removeCallbacksAndMessages(null);
            this.c = null;
        } finally {
            this.d.writeLock().unlock();
        }
    }
}
