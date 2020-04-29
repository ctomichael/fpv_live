package dji.internal.diagnostics;

import dji.diagnostics.model.DJIDiagnostics;
import dji.thirdparty.io.reactivex.functions.Consumer;

final /* synthetic */ class DJIDiagnosticsUpdateObserver$$Lambda$0 implements Consumer {
    private final DJIDiagnosticsUpdateObserver arg$1;

    DJIDiagnosticsUpdateObserver$$Lambda$0(DJIDiagnosticsUpdateObserver dJIDiagnosticsUpdateObserver) {
        this.arg$1 = dJIDiagnosticsUpdateObserver;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$start$0$DJIDiagnosticsUpdateObserver((DJIDiagnostics) obj);
    }
}
