package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataWifi_gGetPushCheckStatus;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class RemoteControllerDiagnosticsHandler$$Lambda$16 implements Predicate {
    private final RemoteControllerDiagnosticsHandler arg$1;

    RemoteControllerDiagnosticsHandler$$Lambda$16(RemoteControllerDiagnosticsHandler remoteControllerDiagnosticsHandler) {
        this.arg$1 = remoteControllerDiagnosticsHandler;
    }

    public Predicate and(Predicate predicate) {
        return Predicate$$CC.and(this, predicate);
    }

    public Predicate negate() {
        return Predicate$$CC.negate(this);
    }

    public Predicate or(Predicate predicate) {
        return Predicate$$CC.or(this, predicate);
    }

    public boolean test(Object obj) {
        return this.arg$1.lambda$init$4$RemoteControllerDiagnosticsHandler((DataWifi_gGetPushCheckStatus) obj);
    }
}
