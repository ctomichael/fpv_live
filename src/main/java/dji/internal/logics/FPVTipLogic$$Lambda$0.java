package dji.internal.logics;

import dji.midware.data.model.P3.Data2100GetPushCheckStatus;
import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class FPVTipLogic$$Lambda$0 implements Action1 {
    private final FPVTipLogic arg$1;

    FPVTipLogic$$Lambda$0(FPVTipLogic fPVTipLogic) {
        this.arg$1 = fPVTipLogic;
    }

    public void call(Object obj) {
        this.arg$1.lambda$init$0$FPVTipLogic((Data2100GetPushCheckStatus) obj);
    }
}
