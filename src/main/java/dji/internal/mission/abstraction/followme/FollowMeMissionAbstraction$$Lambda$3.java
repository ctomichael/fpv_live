package dji.internal.mission.abstraction.followme;

import dji.internal.mission.MissionResultPicker;
import dji.midware.data.model.P3.DataFlycFollowMeMissionSwitch;

final /* synthetic */ class FollowMeMissionAbstraction$$Lambda$3 implements MissionResultPicker {
    private final DataFlycFollowMeMissionSwitch arg$1;

    FollowMeMissionAbstraction$$Lambda$3(DataFlycFollowMeMissionSwitch dataFlycFollowMeMissionSwitch) {
        this.arg$1 = dataFlycFollowMeMissionSwitch;
    }

    public int getMissionActionResult() {
        return this.arg$1.getResult();
    }
}
