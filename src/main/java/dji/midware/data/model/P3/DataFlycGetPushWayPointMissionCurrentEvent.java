package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushWayPointMissionCurrentEvent extends DataBase {
    private static DataFlycGetPushWayPointMissionCurrentEvent instance = null;

    public static synchronized DataFlycGetPushWayPointMissionCurrentEvent getInstance() {
        DataFlycGetPushWayPointMissionCurrentEvent dataFlycGetPushWayPointMissionCurrentEvent;
        synchronized (DataFlycGetPushWayPointMissionCurrentEvent.class) {
            if (instance == null) {
                instance = new DataFlycGetPushWayPointMissionCurrentEvent();
            }
            dataFlycGetPushWayPointMissionCurrentEvent = instance;
        }
        return dataFlycGetPushWayPointMissionCurrentEvent;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int getEventType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getUploadIncidentIsValid() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getUploadIncidentEstimatedTime() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getUploadIncidentReserved() {
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    public int getFinishIncidentIsRepeat() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getFinishIncidentResrved() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getReachIncidentWayPointIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getReachIncidentCurrentStatus() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getReachIncidentReserved() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }
}
