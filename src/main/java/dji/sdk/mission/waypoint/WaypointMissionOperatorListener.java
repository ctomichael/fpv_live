package dji.sdk.mission.waypoint;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;

public interface WaypointMissionOperatorListener {
    void onDownloadUpdate(@NonNull WaypointMissionDownloadEvent waypointMissionDownloadEvent);

    void onExecutionFinish(@Nullable DJIError dJIError);

    void onExecutionStart();

    void onExecutionUpdate(@NonNull WaypointMissionExecutionEvent waypointMissionExecutionEvent);

    void onUploadUpdate(@NonNull WaypointMissionUploadEvent waypointMissionUploadEvent);
}
