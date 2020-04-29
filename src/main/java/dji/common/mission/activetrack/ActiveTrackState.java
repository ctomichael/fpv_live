package dji.common.mission.activetrack;

import com.billy.cc.core.component.CCUtil;
import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ActiveTrackState extends MissionState {
    public static final ActiveTrackState AIRCRAFT_FOLLOWING = new ActiveTrackState("AIRCRAFT_FOLLOWING");
    public static final ActiveTrackState AUTO_SENSING = new ActiveTrackState("AUTO_SENSING");
    public static final ActiveTrackState AUTO_SENSING_FOR_QUICK_SHOT = new ActiveTrackState("AUTO_SENSING_FOR_QUICK_SHOT");
    public static final ActiveTrackState CANNOT_CONFIRM = new ActiveTrackState("CANNOT_CONFIRM");
    public static final ActiveTrackState CANNOT_START = new ActiveTrackState("CANNOT_START");
    public static final ActiveTrackState DETECTING_HUMAN = new ActiveTrackState("DETECTING_HUMAN");
    public static final ActiveTrackState DISCONNECTED = new ActiveTrackState("DISCONNECTED");
    public static final ActiveTrackState FINDING_TRACKED_TARGET = new ActiveTrackState("FINDING_TRACKED_TARGET");
    public static final ActiveTrackState IDLE = new ActiveTrackState("IDLE");
    public static final ActiveTrackState NOT_SUPPORT = new ActiveTrackState("NOT_SUPPORT");
    public static final ActiveTrackState ONLY_CAMERA_FOLLOWING = new ActiveTrackState("ONLY_CAMERA_FOLLOWING");
    public static final ActiveTrackState PERFORMING_QUICK_SHOT = new ActiveTrackState("PERFORMING_QUICK_SHOT");
    public static final ActiveTrackState RECOVERING = new ActiveTrackState("RECOVERING");
    public static final ActiveTrackState UNKNOWN = new ActiveTrackState(CCUtil.PROCESS_UNKNOWN);
    public static final ActiveTrackState WAITING_FOR_CONFIRMATION = new ActiveTrackState("WAITING_FOR_CONFIRMATION");

    private ActiveTrackState(String name) {
        super(name);
    }
}
