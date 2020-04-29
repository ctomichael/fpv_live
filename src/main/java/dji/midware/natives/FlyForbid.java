package dji.midware.natives;

import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class FlyForbid {
    public static native FlyForbidParam native_CheckNearForbidPoints(double d, double d2, Object obj);

    public static native String native_getFlyfrbDbAc();

    public static native boolean native_intersectSegCircle(double d, double d2, double d3, int i);

    static {
        try {
            System.loadLibrary("FlyForbid");
            Log.d("FlyForbid", "load lib success");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.d("FlyForbid", "Couldn't load lib");
        }
    }

    @Keep
    public static class FlyForbidParam {
        public double[] ForbidCountry;
        public double[] ForbidLat;
        public double[] ForbidLon;
        public double[] ForbidRadius;
        public double[] ForbidType;
        public int count;

        public void SetForbidPoint(double[] lon, double[] lat, double[] radius, double[] country, double[] type, int cnt) {
            if (cnt > 0) {
                this.ForbidLon = new double[cnt];
                this.ForbidLat = new double[cnt];
                this.ForbidRadius = new double[cnt];
                this.ForbidCountry = new double[cnt];
                this.ForbidType = new double[cnt];
                for (int i = 0; i < cnt; i++) {
                    this.ForbidLon[i] = lon[i];
                    this.ForbidLat[i] = lat[i];
                    this.ForbidRadius[i] = radius[i];
                    this.ForbidCountry[i] = country[i];
                    this.ForbidType[i] = type[i];
                }
            }
        }
    }
}
