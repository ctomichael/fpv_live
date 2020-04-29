package dji.internal.mission.abstraction.waypoint;

import dji.internal.mission.MissionResultPicker;
import dji.midware.data.model.P3.DataFlycWayPointMissionPauseOrResume;

final /* synthetic */ class WaypointMissionAbstraction$$Lambda$1 implements MissionResultPicker {
    private final DataFlycWayPointMissionPauseOrResume arg$1;

    WaypointMissionAbstraction$$Lambda$1(DataFlycWayPointMissionPauseOrResume dataFlycWayPointMissionPauseOrResume) {
        this.arg$1 = dataFlycWayPointMissionPauseOrResume;
    }

    public int getMissionActionResult() {
        return this.arg$1.getResult();
    }
}
