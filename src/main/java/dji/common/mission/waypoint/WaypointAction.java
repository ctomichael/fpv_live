package dji.common.mission.waypoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class WaypointAction {
    public int actionParam = 0;
    public WaypointActionType actionType;

    public WaypointAction(WaypointActionType actionType2, int actionParam2) {
        this.actionType = actionType2;
        this.actionParam = actionParam2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaypointAction)) {
            return false;
        }
        WaypointAction that = (WaypointAction) o;
        if (this.actionParam != that.actionParam) {
            return false;
        }
        if (this.actionType != that.actionType) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((this.actionType != null ? this.actionType.hashCode() : 0) * 31) + this.actionParam;
    }
}
