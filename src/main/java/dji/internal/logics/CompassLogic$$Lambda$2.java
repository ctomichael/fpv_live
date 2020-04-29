package dji.internal.logics;

import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class CompassLogic$$Lambda$2 implements Action1 {
    private final CompassLogic arg$1;

    CompassLogic$$Lambda$2(CompassLogic compassLogic) {
        this.arg$1 = compassLogic;
    }

    public void call(Object obj) {
        this.arg$1.lambda$new$2$CompassLogic((DataOsdGetPushCommon) obj);
    }
}
