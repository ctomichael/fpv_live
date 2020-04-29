package dji.internal.diagnostics.handler.util;

import dji.thirdparty.io.reactivex.functions.Consumer;

final /* synthetic */ class DiagnosticsModelBase$$Lambda$4 implements Consumer {
    private final DiagnosticsModelBase arg$1;

    DiagnosticsModelBase$$Lambda$4(DiagnosticsModelBase diagnosticsModelBase) {
        this.arg$1 = diagnosticsModelBase;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$resetKeepForAWhileTimer$4$DiagnosticsModelBase((Long) obj);
    }
}
