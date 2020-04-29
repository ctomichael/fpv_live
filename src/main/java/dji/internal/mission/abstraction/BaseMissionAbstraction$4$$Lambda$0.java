package dji.internal.mission.abstraction;

import dji.common.mission.MissionState;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.abstraction.BaseMissionAbstraction;

final /* synthetic */ class BaseMissionAbstraction$4$$Lambda$0 implements Runnable {
    private final BaseMissionAbstraction.AnonymousClass4 arg$1;
    private final MissionState arg$2;
    private final MissionState arg$3;
    private final AbstractionDataHolder.Builder arg$4;

    BaseMissionAbstraction$4$$Lambda$0(BaseMissionAbstraction.AnonymousClass4 r1, MissionState missionState, MissionState missionState2, AbstractionDataHolder.Builder builder) {
        this.arg$1 = r1;
        this.arg$2 = missionState;
        this.arg$3 = missionState2;
        this.arg$4 = builder;
    }

    public void run() {
        this.arg$1.lambda$onFailure$0$BaseMissionAbstraction$4(this.arg$2, this.arg$3, this.arg$4);
    }
}
