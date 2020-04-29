package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushDrawState extends DJICommonDataBase {
    private static DataEyeGetPushDrawState instance = null;

    public static synchronized DataEyeGetPushDrawState getInstance() {
        DataEyeGetPushDrawState dataEyeGetPushDrawState;
        synchronized (DataEyeGetPushDrawState.class) {
            if (instance == null) {
                instance = new DataEyeGetPushDrawState();
            }
            dataEyeGetPushDrawState = instance;
        }
        return dataEyeGetPushDrawState;
    }

    public DrawState getState() {
        if (this._recData == null || this._recData.length == 0) {
            return DrawState.INIT;
        }
        return DrawState.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public boolean isComplexPoints() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 8) != 0;
    }

    public boolean beInvalidHeight() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 16) != 0;
    }

    public boolean beInvalidPoints() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 32) != 0;
    }

    public boolean isBraking() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 64) != 0;
    }

    public boolean canDetour() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 128) != 0;
    }

    public boolean isDetourUp() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 256) != 0;
    }

    public boolean isDetourLeft() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 512) != 0;
    }

    public boolean isDetourRight() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 1024) != 0;
    }

    public boolean isPullPitchStick() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 2048) != 0;
    }

    public boolean isPaused() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 8192) != 0;
    }

    public boolean isDecelerating() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 16384) != 0;
    }

    public boolean reachEndPoint() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 32768) != 0;
    }

    public boolean isFronImageOverExposure() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 65536) != 0;
    }

    public boolean isFronImageUnderExposure() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 131072) != 0;
    }

    public boolean isFrontImageDiff() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 262144) != 0;
    }

    public boolean isFrontSensorDemarkAbnormal() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 524288) != 0;
    }

    public boolean isNonFlying() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 1048576) != 0;
    }

    public boolean isDroneTooLow() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 2097152) != 0;
    }

    public boolean isTripodFolded() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 4194304) != 0;
    }

    public boolean isNearNonFlyZone() {
        return (((Integer) get(1, 5, Integer.class)).intValue() & 8388608) != 0;
    }

    public int getVelocity() {
        return ((Integer) get(6, 2, Integer.class)).intValue();
    }

    public int getSequence() {
        return ((Integer) get(8, 1, Integer.class)).intValue();
    }

    public int getTime() {
        return ((Integer) get(9, 2, Integer.class)).intValue();
    }

    public DataSingleVisualParam.DrawMode getDrawMode() {
        return DataSingleVisualParam.DrawMode.find(((Integer) get(11, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum DrawState {
        INIT(0),
        PREPARE(1),
        READY_TO_GO(2),
        START_AUTO(3),
        START_MANUAL(4),
        PAUSE(5),
        OTHER(100);
        
        private final int data;

        private DrawState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DrawState find(int b) {
            DrawState result = INIT;
            DrawState[] values = values();
            for (DrawState tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
