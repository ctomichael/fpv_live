package com.loc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.HandlerThread;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import dji.midware.Lifecycle;
import dji.sdksharedlib.keycatalog.extension.Utils;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi"})
/* compiled from: CgiManager */
public final class ee {
    int a = 0;
    ArrayList<ed> b = new ArrayList<>();
    TelephonyManager c = null;
    long d = 0;
    CellLocation e;
    boolean f = false;
    PhoneStateListener g = null;
    String h = null;
    boolean i = false;
    StringBuilder j = null;
    HandlerThread k = null;
    private Context l;
    private String m = null;
    private ArrayList<ed> n = new ArrayList<>();
    private int o = -113;
    private ec p = null;
    private Object q;
    private int r = 0;
    /* access modifiers changed from: private */
    public long s = 0;
    /* access modifiers changed from: private */
    public boolean t = false;
    /* access modifiers changed from: private */
    public Object u = new Object();

    /* compiled from: CgiManager */
    class a extends HandlerThread {
        public a(String str) {
            super(str);
        }

        /* access modifiers changed from: protected */
        public final void onLooperPrepared() {
            try {
                super.onLooperPrepared();
                synchronized (ee.this.u) {
                    if (!ee.this.t) {
                        ee eeVar = ee.this;
                        eeVar.g = new PhoneStateListener() {
                            /* class com.loc.ee.AnonymousClass1 */

                            public final void onCellLocationChanged(CellLocation cellLocation) {
                                try {
                                    if (ee.this.a(cellLocation)) {
                                        ee.this.e = cellLocation;
                                        ee.this.f = true;
                                        long unused = ee.this.s = fa.c();
                                    }
                                } catch (Throwable th) {
                                }
                            }

                            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                             method: com.loc.ee.a(boolean, boolean):void
                             arg types: [int, int]
                             candidates:
                              com.loc.ee.a(com.loc.ee, long):long
                              com.loc.ee.a(android.telephony.CellInfoCdma, boolean):com.loc.ed
                              com.loc.ee.a(android.telephony.CellInfoGsm, boolean):com.loc.ed
                              com.loc.ee.a(android.telephony.CellInfoLte, boolean):com.loc.ed
                              com.loc.ee.a(android.telephony.CellInfoWcdma, boolean):com.loc.ed
                              com.loc.ee.a(android.telephony.NeighboringCellInfo, java.lang.String[]):com.loc.ed
                              com.loc.ee.a(com.loc.ee, int):void
                              com.loc.ee.a(int, int):boolean
                              com.loc.ee.a(boolean, boolean):void */
                            public final void onServiceStateChanged(ServiceState serviceState) {
                                try {
                                    switch (serviceState.getState()) {
                                        case 0:
                                            ee.this.a(false, false);
                                            return;
                                        case 1:
                                            ee.this.i();
                                            return;
                                        default:
                                            return;
                                    }
                                } catch (Throwable th) {
                                }
                            }

                            public final void onSignalStrengthChanged(int i) {
                                int i2 = -113;
                                try {
                                    switch (ee.this.a) {
                                        case 1:
                                            i2 = fa.a(i);
                                            break;
                                        case 2:
                                            i2 = fa.a(i);
                                            break;
                                    }
                                    ee.a(ee.this, i2);
                                } catch (Throwable th) {
                                }
                            }

                            public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
                                if (signalStrength != null) {
                                    int i = -113;
                                    try {
                                        switch (ee.this.a) {
                                            case 1:
                                                i = fa.a(signalStrength.getGsmSignalStrength());
                                                break;
                                            case 2:
                                                i = signalStrength.getCdmaDbm();
                                                break;
                                        }
                                        ee.a(ee.this, i);
                                    } catch (Throwable th) {
                                    }
                                }
                            }
                        };
                        int i = 0;
                        if (fa.d() < 7) {
                            try {
                                i = ew.b("android.telephony.PhoneStateListener", "LISTEN_SIGNAL_STRENGTH");
                            } catch (Throwable th) {
                            }
                        } else {
                            try {
                                i = ew.b("android.telephony.PhoneStateListener", "LISTEN_SIGNAL_STRENGTHS");
                            } catch (Throwable th2) {
                            }
                        }
                        if (i == 0) {
                            try {
                                eeVar.c.listen(eeVar.g, 16);
                            } catch (Throwable th3) {
                            }
                        } else {
                            try {
                                eeVar.c.listen(eeVar.g, i | 16);
                            } catch (Throwable th4) {
                            }
                        }
                    }
                }
            } catch (Throwable th5) {
            }
        }

