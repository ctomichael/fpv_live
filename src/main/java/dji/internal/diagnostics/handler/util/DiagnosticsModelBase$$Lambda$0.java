package dji.internal.diagnostics.handler.util;

import dji.thirdparty.io.reactivex.functions.Predicate;

final /* synthetic */ class DiagnosticsModelBase$$Lambda$0 implements Predicate {
    private final DiagnosticsModelBase arg$1;

    DiagnosticsModelBase$$Lambda$0(DiagnosticsModelBase diagnosticsModelBase) {
        this.arg$1 = diagnosticsModelBase;
    }

    public boolean test(Object obj) {
        return this.arg$1.lambda$autoDisappearTimer$0$DiagnosticsModelBase((Integer) obj);
    }
}
