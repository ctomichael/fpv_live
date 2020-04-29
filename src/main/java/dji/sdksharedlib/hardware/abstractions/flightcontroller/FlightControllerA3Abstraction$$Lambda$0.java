package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.util.CallbackUtils;
import dji.midware.data.model.P3.DataFlycSetFChannelInitialization;

final /* synthetic */ class FlightControllerA3Abstraction$$Lambda$0 implements CallbackUtils.ResultPicker {
    private final DataFlycSetFChannelInitialization arg$1;

    FlightControllerA3Abstraction$$Lambda$0(DataFlycSetFChannelInitialization dataFlycSetFChannelInitialization) {
        this.arg$1 = dataFlycSetFChannelInitialization;
    }

    public int getResult() {
        return this.arg$1.getResult();
    }
}
