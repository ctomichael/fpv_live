package dji.common.mission;

import com.billy.cc.core.component.CCUtil;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MissionEvent {
    public static final MissionEvent CAMERA_MODE_CHANGE = new MissionEvent("CAMERA_MODE_CHANGE");
    public static final MissionEvent CONNECTED = new MissionEvent("CONNECTED");
    public static final MissionEvent DISCONNECTED = new MissionEvent("DISCONNECTED");
    public static final MissionEvent DOWNLOAD_DONE = new MissionEvent("DOWNLOAD_DONE");
    public static final MissionEvent DOWNLOAD_FAILED = new MissionEvent("DOWNLOAD_FAILED");
    public static final MissionEvent ENTER_NAVIGATION_MODE = new MissionEvent("ENTER_NAVIGATION_MODE");
    public static final MissionEvent ENTER_POI_MODE = new MissionEvent("ENTER_POI_MODE");
    public static final MissionEvent EXECUTION_FAILED = new MissionEvent("EXECUTION_FAILED");
    public static final MissionEvent EXECUTION_FINISHED = new MissionEvent("EXECUTION_FINISHED");
    public static final MissionEvent EXECUTION_INTERRUPTED = new MissionEvent("EXECUTION_INTERRUPTED");
    public static final MissionEvent EXECUTION_PAUSED = new MissionEvent("EXECUTION_PAUSED");
    public static final MissionEvent EXECUTION_PAUSE_FAILED = new MissionEvent("EXECUTION_PAUSE_FAILED");
    public static final MissionEvent EXECUTION_PROGRESS_UPDATE = new MissionEvent("EXECUTION_PROGRESS_UPDATE");
    public static final MissionEvent EXECUTION_RESUMED = new MissionEvent("EXECUTION_RESUMED");
    public static final MissionEvent EXECUTION_RESUME_FAILED = new MissionEvent("EXECUTION_RESUME_FAILED");
    public static final MissionEvent EXECUTION_STARTED = new MissionEvent("EXECUTION_STARTED");
    public static final MissionEvent EXECUTION_START_FAILED = new MissionEvent("EXECUTION_START_FAILED");
    public static final MissionEvent EXECUTION_STOPPED = new MissionEvent("EXECUTION_STOPPED");
    public static final MissionEvent EXECUTION_STOP_FAILED = new MissionEvent("EXECUTION_STOP_FAILED");
    public static final MissionEvent EXIT_NAVIGATION_MODE = new MissionEvent("EXIT_NAVIGATION_MODE");
    public static final MissionEvent EXIT_POI_MODE = new MissionEvent("EXIT_POI_MODE");
    public static final MissionEvent INITIALIZED = new MissionEvent("INITIALIZED");
    public static final MissionEvent MEASURING_TARGET_STARTED = new MissionEvent("MEASURING_TARGET_STARTED");
    public static final MissionEvent RC_MODE_CHANGED = new MissionEvent("RC_MODE_CHANGED");
    public static final MissionEvent RECOGNIZING_TARGET_FAILED = new MissionEvent("RECOGNIZING_TARGET_FAILED");
    public static final MissionEvent RECOGNIZING_TARGET_STARTED = new MissionEvent("RECOGNIZING_TARGET_STARTED");
    public static final MissionEvent SETUP_DONE = new MissionEvent("SETUP_DONE");
    public static final MissionEvent SETUP_FAILED = new MissionEvent("SETUP_FAILED");
    public static final MissionEvent UNKNOWN = new MissionEvent(CCUtil.PROCESS_UNKNOWN);
    public static final MissionEvent WAITING_FOR_CONFIRMATION = new MissionEvent("WAITING_FOR_CONFIRMATION");
    private final String name;

    public MissionEvent(String name2) {
        this.name = name2;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        }
        if (o instanceof MissionEvent) {
            return this.name.equals(((MissionEvent) o).getName());
        }
        return false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}
