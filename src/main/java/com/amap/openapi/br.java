package com.amap.openapi;

import android.content.Context;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.util.c;
import com.amap.location.offline.OfflineConfig;
import com.amap.location.offline.upload.a;

/* compiled from: OfflineTraining */
class br {
    private static AmapLoc a(OfflineConfig offlineConfig, AmapLoc amapLoc) {
        if (amapLoc == null || offlineConfig.productId != 4) {
            return amapLoc;
        }
        if (offlineConfig.coordinateConverter == null) {
            return null;
        }
        double[] wgsToGcj = offlineConfig.coordinateConverter.wgsToGcj(new double[]{amapLoc.getLat(), amapLoc.getLon()});
        if (wgsToGcj == null || wgsToGcj.length < 2) {
            return null;
        }
        AmapLoc amapLoc2 = new AmapLoc();
        amapLoc2.setLat(wgsToGcj[0]);
        amapLoc2.setLon(wgsToGcj[1]);
        return amapLoc2;
    }

    public static void a(Context context, OfflineConfig offlineConfig, bs bsVar, bu buVar, AmapLoc amapLoc) {
        AmapLoc a;
        if (amapLoc != null && amapLoc.isLocationCorrect()) {
            boolean a2 = a(amapLoc);
            if (a2 && bsVar.i != null) {
                AmapLoc a3 = a(offlineConfig, bsVar.i);
                if (a3 != null) {
                    double a4 = (double) c.a(amapLoc, a3);
                    if (a4 > 300.0d) {
                        ALLog.trace("@_18_3_@", "@_18_3_1_@" + a4);
                        by.a(context).b(bsVar);
                        a.a(100038, ("cellCorrect:" + a4).getBytes());
                    }
                }
            } else if (!a2 && buVar.f != null && (a = a(offlineConfig, buVar.f)) != null) {
                double a5 = (double) c.a(amapLoc, a);
                if (a5 > 100.0d) {
                    ALLog.trace("@_18_3_@", "@_18_3_2_@" + a5);
                    by.a(context).a(buVar, amapLoc);
                    a.a(100038, ("wifiCorrect:" + a5).getBytes());
                }
            }
        }
    }

    public static void a(Context context, bs bsVar) {
        by.a(context).a(bsVar);
    }

    public static void a(Context context, bu buVar) {
        by.a(context).a(buVar);
    }

    private static boolean a(AmapLoc amapLoc) {
        String retype = amapLoc.getRetype();
        return "3".equals(retype) || "4".equals(retype) || "9".equals(retype);
    }
}
