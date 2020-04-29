package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataCenterRTKPushStatus extends DataBase {
    private static volatile DataCenterRTKPushStatus mInstance = null;

    public DataCenterRTKPushStatus(boolean need) {
        this.isNeedPushLosed = need;
    }

    public static DataCenterRTKPushStatus getInstance() {
        if (mInstance == null) {
            mInstance = new DataCenterRTKPushStatus(true);
        }
        return mInstance;
    }

    public int getErrorCode() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getStatus() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getAntannaMGPSNum() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getAntannaMBeidouNum() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getAntannaMGlonassNum() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public int getAntannaSGPSNum() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public int getAntannaSBeidouNum() {
        return ((Integer) get(6, 1, Integer.class)).intValue();
    }

    public int getAntannaSGlonassNum() {
        return ((Integer) get(7, 1, Integer.class)).intValue();
    }

    public int getBaseStationGPSNum() {
        return ((Integer) get(8, 1, Integer.class)).intValue();
    }

    public int getBaseStationBeidouNum() {
        return ((Integer) get(9, 1, Integer.class)).intValue();
    }

    public int getBaseStationGlonassNum() {
        return ((Integer) get(10, 1, Integer.class)).intValue();
    }

    public double getSkyLat() {
        return ((Double) get(11, 8, Double.class)).doubleValue();
    }

    public double getSkyLng() {
        return ((Double) get(19, 8, Double.class)).doubleValue();
    }

    public float getSkyHeight() {
        return ((Float) get(27, 4, Float.class)).floatValue();
    }

    public double getGroundLat() {
        return ((Double) get(31, 8, Double.class)).doubleValue();
    }

    public double getGroundLng() {
        return ((Double) get(39, 8, Double.class)).doubleValue();
    }

    public float getGroundHeight() {
        return ((Float) get(47, 4, Float.class)).floatValue();
    }

    public float getLocateAngle() {
        return ((Float) get(51, 4, Float.class)).floatValue();
    }

    public int getLocateEnable() {
        return ((Integer) get(55, 1, Integer.class)).intValue();
    }

    public boolean isRTKOpen() {
        return ((Integer) get(56, 1, Integer.class)).intValue() == 1;
    }

    public boolean isDirectionEnabled() {
        return ((Integer) get(55, 1, Integer.class)).intValue() == 50;
    }

    public boolean isRtkEnabled() {
        return ((Integer) get(56, 1, Integer.class)).intValue() == 1;
    }

    public float getDiffAge() {
        return ((Float) get(57, 4, Float.class)).floatValue();
    }

    public boolean isPwrEnabled() {
        return ((Integer) get(57, 4, Integer.class)).intValue() == 1;
    }

    public float getStdLat() {
        return ((Float) get(65, 4, Float.class)).floatValue();
    }

    public float getStdLon() {
        return ((Float) get(69, 4, Float.class)).floatValue();
    }

    public float getStdAlt() {
        return ((Float) get(73, 4, Float.class)).floatValue();
    }

    public int getAntannaMGalileoNum() {
        return ((Integer) get(77, 1, Integer.class)).intValue();
    }

    public int getAntannaSGalileoNum() {
        return ((Integer) get(78, 1, Integer.class)).intValue();
    }

    public int getBaseStationGalileoNum() {
        return ((Integer) get(79, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
