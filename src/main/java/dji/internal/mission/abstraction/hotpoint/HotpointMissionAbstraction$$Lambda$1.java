package dji.internal.mission.abstraction.hotpoint;

import dji.internal.mission.MissionResultPicker;
import dji.midware.data.model.P3.DataFlycStartHotPointMissionWithInfo;

final /* synthetic */ class HotpointMissionAbstraction$$Lambda$1 implements MissionResultPicker {
    private final DataFlycStartHotPointMissionWithInfo arg$1;

    HotpointMissionAbstraction$$Lambda$1(DataFlycStartHotPointMissionWithInfo dataFlycStartHotPointMissionWithInfo) {
        this.arg$1 = dataFlycStartHotPointMissionWithInfo;
    }

    public int getMissionActionResult() {
        return this.arg$1.getResult();
    }
}
