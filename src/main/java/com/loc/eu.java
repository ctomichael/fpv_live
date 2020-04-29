package com.loc;

import android.content.Context;
import com.amap.api.location.CoordUtil;
import com.amap.api.location.DPoint;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: OffsetUtil */
public final class eu {
    static double a = 3.141592653589793d;
    private static String b = "Jni_wgs2gcj";
    private static final List<DPoint> c = new ArrayList(Arrays.asList(new DPoint(23.379947d, 119.757001d), new DPoint(24.983296d, 120.474496d), new DPoint(25.518722d, 121.359866d), new DPoint(25.41329d, 122.443582d), new DPoint(24.862708d, 122.288354d), new DPoint(24.461292d, 122.188319d), new DPoint(21.584761d, 120.968923d), new DPoint(21.830837d, 120.654445d)));

    private static double a(double d) {
        return Math.sin(3000.0d * d * (a / 180.0d)) * 2.0E-5d;
    }

    private static double a(double d, double d2) {
        return (Math.cos(d2 / 100000.0d) * (d / 18000.0d)) + (Math.sin(d / 100000.0d) * (d2 / 9000.0d));
    }

    public static DPoint a(Context context, double d, double d2) {
        if (context == null) {
            return null;
        }
        return a(context, new DPoint(d2, d));
    }

    public static DPoint a(Context context, DPoint dPoint) {
        if (context == null) {
            return null;
        }
        if (!CoordUtil.isLoadedSo()) {
            System.loadLibrary(b);
            CoordUtil.setLoadedSo(true);
        }
        return b(dPoint);
    }

    public static DPoint a(DPoint dPoint) {
        if (dPoint == null) {
            return dPoint;
        }
        try {
            if (es.a(dPoint.getLatitude(), dPoint.getLongitude())) {
                return c(dPoint);
            }
            if (!(es.a(new DPoint(dPoint.getLatitude(), dPoint.getLongitude()), c))) {
                return dPoint;
            }
            DPoint c2 = c(dPoint);
            double latitude = c2.getLatitude();
            double longitude = c2.getLongitude();
            double d = longitude - 105.0d;
            double d2 = latitude - 35.0d;
            double sqrt = -100.0d + (2.0d * d) + (3.0d * d2) + (0.2d * d2 * d2) + (0.1d * d * d2) + (0.2d * Math.sqrt(Math.abs(d))) + ((((20.0d * Math.sin((6.0d * d) * a)) + (20.0d * Math.sin((2.0d * d) * a))) * 2.0d) / 3.0d) + ((((20.0d * Math.sin(a * d2)) + (40.0d * Math.sin((d2 / 3.0d) * a))) * 2.0d) / 3.0d) + ((((160.0d * Math.sin((d2 / 12.0d) * a)) + (320.0d * Math.sin((a * d2) / 30.0d))) * 2.0d) / 3.0d);
            double sin = ((((Math.sin((d / 30.0d) * a) * 300.0d) + (150.0d * Math.sin((d / 12.0d) * a))) * 2.0d) / 3.0d) + (d2 * 0.1d * d) + 300.0d + d + (2.0d * d2) + (0.1d * d * d) + (0.1d * Math.sqrt(Math.abs(d))) + ((((20.0d * Math.sin((6.0d * d) * a)) + (20.0d * Math.sin((2.0d * d) * a))) * 2.0d) / 3.0d) + ((((20.0d * Math.sin(a * d)) + (40.0d * Math.sin((d / 3.0d) * a))) * 2.0d) / 3.0d);
            double d3 = (latitude / 180.0d) * a;
            double sin2 = Math.sin(d3);
            double d4 = 1.0d - (sin2 * (0.006693421622965943d * sin2));
            double sqrt2 = Math.sqrt(d4);
            DPoint dPoint2 = new DPoint(latitude + ((sqrt * 180.0d) / ((6335552.717000426d / (d4 * sqrt2)) * a)), ((sin * 180.0d) / ((Math.cos(d3) * (6378245.0d / sqrt2)) * a)) + longitude);
            return new DPoint((2.0d * latitude) - dPoint2.getLatitude(), (longitude * 2.0d) - dPoint2.getLongitude());
        } catch (Throwable th) {
            es.a(th, "OffsetUtil", "b2G");
            return dPoint;
        }
    }

    private static double b(double d) {
        return Math.cos(3000.0d * d * (a / 180.0d)) * 3.0E-6d;
    }

    private static double b(double d, double d2) {
        return (Math.sin(d2 / 100000.0d) * (d / 18000.0d)) + (Math.cos(d / 100000.0d) * (d2 / 9000.0d));
    }

    public static DPoint b(Context context, DPoint dPoint) {
        try {
            if (!es.a(dPoint.getLatitude(), dPoint.getLongitude())) {
                return dPoint;
            }
            double longitude = (double) (((long) (dPoint.getLongitude() * 100000.0d)) % 36000000);
            double latitude = (double) (((long) (dPoint.getLatitude() * 100000.0d)) % 36000000);
            int i = (int) ((-b(longitude, latitude)) + latitude);
            int i2 = (int) (((double) (longitude > 0.0d ? 1 : -1)) + (-a((double) ((int) ((-a(longitude, latitude)) + longitude)), (double) i)) + longitude);
            return a(context, new DPoint(((double) ((int) (((double) (latitude > 0.0d ? 1 : -1)) + ((-b((double) i2, (double) i)) + latitude)))) / 100000.0d, ((double) i2) / 100000.0d));
        } catch (Throwable th) {
            es.a(th, "OffsetUtil", "marbar2G");
            return dPoint;
        }
    }

    private static DPoint b(DPoint dPoint) {
        double[] a2;
        try {
            if (!es.a(dPoint.getLatitude(), dPoint.getLongitude())) {
                return dPoint;
            }
            double[] dArr = new double[2];
            a2 = CoordUtil.convertToGcj(new double[]{dPoint.getLongitude(), dPoint.getLatitude()}, dArr) != 0 ? fb.a(dPoint.getLongitude(), dPoint.getLatitude()) : dArr;
            return new DPoint(a2[1], a2[0]);
        } catch (Throwable th) {
            es.a(th, "OffsetUtil", "cover part2");
            return dPoint;
        }
    }

    private static double c(double d) {
        return new BigDecimal(d).setScale(8, 4).doubleValue();
    }

    private static DPoint c(DPoint dPoint) {
        double d = 0.006401062d;
        double d2 = 0.0060424805d;
        DPoint dPoint2 = null;
        for (int i = 0; i < 2; i++) {
            double longitude = dPoint.getLongitude();
            double latitude = dPoint.getLatitude();
            dPoint2 = new DPoint();
            double d3 = longitude - d;
            double d4 = latitude - d2;
            DPoint dPoint3 = new DPoint();
            dPoint3.setLongitude(c((Math.cos(b(d3) + Math.atan2(d4, d3)) * (a(d4) + Math.sqrt((d3 * d3) + (d4 * d4)))) + 0.0065d));
            dPoint3.setLatitude(c((Math.sin(b(d3) + Math.atan2(d4, d3)) * (a(d4) + Math.sqrt((d3 * d3) + (d4 * d4)))) + 0.006d));
            dPoint2.setLongitude(c((d3 + longitude) - dPoint3.getLongitude()));
            dPoint2.setLatitude(c((d4 + latitude) - dPoint3.getLatitude()));
            d = dPoint.getLongitude() - dPoint2.getLongitude();
            d2 = dPoint.getLatitude() - dPoint2.getLatitude();
        }
        return dPoint2;
    }
}
