package dji.common.camera;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.log.DJILog;

@EXClassNullAway
public class StabilizationState {
    private boolean isActive = false;
    private boolean isPaused = false;
    private StabilizationPauseReason pauseReason = StabilizationPauseReason.OTHER;

    public StabilizationState(boolean isActive2, boolean isPaused2, StabilizationPauseReason reason) {
        this.isActive = isActive2;
        this.isPaused = isPaused2;
        this.pauseReason = reason;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public StabilizationPauseReason getPauseReason() {
        return this.pauseReason;
    }

    public boolean equals(Object o) {
        if (!(o instanceof StabilizationState)) {
            return false;
        }
        StabilizationState that = (StabilizationState) o;
        if (isPaused() == that.isPaused() && isActive() == that.isActive() && getPauseReason() == that.getPauseReason()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 1;
        int i2 = 0;
        if (isPaused()) {
            result = 1;
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (!isActive()) {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (getPauseReason() != null) {
            i2 = getPauseReason().hashCode();
        }
        return i4 + i2;
    }

    public enum StabilizationPauseReason {
        NORMAL(-1),
        UNCHARACTERISTIC(0),
        CAMERA_CHANGING(1),
        GIMBAL_MOVING(2),
        DRONE_MOVING(3),
        TRACKING(4),
        TAP_GO(5),
        STABLE_FAIL(6),
        OTHER(255);
        
        private final int data;

        private StabilizationPauseReason(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static StabilizationPauseReason find(int value) {
            if (CommonUtil.isPM420Platform()) {
                value--;
            }
            StabilizationPauseReason result = OTHER;
            StabilizationPauseReason[] values = values();
            int length = values.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                StabilizationPauseReason tmp = values[i];
                if (tmp._equals(value)) {
                    result = tmp;
                    break;
                }
                i++;
            }
            DJILog.e("vision", "pause reason = " + result, new Object[0]);
            return result;
        }
    }
}
