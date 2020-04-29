package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushRTKLocationData extends DataBase {
    private static DataFlycGetPushRTKLocationData instance = null;

    public static synchronized DataFlycGetPushRTKLocationData getInstance() {
        DataFlycGetPushRTKLocationData dataFlycGetPushRTKLocationData;
        synchronized (DataFlycGetPushRTKLocationData.class) {
            if (instance == null) {
                instance = new DataFlycGetPushRTKLocationData();
            }
            dataFlycGetPushRTKLocationData = instance;
        }
        return dataFlycGetPushRTKLocationData;
    }

    public double getLongitude() {
        return (((Double) get(0, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public double getLatitude() {
        return (((Double) get(8, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public float getHeight() {
        return ((Float) get(16, 4, Float.class)).floatValue();
    }

    public int getHeading() {
        return ((Short) get(20, 2, Short.class)).shortValue();
    }

    public boolean isRTKConnected() {
        return ((Integer) get(22, 1, Integer.class)).intValue() == 1;
    }

    public boolean isRTKHealthy() {
        return ((Integer) get(23, 1, Integer.class)).intValue() == 1;
    }

    public int getUTCSeconds() {
        return ((Integer) get(24, 4, Integer.class)).intValue();
    }

    public int getUTCNanoSeconds() {
        return ((Integer) get(28, 4, Integer.class)).intValue();
    }

    public int getDistanceToHomeDataSource() {
        return (((Integer) get(32, 1, Integer.class)).intValue() >> 3) & 3;
    }

    public boolean hasSetRTKTakeOffHeight() {
        return ((((Integer) get(32, 1, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    public int getHomePointDataSource() {
        return ((Integer) get(32, 1, Integer.class)).intValue() & 3;
    }

    public int getGPSCount() {
        return ((Integer) get(33, 1, Integer.class)).intValue();
    }

    public double getHomePointLongitude() {
        return (((Double) get(34, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public double getHomePointLatitude() {
        return (((Double) get(42, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public float getTakeOffAltitude() {
        return ((Float) get(50, 4, Float.class)).floatValue();
    }

    public float getAircraftDistanceToHome() {
        return ((Float) get(54, 4, Float.class)).floatValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
