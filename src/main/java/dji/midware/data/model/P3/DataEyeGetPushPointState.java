package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataSingleSetPointPos;

@Keep
@EXClassNullAway
public class DataEyeGetPushPointState extends DataBase {
    private static DataEyeGetPushPointState instance = null;

    public static synchronized DataEyeGetPushPointState getInstance() {
        DataEyeGetPushPointState dataEyeGetPushPointState;
        synchronized (DataEyeGetPushPointState.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPointState();
            }
            dataEyeGetPushPointState = instance;
        }
        return dataEyeGetPushPointState;
    }

    public PointMode getTragetMode() {
        return PointMode.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public boolean rcNotInFMode() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 1) != 0;
    }

    public boolean cantDetour() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 2) != 0;
    }

    public boolean brakedByCollision() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 4) != 0;
    }

    public boolean detourUp() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 8) != 0;
    }

    public boolean detourLeft() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 16) != 0;
    }

    public boolean detourRight() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 32) != 0;
    }

    public boolean isStickAdd() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 64) != 0;
    }

    public boolean isOutOfRange() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 128) != 0;
    }

    public boolean isUserQuickPullPitch() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 256) != 0;
    }

    public boolean isInLowFlying() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 512) != 0;
    }

    public boolean isRunningDelay() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 1024) != 0;
    }

    public boolean isInPointing() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 2048) != 0;
    }

    public boolean isTerrianFollow() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 4096) != 0;
    }

    public boolean isPaused() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 8192) != 0;
    }

    public boolean isQuickSpinTapGo() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 32768) != 0;
    }

    public boolean isFrontImageOverExposure() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 65536) != 0;
    }

    public boolean isFrontImageUnderExposure() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 131072) != 0;
    }

    public boolean isFrontImageDiff() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 262144) != 0;
    }

    public boolean isFrontDemarkError() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 524288) != 0;
    }

    public boolean isNonInFlying() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 1048576) != 0;
    }

    public boolean isHadTapStop() {
        return (((Integer) get(1, 3, Integer.class)).intValue() & 2097152) != 0;
    }

    public float getAxisX() {
        return ((Float) get(4, 4, Float.class)).floatValue();
    }

    public float getAxisY() {
        return ((Float) get(8, 4, Float.class)).floatValue();
    }

    public float getAxisZ() {
        return ((Float) get(12, 4, Float.class)).floatValue();
    }

    public float getMaxSpeed() {
        return ((Float) get(16, 2, Float.class)).floatValue();
    }

    public int getSessionId() {
        return ((Integer) get(18, 2, Integer.class)).intValue();
    }

    public DataSingleSetPointPos.TapMode getTapMode() {
        if (this._recData == null || this._recData.length <= 20) {
            return DataSingleSetPointPos.TapMode.POSITIVE_FLY;
        }
        return DataSingleSetPointPos.TapMode.find(((Integer) get(20, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        setRecData(data);
        if (isWantPush()) {
            post();
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum PointMode {
        HIDE(0),
        CANT_FLY(1),
        FLYING(2),
        END_FLY(3),
        OTHER(100);
        
        private int data;

        private PointMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PointMode find(int b) {
            PointMode result = HIDE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
