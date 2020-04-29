package com.loc;

import com.mapzen.android.lost.internal.FusionEngine;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: CellAgeEstimator */
public final class ec {
    private HashMap<Long, ed> a = new HashMap<>();
    private long b = 0;

    private static long a(int i, int i2) {
        return ((((long) i) & 65535) << 32) | (((long) i2) & 65535);
    }

    public final long a(ed edVar) {
        long a2;
        if (edVar == null || !edVar.p) {
            return 0;
        }
        HashMap<Long, ed> hashMap = this.a;
        switch (edVar.k) {
            case 1:
            case 3:
            case 4:
                a2 = a(edVar.c, edVar.d);
                break;
            case 2:
                a2 = a(edVar.h, edVar.i);
                break;
            default:
                a2 = 0;
                break;
        }
        ed edVar2 = hashMap.get(Long.valueOf(a2));
        if (edVar2 == null) {
            edVar.m = fa.c();
            hashMap.put(Long.valueOf(a2), edVar);
            return 0;
        } else if (edVar2.j != edVar.j) {
            edVar.m = fa.c();
            hashMap.put(Long.valueOf(a2), edVar);
            return 0;
        } else {
            edVar.m = edVar2.m;
            hashMap.put(Long.valueOf(a2), edVar);
            return (fa.c() - edVar2.m) / 1000;
        }
    }

    public final void a() {
        this.a.clear();
        this.b = 0;
    }

    public final void a(ArrayList<? extends ed> arrayList) {
        long j = 0;
        if (arrayList != null) {
            long c = fa.c();
            if (this.b <= 0 || c - this.b >= FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS) {
                HashMap<Long, ed> hashMap = this.a;
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    ed edVar = (ed) arrayList.get(i);
                    if (edVar.p) {
                        switch (edVar.k) {
                            case 1:
                            case 3:
                            case 4:
                                j = a(edVar.c, edVar.d);
                                break;
                            case 2:
                                j = a(edVar.h, edVar.i);
                                break;
                        }
                        ed edVar2 = hashMap.get(Long.valueOf(j));
                        if (edVar2 != null) {
                            if (edVar2.j == edVar.j) {
                                edVar.m = edVar2.m;
                            } else {
                                edVar.m = c;
                            }
                        }
                    }
                }
                hashMap.clear();
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    ed edVar3 = (ed) arrayList.get(i2);
                    if (edVar3.p) {
                        switch (edVar3.k) {
                            case 1:
                            case 3:
                            case 4:
                                j = a(edVar3.c, edVar3.d);
                                break;
                            case 2:
                                j = a(edVar3.h, edVar3.i);
                                break;
                        }
                        hashMap.put(Long.valueOf(j), edVar3);
                    }
                }
                this.b = c;
            }
        }
    }
}
