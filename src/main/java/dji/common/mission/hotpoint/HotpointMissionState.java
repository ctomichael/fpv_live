package dji.common.mission.hotpoint;

import com.billy.cc.core.component.CCUtil;
import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class HotpointMissionState extends MissionState {
    public static final HotpointMissionState DISCONNECTED = new HotpointMissionState("DISCONNECTED");
    public static final HotpointMissionState EXECUTING = new HotpointMissionState("EXECUTING");
    public static final HotpointMissionState EXECUTION_PAUSED = new HotpointMissionState("EXECUTION_PAUSED");
    public static final HotpointMissionState INITIAL_PHASE = new HotpointMissionState("INITIAL_PHASE");
    public static final HotpointMissionState NOT_SUPPORTED = new HotpointMissionState("NOT_SUPPORTED");
    public static final HotpointMissionState READY_TO_EXECUTE = new HotpointMissionState("READY_TO_EXECUTE");
    public static final HotpointMissionState RECOVERING = new HotpointMissionState("RECOVERING");
    public static final HotpointMissionState UNKNOWN = new HotpointMissionState(CCUtil.PROCESS_UNKNOWN);

    private HotpointMissionState(String name) {
        super(name);
    }
}
