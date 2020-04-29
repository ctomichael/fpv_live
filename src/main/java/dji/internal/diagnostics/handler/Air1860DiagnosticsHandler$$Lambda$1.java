package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushWirelessState;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class Air1860DiagnosticsHandler$$Lambda$1 implements Predicate {
    private final DataOsdGetPushWirelessState.SdrWirelessState arg$1;

    private Air1860DiagnosticsHandler$$Lambda$1(DataOsdGetPushWirelessState.SdrWirelessState sdrWirelessState) {
        this.arg$1 = sdrWirelessState;
    }

    static Predicate get$Lambda(DataOsdGetPushWirelessState.SdrWirelessState sdrWirelessState) {
        return new Air1860DiagnosticsHandler$$Lambda$1(sdrWirelessState);
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
        return this.arg$1.equals((DataOsdGetPushWirelessState.SdrWirelessState) obj);
    }
}
