package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
public class DataEyeGetPushAdvancedPilotAssistantSystemState extends DJICommonDataBase {
    private static final short APAS_WORK_CONDITION_BINOCULAR_DEEP_IMAGE_INVALID_MASK = 32;
    private static final short APAS_WORK_CONDITION_FLIGHT_CONTROLLER_SUB_MODULE_UNSATISTIED_MASK = 2;
    private static final short APAS_WORK_CONDITION_IS_ON_AIR_MASK = 1;
    private static final short APAS_WORK_CONDITION_IS_ON_LIMIT_AREA_BOUNDARIES_MASK = 8;
    private static final short APAS_WORK_CONDITION_OTHER_NAVIGATION_MODULES_WORK_MASK = 4;
    private static final short APAS_WORK_CONDITION_POSITION_SPEED_INVALID_MASK = 16;
    private static DataEyeGetPushAdvancedPilotAssistantSystemState INSTANCE = null;

    public static synchronized DataEyeGetPushAdvancedPilotAssistantSystemState getInstance() {
        DataEyeGetPushAdvancedPilotAssistantSystemState dataEyeGetPushAdvancedPilotAssistantSystemState;
        synchronized (DataEyeGetPushAdvancedPilotAssistantSystemState.class) {
            if (INSTANCE == null) {
                INSTANCE = new DataEyeGetPushAdvancedPilotAssistantSystemState();
            }
            dataEyeGetPushAdvancedPilotAssistantSystemState = INSTANCE;
        }
        return dataEyeGetPushAdvancedPilotAssistantSystemState;
    }

    public boolean isAPASOn() {
        return ((Byte) get(0, 1, Byte.class)).byteValue() != 0;
    }

    public boolean isAPASWorking() {
        return ((Byte) get(1, 1, Byte.class)).byteValue() != 0;
    }

    public boolean isNotOnAir() {
        return (getConditions() & 1) != 0;
    }

    public boolean isFlightControllerSubModuleUnsatisfied() {
        return (getConditions() & 2) != 0;
    }

    public boolean otherNavigationModulesWork() {
        return (getConditions() & 4) != 0;
    }

    public boolean isOnLimitAreaBoundaries() {
        return (getConditions() & APAS_WORK_CONDITION_IS_ON_LIMIT_AREA_BOUNDARIES_MASK) != 0;
    }

    public boolean isPositionSpeedInvalid() {
        return (getConditions() & 16) != 0;
    }

    public boolean isBinocularDeepImageInvalid() {
        return (getConditions() & APAS_WORK_CONDITION_BINOCULAR_DEEP_IMAGE_INVALID_MASK) != 0;
    }

    private short getConditions() {
        return ((Short) get(2, 1, Short.class)).shortValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
