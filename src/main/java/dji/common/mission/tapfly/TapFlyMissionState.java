package dji.common.mission.tapfly;

import com.billy.cc.core.component.CCUtil;
import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class TapFlyMissionState extends MissionState {
    public static final TapFlyMissionState CAN_NOT_START = new TapFlyMissionState("CAN_NOT_START");
    public static final TapFlyMissionState DISCONNECTED = new TapFlyMissionState("DISCONNECTED");
    public static final TapFlyMissionState EXECUTING = new TapFlyMissionState("EXECUTING");
    public static final TapFlyMissionState EXECUTION_PAUSED = new TapFlyMissionState("EXECUTION_PAUSED");
    public static final TapFlyMissionState EXECUTION_RESETTING = new TapFlyMissionState("EXECUTION_RESETTING");
    public static final TapFlyMissionState EXECUTION_STARTING = new TapFlyMissionState("EXECUTION_STARTING");
    public static final TapFlyMissionState IDLE = new TapFlyMissionState("IDLE");
    public static final TapFlyMissionState NOT_SUPPORT = new TapFlyMissionState("NOT_SUPPORT");
    public static final TapFlyMissionState RECOVERING = new TapFlyMissionState("RECOVERING");
    public static final TapFlyMissionState UNKNOWN = new TapFlyMissionState(CCUtil.PROCESS_UNKNOWN);

    private TapFlyMissionState(String value) {
        super(value);
    }
}
