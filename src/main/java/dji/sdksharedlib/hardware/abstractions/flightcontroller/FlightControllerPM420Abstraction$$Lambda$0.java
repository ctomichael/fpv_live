package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.sdksharedlib.util.LteSignalHelper;

final /* synthetic */ class FlightControllerPM420Abstraction$$Lambda$0 implements LteSignalHelper.KeyValueChanger {
    private final FlightControllerPM420Abstraction arg$1;

    FlightControllerPM420Abstraction$$Lambda$0(FlightControllerPM420Abstraction flightControllerPM420Abstraction) {
        this.arg$1 = flightControllerPM420Abstraction;
    }

    public void notifyValueChangeForKeyPath(Object obj) {
        this.arg$1.lambda$new$0$FlightControllerPM420Abstraction(obj);
    }
}
