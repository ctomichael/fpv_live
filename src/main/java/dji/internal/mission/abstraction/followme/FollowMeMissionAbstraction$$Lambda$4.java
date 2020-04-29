package dji.internal.mission.abstraction.followme;

import dji.internal.mission.MissionResultPicker;
import dji.midware.data.model.P3.DataFlycCancelFollowMeMission;

final /* synthetic */ class FollowMeMissionAbstraction$$Lambda$4 implements MissionResultPicker {
    private final DataFlycCancelFollowMeMission arg$1;

    FollowMeMissionAbstraction$$Lambda$4(DataFlycCancelFollowMeMission dataFlycCancelFollowMeMission) {
        this.arg$1 = dataFlycCancelFollowMeMission;
    }

    public int getMissionActionResult() {
        return this.arg$1.getResult();
    }
}
