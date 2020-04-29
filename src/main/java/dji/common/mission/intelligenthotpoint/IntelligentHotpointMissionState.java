package dji.common.mission.intelligenthotpoint;

import com.billy.cc.core.component.CCUtil;
import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class IntelligentHotpointMissionState extends MissionState {
    public static final IntelligentHotpointMissionState DISCONNECTED = new IntelligentHotpointMissionState("DISCONNECTED");
    public static final IntelligentHotpointMissionState EXECUTING = new IntelligentHotpointMissionState("EXECUTING");
    public static final IntelligentHotpointMissionState EXECUTION_PAUSED = new IntelligentHotpointMissionState("EXECUTION_PAUSED");
    public static final IntelligentHotpointMissionState MEASURING_TARGET = new IntelligentHotpointMissionState("MEASURING_TARGET");
    public static final IntelligentHotpointMissionState NOT_READY = new IntelligentHotpointMissionState("NOT_READY");
    public static final IntelligentHotpointMissionState NOT_SUPPORTED = new IntelligentHotpointMissionState("NOT_SUPPORTED");
    public static final IntelligentHotpointMissionState READY_TO_START = new IntelligentHotpointMissionState("READY_TO_START");
    public static final IntelligentHotpointMissionState RECOGNIZING_TARGET = new IntelligentHotpointMissionState("RECOGNIZING_TARGET");
    public static final IntelligentHotpointMissionState RECOVERING = new IntelligentHotpointMissionState("RECOVERING");
    public static final IntelligentHotpointMissionState UNKNOWN = new IntelligentHotpointMissionState(CCUtil.PROCESS_UNKNOWN);
    public static final IntelligentHotpointMissionState WAITING_FOR_CONFIRMATION = new IntelligentHotpointMissionState("WAITING_FOR_CONFIRMATION");

    private IntelligentHotpointMissionState(String name) {
        super(name);
    }
}
