package com.amap.location.common.util;

import android.location.Location;
import com.amap.location.common.model.AmapLoc;

/* compiled from: DistanceUtil */
public class c {
    public static float a(AmapLoc amapLoc, AmapLoc amapLoc2) {
        return a(new double[]{amapLoc.getLat(), amapLoc.getLon(), amapLoc2.getLat(), amapLoc2.getLon()});
    }

    public static float a(double[] dArr) {
        if (dArr.length != 4) {
            return 0.0f;
        }
        float[] fArr = new float[1];
        Location.distanceBetween(dArr[0], dArr[1], dArr[2], dArr[3], fArr);
        return fArr[0];
    }
}
