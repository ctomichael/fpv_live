package dji.common.mission;

import android.support.annotation.NonNull;
import dji.common.mission.activetrack.ActiveTrackState;
import dji.common.mission.followme.FollowMeMissionState;
import dji.common.mission.hotpoint.HotpointMissionState;
import dji.common.mission.intelligenthotpoint.IntelligentHotpointMissionState;
import dji.common.mission.panorama.PanoramaMissionState;
import dji.common.mission.tapfly.TapFlyMissionState;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;

@EXClassNullAway
public class StateHelper {
    private static final ActiveTrackState[] PUBLIC_ACTIVE_TRACK_STATE = {ActiveTrackState.UNKNOWN, ActiveTrackState.DISCONNECTED, ActiveTrackState.CANNOT_CONFIRM, ActiveTrackState.AIRCRAFT_FOLLOWING, ActiveTrackState.CANNOT_START, ActiveTrackState.DETECTING_HUMAN, ActiveTrackState.AUTO_SENSING, ActiveTrackState.AUTO_SENSING_FOR_QUICK_SHOT, ActiveTrackState.FINDING_TRACKED_TARGET, ActiveTrackState.IDLE, ActiveTrackState.NOT_SUPPORT, ActiveTrackState.RECOVERING, ActiveTrackState.ONLY_CAMERA_FOLLOWING, ActiveTrackState.PERFORMING_QUICK_SHOT, ActiveTrackState.WAITING_FOR_CONFIRMATION};
    private static final FollowMeMissionState[] PUBLIC_FOLLOW_ME_STATE = {FollowMeMissionState.UNKNOWN, FollowMeMissionState.DISCONNECTED, FollowMeMissionState.NOT_SUPPORTED, FollowMeMissionState.RECOVERING, FollowMeMissionState.READY_TO_EXECUTE, FollowMeMissionState.EXECUTING};
    private static final HotpointMissionState[] PUBLIC_HOTPOINT_STATE = {HotpointMissionState.UNKNOWN, HotpointMissionState.DISCONNECTED, HotpointMissionState.NOT_SUPPORTED, HotpointMissionState.RECOVERING, HotpointMissionState.READY_TO_EXECUTE, HotpointMissionState.INITIAL_PHASE, HotpointMissionState.EXECUTING, HotpointMissionState.EXECUTION_PAUSED};
    private static final IntelligentHotpointMissionState[] PUBLIC_INTELLIGENT_HOTPOINT_STATE = {IntelligentHotpointMissionState.UNKNOWN, IntelligentHotpointMissionState.DISCONNECTED, IntelligentHotpointMissionState.NOT_SUPPORTED, IntelligentHotpointMissionState.RECOVERING, IntelligentHotpointMissionState.READY_TO_START, IntelligentHotpointMissionState.RECOGNIZING_TARGET, IntelligentHotpointMissionState.WAITING_FOR_CONFIRMATION, IntelligentHotpointMissionState.MEASURING_TARGET, IntelligentHotpointMissionState.EXECUTING, IntelligentHotpointMissionState.EXECUTION_PAUSED};
    private static final PanoramaMissionState[] PUBLIC_PANORAMA_STATE = {PanoramaMissionState.UNKNOWN, PanoramaMissionState.DISCONNECTED, PanoramaMissionState.NOT_SUPPORTED, PanoramaMissionState.READY_TO_SETUP, PanoramaMissionState.SETTING_UP, PanoramaMissionState.READY_TO_EXECUTE, PanoramaMissionState.EXECUTING};
    private static final TapFlyMissionState[] PUBLIC_TAP_FLY_STATE = {TapFlyMissionState.UNKNOWN, TapFlyMissionState.NOT_SUPPORT, TapFlyMissionState.CAN_NOT_START, TapFlyMissionState.IDLE, TapFlyMissionState.EXECUTION_STARTING, TapFlyMissionState.EXECUTING, TapFlyMissionState.EXECUTION_PAUSED, TapFlyMissionState.EXECUTION_RESETTING, TapFlyMissionState.RECOVERING, TapFlyMissionState.DISCONNECTED};
    private static final WaypointMissionState[] PUBLIC_WAYPOINT_STATE = {WaypointMissionState.UNKNOWN, WaypointMissionState.DISCONNECTED, WaypointMissionState.NOT_SUPPORTED, WaypointMissionState.RECOVERING, WaypointMissionState.READY_TO_UPLOAD, WaypointMissionState.UPLOADING, WaypointMissionState.READY_TO_EXECUTE, WaypointMissionState.EXECUTING, WaypointMissionState.EXECUTION_PAUSED};
    private static final String TAG = "MissionHelper";

    @NonNull
    public static FollowMeMissionState convertToFollowMePublicState(MissionState state) {
        if (state == null) {
            return FollowMeMissionState.UNKNOWN;
        }
        FollowMeMissionState[] followMeMissionStateArr = PUBLIC_FOLLOW_ME_STATE;
        for (FollowMeMissionState followMeMissionState : followMeMissionStateArr) {
            if (followMeMissionState.equals(state)) {
                return followMeMissionState;
            }
        }
        if (state == MissionState.NOT_READY || state == MissionState.EXECUTION_STARTING) {
            return FollowMeMissionState.READY_TO_EXECUTE;
        }
        if (state == MissionState.EXECUTION_PAUSED || state == MissionState.EXECUTION_PAUSING || state == MissionState.EXECUTION_STOPPING || state == MissionState.GOT_STUCK || state == MissionState.EXECUTION_RESUMING) {
            return FollowMeMissionState.EXECUTING;
        }
        return FollowMeMissionState.UNKNOWN;
    }

