package com.amap.openapi;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.amap.location.common.model.CellStatus;
import com.amap.location.common.util.d;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: FpsBufferBuilder */
public class h extends g {
    public h() {
        super(2048);
    }

    private int a(long j, @NonNull List<aa> list) {
        a(list);
        int size = list.size();
        if (size <= 0) {
            return -1;
        }
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            aa aaVar = list.get(i);
            iArr[i] = ar.a(this.a, aaVar.a == j && aaVar.a != -1, aaVar.a, aaVar.b, this.a.a(aaVar.c), aaVar.d, aaVar.f);
        }
        return aq.a(this.a, aq.a(this.a, iArr));
    }

    private int a(@NonNull q qVar) {
        int i;
        int a;
        a(qVar.c);
        int size = qVar.c.size();
        if (size <= 0) {
            return -1;
        }
        int[] iArr = new int[size];
        for (int i2 = 0; i2 < size; i2++) {
            r rVar = qVar.c.get(i2);
            int i3 = -1;
            if (rVar.a == 1) {
                w wVar = (w) rVar.f;
                i3 = rVar.c == 0 ? al.a(this.a, wVar.c, wVar.d, wVar.e, wVar.i) : al.a(this.a, wVar.a, wVar.b, wVar.c, wVar.d, wVar.e, wVar.f, wVar.g, wVar.h, wVar.i);
            } else if (rVar.a == 3) {
                x xVar = (x) rVar.f;
                i3 = am.a(this.a, xVar.a, xVar.b, xVar.c, xVar.d, xVar.e, xVar.f, xVar.g, xVar.h);
            } else if (rVar.a == 2) {
                p pVar = (p) rVar.f;
                i3 = rVar.c == 0 ? ae.a(this.a, pVar.a, pVar.b, pVar.c, pVar.d, pVar.e, pVar.f) : ae.a(this.a, pVar.a, pVar.b, pVar.c, pVar.d, pVar.e, pVar.f, pVar.g);
            } else if (rVar.a == 4) {
                z zVar = (z) rVar.f;
                i3 = ap.a(this.a, zVar.a, zVar.b, zVar.c, zVar.d, zVar.e, zVar.f, zVar.g, zVar.h);
            }
            if (i3 == -1) {
                return -1;
            }
            iArr[i2] = ah.a(this.a, rVar.b, rVar.c, rVar.d, rVar.a, i3);
        }
        int a2 = this.a.a(qVar.b);
        int a3 = af.a(this.a, iArr);
        int size2 = qVar.d.size();
        int[] iArr2 = new int[size2];
        for (int i4 = 0; i4 < size2; i4++) {
            CellStatus.HistoryCell historyCell = qVar.d.get(i4);
            long elapsedRealtime = (SystemClock.elapsedRealtime() - historyCell.lastUpdateTimeMills) / 1000;
            long j = (elapsedRealtime > 32767 || elapsedRealtime < 0) ? 32767 : elapsedRealtime;
            if (historyCell.type == 2) {
                a = an.a(this.a, (byte) 2, historyCell.sid, historyCell.nid, historyCell.bid, (short) ((int) j));
                i = 2;
            } else {
                i = 1;
                a = ao.a(this.a, (byte) 1, historyCell.lac, historyCell.cid, (short) ((int) j));
            }
            iArr2[i4] = ag.a(this.a, (byte) i, a);
        }
        return af.a(this.a, a2, qVar.a, a3, af.b(this.a, iArr2));
    }

    private int a(@NonNull v vVar) {
        return aj.a(this.a, vVar.a, vVar.b, vVar.c, vVar.d, vVar.e, vVar.f, vVar.g, vVar.h, vVar.i);
    }

    private void a(ArrayList<r> arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
            Iterator<r> it2 = arrayList.iterator();
            while (it2.hasNext()) {
                r next = it2.next();
                if (next.a == 1) {
                    w wVar = (w) next.f;
                    next.d = as.a(as.a(wVar.c, wVar.d));
                } else if (next.a == 3) {
                    x xVar = (x) next.f;
                    next.d = as.a(as.a(xVar.c, xVar.d));
                } else if (next.a == 4) {
                    z zVar = (z) next.f;
                    next.d = as.a(as.a(zVar.c, zVar.d));
                } else if (next.a == 2) {
                    p pVar = (p) next.f;
                    next.d = as.a(as.a(pVar.b, pVar.c));
                }
            }
        }
    }

    private void a(@NonNull List<aa> list) {
        for (aa aaVar : list) {
            aaVar.d = as.b(aaVar.a);
        }
    }

    @Nullable
    public byte[] a(@NonNull Context context, @NonNull v vVar, @Nullable q qVar, long j, @Nullable List<aa> list) {
        int i = -1;
        super.a();
        try {
            int a = a(vVar);
            int a2 = (qVar == null || qVar.c.size() <= 0) ? -1 : a(qVar);
            if (list != null && list.size() > 0) {
                i = a(j, list);
            }
            ab.a(this.a);
            ab.a(this.a, a);
            if (a2 > 0) {
                ab.c(this.a, a2);
            }
            if (i > 0) {
                ab.b(this.a, i);
            }
            this.a.h(ab.b(this.a));
            return aw.a(az.a(context), d.a(this.a.f()));
        } catch (Throwable th) {
            return null;
        }
    }
}