        public final void run() {
            try {
                super.run();
            } catch (Throwable th) {
            }
        }
    }

    public ee(Context context) {
        this.l = context;
        if (this.c == null) {
            this.c = (TelephonyManager) fa.a(this.l, "phone");
        }
        if (this.c != null) {
            try {
                this.a = c(this.c.getCellLocation());
            } catch (SecurityException e2) {
                this.h = e2.getMessage();
            } catch (Throwable th) {
                this.h = null;
                es.a(th, "CgiManager", "CgiManager");
                this.a = 0;
            }
            try {
                this.r = r();
                switch (this.r) {
                    case 1:
                        this.q = fa.a(this.l, "phone_msim");
                        break;
                    case 2:
                        this.q = fa.a(this.l, "phone2");
                        break;
                    default:
                        this.q = fa.a(this.l, "phone2");
                        break;
                }
            } catch (Throwable th2) {
            }
            if (this.k == null) {
                this.k = new a("listenerPhoneStateThread");
                this.k.start();
            }
        }
        this.p = new ec();
    }

    private CellLocation a(Object obj, String str, Object... objArr) {
        if (obj == null) {
            return null;
        }
        try {
            Object a2 = ew.a(obj, str, objArr);
            CellLocation cellLocation = a2 != null ? (CellLocation) a2 : null;
            if (b(cellLocation)) {
                return cellLocation;
            }
        } catch (Throwable th) {
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0096, code lost:
        r5 = r0;
     */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.telephony.CellLocation a(java.util.List<android.telephony.CellInfo> r8) {
        /*
            r7 = this;
            r6 = 0
            if (r8 == 0) goto L_0x0009
            boolean r0 = r8.isEmpty()
            if (r0 == 0) goto L_0x000a
        L_0x0009:
            return r6
        L_0x000a:
            r0 = 0
            r1 = r0
        L_0x000c:
            int r0 = r8.size()
            if (r1 >= r0) goto L_0x00bd
            java.lang.Object r0 = r8.get(r1)
            android.telephony.CellInfo r0 = (android.telephony.CellInfo) r0
            if (r0 == 0) goto L_0x002e
            boolean r2 = r0.isRegistered()     // Catch:{ Throwable -> 0x00b6 }
            boolean r3 = r0 instanceof android.telephony.CellInfoCdma     // Catch:{ Throwable -> 0x00b6 }
            if (r3 == 0) goto L_0x0056
            android.telephony.CellInfoCdma r0 = (android.telephony.CellInfoCdma) r0     // Catch:{ Throwable -> 0x00b6 }
            android.telephony.CellIdentityCdma r3 = r0.getCellIdentity()     // Catch:{ Throwable -> 0x00b6 }
            boolean r3 = a(r3)     // Catch:{ Throwable -> 0x00b6 }
            if (r3 != 0) goto L_0x0032
        L_0x002e:
            int r0 = r1 + 1
            r1 = r0
            goto L_0x000c
        L_0x0032:
            com.loc.ed r0 = r7.a(r0, r2)     // Catch:{ Throwable -> 0x00b6 }
            r5 = r0
        L_0x0037:
            if (r5 == 0) goto L_0x00b9
            int r0 = r5.k     // Catch:{ Throwable -> 0x00aa }
            r1 = 2
            if (r0 != r1) goto L_0x0098
            android.telephony.cdma.CdmaCellLocation r0 = new android.telephony.cdma.CdmaCellLocation     // Catch:{ Throwable -> 0x00aa }
            r0.<init>()     // Catch:{ Throwable -> 0x00aa }
            int r1 = r5.i     // Catch:{ Throwable -> 0x00ae }
            int r2 = r5.e     // Catch:{ Throwable -> 0x00ae }
            int r3 = r5.f     // Catch:{ Throwable -> 0x00ae }
            int r4 = r5.g     // Catch:{ Throwable -> 0x00ae }
            int r5 = r5.h     // Catch:{ Throwable -> 0x00ae }
            r0.setCellLocationData(r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x00ae }
            r2 = r0
            r1 = r6
        L_0x0052:
            if (r2 != 0) goto L_0x00a7
            r6 = r1
            goto L_0x0009
        L_0x0056:
            boolean r3 = r0 instanceof android.telephony.CellInfoGsm     // Catch:{ Throwable -> 0x00b6 }
            if (r3 == 0) goto L_0x006c
            android.telephony.CellInfoGsm r0 = (android.telephony.CellInfoGsm) r0     // Catch:{ Throwable -> 0x00b6 }
            android.telephony.CellIdentityGsm r3 = r0.getCellIdentity()     // Catch:{ Throwable -> 0x00b6 }
            boolean r3 = a(r3)     // Catch:{ Throwable -> 0x00b6 }
            if (r3 == 0) goto L_0x002e
            com.loc.ed r0 = a(r0, r2)     // Catch:{ Throwable -> 0x00b6 }
            r5 = r0
            goto L_0x0037
        L_0x006c:
            boolean r3 = r0 instanceof android.telephony.CellInfoWcdma     // Catch:{ Throwable -> 0x00b6 }
            if (r3 == 0) goto L_0x0082
            android.telephony.CellInfoWcdma r0 = (android.telephony.CellInfoWcdma) r0     // Catch:{ Throwable -> 0x00b6 }
            android.telephony.CellIdentityWcdma r3 = r0.getCellIdentity()     // Catch:{ Throwable -> 0x00b6 }
            boolean r3 = a(r3)     // Catch:{ Throwable -> 0x00b6 }
            if (r3 == 0) goto L_0x002e
            com.loc.ed r0 = a(r0, r2)     // Catch:{ Throwable -> 0x00b6 }
            r5 = r0
            goto L_0x0037
        L_0x0082:
            boolean r3 = r0 instanceof android.telephony.CellInfoLte     // Catch:{ Throwable -> 0x00b6 }
            if (r3 == 0) goto L_0x00bb
            android.telephony.CellInfoLte r0 = (android.telephony.CellInfoLte) r0     // Catch:{ Throwable -> 0x00b6 }
            android.telephony.CellIdentityLte r3 = r0.getCellIdentity()     // Catch:{ Throwable -> 0x00b6 }
            boolean r3 = a(r3)     // Catch:{ Throwable -> 0x00b6 }
            if (r3 == 0) goto L_0x002e
            com.loc.ed r0 = a(r0, r2)     // Catch:{ Throwable -> 0x00b6 }
        L_0x0096:
            r5 = r0
            goto L_0x0037
        L_0x0098:
            android.telephony.gsm.GsmCellLocation r0 = new android.telephony.gsm.GsmCellLocation     // Catch:{ Throwable -> 0x00aa }
            r0.<init>()     // Catch:{ Throwable -> 0x00aa }
            int r1 = r5.c     // Catch:{ Throwable -> 0x00b2 }
            int r2 = r5.d     // Catch:{ Throwable -> 0x00b2 }
            r0.setLacAndCid(r1, r2)     // Catch:{ Throwable -> 0x00b2 }
        L_0x00a4:
            r2 = r6
            r1 = r0
            goto L_0x0052
        L_0x00a7:
            r6 = r2
            goto L_0x0009
        L_0x00aa:
            r0 = move-exception
            r2 = r6
            r1 = r6
            goto L_0x0052
        L_0x00ae:
            r1 = move-exception
            r2 = r0
            r1 = r6
            goto L_0x0052
        L_0x00b2:
            r1 = move-exception
            r2 = r6
            r1 = r0
            goto L_0x0052
        L_0x00b6:
            r0 = move-exception
            goto L_0x002e
        L_0x00b9:
            r0 = r6
            goto L_0x00a4
        L_0x00bb:
            r0 = r6
            goto L_0x0096
        L_0x00bd:
            r5 = r6
            goto L_0x0037
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ee.a(java.util.List):android.telephony.CellLocation");
    }

    private static ed a(int i2, boolean z, int i3, int i4, int i5, int i6, int i7) {
        ed edVar = new ed(i2, z);
        edVar.a = i3;
        edVar.b = i4;
        edVar.c = i5;
        edVar.d = i6;
        edVar.j = i7;
        return edVar;
    }

    @SuppressLint({"NewApi"})
    private ed a(CellInfoCdma cellInfoCdma, boolean z) {
        int i2;
        int i3;
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        String[] a2 = fa.a(this.c);
        try {
            i2 = Integer.parseInt(a2[0]);
            try {
                i3 = Integer.parseInt(a2[1]);
            } catch (Throwable th) {
            }
        } catch (Throwable th2) {
            i2 = 0;
            i3 = 0;
            ed a3 = a(2, z, i2, i3, 0, 0, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
            a3.g = cellIdentity.getSystemId();
            a3.h = cellIdentity.getNetworkId();
            a3.i = cellIdentity.getBasestationId();
            a3.e = cellIdentity.getLatitude();
            a3.f = cellIdentity.getLongitude();
            return a3;
        }
        ed a32 = a(2, z, i2, i3, 0, 0, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
        a32.g = cellIdentity.getSystemId();
        a32.h = cellIdentity.getNetworkId();
        a32.i = cellIdentity.getBasestationId();
        a32.e = cellIdentity.getLatitude();
        a32.f = cellIdentity.getLongitude();
        return a32;
    }

    @SuppressLint({"NewApi"})
    private static ed a(CellInfoGsm cellInfoGsm, boolean z) {
        CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
        return a(1, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid(), cellInfoGsm.getCellSignalStrength().getDbm());
    }

    @SuppressLint({"NewApi"})
    private static ed a(CellInfoLte cellInfoLte, boolean z) {
        CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
        ed a2 = a(3, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getTac(), cellIdentity.getCi(), cellInfoLte.getCellSignalStrength().getDbm());
        a2.o = cellIdentity.getPci();
        return a2;
    }

    @SuppressLint({"NewApi"})
    private static ed a(CellInfoWcdma cellInfoWcdma, boolean z) {
        CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
        ed a2 = a(4, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid(), cellInfoWcdma.getCellSignalStrength().getDbm());
        a2.o = cellIdentity.getPsc();
        return a2;
    }

    private static ed a(NeighboringCellInfo neighboringCellInfo, String[] strArr) {
        try {
            ed edVar = new ed(1, false);
            edVar.a = Integer.parseInt(strArr[0]);
            edVar.b = Integer.parseInt(strArr[1]);
            edVar.c = ew.b(neighboringCellInfo, "getLac", new Object[0]);
            edVar.d = neighboringCellInfo.getCid();
            edVar.j = fa.a(neighboringCellInfo.getRssi());
            return edVar;
        } catch (Throwable th) {
            es.a(th, "CgiManager", "getGsm");
            return null;
        }
    }

    private void a(CellLocation cellLocation, String[] strArr, boolean z) {
        ed a2;
        if (cellLocation != null && this.c != null) {
            this.b.clear();
            if (b(cellLocation)) {
                this.a = 1;
                List<NeighboringCellInfo> list = null;
                ArrayList<ed> arrayList = this.b;
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                ed edVar = new ed(1, true);
                edVar.a = fa.g(strArr[0]);
                edVar.b = fa.g(strArr[1]);
                edVar.c = gsmCellLocation.getLac();
                edVar.d = gsmCellLocation.getCid();
                edVar.j = this.o;
                arrayList.add(edVar);
                if (!z) {
                    if (Build.VERSION.SDK_INT <= 28) {
                        list = (List) ew.a(this.c, "getNeighboringCellInfo", new Object[0]);
                    }
                    if (list != null && !list.isEmpty()) {
                        for (NeighboringCellInfo neighboringCellInfo : list) {
                            if (neighboringCellInfo != null && a(neighboringCellInfo.getLac(), neighboringCellInfo.getCid()) && (a2 = a(neighboringCellInfo, strArr)) != null && !this.b.contains(a2)) {
                                this.b.add(a2);
                            }
                        }
                    }
                }
            }
        }
    }

    static /* synthetic */ void a(ee eeVar, int i2) {
        if (i2 == -113) {
            eeVar.o = -113;
            return;
        }
        eeVar.o = i2;
        switch (eeVar.a) {
            case 1:
            case 2:
                if (eeVar.b != null && !eeVar.b.isEmpty()) {
                    try {
                        eeVar.b.get(0).j = eeVar.o;
                        return;
                    } catch (Throwable th) {
                        return;
                    }
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private static boolean a(int i2) {
        return (i2 == -1 || i2 == 0 || i2 > 65535) ? false : true;
    }

    private static boolean a(int i2, int i3) {
        return (i2 == -1 || i2 == 0 || i2 > 65535 || i3 == -1 || i3 == 0 || i3 == 65535 || i3 >= 268435455) ? false : true;
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityCdma cellIdentityCdma) {
        return cellIdentityCdma != null && cellIdentityCdma.getSystemId() > 0 && cellIdentityCdma.getNetworkId() >= 0 && cellIdentityCdma.getBasestationId() >= 0;
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityGsm cellIdentityGsm) {
        return cellIdentityGsm != null && a(cellIdentityGsm.getLac()) && b(cellIdentityGsm.getCid());
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityLte cellIdentityLte) {
        return cellIdentityLte != null && a(cellIdentityLte.getTac()) && b(cellIdentityLte.getCi());
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityWcdma cellIdentityWcdma) {
        return cellIdentityWcdma != null && a(cellIdentityWcdma.getLac()) && b(cellIdentityWcdma.getCid());
    }

    private static boolean b(int i2) {
        return (i2 == -1 || i2 == 0 || i2 == 65535 || i2 >= 268435455) ? false : true;
    }

    private boolean b(CellLocation cellLocation) {
        boolean a2 = a(cellLocation);
        if (!a2) {
            this.a = 0;
        }
        return a2;
    }

    private int c(CellLocation cellLocation) {
        if (this.i || cellLocation == null) {
            return 0;
        }
        if (cellLocation instanceof GsmCellLocation) {
            return 1;
        }
        try {
            Class.forName("android.telephony.cdma.CdmaCellLocation");
            return 2;
        } catch (Throwable th) {
            es.a(th, Utils.TAG, "getCellLocT");
            return 0;
        }
    }

    private CellLocation n() {
        if (this.c != null) {
            try {
                CellLocation cellLocation = this.c.getCellLocation();
                this.h = null;
                if (b(cellLocation)) {
                    this.e = cellLocation;
                    return cellLocation;
                }
            } catch (SecurityException e2) {
                this.h = e2.getMessage();
            } catch (Throwable th) {
                this.h = null;
                es.a(th, "CgiManager", "getCellLocation");
            }
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    private CellLocation o() {
        CellLocation cellLocation = null;
        TelephonyManager telephonyManager = this.c;
        if (telephonyManager == null) {
            return null;
        }
        CellLocation n2 = n();
        if (b(n2)) {
            return n2;
        }
        if (fa.d() >= 18) {
            try {
                cellLocation = a(telephonyManager.getAllCellInfo());
            } catch (SecurityException e2) {
                this.h = e2.getMessage();
            }
        }
        if (cellLocation != null) {
            return cellLocation;
        }
        CellLocation a2 = a(telephonyManager, "getCellLocationExt", 1);
        if (a2 != null) {
            return a2;
        }
        CellLocation a3 = a(telephonyManager, "getCellLocationGemini", 1);
        if (a3 != null) {
        }
        return a3;
    }

    private CellLocation p() {
        Object cast;
        CellLocation cellLocation = null;
        Object obj = this.q;
        if (obj != null) {
            try {
                Class<?> q2 = q();
                if (!(q2.isInstance(obj) && (cellLocation = a((cast = q2.cast(obj)), "getCellLocation", new Object[0])) == null && (cellLocation = a(cast, "getCellLocation", 1)) == null && (cellLocation = a(cast, "getCellLocationGemini", 1)) == null && (cellLocation = a(cast, "getAllCellInfo", 1)) == null)) {
                }
            } catch (Throwable th) {
                es.a(th, "CgiManager", "getSim2Cgi");
            }
        }
        return cellLocation;
    }

    private Class<?> q() {
        String str;
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        switch (this.r) {
            case 0:
                str = "android.telephony.TelephonyManager";
                break;
            case 1:
                str = "android.telephony.MSimTelephonyManager";
                break;
            case 2:
                str = "android.telephony.TelephonyManager2";
                break;
            default:
                str = null;
                break;
        }
        try {
            return systemClassLoader.loadClass(str);
        } catch (Throwable th) {
            es.a(th, "CgiManager", "getSim2TmClass");
            return null;
        }
    }

    private int r() {
        try {
            Class.forName("android.telephony.MSimTelephonyManager");
            this.r = 1;
        } catch (Throwable th) {
        }
        if (this.r == 0) {
            try {
                Class.forName("android.telephony.TelephonyManager2");
                this.r = 2;
            } catch (Throwable th2) {
            }
        }
        return this.r;
    }

    public final ArrayList<ed> a() {
        return this.b;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.min(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.min(double, double):double}
      ClspMth{java.lang.Math.min(float, float):float}
      ClspMth{java.lang.Math.min(int, int):int}
      ClspMth{java.lang.Math.min(long, long):long} */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x01ee, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x01ef, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:191:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00e6, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00e7, code lost:
        r10.h = r0.getMessage();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0141, code lost:
        if (r0 != false) goto L_0x0072;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x01f8 A[ExcHandler: Throwable (th java.lang.Throwable), Splitter:B:37:0x0072] */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x02b7 A[SYNTHETIC, Splitter:B:160:0x02b7] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x008d A[SYNTHETIC, Splitter:B:49:0x008d] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00e6 A[ExcHandler: SecurityException (r0v1 'e' java.lang.SecurityException A[CUSTOM_DECLARE]), Splitter:B:98:0x0143] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a(boolean r11, boolean r12) {
        /*
            r10 = this;
            r3 = 0
            r1 = 1
            r2 = 0
            android.content.Context r0 = r10.l     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            boolean r0 = com.loc.fa.a(r0)     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r10.i = r0     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            boolean r0 = r10.i     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 == 0) goto L_0x00b9
            r0 = r2
        L_0x0010:
            if (r0 != 0) goto L_0x001a
            java.util.ArrayList<com.loc.ed> r0 = r10.b     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            boolean r0 = r0.isEmpty()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 == 0) goto L_0x0219
        L_0x001a:
            boolean r0 = r10.i     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 != 0) goto L_0x003e
            android.telephony.TelephonyManager r0 = r10.c     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 == 0) goto L_0x003e
            android.telephony.CellLocation r0 = r10.o()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            boolean r4 = r10.b(r0)     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r4 != 0) goto L_0x0030
            android.telephony.CellLocation r0 = r10.p()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
        L_0x0030:
            boolean r4 = r10.b(r0)     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r4 == 0) goto L_0x00c9
            r10.e = r0     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            long r4 = com.loc.fa.c()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r10.s = r4     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
        L_0x003e:
            boolean r0 = r10.f     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 != 0) goto L_0x0058
            android.telephony.CellLocation r0 = r10.e     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 != 0) goto L_0x0058
            if (r12 == 0) goto L_0x0058
            r0 = r2
        L_0x0049:
            r4 = 10
            java.lang.Thread.sleep(r4)     // Catch:{ InterruptedException -> 0x00ee }
        L_0x004e:
            int r0 = r0 + 1
            android.telephony.CellLocation r4 = r10.e     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r4 != 0) goto L_0x0058
            r4 = 50
            if (r0 < r4) goto L_0x0049
        L_0x0058:
            r0 = 1
            r10.f = r0     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            android.telephony.CellLocation r0 = r10.e     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            boolean r0 = r10.b(r0)     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 == 0) goto L_0x0072
            android.telephony.TelephonyManager r0 = r10.c     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            java.lang.String[] r4 = com.loc.fa.a(r0)     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            android.telephony.CellLocation r0 = r10.e     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            int r0 = r10.c(r0)     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            switch(r0) {
                case 1: goto L_0x00ff;
                case 2: goto L_0x0106;
                default: goto L_0x0072;
            }
        L_0x0072:
            int r0 = com.loc.fa.d()     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            r1 = 18
            if (r0 < r1) goto L_0x01f9
            android.telephony.TelephonyManager r0 = r10.c     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            if (r0 == 0) goto L_0x01f9
            java.util.ArrayList<com.loc.ed> r4 = r10.n     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            com.loc.ec r5 = r10.p     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            android.telephony.TelephonyManager r0 = r10.c     // Catch:{ SecurityException -> 0x01ee, Throwable -> 0x01f8 }
            java.util.List r1 = r0.getAllCellInfo()     // Catch:{ SecurityException -> 0x01ee, Throwable -> 0x01f8 }
            r0 = 0
            r10.h = r0     // Catch:{ SecurityException -> 0x02eb, Throwable -> 0x01f8 }
        L_0x008b:
            if (r1 == 0) goto L_0x02b5
            int r3 = r1.size()     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            if (r3 == 0) goto L_0x02b5
            if (r4 == 0) goto L_0x0098
            r4.clear()     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
        L_0x0098:
            if (r2 >= r3) goto L_0x02b5
            java.lang.Object r0 = r1.get(r2)     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            android.telephony.CellInfo r0 = (android.telephony.CellInfo) r0     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            if (r0 == 0) goto L_0x00b6
            boolean r6 = r0.isRegistered()     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            boolean r7 = r0 instanceof android.telephony.CellInfoCdma     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            if (r7 == 0) goto L_0x023d
            android.telephony.CellInfoCdma r0 = (android.telephony.CellInfoCdma) r0     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            android.telephony.CellIdentityCdma r7 = r0.getCellIdentity()     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            boolean r7 = a(r7)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            if (r7 != 0) goto L_0x0222
        L_0x00b6:
            int r2 = r2 + 1
            goto L_0x0098
        L_0x00b9:
            long r4 = com.loc.fa.c()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            long r6 = r10.d     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            long r4 = r4 - r6
            r6 = 10000(0x2710, double:4.9407E-320)
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 >= 0) goto L_0x02ee
            r0 = r2
            goto L_0x0010
        L_0x00c9:
            long r4 = com.loc.fa.c()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            long r6 = r10.s     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            long r4 = r4 - r6
            r6 = 60000(0xea60, double:2.9644E-319)
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 <= 0) goto L_0x003e
            r0 = 0
            r10.e = r0     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            java.util.ArrayList<com.loc.ed> r0 = r10.b     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r0.clear()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            java.util.ArrayList<com.loc.ed> r0 = r10.n     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r0.clear()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            goto L_0x003e
        L_0x00e6:
            r0 = move-exception
            java.lang.String r0 = r0.getMessage()
            r10.h = r0
        L_0x00ed:
            return
        L_0x00ee:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            goto L_0x004e
        L_0x00f4:
            r0 = move-exception
            java.lang.String r1 = "CgiManager"
            java.lang.String r2 = "refresh"
            com.loc.es.a(r0, r1, r2)
            goto L_0x00ed
        L_0x00ff:
            android.telephony.CellLocation r0 = r10.e     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r10.a(r0, r4, r11)     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            goto L_0x0072
        L_0x0106:
            android.telephony.CellLocation r5 = r10.e     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r5 == 0) goto L_0x0072
            java.util.ArrayList<com.loc.ed> r0 = r10.b     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r0.clear()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            int r0 = com.loc.fa.d()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r6 = 5
            if (r0 < r6) goto L_0x0072
            java.lang.Object r0 = r10.q     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            if (r0 == 0) goto L_0x0143
            java.lang.Class r0 = r5.getClass()     // Catch:{ Throwable -> 0x01e8, SecurityException -> 0x00e6 }
            java.lang.String r6 = "mGsmCellLoc"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r6)     // Catch:{ Throwable -> 0x01e8, SecurityException -> 0x00e6 }
            boolean r6 = r0.isAccessible()     // Catch:{ Throwable -> 0x01e8, SecurityException -> 0x00e6 }
            if (r6 != 0) goto L_0x012f
            r6 = 1
            r0.setAccessible(r6)     // Catch:{ Throwable -> 0x01e8, SecurityException -> 0x00e6 }
        L_0x012f:
            java.lang.Object r0 = r0.get(r5)     // Catch:{ Throwable -> 0x01e8, SecurityException -> 0x00e6 }
            android.telephony.gsm.GsmCellLocation r0 = (android.telephony.gsm.GsmCellLocation) r0     // Catch:{ Throwable -> 0x01e8, SecurityException -> 0x00e6 }
            if (r0 == 0) goto L_0x01e9
            boolean r6 = r10.b(r0)     // Catch:{ Throwable -> 0x01e8, SecurityException -> 0x00e6 }
            if (r6 == 0) goto L_0x01e9
            r10.a(r0, r4, r11)     // Catch:{ Throwable -> 0x01e8, SecurityException -> 0x00e6 }
            r0 = r1
        L_0x0141:
            if (r0 != 0) goto L_0x0072
        L_0x0143:
            boolean r0 = r10.b(r5)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            if (r0 == 0) goto L_0x0072
            r0 = 2
            r10.a = r0     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            com.loc.ed r0 = new com.loc.ed     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r6 = 2
            r7 = 1
            r0.<init>(r6, r7)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r6 = 0
            r6 = r4[r6]     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r6 = java.lang.Integer.parseInt(r6)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r0.a = r6     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r6 = 1
            r4 = r4[r6]     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r4 = java.lang.Integer.parseInt(r4)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r0.b = r4     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            java.lang.String r4 = "getSystemId"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r4 = com.loc.ew.b(r5, r4, r6)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r0.g = r4     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            java.lang.String r4 = "getNetworkId"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r4 = com.loc.ew.b(r5, r4, r6)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r0.h = r4     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            java.lang.String r4 = "getBaseStationId"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r4 = com.loc.ew.b(r5, r4, r6)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r0.i = r4     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r4 = r10.o     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r0.j = r4     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            java.lang.String r4 = "getBaseStationLatitude"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r4 = com.loc.ew.b(r5, r4, r6)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r0.e = r4     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            java.lang.String r4 = "getBaseStationLongitude"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r4 = com.loc.ew.b(r5, r4, r6)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r0.f = r4     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r4 = r0.e     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            int r5 = r0.f     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            if (r4 != r5) goto L_0x01ec
            int r4 = r0.e     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            if (r4 <= 0) goto L_0x01ec
        L_0x01af:
            int r4 = r0.e     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            if (r4 < 0) goto L_0x01c7
            int r4 = r0.f     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            if (r4 < 0) goto L_0x01c7
            int r4 = r0.e     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r5 = 2147483647(0x7fffffff, float:NaN)
            if (r4 == r5) goto L_0x01c7
            int r4 = r0.f     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r5 = 2147483647(0x7fffffff, float:NaN)
            if (r4 == r5) goto L_0x01c7
            if (r1 == 0) goto L_0x01cd
        L_0x01c7:
            r1 = 0
            r0.e = r1     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r1 = 0
            r0.f = r1     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
        L_0x01cd:
            java.util.ArrayList<com.loc.ed> r1 = r10.b     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            boolean r1 = r1.contains(r0)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            if (r1 != 0) goto L_0x0072
            java.util.ArrayList<com.loc.ed> r1 = r10.b     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            r1.add(r0)     // Catch:{ Throwable -> 0x01dc, SecurityException -> 0x00e6 }
            goto L_0x0072
        L_0x01dc:
            r0 = move-exception
            java.lang.String r1 = "CgiManager"
            java.lang.String r4 = "hdlCdmaLocChange"
            com.loc.es.a(r0, r1, r4)     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            goto L_0x0072
        L_0x01e8:
            r0 = move-exception
        L_0x01e9:
            r0 = r2
            goto L_0x0141
        L_0x01ec:
            r1 = r2
            goto L_0x01af
        L_0x01ee:
            r0 = move-exception
            r1 = r3
        L_0x01f0:
            java.lang.String r0 = r0.getMessage()     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            r10.h = r0     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            goto L_0x008b
        L_0x01f8:
            r0 = move-exception
        L_0x01f9:
            android.telephony.TelephonyManager r0 = r10.c     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 == 0) goto L_0x0213
            android.telephony.TelephonyManager r0 = r10.c     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            java.lang.String r0 = r0.getNetworkOperator()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r10.m = r0     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            java.lang.String r0 = r10.m     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 != 0) goto L_0x0213
            int r0 = r10.a     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r0 = r0 | 8
            r10.a = r0     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
        L_0x0213:
            long r0 = com.loc.fa.c()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r10.d = r0     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
        L_0x0219:
            boolean r0 = r10.i     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 == 0) goto L_0x02c8
            r10.i()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            goto L_0x00ed
        L_0x0222:
            com.loc.ed r0 = r10.a(r0, r6)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r6 = 65535(0xffff, double:3.23786E-319)
            long r8 = r5.a(r0)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            long r6 = java.lang.Math.min(r6, r8)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            int r6 = (int) r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            short r6 = (short) r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r0.l = r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r4.add(r0)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            goto L_0x00b6
        L_0x023a:
            r0 = move-exception
            goto L_0x00b6
        L_0x023d:
            boolean r7 = r0 instanceof android.telephony.CellInfoGsm     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            if (r7 == 0) goto L_0x0265
            android.telephony.CellInfoGsm r0 = (android.telephony.CellInfoGsm) r0     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            android.telephony.CellIdentityGsm r7 = r0.getCellIdentity()     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            boolean r7 = a(r7)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            if (r7 == 0) goto L_0x00b6
            com.loc.ed r0 = a(r0, r6)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r6 = 65535(0xffff, double:3.23786E-319)
            long r8 = r5.a(r0)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            long r6 = java.lang.Math.min(r6, r8)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            int r6 = (int) r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            short r6 = (short) r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r0.l = r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r4.add(r0)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            goto L_0x00b6
        L_0x0265:
            boolean r7 = r0 instanceof android.telephony.CellInfoWcdma     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            if (r7 == 0) goto L_0x028d
            android.telephony.CellInfoWcdma r0 = (android.telephony.CellInfoWcdma) r0     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            android.telephony.CellIdentityWcdma r7 = r0.getCellIdentity()     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            boolean r7 = a(r7)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            if (r7 == 0) goto L_0x00b6
            com.loc.ed r0 = a(r0, r6)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r6 = 65535(0xffff, double:3.23786E-319)
            long r8 = r5.a(r0)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            long r6 = java.lang.Math.min(r6, r8)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            int r6 = (int) r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            short r6 = (short) r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r0.l = r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r4.add(r0)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            goto L_0x00b6
        L_0x028d:
            boolean r7 = r0 instanceof android.telephony.CellInfoLte     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            if (r7 == 0) goto L_0x00b6
            android.telephony.CellInfoLte r0 = (android.telephony.CellInfoLte) r0     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            android.telephony.CellIdentityLte r7 = r0.getCellIdentity()     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            boolean r7 = a(r7)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            if (r7 == 0) goto L_0x00b6
            com.loc.ed r0 = a(r0, r6)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r6 = 65535(0xffff, double:3.23786E-319)
            long r8 = r5.a(r0)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            long r6 = java.lang.Math.min(r6, r8)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            int r6 = (int) r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            short r6 = (short) r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r0.l = r6     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            r4.add(r0)     // Catch:{ Throwable -> 0x023a, SecurityException -> 0x00e6 }
            goto L_0x00b6
        L_0x02b5:
            if (r4 == 0) goto L_0x01f9
            int r0 = r4.size()     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            if (r0 <= 0) goto L_0x01f9
            int r0 = r10.a     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            r0 = r0 | 4
            r10.a = r0     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            r5.a(r4)     // Catch:{ Throwable -> 0x01f8, SecurityException -> 0x00e6 }
            goto L_0x01f9
        L_0x02c8:
            int r0 = r10.a     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            r0 = r0 & 3
            switch(r0) {
                case 1: goto L_0x02d1;
                case 2: goto L_0x02de;
                default: goto L_0x02cf;
            }     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
        L_0x02cf:
            goto L_0x00ed
        L_0x02d1:
            java.util.ArrayList<com.loc.ed> r0 = r10.b     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            boolean r0 = r0.isEmpty()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 == 0) goto L_0x00ed
            r0 = 0
            r10.a = r0     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            goto L_0x00ed
        L_0x02de:
            java.util.ArrayList<com.loc.ed> r0 = r10.b     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            boolean r0 = r0.isEmpty()     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            if (r0 == 0) goto L_0x00ed
            r0 = 0
            r10.a = r0     // Catch:{ SecurityException -> 0x00e6, Throwable -> 0x00f4 }
            goto L_0x00ed
        L_0x02eb:
            r0 = move-exception
            goto L_0x01f0
        L_0x02ee:
            r0 = r1
            goto L_0x0010
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ee.a(boolean, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public final boolean a(CellLocation cellLocation) {
        if (cellLocation == null) {
            return false;
        }
        boolean z = true;
        switch (c(cellLocation)) {
            case 1:
                try {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                    z = a(gsmCellLocation.getLac(), gsmCellLocation.getCid());
                    break;
                } catch (Throwable th) {
                    es.a(th, "CgiManager", "cgiUseful Cgi.I_GSM_T");
                    break;
                }
            case 2:
                try {
                    if (ew.b(cellLocation, "getSystemId", new Object[0]) <= 0 || ew.b(cellLocation, "getNetworkId", new Object[0]) < 0 || ew.b(cellLocation, "getBaseStationId", new Object[0]) < 0) {
                        z = false;
                        break;
                    }
                } catch (Throwable th2) {
                    es.a(th2, "CgiManager", "cgiUseful Cgi.I_CDMA_T");
                    break;
                }
        }
        return z;
    }

    public final ArrayList<ed> b() {
        return this.n;
    }

    public final ed c() {
        if (this.i) {
            return null;
        }
        ArrayList<ed> arrayList = this.b;
        if (arrayList.size() > 0) {
            return arrayList.get(0);
        }
        return null;
    }

    public final ed d() {
        if (this.i) {
            return null;
        }
        ArrayList<ed> arrayList = this.n;
        if (arrayList.size() > 0) {
            return arrayList.get(0);
        }
        return null;
    }

    public final int e() {
        return this.a;
    }

    public final int f() {
        return this.a & 3;
    }

    public final TelephonyManager g() {
        return this.c;
    }

    public final void h() {
        this.p.a();
        this.s = 0;
        synchronized (this.u) {
            this.t = true;
        }
        if (!(this.c == null || this.g == null)) {
            try {
                this.c.listen(this.g, 0);
            } catch (Throwable th) {
                es.a(th, "CgiManager", Lifecycle.STATUS_DESTROY);
            }
        }
        this.g = null;
        if (this.k != null) {
            this.k.quit();
            this.k = null;
        }
        this.o = -113;
        this.c = null;
        this.q = null;
    }

    /* access modifiers changed from: package-private */
    public final void i() {
        this.h = null;
        this.e = null;
        this.a = 0;
        this.b.clear();
        this.n.clear();
    }

    public final String j() {
        return this.h;
    }

    public final String k() {
        return this.m;
    }

    public final String l() {
        if (this.i) {
            i();
        }
        if (this.j == null) {
            this.j = new StringBuilder();
        } else {
            this.j.delete(0, this.j.length());
        }
        switch (this.a & 3) {
            case 1:
                int i2 = 1;
                while (true) {
                    int i3 = i2;
                    if (i3 >= this.b.size()) {
                        break;
                    } else {
                        this.j.append("#").append(this.b.get(i3).b);
                        this.j.append("|").append(this.b.get(i3).c);
                        this.j.append("|").append(this.b.get(i3).d);
                        i2 = i3 + 1;
                    }
                }
        }
        if (this.j.length() > 0) {
            this.j.deleteCharAt(0);
        }
        return this.j.toString();
    }

    public final boolean m() {
        try {
            if (this.c != null && (!TextUtils.isEmpty(this.c.getSimOperator()) || !TextUtils.isEmpty(this.c.getSimCountryIso()))) {
                return true;
            }
        } catch (Throwable th) {
        }
        try {
            int a2 = fa.a(fa.c(this.l));
            return a2 == 0 || a2 == 4 || a2 == 2 || a2 == 5 || a2 == 3;
        } catch (Throwable th2) {
        }
    }
}
