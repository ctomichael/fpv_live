package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.util.CallbackUtils;
import dji.midware.data.model.P3.DataFlycSetFChannelOutputValue;

final /* synthetic */ class FlightControllerA3Abstraction$$Lambda$1 implements CallbackUtils.ResultPicker {
    private final DataFlycSetFChannelOutputValue arg$1;

    FlightControllerA3Abstraction$$Lambda$1(DataFlycSetFChannelOutputValue dataFlycSetFChannelOutputValue) {
        this.arg$1 = dataFlycSetFChannelOutputValue;
    }

    public int getResult() {
        return this.arg$1.getResult();
    }
}
