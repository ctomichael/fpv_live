package dji.internal.diagnostics.handler.util;

import dji.thirdparty.io.reactivex.functions.Predicate;

final /* synthetic */ class DiagnosticsModelBase$$Lambda$6 implements Predicate {
    private final DiagnosticsModelBase arg$1;

    DiagnosticsModelBase$$Lambda$6(DiagnosticsModelBase diagnosticsModelBase) {
        this.arg$1 = diagnosticsModelBase;
    }

    public boolean test(Object obj) {
        return this.arg$1.lambda$autoUpdateExtraData$6$DiagnosticsModelBase((Long) obj);
    }
}
