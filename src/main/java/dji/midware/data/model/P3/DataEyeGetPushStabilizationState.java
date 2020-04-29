package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushStabilizationState extends DataBase {
    private static DataEyeGetPushStabilizationState instance = null;

    public static synchronized DataEyeGetPushStabilizationState getInstance() {
        DataEyeGetPushStabilizationState dataEyeGetPushStabilizationState;
        synchronized (DataEyeGetPushStabilizationState.class) {
            if (instance == null) {
                instance = new DataEyeGetPushStabilizationState();
            }
            dataEyeGetPushStabilizationState = instance;
        }
        return dataEyeGetPushStabilizationState;
    }

    public boolean getStateIsPaused() {
        if (!getStateIsTurnOn()) {
            return true;
        }
        if ((((Integer) get(0, 1, Integer.class)).intValue() & 64) == 0) {
            return true;
        }
        return false;
    }

    public boolean getStateIsTurnOn() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 128) != 0;
    }

    public PauseReason getPauseReason() {
        return PauseReason.find(((Integer) get(0, 1, Integer.class)).intValue() & 63);
    }

    public int getPauseReasonValue() {
        return ((Integer) get(0, 1, Integer.class)).intValue() & 63;
    }

    public NewPauseReason getNewPauseReason() {
        return NewPauseReason.find(((Integer) get(0, 1, Integer.class)).intValue() & 63);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public enum NewPauseReason {
        Normal(0),
        UnCharacteristic(1),
        CameraChanging(2),
        GimbalMoving(3),
        DroneMoving(4),
        Tracking(5),
        TapGo(6),
        OTHER(255);
        
        private final int data;

        private NewPauseReason(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static NewPauseReason find(int b) {
            NewPauseReason result = OTHER;
            NewPauseReason[] values = values();
            for (NewPauseReason tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum PauseReason {
        UnCharacteristic(0),
        CameraChanging(1),
        GimbalMoving(2),
        DroneMoving(3),
        Tracking(4),
        TapGo(5),
        OTHER(255);
        
        private final int data;

        private PauseReason(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PauseReason find(int b) {
            PauseReason result = OTHER;
            PauseReason[] values = values();
            for (PauseReason tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
