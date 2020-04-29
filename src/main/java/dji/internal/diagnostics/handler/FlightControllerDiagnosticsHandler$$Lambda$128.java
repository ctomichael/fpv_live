package dji.internal.diagnostics.handler;

import dji.thirdparty.io.reactivex.MaybeEmitter;
import dji.thirdparty.io.reactivex.MaybeOnSubscribe;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$128 implements MaybeOnSubscribe {
    private final FlightControllerDiagnosticsHandler arg$1;

    FlightControllerDiagnosticsHandler$$Lambda$128(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler) {
        this.arg$1 = flightControllerDiagnosticsHandler;
    }

    public void subscribe(MaybeEmitter maybeEmitter) {
        this.arg$1.lambda$fetchRedundancyErrorInfo$77$FlightControllerDiagnosticsHandler(maybeEmitter);
    }
}
