package dji.internal.logics;

import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class GimbalLogic$$Lambda$0 implements Action1 {
    private final GimbalLogic arg$1;

    GimbalLogic$$Lambda$0(GimbalLogic gimbalLogic) {
        this.arg$1 = gimbalLogic;
    }

    public void call(Object obj) {
        this.arg$1.lambda$new$0$GimbalLogic((DataGimbalGetPushParams) obj);
    }
}
