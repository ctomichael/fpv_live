package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataCameraGetPushTimelapseParms extends DJICameraDataBase {
    private static DataCameraGetPushTimelapseParms instance = null;

    @Keep
    public class TimelapsePushPointInfo {
        public int duration = 0;
        public int interval = 0;
        public int pitch = 0;
        public int roll = 0;
        public int yaw = 0;

        public TimelapsePushPointInfo() {
        }
    }

    public static synchronized DataCameraGetPushTimelapseParms getInstance() {
        DataCameraGetPushTimelapseParms dataCameraGetPushTimelapseParms;
        synchronized (DataCameraGetPushTimelapseParms.class) {
            if (instance == null) {
                instance = new DataCameraGetPushTimelapseParms();
            }
            dataCameraGetPushTimelapseParms = instance;
        }
        return dataCameraGetPushTimelapseParms;
    }

    public int getControlMode() {
        return ((Integer) get(0, 1, Integer.class)).intValue() & 3;
    }

    public int getGimbalPointCount() {
        return ((Integer) get(0, 1, Integer.class)).intValue() >> 2;
    }

    public ArrayList<TimelapsePushPointInfo> getGimbalInfoPoints() {
        ArrayList<TimelapsePushPointInfo> mGimbalPoints = new ArrayList<>();
        for (int i = 1; i <= getGimbalPointCount(); i++) {
            TimelapsePushPointInfo p = new TimelapsePushPointInfo();
            p.interval = getInterval(i);
            p.duration = getDuration(i);
            p.yaw = getYaw(i);
            p.roll = getRoll(i);
            p.pitch = getPitch(i);
            mGimbalPoints.add(p);
        }
        return mGimbalPoints;
    }

    public int getInterval(int gimbalIndex) {
        return ((Integer) get(((gimbalIndex - 1) * 12) + 1, 2, Integer.class)).intValue();
    }

    public int getDuration(int gimbalIndex) {
        return ((Integer) get(((gimbalIndex - 1) * 12) + 3, 4, Integer.class)).intValue();
    }

    public int getTotalDuration() {
        int duration = 0;
        for (int i = 1; i <= getGimbalPointCount(); i++) {
            duration += getDuration(i);
        }
        return duration;
    }

    public int getYaw(int gimbalIndex) {
        return ((Integer) get(((gimbalIndex - 1) * 12) + 7, 2, Integer.class)).intValue();
    }

    public int getRoll(int gimbalIndex) {
        return ((Integer) get(((gimbalIndex - 1) * 12) + 9, 2, Integer.class)).intValue();
    }

    public int getPitch(int gimbalIndex) {
        return ((Integer) get(((gimbalIndex - 1) * 12) + 11, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
