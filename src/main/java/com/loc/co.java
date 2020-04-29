package com.loc;

import android.content.Context;
import com.amap.location.offline.OfflineConfig;
import dji.publics.LogReport.base.Fields;
import java.lang.reflect.Method;

/* compiled from: OfflineCoordinateConverter */
public final class co implements OfflineConfig.ICoordinateConverter {
    Context a = null;
    Object b = null;

    public co(Context context) {
        this.a = context;
    }

    private double[] a(double[] dArr) {
        try {
            if (this.b == null) {
                this.b = Class.forName("com.amap.api.location.CoordinateConverter").getConstructor(Context.class).newInstance(this.a);
            }
            if (cm.a(dArr[0], dArr[1])) {
                Object[] objArr = {"GPS"};
                Object newInstance = Class.forName("com.amap.api.location.DPoint").getConstructor(Double.TYPE, Double.TYPE).newInstance(Double.valueOf(dArr[0]), Double.valueOf(dArr[1]));
                Method declaredMethod = Class.forName("com.amap.api.location.CoordinateConverter$CoordType").getDeclaredMethod("valueOf", String.class);
                if (!declaredMethod.isAccessible()) {
                    declaredMethod.setAccessible(true);
                }
                Object invoke = declaredMethod.invoke(null, objArr);
                ck.a(this.b, "coord", newInstance);
                ck.a(this.b, Fields.Dgo_quiz.FROM, invoke);
                Object a2 = ck.a(this.b, "convert", new Object[0]);
                dArr[0] = ((Double) ck.a(a2, "getLatitude", new Object[0])).doubleValue();
                dArr[1] = ((Double) ck.a(a2, "getLongitude", new Object[0])).doubleValue();
            }
        } catch (Throwable th) {
            cm.a(th, "OfflineCoordinateConverter", "wgsToGcj");
        }
        return dArr;
    }

    public final double[] wgsToGcj(double[] dArr) {
        if (this.a == null || dArr == null || dArr.length != 2) {
            return null;
        }
        return a(dArr);
    }
}
