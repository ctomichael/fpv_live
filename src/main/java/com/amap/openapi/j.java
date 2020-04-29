package com.amap.openapi;

import android.content.Context;
import android.support.annotation.NonNull;
import com.amap.location.common.util.d;
import java.util.List;

/* compiled from: TrackBufferBuilder */
public class j extends g {
    public j() {
        super(500);
    }

    private int a(@NonNull v vVar) {
        return aj.a(this.a, vVar.a, vVar.b, vVar.c, vVar.d, vVar.e, vVar.f, vVar.g, vVar.h, vVar.i, vVar.j);
    }

    private int a(@NonNull List<y> list) {
        int size = list.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            y yVar = list.get(i);
            iArr[i] = ak.a(this.a, yVar.a, yVar.b, yVar.c, yVar.d, yVar.e);
        }
        return ac.a(this.a, iArr);
    }

    public byte[] a(@NonNull Context context, @NonNull v vVar, @NonNull List<y> list, byte b) {
        a();
        try {
            this.a.h(ac.a(this.a, a(vVar), a(list), b));
            return aw.a(az.a(context), d.a(this.a.f()));
        } catch (Throwable th) {
            return null;
        }
    }
}
