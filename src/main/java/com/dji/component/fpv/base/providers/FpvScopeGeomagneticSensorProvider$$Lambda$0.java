package com.dji.component.fpv.base.providers;

import java.util.Comparator;

final /* synthetic */ class FpvScopeGeomagneticSensorProvider$$Lambda$0 implements Comparator {
    static final Comparator $instance = new FpvScopeGeomagneticSensorProvider$$Lambda$0();

    private FpvScopeGeomagneticSensorProvider$$Lambda$0() {
    }

    public int compare(Object obj, Object obj2) {
        return ((Float) obj).compareTo((Float) obj2);
    }
}
