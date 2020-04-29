package dji.internal.mission.abstraction.waypoint;

import dji.common.mission.waypoint.WaypointMission;

final /* synthetic */ class WaypointMissionAbstraction$$Lambda$4 implements Runnable {
    private final WaypointMission.Builder arg$1;

    WaypointMissionAbstraction$$Lambda$4(WaypointMission.Builder builder) {
        this.arg$1 = builder;
    }

    public void run() {
        WaypointMissionAbstraction.lambda$postMissionEvent$4$WaypointMissionAbstraction(this.arg$1);
    }
}
