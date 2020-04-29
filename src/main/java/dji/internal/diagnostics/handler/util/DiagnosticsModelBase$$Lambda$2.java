package dji.internal.diagnostics.handler.util;

import dji.thirdparty.io.reactivex.functions.Consumer;

final /* synthetic */ class DiagnosticsModelBase$$Lambda$2 implements Consumer {
    private final DiagnosticsModelBase arg$1;

    DiagnosticsModelBase$$Lambda$2(DiagnosticsModelBase diagnosticsModelBase) {
        this.arg$1 = diagnosticsModelBase;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$resetKeepForAWhileTimer$2$DiagnosticsModelBase((Long) obj);
    }
}
