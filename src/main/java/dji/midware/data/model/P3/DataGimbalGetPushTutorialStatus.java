package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataGimbalGetPushTutorialStatus extends DataBase {
    private static DataGimbalGetPushTutorialStatus instance = null;

    public static synchronized DataGimbalGetPushTutorialStatus getInstance() {
        DataGimbalGetPushTutorialStatus dataGimbalGetPushTutorialStatus;
        synchronized (DataGimbalGetPushTutorialStatus.class) {
            if (instance == null) {
                instance = new DataGimbalGetPushTutorialStatus();
            }
            dataGimbalGetPushTutorialStatus = instance;
        }
        return dataGimbalGetPushTutorialStatus;
    }

    @Keep
    public enum TutorialStatus {
        STEP_FINISH(0),
        STEP_START(1),
        STEP_UNLOCK_GIMBAL(2),
        STEP_HOLD_GIMBAL_UPRIGHT(3),
        STEP_FOLLOW(4),
        STEP_STICK(5),
        STEP_LOCK_DIRECTION(6),
        STEP_RECENTER(7),
        STEP_SELFIE(8),
        STEP_PUSH(9),
        STEP_APP_CONTROL(10),
        OTHER(255);
        
        private int data;

        private TutorialStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TutorialStatus find(int b) {
            TutorialStatus result = STEP_FINISH;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public TutorialStatus getCurStep() {
        return TutorialStatus.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public boolean getStepStatus(TutorialStatus step) {
        return ((((Integer) get(1, 4, Integer.class)).intValue() >> (step.value() + -2)) & 1) == 1;
    }

    public int getIsUnlock() {
        return ((Integer) get(1, 4, Integer.class)).intValue() & 1;
    }

    public int getIsUpright() {
        return (((Integer) get(1, 4, Integer.class)).intValue() >> 1) & 1;
    }

    public int getIsFollowFinish() {
        return (((Integer) get(1, 4, Integer.class)).intValue() >> 2) & 1;
    }

    public int getIsStickFinish() {
        return (((Integer) get(1, 4, Integer.class)).intValue() >> 3) & 1;
    }

    public int getIsLockDirectionFinish() {
        return (((Integer) get(1, 4, Integer.class)).intValue() >> 4) & 1;
    }

    public int getIsRecentFinish() {
        return (((Integer) get(1, 4, Integer.class)).intValue() >> 5) & 1;
    }

    public int getIsSelfieFinish() {
        return (((Integer) get(1, 4, Integer.class)).intValue() >> 6) & 1;
    }

    public int getIsHandlePushFinish() {
        return (((Integer) get(1, 4, Integer.class)).intValue() >> 7) & 1;
    }

    public int getIsAppControlFinish() {
        return (((Integer) get(1, 4, Integer.class)).intValue() >> 8) & 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