    @NonNull
    public static WaypointMissionState convertToWaypointPublicState(MissionState state) {
        if (state == null) {
            return WaypointMissionState.UNKNOWN;
        }
        DJILog.d(TAG, "internal state=" + state.getName(), new Object[0]);
        WaypointMissionState[] waypointMissionStateArr = PUBLIC_WAYPOINT_STATE;
        for (WaypointMissionState waypointMissionState : waypointMissionStateArr) {
            if (waypointMissionState.equals(state)) {
                return waypointMissionState;
            }
        }
        if (state == MissionState.NOT_READY || state == MissionState.READY_TO_RETRY_UPLOAD || state == MissionState.UPLOAD_STARTING) {
            return WaypointMissionState.READY_TO_UPLOAD;
        }
        if (state == MissionState.EXECUTION_STARTING) {
            return WaypointMissionState.READY_TO_EXECUTE;
        }
        if (state == MissionState.EXECUTION_PAUSING || state == MissionState.EXECUTION_STOPPING) {
            return WaypointMissionState.EXECUTING;
        }
        if (state == MissionState.EXECUTION_RESUMING) {
            return WaypointMissionState.EXECUTION_PAUSED;
        }
        return WaypointMissionState.UNKNOWN;
    }

    @NonNull
    public static HotpointMissionState convertToHotpointPublicState(MissionState state) {
        if (state == null) {
            return HotpointMissionState.UNKNOWN;
        }
        HotpointMissionState[] hotpointMissionStateArr = PUBLIC_HOTPOINT_STATE;
        for (HotpointMissionState hotpointMissionState : hotpointMissionStateArr) {
            if (hotpointMissionState.equals(state)) {
                return hotpointMissionState;
            }
        }
        if (state == MissionState.NOT_READY || state == MissionState.EXECUTION_STARTING) {
            return HotpointMissionState.READY_TO_EXECUTE;
        }
        if (state == MissionState.EXECUTION_PAUSING || state == MissionState.EXECUTION_STOPPING) {
            return HotpointMissionState.EXECUTING;
        }
        if (state == MissionState.EXECUTION_RESUMING) {
            return HotpointMissionState.EXECUTION_PAUSED;
        }
        return HotpointMissionState.UNKNOWN;
    }

    @NonNull
    public static IntelligentHotpointMissionState convertToIntelligentHotpointPublicState(MissionState state) {
        if (state == null) {
            return IntelligentHotpointMissionState.UNKNOWN;
        }
        IntelligentHotpointMissionState[] intelligentHotpointMissionStateArr = PUBLIC_INTELLIGENT_HOTPOINT_STATE;
        for (IntelligentHotpointMissionState intelligentHotpointMissionState : intelligentHotpointMissionStateArr) {
            if (intelligentHotpointMissionState.equals(state)) {
                return intelligentHotpointMissionState;
            }
        }
        if (state == MissionState.SEND_TRACK_TARGET) {
            return IntelligentHotpointMissionState.RECOGNIZING_TARGET;
        }
        if (state == MissionState.MEASURING_TARGET_STARTING) {
            return IntelligentHotpointMissionState.WAITING_FOR_CONFIRMATION;
        }
        if (state == MissionState.EXECUTION_PAUSING || state == MissionState.EXECUTION_STOPPING) {
            return IntelligentHotpointMissionState.EXECUTING;
        }
        if (state == MissionState.EXECUTION_RESUMING) {
            return IntelligentHotpointMissionState.EXECUTION_PAUSED;
        }
        return IntelligentHotpointMissionState.UNKNOWN;
    }

    @NonNull
    public static ActiveTrackState convertToActiveTrackPublicState(MissionState state) {
        if (state == null) {
            return ActiveTrackState.UNKNOWN;
        }
        ActiveTrackState[] activeTrackStateArr = PUBLIC_ACTIVE_TRACK_STATE;
        for (ActiveTrackState activeTrackState : activeTrackStateArr) {
            if (activeTrackState.equals(state)) {
                return activeTrackState;
            }
        }
        return ActiveTrackState.UNKNOWN;
    }

    @NonNull
    public static TapFlyMissionState convertToTapFlyMissionPublicState(MissionState state) {
        if (state == null) {
            return TapFlyMissionState.UNKNOWN;
        }
        TapFlyMissionState[] tapFlyMissionStateArr = PUBLIC_TAP_FLY_STATE;
        for (TapFlyMissionState missionState : tapFlyMissionStateArr) {
            if (missionState.equals(state)) {
                return missionState;
            }
        }
        if (state == TapFlyMissionState.EXECUTION_STARTING) {
            return TapFlyMissionState.EXECUTING;
        }
        return TapFlyMissionState.UNKNOWN;
    }

    @NonNull
    public static PanoramaMissionState convertToPanoramaPublicState(MissionState state) {
        if (state == null) {
            return null;
        }
        PanoramaMissionState[] panoramaMissionStateArr = PUBLIC_PANORAMA_STATE;
        for (PanoramaMissionState panoramaMissionState : panoramaMissionStateArr) {
            if (panoramaMissionState.equals(state)) {
                return panoramaMissionState;
            }
        }
        if (state == MissionState.EXECUTION_STARTING || state == MissionState.EXECUTION_STOPPING) {
            return PanoramaMissionState.EXECUTING;
        }
        return null;
    }
}
