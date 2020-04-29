package dji.internal.logics;

import dji.midware.data.model.P3.DataWifiGetPushElecSignal;
import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class RadioChannelQualityLogic$$Lambda$1 implements Action1 {
    private final RadioChannelQualityLogic arg$1;

    RadioChannelQualityLogic$$Lambda$1(RadioChannelQualityLogic radioChannelQualityLogic) {
        this.arg$1 = radioChannelQualityLogic;
    }

    public void call(Object obj) {
        this.arg$1.lambda$new$1$RadioChannelQualityLogic((DataWifiGetPushElecSignal) obj);
    }
}
