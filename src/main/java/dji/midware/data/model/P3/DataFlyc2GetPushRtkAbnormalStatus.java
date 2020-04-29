package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

public class DataFlyc2GetPushRtkAbnormalStatus extends DataBase {
    private static DataFlyc2GetPushRtkAbnormalStatus instance = null;

    private DataFlyc2GetPushRtkAbnormalStatus() {
    }

    public static synchronized DataFlyc2GetPushRtkAbnormalStatus getInstance() {
        DataFlyc2GetPushRtkAbnormalStatus dataFlyc2GetPushRtkAbnormalStatus;
        synchronized (DataFlyc2GetPushRtkAbnormalStatus.class) {
            if (instance == null) {
                instance = new DataFlyc2GetPushRtkAbnormalStatus();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = true;
            }
            dataFlyc2GetPushRtkAbnormalStatus = instance;
        }
        return dataFlyc2GetPushRtkAbnormalStatus;
    }

    public boolean isBaseStationMoving() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 17) & 1) == 1;
    }

    public boolean isBaseStationFell() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 16) & 1) == 1;
    }

    public boolean isBaseStationSourceChanged() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 15) & 1) == 1;
    }

    public boolean isRTKVersionNotMatched() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 14) & 1) == 1;
    }

    public boolean isSpeedChangedSlowlyWhenStill() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 13) & 1) == 1;
    }

    public boolean isPositionChangedSlowlyWhenStill() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 12) & 1) == 1;
    }

    public boolean isRtkHasBigDifferenceWithGps() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 11) & 1) == 1;
    }

    public boolean isRtkSpeedAndPositionNotMatched() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 10) & 1) == 1;
    }

    public boolean isSinglePositionDifferentWithFixPosition() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 9) & 1) == 1;
    }

    public boolean isHeightDifferent() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 8) & 1) == 1;
    }

    public boolean isSpeedChangeRapidly() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 7) & 1) == 1;
    }

    public boolean isPositionChangeRapidly() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 6) & 1) == 1;
    }

    public boolean isStuck() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 5) & 1) == 1;
    }

    public boolean isDataInvalid() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 4) & 1) == 1;
    }

    public boolean isFrequencyError() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 3) & 1) == 1;
    }

    public boolean isCourseNotMatched() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 2) & 1) == 1;
    }

    public boolean isResultExceedsLimitation() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >>> 1) & 1) == 1;
    }

    public boolean isDisconnected() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isPositionSwitchOn() {
        return ((((Integer) get(4, 2, Integer.class)).intValue() >>> 10) & 1) == 1;
    }

    public boolean isYawSwitchOn() {
        return ((((Integer) get(4, 2, Integer.class)).intValue() >>> 9) & 1) == 1;
    }

    public ActionRequired getPositioningActionRequired() {
        return ActionRequired.find((((Integer) get(4, 2, Integer.class)).intValue() >>> 7) & 3);
    }

    public ActionRequired getOrienteeringActionRequired() {
        return ActionRequired.find((((Integer) get(4, 2, Integer.class)).intValue() >>> 5) & 3);
    }

    public boolean isHeightInvalid() {
        return ((((Integer) get(4, 2, Integer.class)).intValue() >>> 4) & 1) == 1;
    }

    public boolean isHorizontalVelocityInvalid() {
        return ((((Integer) get(4, 2, Integer.class)).intValue() >>> 3) & 1) == 1;
    }

    public boolean isHorizontalPositionInvalid() {
        return ((((Integer) get(4, 2, Integer.class)).intValue() >>> 2) & 1) == 1;
    }

    public boolean isYawInvalid() {
        return ((((Integer) get(4, 2, Integer.class)).intValue() >>> 1) & 1) == 1;
    }

    public boolean isRTKDisconnected() {
        return (((Integer) get(4, 2, Integer.class)).intValue() & 1) == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public enum ActionRequired {
        NO_ACTION_REQUIRED(0),
        FLY_CAUTIOUSLY(1),
        RESTART(2);
        
        private final int value;

        private ActionRequired(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ActionRequired find(int value2) {
            ActionRequired result = NO_ACTION_REQUIRED;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
