package com.amap.openapi;

import android.support.annotation.NonNull;
import com.amap.location.common.util.b;
import com.mapzen.android.lost.internal.Clock;
import com.mapzen.android.lost.internal.FusionEngine;
import dji.pilot.publics.util.DJITimeUtils;
import kotlin.jvm.internal.LongCompanionObject;

/* compiled from: CountConfigProxy */
public class eb implements dp {
    private dp a;

    public eb(@NonNull dp dpVar) {
        this.a = dpVar;
    }

    public long a() {
        return b.a(this.a.a(), 0, (long) LongCompanionObject.MAX_VALUE);
    }

    public long a(int i) {
        return b.a(this.a.a(i), 1000, 10000000);
    }

    public long b(int i) {
        return b.a(this.a.b(i), 0, 50000000);
    }

    public void b() {
        this.a.b();
    }

    public long c() {
        return b.a(this.a.c(), 0, (long) Clock.MS_TO_NS);
    }

    public boolean c(int i) {
        return this.a.c(i);
    }

    public long d() {
        return b.a(this.a.d(), (long) FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS, (long) DJITimeUtils.MILLIS_IN_DAY);
    }

    public long e() {
        return b.a(this.a.e(), 1000, 3600000);
    }

    public int f() {
        return b.a(this.a.f(), 1000, 600000);
    }

    public long g() {
        return b.a(this.a.g(), 0, 50000000);
    }

    public long h() {
        return b.a(this.a.h(), 0, (long) LongCompanionObject.MAX_VALUE);
    }
}
