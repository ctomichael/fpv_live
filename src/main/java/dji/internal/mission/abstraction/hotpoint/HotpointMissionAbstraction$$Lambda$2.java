package dji.internal.mission.abstraction.hotpoint;

import dji.internal.mission.MissionResultPicker;
import dji.midware.data.model.P3.DataFlycHotPointMissionSwitch;

final /* synthetic */ class HotpointMissionAbstraction$$Lambda$2 implements MissionResultPicker {
    private final DataFlycHotPointMissionSwitch arg$1;

    HotpointMissionAbstraction$$Lambda$2(DataFlycHotPointMissionSwitch dataFlycHotPointMissionSwitch) {
        this.arg$1 = dataFlycHotPointMissionSwitch;
    }

    public int getMissionActionResult() {
        return this.arg$1.getResult();
    }
}
