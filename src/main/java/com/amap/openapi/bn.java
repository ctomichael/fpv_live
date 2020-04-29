package com.amap.openapi;

import android.os.Build;
import android.os.SystemClock;
import android.util.ArrayMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: RssiInfoManager */
public class bn {
    private Map<Long, a> a;
    private Map<Long, a> b;
    private Map<Long, a> c;
    private Map<Long, a> d;
    private Object e = new Object();
    private Object f = new Object();

    /* compiled from: RssiInfoManager */
    private static class a {
        int a;
        long b;
        boolean c;

        private a() {
        }
    }

    public bn() {
        if (Build.VERSION.SDK_INT >= 19) {
            this.a = new ArrayMap();
            this.b = new ArrayMap();
            this.c = new ArrayMap();
            this.d = new ArrayMap();
            return;
        }
        this.a = new HashMap();
        this.b = new HashMap();
        this.c = new HashMap();
        this.d = new HashMap();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.min(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.min(double, double):double}
      ClspMth{java.lang.Math.min(float, float):float}
      ClspMth{java.lang.Math.min(int, int):int}
      ClspMth{java.lang.Math.min(long, long):long} */
    private short a(Map<Long, a> map, long j) {
        short s;
        synchronized (map) {
            a aVar = map.get(Long.valueOf(j));
            if (aVar == null) {
                s = 0;
            } else {
                short max = (short) ((int) Math.max(1L, Math.min(32767L, (b() - aVar.b) / 1000)));
                s = aVar.c ? max : (short) (-max);
            }
        }
        return s;
    }

    private void a(List<bm> list, Map<Long, a> map, Map<Long, a> map2) {
        long b2 = b();
        if (map.isEmpty()) {
            for (bm bmVar : list) {
                a aVar = new a();
                aVar.a = bmVar.b();
                aVar.b = b2;
                aVar.c = false;
                map2.put(Long.valueOf(bmVar.a()), aVar);
            }
            return;
        }
        for (bm bmVar2 : list) {
            long a2 = bmVar2.a();
            a aVar2 = map.get(Long.valueOf(a2));
            if (aVar2 == null) {
                aVar2 = new a();
                aVar2.a = bmVar2.b();
                aVar2.b = b2;
                aVar2.c = true;
            } else if (aVar2.a != bmVar2.b()) {
                aVar2.a = bmVar2.b();
                aVar2.b = b2;
                aVar2.c = true;
            }
            map2.put(Long.valueOf(a2), aVar2);
        }
    }

    private static long b() {
        return SystemClock.elapsedRealtime();
    }

    public short a(long j) {
        return a(this.a, j);
    }

    public void a() {
        synchronized (this.e) {
            this.a.clear();
        }
        synchronized (this.f) {
            this.c.clear();
        }
    }

    public void a(List<bm> list) {
        if (list != null && !list.isEmpty()) {
            synchronized (this.e) {
                a(list, this.a, this.b);
                Map<Long, a> map = this.a;
                this.a = this.b;
                this.b = map;
                this.b.clear();
            }
        }
    }

    public short b(long j) {
        return a(this.c, j);
    }

    public void b(List<bm> list) {
        if (list != null && !list.isEmpty()) {
            synchronized (this.f) {
                a(list, this.c, this.d);
                Map<Long, a> map = this.c;
                this.c = this.d;
                this.d = map;
                this.d.clear();
            }
        }
    }
}
