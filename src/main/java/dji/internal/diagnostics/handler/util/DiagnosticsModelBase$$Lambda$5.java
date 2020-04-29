package dji.internal.diagnostics.handler.util;

import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.utils.Optional;

final /* synthetic */ class DiagnosticsModelBase$$Lambda$5 implements Consumer {
    private final DiagnosticsModelBase arg$1;

    DiagnosticsModelBase$$Lambda$5(DiagnosticsModelBase diagnosticsModelBase) {
        this.arg$1 = diagnosticsModelBase;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$delayNotifyChange$5$DiagnosticsModelBase((Optional) obj);
    }
}
