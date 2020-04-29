package dji.common.mission.followme;

import com.billy.cc.core.component.CCUtil;
import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class FollowMeMissionState extends MissionState {
    public static final FollowMeMissionState DISCONNECTED = new FollowMeMissionState("DISCONNECTED");
    public static final FollowMeMissionState EXECUTING = new FollowMeMissionState("EXECUTING");
    public static final FollowMeMissionState GOT_STUCK = new FollowMeMissionState("GOT_STUCK");
    public static final FollowMeMissionState NOT_READY = new FollowMeMissionState("NOT_READY");
    public static final FollowMeMissionState NOT_SUPPORTED = new FollowMeMissionState("NOT_SUPPORTED");
    public static final FollowMeMissionState READY_TO_EXECUTE = new FollowMeMissionState("READY_TO_EXECUTE");
    public static final FollowMeMissionState RECOVERING = new FollowMeMissionState("RECOVERING");
    public static final FollowMeMissionState UNKNOWN = new FollowMeMissionState(CCUtil.PROCESS_UNKNOWN);

    private FollowMeMissionState(String name) {
        super(name);
    }
}
