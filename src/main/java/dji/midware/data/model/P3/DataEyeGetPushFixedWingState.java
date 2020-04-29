package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushFixedWingState extends DataBase {
    private static DataEyeGetPushFixedWingState instance = null;

    public static synchronized DataEyeGetPushFixedWingState getInstance() {
        DataEyeGetPushFixedWingState dataEyeGetPushFixedWingState;
        synchronized (DataEyeGetPushFixedWingState.class) {
            if (instance == null) {
                instance = new DataEyeGetPushFixedWingState();
            }
            dataEyeGetPushFixedWingState = instance;
        }
        return dataEyeGetPushFixedWingState;
    }

    public FixedWingState getFixedWingState() {
        return FixedWingState.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public boolean rcModeError() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 1) != 0;
    }

    public boolean cantDetour() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isBraking() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 4) != 0;
    }

    public boolean isDetourUp() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 8) != 0;
    }

    public boolean exitByPortrait() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 16) != 0;
    }

    public boolean isFixWingGimbalCtrled() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 32) == 0;
    }

    public boolean isPullPitchStick() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 256) != 0;
    }

    public boolean isDroneTooLow() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 512) != 0;
    }

    public boolean hasDelay() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 1024) != 0;
    }

    public boolean forceHoriFly() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 4096) != 0;
    }

    public boolean isPaused() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 8192) != 0;
    }

    public boolean isDecelerating() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 16384) != 0;
    }

    public boolean isSpinning() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 32768) != 0;
    }

    public boolean isFronImageOverExposure() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 65536) != 0;
    }

    public boolean isFronImageUnderExposure() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 131072) != 0;
    }

    public boolean isFrontImageDiff() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 262144) != 0;
    }

    public boolean isFrontSensorDemarkAbnormal() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 524288) != 0;
    }

    public boolean isNonFlying() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 1048576) != 0;
    }

    public boolean stopByUser() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 2097152) != 0;
    }

    public boolean isTripodFolded() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 4194304) != 0;
    }

    public boolean isNearNonFlyZone() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & 8388608) != 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum FixedWingState {
        NON_MATCH(0),
        MATCH_CONDITION(1),
        ALREADY(2),
        OTHER(100);
        
        private static volatile FixedWingState[] sValues = null;
        private final int data;

        private FixedWingState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FixedWingState find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            FixedWingState result = NON_MATCH;
            FixedWingState[] fixedWingStateArr = sValues;
            for (FixedWingState tmp : fixedWingStateArr) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
