package dji.internal.diagnostics;

import dji.thirdparty.io.reactivex.functions.Consumer;

final /* synthetic */ class DJIDiagnosticsManagerImpl$$Lambda$4 implements Consumer {
    private final DJIDiagnosticsManagerImpl arg$1;

    DJIDiagnosticsManagerImpl$$Lambda$4(DJIDiagnosticsManagerImpl dJIDiagnosticsManagerImpl) {
        this.arg$1 = dJIDiagnosticsManagerImpl;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$delayRemoveHandler$4$DJIDiagnosticsManagerImpl((Long) obj);
    }
}
