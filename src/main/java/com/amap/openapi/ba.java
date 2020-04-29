package com.amap.openapi;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.ShortCompanionObject;

/* compiled from: GpsUtil */
public class ba {
    public static short a(@NonNull List<y> list, boolean z, List<ct> list2) {
        double d = 0.0d;
        int i = 0;
        short s = ShortCompanionObject.MAX_VALUE;
        list.clear();
        if (list2 == null) {
            return ShortCompanionObject.MAX_VALUE;
        }
        Iterator<ct> it2 = list2.iterator();
        while (true) {
            short s2 = s;
            if (!it2.hasNext()) {
                return s2;
            }
            ct next = it2.next();
            int b = next.b();
            float d2 = next.d();
            boolean a = next.a();
            if (b > 1 && b <= 32) {
                if (a && ((double) d2) > 10.0d) {
                    d += (double) next.c();
                    i++;
                }
                if (z) {
                    y yVar = new y();
                    yVar.a = (byte) b;
                    yVar.b = (byte) Math.round(next.c());
                    yVar.c = (byte) Math.round(d2);
                    yVar.d = (short) Math.round(next.e());
                    yVar.e = (byte) (a ? 1 : 0);
                    list.add(yVar);
                }
            }
            s = i > 0 ? (short) Math.round(((float) (d / ((double) i))) * 100.0f) : s2;
        }
    }

    public static void a(@NonNull v vVar, @NonNull Location location, long j, long j2) {
        vVar.b = j;
        vVar.a = j2;
        vVar.c = (int) (location.getLongitude() * 1000000.0d);
        vVar.d = (int) (location.getLatitude() * 1000000.0d);
        vVar.e = (int) location.getAltitude();
        vVar.f = (int) location.getAccuracy();
        vVar.g = (int) location.getSpeed();
        vVar.h = (short) ((int) location.getBearing());
        Bundle extras = location.getExtras();
        vVar.i = 0;
        if (extras != null) {
            try {
                vVar.i = (byte) extras.getInt("satellites", 0);
            } catch (Exception e) {
            }
        }
    }

    public static void a(@NonNull v vVar, short s, @NonNull Location location, long j, long j2) {
        vVar.j = s;
        a(vVar, location, j, j2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x000d A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(android.content.Context r3, android.location.Location r4) {
        /*
            r0 = 0
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 18
            if (r1 < r2) goto L_0x000f
            boolean r1 = com.amap.openapi.az.a(r4)
            if (r1 == 0) goto L_0x000e
        L_0x000d:
            r0 = 1
        L_0x000e:
            return r0
        L_0x000f:
            java.lang.String r1 = android.os.Build.MODEL
            java.lang.String r2 = "sdk"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L_0x000d
            boolean r1 = com.amap.openapi.az.b(r3)
            if (r1 != 0) goto L_0x000d
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.ba.a(android.content.Context, android.location.Location):boolean");
    }

    public static boolean a(Location location) {
        return location != null && "gps".equalsIgnoreCase(location.getProvider()) && location.getLatitude() > -90.0d && location.getLatitude() < 90.0d && location.getLongitude() > -180.0d && location.getLongitude() < 180.0d;
    }
}
