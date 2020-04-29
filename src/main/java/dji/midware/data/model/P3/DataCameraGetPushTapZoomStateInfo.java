package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushTapZoomStateInfo extends DJICameraDataBase {
    private static DataCameraGetPushTapZoomStateInfo instance = null;

    public static synchronized DataCameraGetPushTapZoomStateInfo getInstance() {
        DataCameraGetPushTapZoomStateInfo dataCameraGetPushTapZoomStateInfo;
        synchronized (DataCameraGetPushTapZoomStateInfo.class) {
            if (instance == null) {
                instance = new DataCameraGetPushTapZoomStateInfo();
                instance.isNeedPushLosed = true;
            }
            dataCameraGetPushTapZoomStateInfo = instance;
        }
        return dataCameraGetPushTapZoomStateInfo;
    }

    public WorkingState getWorkingState() {
        return WorkingState.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getTapZoomState() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getGimbalState() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getMultiplier() {
        return getMultiplier(-1);
    }

    public int getMultiplier(int index) {
        return ((Integer) get(2, 1, Integer.class, index)).intValue();
    }

    @Keep
    public enum WorkingState {
        IDLE(0),
        ZOOM_IN(1),
        ZOOM_OUT(2),
        ZOOM_LIMITED(3),
        Unknown(255);
        
        private int value;

        private WorkingState(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        public boolean _equals(int b) {
            return this.value == b;
        }

        public static WorkingState find(int value2) {
            WorkingState result = Unknown;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
