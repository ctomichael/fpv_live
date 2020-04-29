package dji.common.mission.waypoint;

import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public abstract class WaypointMissionEvent {
    @Nullable
    private final DJIError error;

    public WaypointMissionEvent(@Nullable DJIError error2) {
        this.error = error2;
    }

    @Nullable
    public DJIError getError() {
        return this.error;
    }
}
