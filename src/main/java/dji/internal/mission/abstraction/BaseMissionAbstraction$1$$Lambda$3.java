package dji.internal.mission.abstraction;

import dji.common.mission.MissionState;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.abstraction.BaseMissionAbstraction;

final /* synthetic */ class BaseMissionAbstraction$1$$Lambda$3 implements Runnable {
    private final BaseMissionAbstraction.AnonymousClass1 arg$1;
    private final MissionState arg$2;
    private final AbstractionDataHolder.Builder arg$3;

    BaseMissionAbstraction$1$$Lambda$3(BaseMissionAbstraction.AnonymousClass1 r1, MissionState missionState, AbstractionDataHolder.Builder builder) {
        this.arg$1 = r1;
        this.arg$2 = missionState;
        this.arg$3 = builder;
    }

    public void run() {
        this.arg$1.lambda$onFailure$3$BaseMissionAbstraction$1(this.arg$2, this.arg$3);
    }
}
