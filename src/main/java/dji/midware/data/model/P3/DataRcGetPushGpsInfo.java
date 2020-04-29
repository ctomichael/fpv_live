package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataRcGetPushGpsInfo extends DataBase {
    private static DataRcGetPushGpsInfo instance = null;

    public static synchronized DataRcGetPushGpsInfo getInstance() {
        DataRcGetPushGpsInfo dataRcGetPushGpsInfo;
        synchronized (DataRcGetPushGpsInfo.class) {
            if (instance == null) {
                instance = new DataRcGetPushGpsInfo();
            }
            dataRcGetPushGpsInfo = instance;
        }
        return dataRcGetPushGpsInfo;
    }

    public DataRcGetPushGpsInfo() {
    }

    public DataRcGetPushGpsInfo(boolean isRegist) {
        super(isRegist);
    }

    public double getLatitude() {
        return (((double) ((Integer) get(7, 4, Integer.class)).intValue()) * 1.0d) / 1.0E7d;
    }

    public double getLongitude() {
        return (((double) ((Integer) get(11, 4, Integer.class)).intValue()) * 1.0d) / 1.0E7d;
    }

    public int getXSpeed() {
        return ((Integer) get(15, 4, Integer.class)).intValue() / 1000;
    }

    public int getYSpeed() {
        return ((Integer) get(19, 4, Integer.class)).intValue() / 1000;
    }

    public int getGpsNum() {
        return ((Short) get(23, 1, Short.class)).shortValue();
    }

    public boolean getGpsStatus() {
        return ((Short) get(28, 2, Short.class)).shortValue() == 1;
    }

    public short getHour() {
        return ((Short) get(0, 1, Short.class)).shortValue();
    }

    public short getMinute() {
        return ((Short) get(1, 1, Short.class)).shortValue();
    }

    public short getSecond() {
        return ((Short) get(2, 1, Short.class)).shortValue();
    }

    public int getYear() {
        return ((Integer) get(3, 2, Integer.class)).intValue();
    }

    public short getMonth() {
        return ((Short) get(5, 1, Short.class)).shortValue();
    }

    public short getDay() {
        return ((Short) get(6, 1, Short.class)).shortValue();
    }

    public Float getAccuracy() {
        return (Float) get(24, 4, Float.class);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
