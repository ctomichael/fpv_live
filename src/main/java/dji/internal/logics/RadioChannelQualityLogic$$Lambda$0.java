package dji.internal.logics;

import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class RadioChannelQualityLogic$$Lambda$0 implements Action1 {
    private final RadioChannelQualityLogic arg$1;

    RadioChannelQualityLogic$$Lambda$0(RadioChannelQualityLogic radioChannelQualityLogic) {
        this.arg$1 = radioChannelQualityLogic;
    }

    public void call(Object obj) {
        this.arg$1.lambda$new$0$RadioChannelQualityLogic((DataOsdGetPushChannalStatus) obj);
    }
}
