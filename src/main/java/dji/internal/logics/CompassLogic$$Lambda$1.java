package dji.internal.logics;

import dji.internal.logics.LogicManager;
import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class CompassLogic$$Lambda$1 implements Action1 {
    private final CompassLogic arg$1;

    CompassLogic$$Lambda$1(CompassLogic compassLogic) {
        this.arg$1 = compassLogic;
    }

    public void call(Object obj) {
        this.arg$1.lambda$new$1$CompassLogic((LogicManager.SensorShouldUpdateEvent) obj);
    }
}
