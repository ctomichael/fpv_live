package dji.common.mission.waypoint;

import dji.common.mission.MissionEvent;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class WaypointEvent extends MissionEvent {
    public static final WaypointEvent DOWNLOAD_DONE = new WaypointEvent("DOWNLOAD_DONE");
    public static final WaypointEvent DOWNLOAD_FAILED = new WaypointEvent("DOWNLOAD_FAILED");
    public static final WaypointEvent DOWNLOAD_PROGRESS_UPDATE = new WaypointEvent("DOWNLOAD_PROGRESS_UPDATE");
    public static final WaypointEvent MISSION_RELOADED = new WaypointEvent("Mission_Reloaded");
    public static final WaypointEvent SIMULATION_OFF = new WaypointEvent("SIMULATION_OFF");
    public static final WaypointEvent UPLOAD_DONE = new WaypointEvent("UPLOAD_DONE");
    public static final WaypointEvent UPLOAD_FAILED = new WaypointEvent("UPLOAD_FAILED");
    public static final WaypointEvent UPLOAD_PROGRESS_UPDATE = new WaypointEvent("UPLOAD_PROGRESS_UPDATE");

    public WaypointEvent(String name) {
        super(name);
    }
}
