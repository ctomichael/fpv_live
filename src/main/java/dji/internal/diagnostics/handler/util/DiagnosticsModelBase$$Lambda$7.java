package dji.internal.diagnostics.handler.util;

import dji.thirdparty.io.reactivex.functions.Function;

final /* synthetic */ class DiagnosticsModelBase$$Lambda$7 implements Function {
    private final DiagnosticsModelBase arg$1;

    DiagnosticsModelBase$$Lambda$7(DiagnosticsModelBase diagnosticsModelBase) {
        this.arg$1 = diagnosticsModelBase;
    }

    public Object apply(Object obj) {
        return this.arg$1.lambda$autoUpdateExtraData$7$DiagnosticsModelBase((Long) obj);
    }
}
