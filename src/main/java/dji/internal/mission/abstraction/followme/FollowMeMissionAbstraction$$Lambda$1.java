package dji.internal.mission.abstraction.followme;

import dji.internal.mission.MissionResultPicker;
import dji.midware.data.model.P3.DataFlycStartFollowMeWithInfo;

final /* synthetic */ class FollowMeMissionAbstraction$$Lambda$1 implements MissionResultPicker {
    private final DataFlycStartFollowMeWithInfo arg$1;

    FollowMeMissionAbstraction$$Lambda$1(DataFlycStartFollowMeWithInfo dataFlycStartFollowMeWithInfo) {
        this.arg$1 = dataFlycStartFollowMeWithInfo;
    }

    public int getMissionActionResult() {
        return this.arg$1.getResult();
    }
}
