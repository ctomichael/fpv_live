package dji.internal.diagnostics.handler.util;

import dji.thirdparty.io.reactivex.functions.Consumer;

final /* synthetic */ class DiagnosticsModelBase$$Lambda$1 implements Consumer {
    private final DiagnosticsModelBase arg$1;

    DiagnosticsModelBase$$Lambda$1(DiagnosticsModelBase diagnosticsModelBase) {
        this.arg$1 = diagnosticsModelBase;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$autoDisappearTimer$1$DiagnosticsModelBase((Integer) obj);
    }
}
