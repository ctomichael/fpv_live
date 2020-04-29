package dji.internal.logics;

import dji.midware.data.config.P3.ProductType;
import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class CompassLogic$$Lambda$0 implements Action1 {
    private final CompassLogic arg$1;

    CompassLogic$$Lambda$0(CompassLogic compassLogic) {
        this.arg$1 = compassLogic;
    }

    public void call(Object obj) {
        this.arg$1.lambda$new$0$CompassLogic((ProductType) obj);
    }
}
