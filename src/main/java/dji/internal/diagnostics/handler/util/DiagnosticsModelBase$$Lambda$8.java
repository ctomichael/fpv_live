package dji.internal.diagnostics.handler.util;

import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.utils.Optional;

final /* synthetic */ class DiagnosticsModelBase$$Lambda$8 implements Consumer {
    private final DiagnosticsModelBase arg$1;

    DiagnosticsModelBase$$Lambda$8(DiagnosticsModelBase diagnosticsModelBase) {
        this.arg$1 = diagnosticsModelBase;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$autoUpdateExtraData$8$DiagnosticsModelBase((Optional) obj);
    }
}
