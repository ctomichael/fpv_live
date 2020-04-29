package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushWayPointMissionInfo extends DataBase {
    private static DataFlycGetPushWayPointMissionInfo instance = null;

    public static synchronized DataFlycGetPushWayPointMissionInfo getInstance() {
        DataFlycGetPushWayPointMissionInfo dataFlycGetPushWayPointMissionInfo;
        synchronized (DataFlycGetPushWayPointMissionInfo.class) {
            if (instance == null) {
                instance = new DataFlycGetPushWayPointMissionInfo();
            }
            dataFlycGetPushWayPointMissionInfo = instance;
        }
        return dataFlycGetPushWayPointMissionInfo;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getMissionType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getReserved() {
        return ((Integer) get(1, 3, Integer.class)).intValue();
    }

    public int getTargetWayPoint() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getCurrentStatus() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getErrorNotification() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getWaypointMissionVelocity() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public int getWayPointStatus() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    @Keep
    public enum RunningStatus {
        NotRunning(0),
        Running(1),
        Paused(2);
        
        private int data;

        private RunningStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RunningStatus find(int b) {
            RunningStatus result = NotRunning;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public RunningStatus getRunningStatus() {
        return RunningStatus.find(((Integer) get(4, 1, Integer.class)).intValue());
    }

    public int getHotPointMissionStatus() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getHotPointRadius() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getHotPointReason() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public int getHotPointSpeed() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public boolean isCameraTrackingEnabled() {
        return ((Integer) get(6, 1, Integer.class)).intValue() == 1;
    }

    public boolean isHotPointClockwise() {
        return (((Integer) get(7, 1, Integer.class)).intValue() & 1) == 1;
    }

    public float getMaxHotPointSpeed() {
        return ((Float) get(8, 4, Float.class)).floatValue();
    }

    public int getFollowMeStatus() {
        return ((Integer) get(1, 1, Integer.class)).intValue() & 15;
    }

    public int getFollowMeGpsLevel() {
        return (((Integer) get(1, 1, Integer.class)).intValue() >> 4) & 15;
    }

    public int getFollowMeDistance() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getFollowMeReason() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }

    public int getMissionStatus() {
        return ((Integer) get(1, 1, Integer.class)).intValue() & 3;
    }

    public boolean isPositionValid() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 4) == 4;
    }

    public int getLimitedHeight() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getCurrentHeight() {
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    public int isTrackingEnabled() {
        return ((Integer) get(6, 1, Integer.class)).intValue();
    }

    public boolean isClockwise() {
        return (((Integer) get(7, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getLastMissionType() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean isBroken() {
        if (getMissionType() != 6) {
            return false;
        }
        if ((((Integer) get(2, 1, Integer.class)).intValue() & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isVelocityControl() {
        if (getMissionType() == 6) {
            return (((Integer) get(2, 1, Integer.class)).intValue() & 2) != 0;
        }
        return true;
    }
}
