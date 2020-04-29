package com.amap.openapi;

import android.net.wifi.ScanResult;
import com.amap.location.common.util.f;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.cmc.BodyPartID;

/* compiled from: RssiManager */
public class as {
    private static bn a = new bn();

    /* compiled from: RssiManager */
    public static class a implements bm {
        private int a;
        private int b;
        private int c;

        a(int i, int i2, int i3) {
            this.a = i;
            this.b = i2;
            this.c = i3;
        }

        public long a() {
            return as.a(this.a, this.b);
        }

        public int b() {
            return this.c;
        }
    }

    /* compiled from: RssiManager */
    public static class b implements bm {
        private long a;
        private int b;

        b(long j, int i) {
            this.a = j;
            this.b = i;
        }

        public long a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }
    }

    public static long a(int i, int i2) {
        return ((((long) i) & BodyPartID.bodyIdMax) << 32) | (((long) i2) & BodyPartID.bodyIdMax);
    }

    public static synchronized short a(long j) {
        short a2;
        synchronized (as.class) {
            a2 = a.a(j);
        }
        return a2;
    }

    public static synchronized void a() {
        synchronized (as.class) {
            a.a();
        }
    }

    public static synchronized void a(List<r> list) {
        synchronized (as.class) {
            if (list != null) {
                if (!list.isEmpty()) {
                    ArrayList arrayList = new ArrayList(list.size());
                    for (r rVar : list) {
                        if (rVar.a == 1) {
                            w wVar = (w) rVar.f;
                            arrayList.add(new a(wVar.c, wVar.d, wVar.e));
                        } else if (rVar.a == 3) {
                            x xVar = (x) rVar.f;
                            arrayList.add(new a(xVar.c, xVar.d, xVar.f));
                        } else if (rVar.a == 4) {
                            z zVar = (z) rVar.f;
                            arrayList.add(new a(zVar.c, zVar.d, zVar.f));
                        } else if (rVar.a == 2) {
                            p pVar = (p) rVar.f;
                            arrayList.add(new a(pVar.b, pVar.c, pVar.f));
                        }
                    }
                    a.a(arrayList);
                }
            }
        }
    }

    public static synchronized short b(long j) {
        short b2;
        synchronized (as.class) {
            b2 = a.b(j);
        }
        return b2;
    }

    public static synchronized void b(List<ScanResult> list) {
        synchronized (as.class) {
            if (list != null) {
                if (!list.isEmpty()) {
                    ArrayList arrayList = new ArrayList(list.size());
                    for (ScanResult scanResult : list) {
                        arrayList.add(new b(f.a(scanResult.BSSID), scanResult.level));
                    }
                    a.b(arrayList);
                }
            }
        }
    }
}
