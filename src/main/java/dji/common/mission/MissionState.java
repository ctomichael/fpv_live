package dji.common.mission;

import com.billy.cc.core.component.CCUtil;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MissionState {
    public static final MissionState AIRCRAFT_FOLLOWING = new MissionState("AIRCRAFT_FOLLOWING");
    public static final MissionState AUTO_SENSING = new MissionState("AUTO_SENSING");
    public static final MissionState AUTO_SENSING_FOR_QUICK_SHOT = new MissionState("AUTO_SENSING_FOR_QUICK_SHOT");
    public static final MissionState CANNOT_CONFIRM = new MissionState("CANNOT_CONFIRM");
    public static final MissionState CANNOT_START = new MissionState("CANNOT_START");
    public static final MissionState CAN_NOT_START = new MissionState("CAN_NOT_START");
    public static final MissionState DETECTING_HUMAN = new MissionState("DETECTING_HUMAN");
    public static final MissionState DISCONNECTED = new MissionState("DISCONNECTED");
    public static final MissionState EXECUTING = new MissionState("EXECUTING");
    public static final MissionState EXECUTION_PAUSED = new MissionState("EXECUTION_PAUSED");
    public static final MissionState EXECUTION_PAUSING = new MissionState("EXECUTION_PAUSING");
    public static final MissionState EXECUTION_RESETTING = new MissionState("EXECUTION_RESETTING");
    public static final MissionState EXECUTION_RESUMING = new MissionState("EXECUTION_RESUMING");
    public static final MissionState EXECUTION_STARTING = new MissionState("EXECUTION_STARTING");
    public static final MissionState EXECUTION_STOPPING = new MissionState("EXECUTION_STOPPING");
    public static final MissionState FINDING_TRACKED_TARGET = new MissionState("FINDING_TRACKED_TARGET");
    public static final MissionState GOT_STUCK = new MissionState("GOT_STUCK");
    public static final MissionState IDLE = new MissionState("IDLE");
    public static final MissionState INITIAL_PHASE = new MissionState("INITIAL_PHASE");
    public static final MissionState MEASURING_TARGET = new MissionState("MEASURING_TARGET");
    public static final MissionState MEASURING_TARGET_STARTING = new MissionState("MEASURING_TARGET_STARTING");
    public static final MissionState NOT_READY = new MissionState("NOT_READY");
    public static final MissionState NOT_SUPPORTED = new MissionState("NOT_SUPPORTED");
    public static final MissionState ONLY_CAMERA_FOLLOWING = new MissionState("ONLY_CAMERA_FOLLOWING");
    public static final MissionState QUICK_MOVIE = new MissionState("PERFORMING_QUICK_SHOT");
    public static final MissionState READY_TO_EXECUTE = new MissionState("READY_TO_EXECUTE");
    public static final MissionState READY_TO_RETRY_UPLOAD = new MissionState("READY_TO_RETRY_UPLOAD");
    public static final MissionState READY_TO_SETUP = new MissionState("READY_TO_SETUP");
    public static final MissionState READY_TO_START = new MissionState("READY_TO_START");
    public static final MissionState READY_TO_UPLOAD = new MissionState("READY_TO_UPLOAD");
    public static final MissionState RECOGNIZING_TARGET = new MissionState("RECOGNIZING_TARGET");
    public static final MissionState RECOVERING = new MissionState("RECOVERING");
    public static final MissionState SEND_TRACK_TARGET = new MissionState("SEND_TRACK_TARGET");
    public static final MissionState SETTING_UP = new MissionState("SETTING_UP");
    public static final MissionState UNKNOWN = new MissionState(CCUtil.PROCESS_UNKNOWN);
    public static final MissionState UPLOADING = new MissionState("UPLOADING");
    public static final MissionState UPLOAD_STARTING = new MissionState("UPLOAD_STARTING");
    public static final MissionState WAITING_FOR_CONFIRMATION = new MissionState("WAITING_FOR_CONFIRMATION");
    private final String name;

    public MissionState(String name2) {
        this.name = name2;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        }
        if (o instanceof MissionState) {
            return this.name.equals(((MissionState) o).getName());
        }
        return false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return this.name;
    }
}
