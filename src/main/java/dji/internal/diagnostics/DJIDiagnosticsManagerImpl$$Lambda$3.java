package dji.internal.diagnostics;

import dji.thirdparty.io.reactivex.functions.Predicate;

final /* synthetic */ class DJIDiagnosticsManagerImpl$$Lambda$3 implements Predicate {
    private final DJIDiagnosticsManagerImpl arg$1;

    DJIDiagnosticsManagerImpl$$Lambda$3(DJIDiagnosticsManagerImpl dJIDiagnosticsManagerImpl) {
        this.arg$1 = dJIDiagnosticsManagerImpl;
    }

    public boolean test(Object obj) {
        return this.arg$1.lambda$delayRemoveHandler$3$DJIDiagnosticsManagerImpl((Long) obj);
    }
}
