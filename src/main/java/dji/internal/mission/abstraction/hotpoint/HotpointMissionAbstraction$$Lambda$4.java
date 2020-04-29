package dji.internal.mission.abstraction.hotpoint;

import dji.internal.mission.MissionResultPicker;
import dji.midware.data.model.P3.DataFlycCancelHotPointMission;

final /* synthetic */ class HotpointMissionAbstraction$$Lambda$4 implements MissionResultPicker {
    private final DataFlycCancelHotPointMission arg$1;

    HotpointMissionAbstraction$$Lambda$4(DataFlycCancelHotPointMission dataFlycCancelHotPointMission) {
        this.arg$1 = dataFlycCancelHotPointMission;
    }

    public int getMissionActionResult() {
        return this.arg$1.getResult();
    }
}
