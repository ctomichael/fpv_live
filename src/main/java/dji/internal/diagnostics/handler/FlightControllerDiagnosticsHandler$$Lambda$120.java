package dji.internal.diagnostics.handler;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$120 implements Function {
    static final Function $instance = new FlightControllerDiagnosticsHandler$$Lambda$120();

    private FlightControllerDiagnosticsHandler$$Lambda$120() {
    }

    public Object apply(Object obj) {
        return ((Observable) obj).delay(3, TimeUnit.SECONDS, Schedulers.io());
    }
}
