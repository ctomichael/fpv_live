package dji.common.mission.waypoint;

import com.billy.cc.core.component.CCUtil;
import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class WaypointMissionState extends MissionState {
    public static final WaypointMissionState DISCONNECTED = new WaypointMissionState("DISCONNECTED");
    public static final WaypointMissionState EXECUTING = new WaypointMissionState("EXECUTING");
    public static final WaypointMissionState EXECUTION_PAUSED = new WaypointMissionState("EXECUTION_PAUSED");
    public static final WaypointMissionState NOT_SUPPORTED = new WaypointMissionState("NOT_SUPPORTED");
    public static final WaypointMissionState READY_TO_EXECUTE = new WaypointMissionState("READY_TO_EXECUTE");
    public static final WaypointMissionState READY_TO_UPLOAD = new WaypointMissionState("READY_TO_UPLOAD");
    public static final WaypointMissionState RECOVERING = new WaypointMissionState("RECOVERING");
    public static final WaypointMissionState UNKNOWN = new WaypointMissionState(CCUtil.PROCESS_UNKNOWN);
    public static final WaypointMissionState UPLOADING = new WaypointMissionState("UPLOADING");

    private WaypointMissionState(String name) {
        super(name);
    }
}
