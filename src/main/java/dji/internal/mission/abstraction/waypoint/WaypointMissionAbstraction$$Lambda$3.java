package dji.internal.mission.abstraction.waypoint;

import dji.internal.mission.MissionResultPicker;
import dji.midware.data.model.P3.DataFlycWayPointMissionSwitch;

final /* synthetic */ class WaypointMissionAbstraction$$Lambda$3 implements MissionResultPicker {
    private final DataFlycWayPointMissionSwitch arg$1;

    WaypointMissionAbstraction$$Lambda$3(DataFlycWayPointMissionSwitch dataFlycWayPointMissionSwitch) {
        this.arg$1 = dataFlycWayPointMissionSwitch;
    }

    public int getMissionActionResult() {
        return this.arg$1.getResult();
    }
}
